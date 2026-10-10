package Experiments

/**
 * Measures the execution time of a block of code.
 */

object Timer {
    /**
     * Runs a block once and measures how long it takes.
     *
     * The block is passed by name, so it runs inside the measurement and the
     * time of building its arguments is not counted by the caller.
     *
     * @param block the computation to measure
     * @tparam A the type of its result
     * @return the result of the block and the elapsed time in milliseconds
     */
    def time[A](block: => A): (A, Double) = {
      val start = System.nanoTime()
      val result = block
      val elapsed = (System.nanoTime() - start) / 1000000.0
      (result, elapsed)
    }
  
}
