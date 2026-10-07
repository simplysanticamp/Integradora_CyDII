/** Unit tests for the randomized quick sort with a 3-way partition. */
class QuickSort3Suite extends munit.FunSuite {

  test("partition3 of an empty list returns three empty lists") {
    assertEquals(QuickSort3.partition3(Nil, 5, Nil, Nil, Nil), (Nil, Nil, Nil))
  }

  test("partition3 classifies smaller, equal and greater elements") {
    val (smaller, equal, greater) =
      QuickSort3.partition3(List(3, 8, 3, 1, 8, 5), 5, Nil, Nil, Nil)
    assertEquals(smaller.sorted, List(1, 3, 3))
    assertEquals(equal, List(5))
    assertEquals(greater.sorted, List(8, 8))
  }

  test("partition3 with every element equal to the pivot") {
    val (smaller, equal, greater) =
      QuickSort3.partition3(List(4, 4, 4, 4), 4, Nil, Nil, Nil)
    assertEquals(smaller, Nil)
    assertEquals(equal.length, 4)
    assertEquals(greater, Nil)
  }

  test("partition3 respects the accumulators it receives") {
    val (smaller, equal, greater) =
      QuickSort3.partition3(List(1, 6), 3, List(0), List(3), List(9))
    assertEquals(smaller.sorted, List(0, 1))
    assertEquals(equal, List(3))
    assertEquals(greater.sorted, List(6, 9))
  }

  test("partition3 keeps every element exactly once") {
    val input = List(7, 3, 7, 1, 9, 3, 5, 5, 0)
    val (smaller, equal, greater) = QuickSort3.partition3(input, 5, Nil, Nil, Nil)
    assertEquals((smaller ::: equal ::: greater).sorted, input.sorted)
  }

  test("quickSort3 of an empty list") {
    assertEquals(QuickSort3.quickSort3(Nil), Nil)
  }

  test("quickSort3 of a single element") {
    assertEquals(QuickSort3.quickSort3(List(7)), List(7))
  }

  test("quickSort3 of an already sorted list") {
    assertEquals(QuickSort3.quickSort3(List(1, 2, 3, 4, 5)), List(1, 2, 3, 4, 5))
  }

  test("quickSort3 of a reversed list") {
    assertEquals(QuickSort3.quickSort3(List(5, 4, 3, 2, 1)), List(1, 2, 3, 4, 5))
  }

  test("quickSort3 with repeated elements") {
    assertEquals(QuickSort3.quickSort3(List(3, 1, 3, 2, 1)), List(1, 1, 2, 3, 3))
  }

  test("quickSort3 with all elements equal") {
    assertEquals(QuickSort3.quickSort3(List(5, 5, 5, 5, 5, 5)), List(5, 5, 5, 5, 5, 5))
  }

  test("quickSort3 with many repeated values and a few different ones") {
    val input = List(4, 2, 4, 4, 4, 1, 4, 4, 9, 4)
    assertEquals(QuickSort3.quickSort3(input), List(1, 2, 4, 4, 4, 4, 4, 4, 4, 9))
  }

  test("quickSort3 example 2 from the statement") {
    assertEquals(
      QuickSort3.quickSort3(List(4, 4, 1, 9, 4, 1, 4, 4)),
      List(1, 1, 4, 4, 4, 4, 4, 9)
    )
  }

  test("quickSort3 with negative numbers") {
    assertEquals(QuickSort3.quickSort3(List(3, -1, -7, 0, 3, -1)), List(-7, -1, -1, 0, 3, 3))
  }

  test("quickSort3 gives the same result for any seed") {
    val input    = List(9, 3, 7, 3, 1, 8, 2, 8, 0, -4)
    val expected = List(-4, 0, 1, 2, 3, 3, 7, 8, 8, 9)
    assertEquals(QuickSort3.quickSort3(input, 1L), expected)
    assertEquals(QuickSort3.quickSort3(input, 99L), expected)
    assertEquals(QuickSort3.quickSort3(input, -12345L), expected)
  }

  test("quickSort3 agrees with the library sort on a pseudo-random list") {
    val input = List.range(0, 2000).map(i => (i * 7919 + 13) % 1013 - 500)
    assertEquals(QuickSort3.quickSort3(input), input.sorted)
  }

  test("quickSort3 agrees with the 2-way quickSort") {
    val input = List.range(0, 3000).map(i => (i * 31 + 7) % 50)
    assertEquals(QuickSort3.quickSort3(input), QuickSort.quickSort(input))
  }

  test("quickSort3 of a large sorted list does not overflow the stack") {
    val input = List.range(0, 100000)
    assertEquals(QuickSort3.quickSort3(input), input)
  }

  test("quickSort3 of a large reversed list does not overflow the stack") {
    val input = List.range(100000, 0, -1)
    assertEquals(QuickSort3.quickSort3(input), List.range(1, 100001))
  }

  test("quickSort3 of one million equal elements") {
    val input = List.fill(1000000)(8)
    assertEquals(QuickSort3.quickSort3(input).length, 1000000)
  }

  test("quickSort3 of a large list with only five distinct values") {
    val input  = List.range(0, 300000).map(i => (i * 7) % 5)
    val sorted = QuickSort3.quickSort3(input)
    assertEquals(sorted.length, 300000)
    assertEquals(sorted.head, 0)
    assertEquals(sorted.last, 4)
  }

}