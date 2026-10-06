import scala.annotation.tailrec

object QuickSort3 {

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

    def quickSort3(inputList: List[Int]): List[Int] = inputList match {
    case Nil => Nil
    case p :: rest =>
      val (smaller, equal, greater) = partition3(rest, p, Nil, Nil, Nil)
      QuickSort.appendTR(
        quickSort3(smaller),
        QuickSort.appendTR(p :: equal, quickSort3(greater))
      )
    }
}

