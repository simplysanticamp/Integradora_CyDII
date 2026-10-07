import scala.annotation.tailrec

/**
 * Randomized quick sort with a 2-way partition (baseline of the project).
 *
 * The pivot is chosen pseudo-randomly with a pure generator, so the function stays
 * pure and deterministic for a given seed. With many repeated elements this version
 * degrades.
 */
object QuickSort {

  /** Seed used when the caller does not provide one. */
  val DefaultSeed: Long = 2026L

  /**
   * Splits a list around a pivot in a single pass.
   *
   * @param input             the elements to classify
   * @param pivot             the pivot value
   * @param smallerAcc        elements strictly smaller than the pivot, in reverse order
   * @param greaterOrEqualAcc elements greater than or equal to the pivot, in reverse order
   * @return the elements smaller than the pivot and the elements greater than or equal
   *         to it, both in their original relative order
   */
  @tailrec
  def partition2(
                  input: List[Int],
                  pivot: Int,
                  smallerAcc: List[Int],
                  greaterOrEqualAcc: List[Int]
                ): (List[Int], List[Int]) = input match {
    case Nil => (ListUtils.reverse(smallerAcc), ListUtils.reverse(greaterOrEqualAcc))
    case head :: tail =>
      if (head < pivot) partition2(tail, pivot, head :: smallerAcc, greaterOrEqualAcc)
      else partition2(tail, pivot, smallerAcc, head :: greaterOrEqualAcc)
  }

  /**
   * Sorts a list in increasing order choosing each pivot pseudo-randomly.
   *
   * @param input the list to sort
   * @param seed  the seed that determines every pivot choice
   * @return the elements of `input` in increasing order
   */
  def quickSort(input: List[Int], seed: Long): List[Int] = input match {
    case Nil      => Nil
    case _ :: Nil => input
    case _ =>
      val index = PseudoRandom.indexBelow(seed, ListUtils.size(input))
      ListUtils.extractAt(input, index) match {
        case Some((pivot, rest)) =>
          val (smaller, greaterOrEqual) = partition2(rest, pivot, Nil, Nil)
          val leftSeed                  = PseudoRandom.next(seed)
          val rightSeed                 = PseudoRandom.next(leftSeed)
          ListUtils.append(
            quickSort(smaller, leftSeed),
            pivot :: quickSort(greaterOrEqual, rightSeed)
          )
        case None => input // unreachable: the index is always inside the list
      }
  }

  /**
   * Sorts a list in increasing order using [[DefaultSeed]].
   *
   * @param input the list to sort
   * @return the elements of `input` in increasing order
   */
  def quickSort(input: List[Int]): List[Int] = quickSort(input, DefaultSeed)
}