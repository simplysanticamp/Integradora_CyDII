import scala.annotation.tailrec

object InversionCount {


  @tailrec
  private def length(l: List[Int], acc: Int): Int = l match {
    case Nil       => acc
    case _ :: tail => length(tail, acc + 1)
  }


  @tailrec
  private def reverseAppend(reversed: List[Int], rest: List[Int]): List[Int] =
    reversed match {
      case Nil          => rest
      case head :: tail => reverseAppend(tail, head :: rest)
    }


  @tailrec
  private def split(l: List[Int], n: Int, front: List[Int]): (List[Int], List[Int]) =
    (l, n) match {
      case (Nil, _)          => (reverseAppend(front, Nil), Nil)
      case (_, 0)            => (reverseAppend(front, Nil), l)
      case (head :: tail, _) => split(tail, n - 1, head :: front)
    }


  @tailrec
  private def mergeAcc(
                        left: List[Int],
                        leftSize: Int,
                        right: List[Int],
                        acc: List[Int],
                        inversions: Long
                      ): (List[Int], Long) = (left, right) match {
    case (Nil, _) => (reverseAppend(acc, right), inversions)
    case (_, Nil) => (reverseAppend(acc, left), inversions)
    case (leftHead :: leftTail, rightHead :: rightTail) =>
      if (leftHead <= rightHead)
        mergeAcc(leftTail, leftSize - 1, right, leftHead :: acc, inversions)
      else
        mergeAcc(left, leftSize, rightTail, rightHead :: acc, inversions + leftSize)
  }

  def merge(left: List[Int], right: List[Int]): (List[Int], Long) =
    mergeAcc(left, length(left, 0), right, Nil, 0L)


  def mergeSort(l: List[Int]): (List[Int], Long) = l match {
    case Nil      => (Nil, 0L)
    case _ :: Nil => (l, 0L)
    case _ =>
      val (left, right)             = split(l, length(l, 0) / 2, Nil)
      val (sortedLeft, leftCount)   = mergeSort(left)
      val (sortedRight, rightCount) = mergeSort(right)
      val (merged, crossCount)      = merge(sortedLeft, sortedRight)
      (merged, leftCount + rightCount + crossCount)
  }


  def countInversions(l: List[Int]): Long = mergeSort(l)._2
}