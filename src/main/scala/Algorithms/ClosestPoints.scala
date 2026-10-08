package Algorithms

import scala.annotation.tailrec

/**
 * Closest pair of points on the plane using divide and conquer in O(n log n).
 *
 * A point is a list with exactly two integers: List(x, y).
 *
 * Strategy:
 *  1. Sort the points by x once using merge sort.
 *  2. Split the list into two halves and solve each half recursively. This gives
 *     d1 and d2, and then d = min(d1, d2).
 *  3. Merge the two halves sorted by y to keep the whole set ordered by y.
 *     This takes linear time.
 *  4. Keep only the points whose distance from the middle line is less than d.
 *  5. Check the points in y order to find d0, the best distance across the line.
 *  6. The final answer is min(d, d0).
 *
 * Distances are kept squared as Double until the end to avoid square roots
 * and possible Int overflow.
 */
object ClosestPoints {

  /** A point on the plane, encoded as `List(x, y)`. */
  type Point = List[Int]

  /** Axis selector for the x coordinate. */
  val XAxis: Int = 0

  /** Axis selector for the y coordinate. */
  val YAxis: Int = 1

  /** Sub-problems with at most this many points are solved by brute force. */
  private val BaseCaseSize: Int = 3

  /**
   * Reads one coordinate of a point.
   *
   * @param point the point, which must be a list of exactly two integers
   * @param axis  [[XAxis]] or [[YAxis]]
   * @return the selected coordinate
   * @throws IllegalArgumentException if the point is not a pair of integers
   */
  def coordinate(point: Point, axis: Int): Int = point match {
    case x :: y :: Nil => if (axis == XAxis) x else y
    case _ =>
      throw new IllegalArgumentException(s"A point must have exactly two coordinates: $point")
  }

  /**
   * Squared Euclidean distance between two points.
   *
   * Coordinates are converted to `Double` before subtracting, so extreme `Int`
   * values do not overflow.
   *
   * @param p the first point
   * @param q the second point
   * @return (px - qx)^2 + (py - qy)^2
   */
  def distanceSquared(p: Point, q: Point): Double = {
    val dx = coordinate(p, XAxis).toDouble - coordinate(q, XAxis).toDouble
    val dy = coordinate(p, YAxis).toDouble - coordinate(q, YAxis).toDouble
    dx * dx + dy * dy
  }

  /**
   * Smallest squared distance between `p` and any point of `others`.
   *
   * @param p      the reference point
   * @param others the candidate points
   * @param best   the best squared distance known so far
   * @return the minimum between `best` and the distances from `p` to `others`
   */
  @tailrec
  private def bestAgainst(p: Point, others: List[Point], best: Double): Double =
    others match {
      case Nil       => best
      case q :: tail => bestAgainst(p, tail, math.min(best, distanceSquared(p, q)))
    }

  /**
   * Smallest squared distance between any two points of a list, comparing all pairs.
   * Used only for the base case of the recursion.
   *
   * @param points the points to compare
   * @param best   the best squared distance known so far (use positive infinity at first)
   * @return the minimum between `best` and every pairwise squared distance
   */
  @tailrec
  def bruteForce(points: List[Point], best: Double): Double = points match {
    case Nil       => best
    case p :: rest => bruteForce(rest, bestAgainst(p, rest, best))
  }

  /**
   * Merges two lists that are already sorted by the given axis.
   *
   * @param left  first sorted list
   * @param right second sorted list
   * @param axis  [[XAxis]] or [[YAxis]]
   * @param acc   the merged prefix, in reverse order (start with `Nil`)
   * @return all the points of both lists, sorted by the axis
   */
  @tailrec
  private def mergeAcc(
                        left: List[Point],
                        right: List[Point],
                        axis: Int,
                        acc: List[Point]
                      ): List[Point] = (left, right) match {
    case (Nil, _) => ListUtils.reverseAppend(acc, right)
    case (_, Nil) => ListUtils.reverseAppend(acc, left)
    case (l :: leftTail, r :: rightTail) =>
      if (coordinate(l, axis) <= coordinate(r, axis)) mergeAcc(leftTail, right, axis, l :: acc)
      else mergeAcc(left, rightTail, axis, r :: acc)
  }

  /**
   * Merges two lists that are already sorted by the given axis.
   *
   * @param left  first sorted list
   * @param right second sorted list
   * @param axis  [[XAxis]] or [[YAxis]]
   * @return all the points of both lists, sorted by the axis
   */
  def mergeByAxis(left: List[Point], right: List[Point], axis: Int): List[Point] =
    mergeAcc(left, right, axis, Nil)

  /**
   * Merge sort of points by one coordinate.
   *
   * @param points the points to sort
   * @param axis   [[XAxis]] or [[YAxis]]
   * @return the points in increasing order of the selected coordinate
   */
  def sortByAxis(points: List[Point], axis: Int): List[Point] = points match {
    case Nil | _ :: Nil => points
    case _ =>
      val (left, right) = ListUtils.splitAt(points, ListUtils.size(points) / 2)
      mergeByAxis(sortByAxis(left, axis), sortByAxis(right, axis), axis)
  }

  /**
   * Selects, keeping their order, the points whose squared x-distance to the
   * middle line is strictly smaller than `bound`.
   *
   * @param points the points to inspect
   * @param midX   the x coordinate of the middle line
   * @param bound  the best squared distance found so far
   * @param acc    the selected points so far, in reverse order (start with `Nil`)
   * @return the points that can still improve the best distance
   */
  @tailrec
  private def stripAcc(points: List[Point], midX: Int, bound: Double, acc: List[Point]): List[Point] =
    points match {
      case Nil => ListUtils.reverse(acc)
      case p :: rest =>
        val dx = coordinate(p, XAxis).toDouble - midX.toDouble
        if (dx * dx < bound) stripAcc(rest, midX, bound, p :: acc)
        else stripAcc(rest, midX, bound, acc)
    }

  /**
   * Builds the strip around the middle line.
   *
   * @param points the points, normally sorted by y
   * @param midX   the x coordinate of the middle line
   * @param bound  the best squared distance found so far
   * @return the points within the strip, in the same order as the input
   */
  def stripPoints(points: List[Point], midX: Int, bound: Double): List[Point] =
    stripAcc(points, midX, bound, Nil)

  /**
   * Compares `p` with the points that follow it in a y-sorted strip. It stops as
   * soon as the y-distance alone is not smaller than the best distance, so only a
   * constant number of points are inspected.
   *
   * @param p    the reference point
   * @param rest the points after `p`, sorted by y
   * @param best the best squared distance known so far
   * @return the minimum between `best` and the distances from `p` to `rest`
   */
  @tailrec
  private def compareAhead(p: Point, rest: List[Point], best: Double): Double = rest match {
    case Nil => best
    case q :: tail =>
      val dy = coordinate(q, YAxis).toDouble - coordinate(p, YAxis).toDouble
      if (dy * dy >= best) best
      else compareAhead(p, tail, math.min(best, distanceSquared(p, q)))
  }

  /**
   * Scans a y-sorted strip looking for a pair closer than `best`.
   *
   * @param strip the points of the strip, sorted by y
   * @param best  the best squared distance known so far
   * @return min(best, d0)^2, where d0 is the closest pair found in the strip
   */
  @tailrec
  def scanStrip(strip: List[Point], best: Double): Double = strip match {
    case Nil       => best
    case p :: rest => scanStrip(rest, compareAhead(p, rest, best))
  }

  /**
   * Recursive step of the algorithm.
   *
   * @param sortedByX the points, sorted by x
   * @param size      the number of points of `sortedByX`
   * @return the same points sorted by y and the minimum squared distance among them
   */
  def closestRec(sortedByX: List[Point], size: Int): (List[Point], Double) =
    if (size <= BaseCaseSize)
      (sortByAxis(sortedByX, YAxis), bruteForce(sortedByX, Double.PositiveInfinity))
    else {
      val leftSize = size / 2
      ListUtils.splitAt(sortedByX, leftSize) match {
        case (leftHalf, rightHalf @ (first :: _)) =>
          val midX                     = coordinate(first, XAxis)
          val (leftByY, leftMin)       = closestRec(leftHalf, leftSize)
          val (rightByY, rightMin)     = closestRec(rightHalf, size - leftSize)
          val best                     = math.min(leftMin, rightMin)
          val byY                      = mergeByAxis(leftByY, rightByY, YAxis)
          (byY, scanStrip(stripPoints(byY, midX, best), best))
        case _ =>
          (sortByAxis(sortedByX, YAxis), bruteForce(sortedByX, Double.PositiveInfinity))
      }
    }

  /**
   * Rounds a number to four decimal digits.
   *
   * @param value the number to round
   * @return `value` rounded to the nearest multiple of 0.0001
   */
  def roundToFourDecimals(value: Double): Double = math.round(value * 10000.0) / 10000.0

  /**
   * Smallest Euclidean distance between two different points of the set.
   * Two points with the same coordinates are different points, so they give 0.
   *
   * @param points the points, each one a list `List(x, y)`; at least two are required
   * @return the minimum distance, rounded to four decimal digits
   * @throws IllegalArgumentException if there are fewer than two points or a point is malformed
   */
  def closestDistance(points: List[Point]): Double = points match {
    case _ :: _ :: _ =>
      val sortedByX    = sortByAxis(points, XAxis)
      val (_, minimum) = closestRec(sortedByX, ListUtils.size(points))
      roundToFourDecimals(math.sqrt(minimum))
    case _ =>
      throw new IllegalArgumentException("At least two points are required")
  }
}