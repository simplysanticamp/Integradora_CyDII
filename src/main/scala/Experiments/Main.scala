package Experiments

import java.io.{File, PrintWriter}
import scala.annotation.tailrec

/**
 * Entry point of the experiment.
 *
 * Usage (from the project folder):
 * {{{
 * sbt "runMain Experiments.Main quick"   // sizes up to 10^4, to check that everything works
 * sbt "runMain Experiments.Main full"    // all sizes, up to 10^6
 * }}}
 * The times go to `results/timings.csv` and the machine description to
 * `results/environment.txt`. Inputs are cached in `data/`.
 */
object Main {

  private val QuickSizes: List[Int] = List(10, 50, 100, 1000, 5000, 10000)

  /** Toy, small, medium, between medium and large, and large sizes. */
  private val FullSizes: List[Int] =
    List(10, 50, 100, 1000, 5000, 10000, 50000, 100000, 500000, 1000000)

  /** Algorithm and input kind of every experiment. */
  private val Plan: List[(Algorithm, Scenario)] = List(
    (Algorithm.QuickSortTwoWay, Scenario.Random),
    (Algorithm.QuickSortTwoWay, Scenario.Sorted),
    (Algorithm.QuickSortTwoWay, Scenario.Reversed),
    (Algorithm.QuickSortTwoWay, Scenario.FewDistinct),
    (Algorithm.QuickSortThreeWay, Scenario.Random),
    (Algorithm.QuickSortThreeWay, Scenario.Sorted),
    (Algorithm.QuickSortThreeWay, Scenario.Reversed),
    (Algorithm.QuickSortThreeWay, Scenario.FewDistinct),
    (Algorithm.InversionCount, Scenario.Random),
    (Algorithm.InversionCount, Scenario.Sorted),
    (Algorithm.InversionCount, Scenario.Reversed),
    (Algorithm.ClosestPoints, Scenario.Points)
  )

  /**
   * Largest size that is run for a combination. The 2-way quicksort is quadratic
   * with few distinct values, so it is stopped early; say so in the report.
   *
   * @param algorithm the algorithm
   * @param scenario  the kind of input
   * @return the largest size to run
   */
  private def maxSize(algorithm: Algorithm, scenario: Scenario): Int = (algorithm, scenario) match {
    case (Algorithm.QuickSortTwoWay, Scenario.FewDistinct) => 50000
    case _                                                 => Int.MaxValue
  }

  /**
   * Measures every size for one algorithm and input kind, writing each row as soon as it is ready.
   *
   * @param algorithm the algorithm
   * @param scenario  the kind of input
   * @param sizes     the sizes still to run
   * @param writer    the CSV file
   */
  @tailrec
  private def runSizes(algorithm: Algorithm, scenario: Scenario, sizes: List[Int], writer: PrintWriter): Unit =
    sizes match {
      case Nil          => ()
      case n :: rest    =>
        if (n <= maxSize(algorithm, scenario)) {
          val result = ExperimentRunner.measure(algorithm, scenario, n)
          writer.println(result.toCsv)
          writer.flush()
          println(s"${result.algorithm} | ${result.scenario} | n=$n | ${result.meanText} ms")
        }
        runSizes(algorithm, scenario, rest, writer)
    }

  /**
   * Runs the whole plan.
   *
   * @param plan   the combinations still to run
   * @param sizes  the sizes to use
   * @param writer the CSV file
   */
  @tailrec
  private def runPlan(plan: List[(Algorithm, Scenario)], sizes: List[Int], writer: PrintWriter): Unit =
    plan match {
      case Nil                          => ()
      case (algorithm, scenario) :: rest =>
        runSizes(algorithm, scenario, sizes, writer)
        runPlan(rest, sizes, writer)
    }

  /**
   * Writes what the JVM knows about the machine. Add the processor model and
   * the installed RAM by hand, because the JVM does not report them.
   *
   * @param path the output file
   */
  private def writeEnvironment(path: String): Unit = {
    val writer = new PrintWriter(path)
    try {
      writer.println(s"os: ${System.getProperty("os.name")} ${System.getProperty("os.version")}")
      writer.println(s"arch: ${System.getProperty("os.arch")}")
      writer.println(s"java: ${System.getProperty("java.version")}")
      writer.println(s"logical processors: ${Runtime.getRuntime.availableProcessors()}")
      writer.println(s"max heap (MB): ${Runtime.getRuntime.maxMemory() / (1024 * 1024)}")
    } finally writer.close()
  }

  /**
   * Program entry point.
   *
   * @param args `quick` (default) or `full`
   */
  def main(args: Array[String]): Unit = {
    val sizes = args.toList match {
      case "full" :: _ => FullSizes
      case _           => QuickSizes
    }
    new File("results").mkdirs()
    writeEnvironment("results/environment.txt")
    val writer = new PrintWriter("results/timings.csv")
    try {
      writer.println("algorithm,scenario,n,mean_ms,checksum")
      runPlan(Plan, sizes, writer)
    } finally writer.close()
    println("Done. Results in results/timings.csv")
  }
}
