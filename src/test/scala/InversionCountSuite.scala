class InversionCountSuite extends munit.FunSuite {
  // --- countInversions: examples from the statement ---

  test("example 1 from the statement") {
    assertEquals(InversionCount.countInversions(List(2, 3, 9, 2, 9)), 2L)
  }

  test("example 2: strictly decreasing") {
    assertEquals(InversionCount.countInversions(List(9, 7, 5, 3)), 6L)
  }

  // --- countInversions: edge cases ---

  test("single element has no inversions") {
    assertEquals(InversionCount.countInversions(List(5)), 0L)
  }

  test("two elements in order") {
    assertEquals(InversionCount.countInversions(List(1, 2)), 0L)
  }

  test("two elements inverted") {
    assertEquals(InversionCount.countInversions(List(2, 1)), 1L)
  }

  test("sorted list has no inversions") {
    assertEquals(InversionCount.countInversions(List(1, 2, 3, 4, 5)), 0L)
  }

  test("all equal elements have no inversions") {
    assertEquals(InversionCount.countInversions(List(4, 4, 4, 4)), 0L)
  }

  test("duplicates: equal elements do not count as inversions") {
    assertEquals(InversionCount.countInversions(List(2, 2, 1)), 2L)
  }

  test("negative numbers") {
    assertEquals(InversionCount.countInversions(List(-1, -5, 3, -2)), 3L)
  }

  test("odd length list") {
    assertEquals(InversionCount.countInversions(List(3, 1, 2)), 2L)
  }

  test("even length list") {
    assertEquals(InversionCount.countInversions(List(4, 3, 2, 1)), 6L)
  }

  // --- merge ---

  test("merge counts cross pairs and returns sorted list") {
    val (sorted, count) = InversionCount.merge(List(1, 4, 6), List(2, 3, 7))
    assertEquals(sorted, List(1, 2, 3, 4, 6, 7))
    assertEquals(count, 4L)
  }

  test("merge with an empty list") {
    assertEquals(InversionCount.merge(Nil, List(1, 2)), (List(1, 2), 0L))
    assertEquals(InversionCount.merge(List(1, 2), Nil), (List(1, 2), 0L))
  }

  test("merge does not count equal elements") {
    assertEquals(InversionCount.merge(List(2, 2), List(2)), (List(2, 2, 2), 0L))
  }

  test("merge when every left element is greater than every right element") {
    assertEquals(InversionCount.merge(List(3, 3), List(1, 2)), (List(1, 2, 3, 3), 4L))
  }

  // --- mergeSort ---

  test("mergeSort returns sorted list and count") {
    val (sorted, count) = InversionCount.mergeSort(List(3, 1, 2))
    assertEquals(sorted, List(1, 2, 3))
    assertEquals(count, 2L)
  }

  test("mergeSort of a decreasing list") {
    val (sorted, count) = InversionCount.mergeSort(List(5, 4, 3, 2, 1))
    assertEquals(sorted, List(1, 2, 3, 4, 5))
    assertEquals(count, 10L)
  }

  // --- large input ---

  test("large descending list does not overflow Int") {
    val n = 100000
    val l = List.range(n, 0, -1)
    assertEquals(InversionCount.countInversions(l), n.toLong * (n - 1) / 2)
  }

}
