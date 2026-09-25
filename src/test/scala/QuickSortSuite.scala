class QuickSortSuite extends munit.FunSuite {

  test("menores") {
    assertEquals(QuickSort.menores(List(), 5), List())
    assertEquals(QuickSort.menores(List(5), 5), List())
    assertEquals(QuickSort.menores(List(1, 2, 3, 4, 5), 3), List(1, 2))
    assertEquals(QuickSort.menores(List(5, 4, 3, 2, 1), 3), List(2, 1))
    assertEquals(QuickSort.menores(List(1, 2, 2, 3, 3), 2), List(1))
  }

  test("mayores") {
    assertEquals(QuickSort.mayores(List(), 5), List())
    assertEquals(QuickSort.mayores(List(5), 5), List(5))
    assertEquals(QuickSort.mayores(List(1, 2, 3, 4, 5), 3), List(3, 4, 5))
    assertEquals(QuickSort.mayores(List(5, 4, 3, 2, 1), 3), List(5, 4, 3))
    assertEquals(QuickSort.mayores(List(1, 2, 2, 3, 3), 2), List(2, 2, 3, 3))
  }

  test("appendTR") {
    assertEquals(QuickSort.appendTR(List(), List()), List())
    assertEquals(QuickSort.appendTR(List(), List(1, 2, 3)), List(1, 2, 3))
    assertEquals(QuickSort.appendTR(List(1, 2, 3), List()), List(1, 2, 3))
    assertEquals(QuickSort.appendTR(List(1, 2), List(3, 4)), List(1, 2, 3, 4))
  }

  test("separar") {
    assertEquals(QuickSort.separar(List(), 5, Nil, Nil), (List(), List()))
    assertEquals(QuickSort.separar(List(5), 5, Nil, Nil), (List(), List(5)))
    assertEquals(QuickSort.separar(List(1, 2, 3, 4, 5), 3, Nil, Nil), (List(1, 2), List(3, 4, 5)))
    assertEquals(QuickSort.separar(List(5, 4, 3, 2, 1), 3, Nil, Nil), (List(2, 1), List(5, 4, 3)))
    assertEquals(QuickSort.separar(List(2, 2, 2, 2, 2), 2, Nil, Nil), (List(), List(2, 2, 2, 2, 2)))
  }

  test("quickSort") {
    assertEquals(QuickSort.quickSort(List()), List())
    assertEquals(QuickSort.quickSort(List(5)), List(5))
    assertEquals(QuickSort.quickSort(List(1, 2, 3, 4, 5)), List(1, 2, 3, 4, 5))
    assertEquals(QuickSort.quickSort(List(5, 4, 3, 2, 1)), List(1, 2, 3, 4, 5))
    assertEquals(QuickSort.quickSort(List(2, 1, 2, 1, 2)), List(1, 1, 2, 2, 2))
  }

}
