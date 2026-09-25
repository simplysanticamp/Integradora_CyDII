class QuickSort3 {
    def separar3(
                  inputList: List[Int],
                  p: Int,
                  menoresAcc: List[Int],
                  igualesAcc: List[Int],
                  mayoresAcc: List[Int]
                ): (List[Int], List[Int], List[Int]) =
      inputList match
        case Nil => (menoresAcc, igualesAcc, mayoresAcc)
        case head :: tail =>
          if head < p then
            separar3(tail, p, head :: menoresAcc, igualesAcc, mayoresAcc)
          else if head == p then
            separar3(tail, p, menoresAcc, head :: igualesAcc, mayoresAcc)
          else
            separar3(tail, p, menoresAcc, igualesAcc, head :: mayoresAcc)

    def quickSort3(inputList: List[Int]): List[Int] =
      inputList match
        case Nil => Nil
        case p :: rest =>
          val (menores, iguales, mayores) = separar3(rest, p, Nil, Nil, Nil)
          QuickSort.appendTR(
            quickSort3(menores),
            QuickSort.appendTR(p :: iguales, quickSort3(mayores))
          )
}

