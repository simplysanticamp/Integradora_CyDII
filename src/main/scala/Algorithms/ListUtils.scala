package Algorithms

import scala.annotation.tailrec

/**
 * Generic helpers over immutable lists, shared by all the algorithms.
 *
 * Every function is written with structural recursion and pattern matching.
 * The ones that start an iterative process are tail recursive, so they are
 * stack safe even with millions of elements.
 */
object ListUtils {

  /**
   * Counts the elements of a list.
   *
   * @param list the list to measure
   * @param acc  the number of elements already counted (start with 0)
   * @tparam A the element type
   * @return `acc` plus the number of elements of `list`
   */
  @tailrec
  def length[A](list: List[A], acc: Int): Int = list match {
    case Nil       => acc
    case _ :: tail => length(tail, acc + 1)
  }

  /**
   * Counts the elements of a list.
   *
   * @param list the list to measure
   * @tparam A the element type
   * @return the number of elements of `list`
   */
  def size[A](list: List[A]): Int = length(list, 0)

  /**
   * Prepends the reverse of `reversed` to `rest`.
   *
   * @param reversed the list whose elements are moved one by one onto `rest`
   * @param rest     the list that receives the elements
   * @tparam A the element type
   * @return `reversed` read backwards, followed by `rest`
   */
  @tailrec
  def reverseAppend[A](reversed: List[A], rest: List[A]): List[A] = reversed match {
    case Nil          => rest
    case head :: tail => reverseAppend(tail, head :: rest)
  }

  /**
   * Reverses a list.
   *
   * @param list the list to reverse
   * @tparam A the element type
   * @return the elements of `list` in the opposite order
   */
  def reverse[A](list: List[A]): List[A] = reverseAppend(list, Nil)

  /**
   * Concatenates two lists in constant stack space.
   *
   * @param first  the elements that come first
   * @param second the elements that come last
   * @tparam A the element type
   * @return `first` followed by `second`
   */
  def append[A](first: List[A], second: List[A]): List[A] =
    reverseAppend(reverse(first), second)

  /**
   * Splits a list after its first `n` elements.
   *
   * @param list  the list to split
   * @param n     the number of elements that go in the front part
   * @param front the reversed front part built so far (start with `Nil`)
   * @tparam A the element type
   * @return the first `n` elements (or all of them if there are fewer) and the rest
   */
  @tailrec
  def splitAt[A](list: List[A], n: Int, front: List[A]): (List[A], List[A]) =
    (list, n) match {
      case (Nil, _)          => (reverse(front), Nil)
      case (_, 0)            => (reverse(front), list)
      case (head :: tail, _) => splitAt(tail, n - 1, head :: front)
    }

  /**
   * Splits a list after its first `n` elements.
   *
   * @param list the list to split
   * @param n    the number of elements that go in the front part
   * @tparam A the element type
   * @return the first `n` elements (or all of them if there are fewer) and the rest
   */
  def splitAt[A](list: List[A], n: Int): (List[A], List[A]) = splitAt(list, n, Nil)

  /**
   * Removes the element at a given position.
   *
   * @param list  the list to take the element from
   * @param index the zero-based position of the element
   * @tparam A the element type
   * @return the element and the remaining elements in their original order,
   *         or `None` if `index` is out of range
   */
  def extractAt[A](list: List[A], index: Int): Option[(A, List[A])] =
    splitAt(list, index) match {
      case (front, element :: back) => Some((element, append(front, back)))
      case _                        => None
    }
}