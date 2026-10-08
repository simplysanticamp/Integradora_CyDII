package Experiments

import Algorithms.{ClosestPoints, InversionCount, ListUtils, QuickSort, QuickSort3}
import Experiments.InputIO

import scala.annotation.tailrec

/**
 * The outcome of measuring one algorithm on one input size.
 *
 * @param algorithm the algorithm label
 * @param scenario  the scenario label
 * @param n         the input size
 * @param meanMs    the mean time in milliseconds, without the first run
 * @param checksum  a value computed from the outputs, so the JVM cannot discard the work
 */
final case class Result(algorithm: String, scenario: String, n: Int, meanMs: Double, checksum: Long) {

  /** The mean time as text with a dot as decimal separator, whatever the system locale. */
  def meanText: String = String.format(java.util.Locale.ROOT, "%.3f", Double.box(meanMs))

  /** One line of the CSV file. */
  def toCsv: String = s"$algorithm,$scenario,$n,$meanText,$checksum"
}

/**
 * Runs each algorithm several times on the same input and averages the times.
 *
 * Every size is run [[Repetitions]] times, the first run is discarded (the JVM
 * is still warming up) and the remaining ones are averaged.
 */
object ExperimentRunner {

  /** Number of runs per algorithm and input. */
  val Repetitions: Int = 10

  /**
   * Runs one algorithm once.
   *
   * ADJUST the four calls below to the names and result types of your own objects.
   *
   * @param algorithm the algorithm to run
   * @param ints      the integer input (used by the sorting and inversion algorithms)
   * @param points    the point input (used by the closest points algorithm)
   * @return a number derived from the output, used only to keep the work alive
   */
  def execute(algorithm: Algorithm, ints: List[Int], points: List[List[Int]]): Long = algorithm match {
    case Algorithms.QuickSort   => ListUtils.size(QuickSort.quickSort(ints)).toLong
    case Algorithms.QuickSort3 => ListUtils.size(QuickSort3.quickSort3(ints)).toLong
    case Algorithms.InversionCount    => InversionCount.countInversions(ints)
    case Algorithms.ClosestPoints     => (ClosestPoints.closestDistance(points) * 10000).toLong
  }

  /**
   * Loads an integer input from disk, generating and saving it the first time.
   *
   * @param scenario the kind of input
   * @param n        the input size
   * @return the input list
   */
  private def intsFor(scenario: Scenario, n: Int): List[Int] = {
    val path = s"data/${scenario.label}_$n.txt"
    if (InputIO.exists(path)) InputIO.loadInts(path)
    else {
      val generated = InputGenerator.ints(scenario, n)
      InputIO.saveInts(path, generated)
      generated
    }
  }

  /**
   * Loads a point input from disk, generating and saving it the first time.
   *
   * @param n the number of points
   * @return the points
   */
  private def pointsFor(n: Int): List[List[Int]] = {
    val path = s"data/${Scenario.Points.label}_$n.txt"
    if (InputIO.exists(path)) InputIO.loadPoints(path)
    else {
      val generated = InputGenerator.points(n)
      InputIO.savePoints(path, generated)
      generated
    }
  }

  /**
   * Runs the algorithm `remaining` more times, timing each run.
   *
   * @param algorithm the algorithm to run
   * @param ints      the integer input
   * @param points    the point input
   * @param remaining runs still to do
   * @param times     times measured so far, newest first
   * @param checksum  sum of the outputs so far
   * @return all times (newest first) and the checksum
   */
  @tailrec
  private def timeRuns(
                        algorithm: Algorithm,
                        ints: List[Int],
                        points: List[List[Int]],
                        remaining: Int,
                        times: List[Double],
                        checksum: Long
                      ): (List[Double], Long) =
    if (remaining == 0) (times, checksum)
    else {
      val (value, ms) = Timer.time(execute(algorithm, ints, points))
      timeRuns(algorithm, ints, points, remaining - 1, ms :: times, checksum + value)
    }

  /**
   * Adds up a list of numbers.
   *
   * @param values the numbers still to add
   * @param acc    the sum so far
   * @return the total
   */
  @tailrec
  private def sum(values: List[Double], acc: Double): Double = values match {
    case Nil          => acc
    case head :: tail => sum(tail, acc + head)
  }

  /**
   * Measures one algorithm on one input size.
   *
   * The input is created or loaded before the clock starts, so only the
   * algorithm is timed.
   *
   * @param algorithm the algorithm to measure
   * @param scenario  the kind of input
   * @param n         the input size
   * @return the mean time without the first run
   */
  def measure(algorithm: Algorithm, scenario: Scenario, n: Int): Result = {
    val inputs: (List[Int], List[List[Int]]) = scenario match {
      case Scenario.Points => (Nil, pointsFor(n))
      case _               => (intsFor(scenario, n), Nil)
    }
    val (times, checksum) = timeRuns(algorithm, inputs._1, inputs._2, Repetitions, Nil, 0L)
    val kept              = ListUtils.reverse(times).tail
    val mean              = sum(kept, 0.0) / ListUtils.size(kept)
    Result(algorithm.label, scenario.label, n, mean, checksum)
  }
}
