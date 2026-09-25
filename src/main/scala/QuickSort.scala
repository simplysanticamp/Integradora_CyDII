import scala.annotation.tailrec

object QuickSort {

  // Elementos de inputList estrictamente menores que p (conserva el orden original)
  def menores(inputList: List[Int], p: Int): List[Int] = {
    @tailrec
    def aux(l: List[Int], acc: List[Int]): List[Int] = l match {
      case Nil                  => acc.reverse
      case h :: t if h < p      => aux(t, h :: acc)
      case _ :: t               => aux(t, acc)
    }
    aux(inputList, Nil)
  }

  // Elementos de inputList mayores o iguales que p (conserva el orden original)
  def mayores(inputList: List[Int], p: Int): List[Int] = {
    @tailrec
    def aux(l: List[Int], acc: List[Int]): List[Int] = l match {
      case Nil                  => acc.reverse
      case h :: t if h >= p     => aux(t, h :: acc)
      case _ :: t               => aux(t, acc)
    }
    aux(inputList, Nil)
  }

  // Concatena inputL1 ++ inputL2 con recursión de cola
  def appendTR(inputL1: List[Int], inputL2: List[Int]): List[Int] = {
    @tailrec
    def reverso(l: List[Int], acc: List[Int]): List[Int] = l match {
      case Nil    => acc
      case h :: t => reverso(t, h :: acc)
    }
    @tailrec
    def pegar(rev1: List[Int], acc: List[Int]): List[Int] = rev1 match {
      case Nil    => acc
      case h :: t => pegar(t, h :: acc)
    }
    pegar(reverso(inputL1, Nil), inputL2)
  }

  // Parte inputList en una sola pasada: (menores que p, mayores o iguales que p)
  // Se llama inicialmente con menoresAcc = Nil y mayoresAcc = Nil
  @tailrec
  def separar(
               inputList: List[Int],
               p: Int,
               menoresAcc: List[Int],
               mayoresAcc: List[Int]
             ): (List[Int], List[Int]) = inputList match {
    case Nil              => (menoresAcc.reverse, mayoresAcc.reverse)
    case h :: t if h < p  => separar(t, p, h :: menoresAcc, mayoresAcc)
    case h :: t           => separar(t, p, menoresAcc, h :: mayoresAcc)
  }

  // Ordena ascendentemente usando la cabeza como pivote
  def quickSort(inputList: List[Int]): List[Int] = inputList match {
    case Nil          => Nil
    case p :: resto   =>
      val (men, may) = separar(resto, p, Nil, Nil)
      appendTR(quickSort(men), p :: quickSort(may))
  }

}
