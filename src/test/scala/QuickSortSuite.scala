/** Unit tests for the randomized quick sort with a 2-way partition. */
class QuickSortSuite extends munit.FunSuite {

  test("partition2 of an empty list") {
    assertEquals(QuickSort.partition2(Nil, 5, Nil, Nil), (Nil, Nil))
  }

  test("partition2 puts the pivot value in the greater-or-equal part") {
    assertEquals(QuickSort.partition2(List(5), 5, Nil, Nil), (Nil, List(5)))
  }

  test("partition2 keeps the original order inside each part") {
    assertEquals(
      QuickSort.partition2(List(1, 2, 3, 4, 5), 3, Nil, Nil),
      (List(1, 2), List(3, 4, 5))
    )
    assertEquals(
      QuickSort.partition2(List(5, 4, 3, 2, 1), 3, Nil, Nil),
      (List(2, 1), List(5, 4, 3))
    )
  }

  test("partition2 with every element equal to the pivot") {
    assertEquals(
      QuickSort.partition2(List(2, 2, 2, 2, 2), 2, Nil, Nil),
      (Nil, List(2, 2, 2, 2, 2))
    )
  }

  test("quickSort of an empty list") {
    assertEquals(QuickSort.quickSort(Nil), Nil)
  }

  test("quickSort of a single element") {
    assertEquals(QuickSort.quickSort(List(5)), List(5))
  }

  test("quickSort of an already sorted list") {
    assertEquals(QuickSort.quickSort(List(1, 2, 3, 4, 5)), List(1, 2, 3, 4, 5))
  }

  test("quickSort of a reversed list") {
    assertEquals(QuickSort.quickSort(List(5, 4, 3, 2, 1)), List(1, 2, 3, 4, 5))
  }

  test("quickSort with repeated elements") {
    assertEquals(QuickSort.quickSort(List(2, 1, 2, 1, 2)), List(1, 1, 2, 2, 2))
  }

  test("quickSort with negative numbers") {
    assertEquals(QuickSort.quickSort(List(3, -1, -7, 0, 3, -1)), List(-7, -1, -1, 0, 3, 3))
  }

  test("quickSort gives the same result for any seed") {
    val input    = List(9, 3, 7, 3, 1, 8, 2, 8, 0, -4)
    val expected = List(-4, 0, 1, 2, 3, 3, 7, 8, 8, 9)
    assertEquals(QuickSort.quickSort(input, 1L), expected)
    assertEquals(QuickSort.quickSort(input, 99L), expected)
    assertEquals(QuickSort.quickSort(input, -12345L), expected)
  }

  test("quickSort is deterministic for a fixed seed") {
    val input = List(5, 2, 9, 1, 5, 6)
    assertEquals(QuickSort.quickSort(input, 3L), QuickSort.quickSort(input, 3L))
  }

  test("quickSort agrees with the library sort on a pseudo-random list") {
    val input = List.range(0, 2000).map(i => (i * 7919 + 13) % 1013 - 500)
    assertEquals(QuickSort.quickSort(input), input.sorted)
  }

  test("quickSort of a large sorted list does not overflow the stack") {
    val input = List.range(0, 100000)
    assertEquals(QuickSort.quickSort(input), input)
  }

  test("quickSort of a large reversed list does not overflow the stack") {
    val input = List.range(100000, 0, -1)
    assertEquals(QuickSort.quickSort(input), List.range(1, 100001))
  }

}