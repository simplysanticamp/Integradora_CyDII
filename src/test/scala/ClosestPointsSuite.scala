import scala.annotation.tailrec

/** Tests for the closest pair of points algorithm. */
class ClosestPointsSuite extends munit.FunSuite {

  private val Tolerance = 1e-9

  // Helper functions 
  /** Gets the next state of the generator. */
  private def nextState(state: Long): Long =
    state * 6364136223846793005L + 1442695040888963407L

  /** Converts a generator state to a value in the given range. */
  private def valueOf(state: Long, range: Int): Int =
    ((state >>> 33) % (2L * range + 1)).toInt - range

  /** Creates random points with coordinates in the given range. */
  private def randomPoints(count: Int, seed: Long, range: Int): List[List[Int]] = {
    @tailrec
    def loop(remaining: Int, state: Long, acc: List[List[Int]]): List[List[Int]] =
      if (remaining == 0) acc
      else {
        val sx = nextState(state)
        val sy = nextState(sx)
        loop(remaining - 1, sy, List(valueOf(sx, range), valueOf(sy, range)) :: acc)
      }
    loop(count, seed, Nil)
  }

  /** Creates points on a line in descending x order. */
  private def descendingLine(count: Int, step: Int): List[List[Int]] = {
    @tailrec
    def loop(i: Int, acc: List[List[Int]]): List[List[Int]] =
      if (i == count) acc else loop(i + 1, List(i * step, 0) :: acc)
    loop(0, Nil)
  }

  /** Calculates the minimum squared distance using a simple reference method. */
  private def referenceSquared(points: List[List[Int]]): Double = {
    @tailrec
    def against(p: List[Int], rest: List[List[Int]], best: Double): Double = rest match {
      case Nil => best
      case q :: tail =>
        val dx = p(0).toDouble - q(0).toDouble
        val dy = p(1).toDouble - q(1).toDouble
        against(p, tail, math.min(best, dx * dx + dy * dy))
    }
    @tailrec
    def all(ps: List[List[Int]], best: Double): Double = ps match {
      case Nil       => best
      case p :: rest => all(rest, against(p, rest, best))
    }
    all(points, Double.PositiveInfinity)
  }

  /** Calculates the expected answer rounded to four decimal places. */
  private def expected(points: List[List[Int]]): Double =
    math.round(math.sqrt(referenceSquared(points)) * 10000.0) / 10000.0

  // closestDistance examples
  test("example 1 from the statement: only two points") {
    assertEqualsDouble(ClosestPoints.closestDistance(List(List(0, 0), List(3, 4))), 5.0, Tolerance)
  }

  test("example 2 from the statement: three points") {
    val points = List(List(0, 0), List(3, 4), List(1, 1))
    assertEqualsDouble(ClosestPoints.closestDistance(points), 1.4142, Tolerance)
  }

  // closestDistance edge cases
  test("two points with the same coordinates have distance zero") {
    assertEqualsDouble(ClosestPoints.closestDistance(List(List(2, 2), List(2, 2))), 0.0, Tolerance)
  }

  test("a repeated point among distinct ones gives distance zero") {
    val points = List(List(0, 0), List(10, 10), List(5, 5), List(10, 10), List(-3, 8))
    assertEqualsDouble(ClosestPoints.closestDistance(points), 0.0, Tolerance)
  }

  test("all points equal") {
    val points = List(List(7, 7), List(7, 7), List(7, 7), List(7, 7), List(7, 7))
    assertEqualsDouble(ClosestPoints.closestDistance(points), 0.0, Tolerance)
  }

  test("points sharing the same x coordinate (vertical line)") {
    val points = List(List(4, 0), List(4, 100), List(4, 7), List(4, 12), List(4, -30))
    assertEqualsDouble(ClosestPoints.closestDistance(points), 5.0, Tolerance)
  }

  test("points sharing the same y coordinate (horizontal line)") {
    val points = List(List(0, 3), List(50, 3), List(21, 3), List(27, 3), List(-9, 3))
    assertEqualsDouble(ClosestPoints.closestDistance(points), 6.0, Tolerance)
  }

  test("negative coordinates") {
    // The closest pair has a distance of 4.1231
    val points = List(List(-5, -5), List(-1, -2), List(-4, -9), List(-8, -1))
    assertEqualsDouble(ClosestPoints.closestDistance(points), 4.1231, Tolerance)
  }

  test("result is rounded to four decimal digits") {
    // The distance is sqrt(2), which is about 1.41421356
    val result = ClosestPoints.closestDistance(List(List(0, 0), List(1, 1)))
    assertEqualsDouble(result, 1.4142, 1e-12)
  }

  test("input order does not change the result") {
    val points = List(List(9, 9), List(0, 0), List(4, 3), List(20, 1), List(5, 5))
    val reversed = ListUtils.reverse(points)
    assertEqualsDouble(
      ClosestPoints.closestDistance(points),
      ClosestPoints.closestDistance(reversed),
      Tolerance
    )
  }

  test("extreme Int coordinates do not overflow") {
    val points = List(List(Int.MinValue, 0), List(Int.MaxValue, 0))
    assertEqualsDouble(ClosestPoints.closestDistance(points), 4294967295.0, 1e-3)
  }

  test("extreme Int coordinates on both axes") {
    val points = List(List(Int.MinValue, Int.MinValue), List(Int.MaxValue, Int.MaxValue))
    assertEqualsDouble(ClosestPoints.closestDistance(points), 6074000998.5379, 1e-3)
  }

  test("fewer than two points is rejected") {
    intercept[IllegalArgumentException](ClosestPoints.closestDistance(List(List(1, 1))))
    intercept[IllegalArgumentException](ClosestPoints.closestDistance(Nil))
  }

  test("a point that is not a pair of integers is rejected") {
    intercept[IllegalArgumentException](
      ClosestPoints.closestDistance(List(List(1, 2, 3), List(4, 5)))
    )
  }
  
  // closestDistance when the pair crosses the dividing line

  test("closest pair straddles the dividing line") {
    // The closest pair crosses the dividing line
    val points = List(List(0, 0), List(4, 50), List(9, 100), List(10, 2))
    assertEqualsDouble(ClosestPoints.closestDistance(points), 10.198, Tolerance)
  }

  test("closest pair straddles the line with many points on both sides") {
    val left  = List(List(0, 0), List(0, 100), List(0, 200), List(0, 300))
    val right = List(List(20, 0), List(20, 100), List(20, 200), List(20, 300))
    val close = List(List(9, 150), List(11, 151))
    val points = ListUtils.append(left, ListUtils.append(close, right))
    assertEqualsDouble(ClosestPoints.closestDistance(points), 2.2361, Tolerance)
  }

  // closestDistance compared with the reference method

  test("matches the reference on 50 small random inputs") {
    @tailrec
    def check(seed: Long, remaining: Int): Unit =
      if (remaining > 0) {
        val points = randomPoints(2 + (remaining % 20), seed, 50)
        assertEqualsDouble(ClosestPoints.closestDistance(points), expected(points), Tolerance)
        check(nextState(seed), remaining - 1)
      }
    check(42L, 50)
  }

  test("matches the reference on dense inputs full of repeated coordinates") {
    @tailrec
    def check(seed: Long, remaining: Int): Unit =
      if (remaining > 0) {
        val points = randomPoints(30, seed, 4)
        assertEqualsDouble(ClosestPoints.closestDistance(points), expected(points), Tolerance)
        check(nextState(seed), remaining - 1)
      }
    check(7L, 30)
  }

  test("matches the reference on 500 random points with a wide range") {
    val points = randomPoints(500, 2026L, 1000000)
    assertEqualsDouble(ClosestPoints.closestDistance(points), expected(points), Tolerance)
  }

  // closestDistance with large inputs

  test("100000 collinear points in descending order") {
    assertEqualsDouble(ClosestPoints.closestDistance(descendingLine(100000, 10)), 10.0, Tolerance)
  }

  test("200000 random points finish without stack overflow") {
    val result = ClosestPoints.closestDistance(randomPoints(200000, 99L, 1000000000))
    assert(result >= 0.0)
  }

  // Helper functions

  test("coordinate selects x or y of a point") {
    assertEquals(ClosestPoints.coordinate(List(3, -8), ClosestPoints.XAxis), 3)
    assertEquals(ClosestPoints.coordinate(List(3, -8), ClosestPoints.YAxis), -8)
  }

  test("distanceSquared of the 3-4-5 triangle") {
    assertEqualsDouble(ClosestPoints.distanceSquared(List(0, 0), List(3, 4)), 25.0, Tolerance)
  }

  test("distanceSquared is symmetric and zero for equal points") {
    assertEqualsDouble(ClosestPoints.distanceSquared(List(1, 2), List(1, 2)), 0.0, Tolerance)
    assertEqualsDouble(
      ClosestPoints.distanceSquared(List(1, 2), List(-4, 6)),
      ClosestPoints.distanceSquared(List(-4, 6), List(1, 2)),
      Tolerance
    )
  }

  test("distanceSquared does not overflow with extreme coordinates") {
    val d = ClosestPoints.distanceSquared(List(Int.MinValue, 0), List(Int.MaxValue, 0))
    assert(d > 1.8e19)
  }

  test("bruteForce on an empty or single list returns the initial bound") {
    assertEquals(ClosestPoints.bruteForce(Nil, Double.PositiveInfinity), Double.PositiveInfinity)
    assertEquals(
      ClosestPoints.bruteForce(List(List(1, 1)), Double.PositiveInfinity),
      Double.PositiveInfinity
    )
  }

  test("bruteForce finds the smallest squared distance") {
    val points = List(List(0, 0), List(3, 4), List(1, 1))
    assertEqualsDouble(ClosestPoints.bruteForce(points, Double.PositiveInfinity), 2.0, Tolerance)
  }

  test("bruteForce respects an initial bound that is already smaller") {
    val points = List(List(0, 0), List(3, 4))
    assertEqualsDouble(ClosestPoints.bruteForce(points, 1.0), 1.0, Tolerance)
  }

  test("mergeByAxis merges two lists sorted by x") {
    val left  = List(List(1, 9), List(5, 0))
    val right = List(List(2, 3), List(8, 8))
    assertEquals(
      ClosestPoints.mergeByAxis(left, right, ClosestPoints.XAxis),
      List(List(1, 9), List(2, 3), List(5, 0), List(8, 8))
    )
  }

  test("mergeByAxis merges two lists sorted by y") {
    val left  = List(List(9, 1), List(0, 5))
    val right = List(List(3, 2), List(8, 8))
    assertEquals(
      ClosestPoints.mergeByAxis(left, right, ClosestPoints.YAxis),
      List(List(9, 1), List(3, 2), List(0, 5), List(8, 8))
    )
  }

  test("mergeByAxis with an empty side") {
    val points = List(List(1, 1), List(2, 2))
    assertEquals(ClosestPoints.mergeByAxis(Nil, points, ClosestPoints.XAxis), points)
    assertEquals(ClosestPoints.mergeByAxis(points, Nil, ClosestPoints.XAxis), points)
  }

  test("sortByAxis sorts by x") {
    val points = List(List(5, 1), List(-2, 7), List(9, 0), List(0, 4))
    assertEquals(
      ClosestPoints.sortByAxis(points, ClosestPoints.XAxis),
      List(List(-2, 7), List(0, 4), List(5, 1), List(9, 0))
    )
  }

  test("sortByAxis sorts by y") {
    val points = List(List(5, 1), List(-2, 7), List(9, 0), List(0, 4))
    assertEquals(
      ClosestPoints.sortByAxis(points, ClosestPoints.YAxis),
      List(List(9, 0), List(5, 1), List(0, 4), List(-2, 7))
    )
  }

  test("sortByAxis on empty and single lists") {
    assertEquals(ClosestPoints.sortByAxis(Nil, ClosestPoints.XAxis), Nil)
    assertEquals(ClosestPoints.sortByAxis(List(List(1, 2)), ClosestPoints.YAxis), List(List(1, 2)))
  }

  test("stripPoints keeps only points strictly closer than the bound to the middle line") {
    // The middle line is x = 10 and the bound is 9
    val points = List(List(0, 1), List(8, 2), List(10, 3), List(12, 4), List(13, 5), List(30, 6))
    assertEquals(
      ClosestPoints.stripPoints(points, 10, 9.0),
      List(List(8, 2), List(10, 3), List(12, 4))
    )
  }

  test("stripPoints preserves the y ordering of its input") {
    val points = List(List(10, 1), List(11, 2), List(9, 3))
    assertEquals(ClosestPoints.stripPoints(points, 10, 100.0), points)
  }

  test("stripPoints with a zero bound returns an empty strip") {
    assertEquals(ClosestPoints.stripPoints(List(List(10, 1)), 10, 0.0), Nil)
  }

  test("scanStrip on an empty or single point strip returns the bound") {
    assertEqualsDouble(ClosestPoints.scanStrip(Nil, 50.0), 50.0, Tolerance)
    assertEqualsDouble(ClosestPoints.scanStrip(List(List(1, 1)), 50.0), 50.0, Tolerance)
  }

  test("scanStrip improves the bound when a closer pair exists") {
    val strip = List(List(0, 0), List(1, 1), List(5, 20))
    assertEqualsDouble(ClosestPoints.scanStrip(strip, 100.0), 2.0, Tolerance)
  }

  test("scanStrip keeps the bound when no pair is closer") {
    val strip = List(List(0, 0), List(10, 10))
    assertEqualsDouble(ClosestPoints.scanStrip(strip, 4.0), 4.0, Tolerance)
  }

  test("closestRec returns the points sorted by y and the minimum squared distance") {
    val sortedByX = List(List(0, 0), List(4, 50), List(9, 100), List(10, 2))
    val (byY, minSquared) = ClosestPoints.closestRec(sortedByX, 4)
    assertEquals(byY, List(List(0, 0), List(10, 2), List(4, 50), List(9, 100)))
    assertEqualsDouble(minSquared, 104.0, Tolerance)
  }

  test("closestRec base case with three points") {
    val sortedByX = List(List(0, 0), List(1, 1), List(3, 4))
    val (byY, minSquared) = ClosestPoints.closestRec(sortedByX, 3)
    assertEquals(byY, List(List(0, 0), List(1, 1), List(3, 4)))
    assertEqualsDouble(minSquared, 2.0, Tolerance)
  }

}