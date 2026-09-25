class QuickSort3Suite extends munit.FunSuite:

  // --- separar3 ---

  test("separar3 con lista vacia retorna tres listas vacias") {
    assertEquals(QuickSort3.separar3(Nil, 5, Nil, Nil, Nil), (Nil, Nil, Nil))
  }

  test("separar3 clasifica menores, iguales y mayores respecto al pivote") {
    val (menores, iguales, mayores) =
      QuickSort3.separar3(List(3, 8, 3, 1, 8, 5), 5, Nil, Nil, Nil)
    assertEquals(menores.sorted, List(1, 3, 3))
    assertEquals(iguales, List(5))
    assertEquals(mayores.sorted, List(8, 8))
  }

  test("separar3 con todos los elementos iguales al pivote") {
    val (menores, iguales, mayores) =
      QuickSort3.separar3(List(4, 4, 4, 4), 4, Nil, Nil, Nil)
    assertEquals(menores, Nil)
    assertEquals(iguales.length, 4)
    assertEquals(mayores, Nil)
  }

  test("separar3 respeta los acumuladores recibidos") {
    val (menores, iguales, mayores) =
      QuickSort3.separar3(List(1, 6), 3, List(0), List(3), List(9))
    assertEquals(menores.sorted, List(0, 1))
    assertEquals(iguales, List(3))
    assertEquals(mayores.sorted, List(6, 9))
  }

  // --- quickSort3 ---

  test("quickSort3 con lista vacia") {
    assertEquals(QuickSort3.quickSort3(Nil), Nil)
  }

  test("quickSort3 con un solo elemento") {
    assertEquals(QuickSort3.quickSort3(List(7)), List(7))
  }

  test("quickSort3 con lista ya ordenada") {
    assertEquals(QuickSort3.quickSort3(List(1, 2, 3, 4, 5)), List(1, 2, 3, 4, 5))
  }

  test("quickSort3 con lista en orden inverso") {
    assertEquals(QuickSort3.quickSort3(List(5, 4, 3, 2, 1)), List(1, 2, 3, 4, 5))
  }

  test("quickSort3 con elementos repetidos") {
    assertEquals(QuickSort3.quickSort3(List(3, 1, 3, 2, 1)), List(1, 1, 2, 3, 3))
  }

  test("quickSort3 con muchos valores iguales al pivote") {
    assertEquals(
      QuickSort3.quickSort3(List(5, 5, 5, 5, 5, 5)),
      List(5, 5, 5, 5, 5, 5)
    )
  }

  test("quickSort3 con muchos valores repetidos y algunos distintos") {
    val entrada = List(4, 2, 4, 4, 4, 1, 4, 4, 9, 4)
    assertEquals(
      QuickSort3.quickSort3(entrada),
      List(1, 2, 4, 4, 4, 4, 4, 4, 4, 9)
    )
  }