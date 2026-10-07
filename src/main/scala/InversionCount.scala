import scala.annotation.tailrec

/**
 * Number of inversions of a sequence, computed with a modified merge sort.
 *
 * An inversion is a pair of indices `i < j` with `a(i) > a(j)`. Merge and MergeSort
 * return the sorted list together with the number of inversions found.
 */
object InversionCount {

  /**
   * Merges two sorted lists while counting the cross inversions.
   *
   * @param left       the remaining elements of the sorted left list
   * @param leftSize   the number of elements of `left`
   * @param right      the remaining elements of the sorted right list
   * @param acc        the merged prefix, in reverse order
   * @param inversions the cross inversions counted so far
   * @return the merged sorted list and the total number of cross inversions
   */
  @tailrec
  private def mergeAcc(
                        left: List[Int],
                        leftSize: Int,
                        right: List[Int],
                        acc: List[Int],
                        inversions: Long
                      ): (List[Int], Long) = (left, right) match {
    case (Nil, _) => (ListUtils.reverseAppend(acc, right), inversions)
    case (_, Nil) => (ListUtils.reverseAppend(acc, left), inversions)
    case (leftHead :: leftTail, rightHead :: rightTail) =>
      if (leftHead <= rightHead)
        mergeAcc(leftTail, leftSize - 1, right, leftHead :: acc, inversions)
      else
        mergeAcc(left, leftSize, rightTail, rightHead :: acc, inversions + leftSize)
  }

  /**
   * Merges two sorted lists and counts the pairs (b, c) with b in `left`, c in `right` and b > c.
   *
   * @param left  a sorted list
   * @param right a sorted list
   * @return the merged sorted list and the number of cross inversions
   */
  def merge(left: List[Int], right: List[Int]): (List[Int], Long) =
    mergeAcc(left, ListUtils.size(left), right, Nil, 0L)

  /**
   * Sorts a list and counts its inversions.
   *
   * @param l the list to sort
   * @return the sorted list and the number of inversions of `l`
   */
  def mergeSort(l: List[Int]): (List[Int], Long) = l match {
    case Nil      => (Nil, 0L)
    case _ :: Nil => (l, 0L)
    case _ =>
      val (left, right)             = ListUtils.splitAt(l, ListUtils.size(l) / 2)
      val (sortedLeft, leftCount)   = mergeSort(left)
      val (sortedRight, rightCount) = mergeSort(right)
      val (merged, crossCount)      = merge(sortedLeft, sortedRight)
      (merged, leftCount + rightCount + crossCount)
  }

  /**
   * Counts the inversions of a sequence.
   *
   * @param l the sequence
   * @return the number of pairs (i, j) with i < j and l(i) > l(j)
   */
  def countInversions(l: List[Int]): Long = mergeSort(l)._2
}