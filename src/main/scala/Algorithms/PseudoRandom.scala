package Algorithms

/**
 * Pure pseudo-random numbers
 *
 * There is no hidden state: the next value is a function of the seed, so the same
 * seed always gives the same result. This keeps the randomized pivot selection of
 * quick sort referentially transparent and the experiments reproducible.
 */
object PseudoRandom {

  /**
   * Computes the next seed of the sequence.
   *
   * @param seed the current seed
   * @return a well mixed 64-bit value derived from `seed`
   */
  def next(seed: Long): Long = {
    val z0 = seed + 0x9e3779b97f4a7c15L
    val z1 = (z0 ^ (z0 >>> 30)) * 0xbf58476d1ce4e5b9L
    val z2 = (z1 ^ (z1 >>> 27)) * 0x94d049bb133111ebL
    z2 ^ (z2 >>> 31)
  }

  /**
   * Picks a pseudo-random index in `[0, bound)`.
   *
   * @param seed  the seed that determines the index
   * @param bound the exclusive upper limit, which must be positive
   * @return an index between 0 (inclusive) and `bound` (exclusive)
   */
  def indexBelow(seed: Long, bound: Int): Int =
    ((next(seed) >>> 1) % bound.toLong).toInt
}