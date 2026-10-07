import scala.annotation.tailrec

/**
 * Randomized quick sort with a 3-way partition (less than, equal to, greater than the pivot).
 *
 * All the elements equal to the pivot are placed in their final position at once and
 * never take part in the recursive calls, so inputs with few distinct values are
 * sorted much faster than with the 2-way partition of [[QuickSort]].
 */
object QuickSort3 {

  /** Seed used when the caller does not provide one. */
  val DefaultSeed: Long = 2026L

  /**
   * Splits a list into three parts around a pivot in a single pass.
   *
   * @param inputList  the elements to classify
   * @param p          the pivot value
   * @param smallerAcc elements strictly smaller than `p` found so far
   * @param equalAcc   elements equal to `p` found so far
   * @param greaterAcc elements strictly greater than `p` found so far
   * @return the elements smaller than, equal to and greater than the pivot
   *         (the order inside each part is not preserved)
   */
  @tailrec
  def partition3(
                  inputList: List[Int],
                  p: Int,
                  smallerAcc: List[Int],
                  equalAcc: List[Int],
                  greaterAcc: List[Int]
                ): (List[Int], List[Int], List[Int]) = inputList match {
    case Nil => (smallerAcc, equalAcc, greaterAcc)
    case head :: tail =>
      if (head < p) partition3(tail, p, head :: smallerAcc, equalAcc, greaterAcc)
      else if (head == p) partition3(tail, p, smallerAcc, head :: equalAcc, greaterAcc)
      else partition3(tail, p, smallerAcc, equalAcc, head :: greaterAcc)
  }

  /**
   * Sorts a list in increasing order choosing each pivot pseudo-randomly.
   *
   * @param inputList the list to sort
   * @param seed      the seed that determines every pivot choice
   * @return the elements of `inputList` in increasing order
   */
  def quickSort3(inputList: List[Int], seed: Long): List[Int] = inputList match {
    case Nil      => Nil
    case _ :: Nil => inputList
    case _ =>
      val index = PseudoRandom.indexBelow(seed, ListUtils.size(inputList))
      ListUtils.extractAt(inputList, index) match {
        case Some((pivot, rest)) =>
          val (smaller, equal, greater) = partition3(rest, pivot, Nil, Nil, Nil)
          val leftSeed                  = PseudoRandom.next(seed)
          val rightSeed                 = PseudoRandom.next(leftSeed)
          ListUtils.append(
            quickSort3(smaller, leftSeed),
            ListUtils.append(pivot :: equal, quickSort3(greater, rightSeed))
          )
        case None => inputList // unreachable: the index is always inside the list
      }
  }

  /**
   * Sorts a list in increasing order using [[DefaultSeed]].
   *
   * @param inputList the list to sort
   * @return the elements of `inputList` in increasing order
   */
  def quickSort3(inputList: List[Int]): List[Int] = quickSort3(inputList, DefaultSeed)
}