package Experiments

import Algorithms.PseudoRandom
import scala.annotation.tailrec

/**
 * Generates the experiment inputs.
 *
 * Everything is built with tail recursion and the pure generator of
 * [[Algorithms.PseudoRandom]], so the same size always gives the same input.
 */
object InputGenerator {

  /** Seed shared by every generated input. */
  val Seed: Long = 42L

  /** Number of distinct values in the [[Scenario.FewDistinct]] scenario. */
  val FewDistinctValues: Int = 10

  /** Half of the width of the range of the random integers and coordinates. */
  private val Half: Int = 1000000

  /**
   * Builds a list of pseudo-random integers.
   *
   * @param remaining number of elements still to generate
   * @param seed      current seed of the generator
   * @param bound     exclusive upper limit of the generated indices
   * @param offset    value added to every generated index
   * @param acc       elements generated so far
   * @return the generated list
   */
  @tailrec
  private def randomInts(remaining: Int, seed: Long, bound: Int, offset: Int, acc: List[Int]): List[Int] =
    if (remaining == 0) acc
    else randomInts(
      remaining - 1,
      PseudoRandom.next(seed),
      bound,
      offset,
      (PseudoRandom.indexBelow(seed, bound) + offset) :: acc
    )

  /**
   * Builds the increasing list `0, 1, ..., i`.
   *
   * @param i   the last value to include (start with `n - 1`)
   * @param acc values already placed
   * @return the increasing list
   */
  @tailrec
  private def ascending(i: Int, acc: List[Int]): List[Int] =
    if (i < 0) acc else ascending(i - 1, i :: acc)

  /**
   * Builds the decreasing list `n - 1, ..., i`.
   *
   * @param i   the next value to include (start with 0)
   * @param n   exclusive upper limit
   * @param acc values already placed
   * @return the decreasing list
   */
  @tailrec
  private def descending(i: Int, n: Int, acc: List[Int]): List[Int] =
    if (i >= n) acc else descending(i + 1, n, i :: acc)

  /**
   * Builds a list of pseudo-random points, each one a list `List(x, y)`.
   *
   * @param remaining number of points still to generate
   * @param seed      current seed of the generator
   * @param acc       points generated so far
   * @return the generated points
   */
  @tailrec
  private def randomPoints(remaining: Int, seed: Long, acc: List[List[Int]]): List[List[Int]] =
    if (remaining == 0) acc
    else {
      val seedY = PseudoRandom.next(seed)
      val x     = PseudoRandom.indexBelow(seed, 2 * Half + 1) - Half
      val y     = PseudoRandom.indexBelow(seedY, 2 * Half + 1) - Half
      randomPoints(remaining - 1, PseudoRandom.next(seedY), List(x, y) :: acc)
    }

  /**
   * Generates a list of integers for a scenario.
   *
   * @param scenario the kind of input (any scenario except [[Scenario.Points]])
   * @param n        the number of elements
   * @return the list of `n` integers
   */
  def ints(scenario: Scenario, n: Int): List[Int] = scenario match {
    case Scenario.Random      => randomInts(n, Seed, 2 * Half + 1, -Half, Nil)
    case Scenario.FewDistinct => randomInts(n, Seed, FewDistinctValues, 0, Nil)
    case Scenario.Sorted      => ascending(n - 1, Nil)
    case Scenario.Reversed    => descending(0, n, Nil)
    case Scenario.Points      => Nil
  }

  /**
   * Generates `n` pseudo-random points with coordinates in `[-10^6, 10^6]`.
   *
   * @param n the number of points
   * @return the points, each one a list of two integers
   */
  def points(n: Int): List[List[Int]] = randomPoints(n, Seed, Nil)
}
