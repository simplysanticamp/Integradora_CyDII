package Experiments

/**
 * The algorithms that are measured in the experiment.
 *
 * @param label the name written in the CSV file
 */
enum Algorithm(val label: String) {
  case QuickSortTwoWay extends Algorithm("quicksort_2way")
  case QuickSortThreeWay extends Algorithm("quicksort_3way")
  case InversionCount extends Algorithm("inversion_count")
  case ClosestPoints extends Algorithm("closest_points")
}

/**
 * The kinds of input used in the experiment.
 *
 * @param label the name written in the CSV file and in the input file names
 */
enum Scenario(val label: String) {
  /** Integers spread over a wide range (few repeated values). */
  case Random extends Scenario("random")

  /** Integers taken from only a handful of distinct values (the 3-way partition case). */
  case FewDistinct extends Scenario("few_distinct")

  /** Distinct integers already in increasing order. */
  case Sorted extends Scenario("sorted")

  /** Distinct integers in decreasing order (maximum number of inversions). */
  case Reversed extends Scenario("reversed")

  /** Points on the plane, each one a list of two integers. */
  case Points extends Scenario("points")
}
