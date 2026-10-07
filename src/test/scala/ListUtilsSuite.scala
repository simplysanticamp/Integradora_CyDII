/** Unit tests for the generic list helpers shared by all the algorithms. */
class ListUtilsSuite extends munit.FunSuite {

  // size 

  test("size of an empty list is zero") {
    assertEquals(ListUtils.size(Nil), 0)
  }

  test("size of a non-empty list") {
    assertEquals(ListUtils.size(List(7, 8, 9)), 3)
  }

  test("size works on any element type") {
    assertEquals(ListUtils.size(List("a", "b")), 2)
  }

  //reverse

  test("reverse of an empty list") {
    assertEquals(ListUtils.reverse(List.empty[Int]), Nil)
  }

  test("reverse of a single element list") {
    assertEquals(ListUtils.reverse(List(1)), List(1))
  }

  test("reverse of a list with several elements") {
    assertEquals(ListUtils.reverse(List(1, 2, 3, 4)), List(4, 3, 2, 1))
  }

  //reverseAppend

  test("reverseAppend prepends the reversed first list to the second") {
    assertEquals(ListUtils.reverseAppend(List(3, 2, 1), List(4, 5)), List(1, 2, 3, 4, 5))
  }

  test("reverseAppend with an empty first list returns the second") {
    assertEquals(ListUtils.reverseAppend(Nil, List(4, 5)), List(4, 5))
  }

  // append

  test("append of two empty lists") {
    assertEquals(ListUtils.append(List.empty[Int], Nil), Nil)
  }

  test("append with an empty left list") {
    assertEquals(ListUtils.append(Nil, List(1, 2)), List(1, 2))
  }

  test("append with an empty right list") {
    assertEquals(ListUtils.append(List(1, 2), Nil), List(1, 2))
  }

  test("append keeps the order of both lists") {
    assertEquals(ListUtils.append(List(1, 2), List(3, 4)), List(1, 2, 3, 4))
  }

  //splitAt

  test("splitAt zero leaves the front empty") {
    assertEquals(ListUtils.splitAt(List(1, 2, 3), 0), (Nil, List(1, 2, 3)))
  }

  test("splitAt in the middle keeps the original order") {
    assertEquals(ListUtils.splitAt(List(1, 2, 3, 4, 5), 2), (List(1, 2), List(3, 4, 5)))
  }

  test("splitAt the exact length leaves the back empty") {
    assertEquals(ListUtils.splitAt(List(1, 2, 3), 3), (List(1, 2, 3), Nil))
  }

  test("splitAt beyond the length returns the whole list in front") {
    assertEquals(ListUtils.splitAt(List(1, 2, 3), 10), (List(1, 2, 3), Nil))
  }

  test("splitAt on an empty list") {
    assertEquals(ListUtils.splitAt(List.empty[Int], 3), (Nil, Nil))
  }

  // stack safety

  test("all helpers are stack safe on one million elements") {
    val big = List.range(0, 1000000)
    assertEquals(ListUtils.size(big), 1000000)
    assertEquals(ListUtils.reverse(big).head, 999999)
    assertEquals(ListUtils.append(big, big).length, 2000000)
    val (front, back) = ListUtils.splitAt(big, 500000)
    assertEquals(front.length, 500000)
    assertEquals(back.head, 500000)
  }

}