import Algorithms.PseudoRandom

import scala.annotation.tailrec

/** Unit tests for the pure pseudo-random generator used to pick pivots. */
class PseudoRandomSuite extends munit.FunSuite {

  test("next is deterministic for the same seed") {
    assertEquals(PseudoRandom.next(42L), PseudoRandom.next(42L))
  }

  test("next gives different values for different seeds") {
    assert(PseudoRandom.next(1L) != PseudoRandom.next(2L))
  }

  test("next does not get stuck on a fixed point") {
    assert(PseudoRandom.next(0L) != 0L)
    assert(PseudoRandom.next(PseudoRandom.next(0L)) != PseudoRandom.next(0L))
  }

  test("indexBelow is deterministic") {
    assertEquals(PseudoRandom.indexBelow(7L, 100), PseudoRandom.indexBelow(7L, 100))
  }

  test("indexBelow with bound one always returns zero") {
    assertEquals(PseudoRandom.indexBelow(123L, 1), 0)
    assertEquals(PseudoRandom.indexBelow(-5L, 1), 0)
  }

  test("indexBelow stays inside the range for many seeds, including negative ones") {
    @tailrec
    def check(seed: Long, remaining: Int): Unit =
      if (remaining > 0) {
        val index = PseudoRandom.indexBelow(seed, 17)
        assert(index >= 0 && index < 17)
        check(PseudoRandom.next(seed), remaining - 1)
      }
    check(Long.MinValue, 5000)
    check(-1L, 5000)
  }

  test("indexBelow eventually reaches every value of a small range") {
    @tailrec
    def collect(seed: Long, remaining: Int, seen: Set[Int]): Set[Int] =
      if (remaining == 0) seen
      else collect(PseudoRandom.next(seed), remaining - 1, seen + PseudoRandom.indexBelow(seed, 10))
    assertEquals(collect(1L, 1000, Set.empty), Set(0, 1, 2, 3, 4, 5, 6, 7, 8, 9))
  }

}