# Test design

All tests use munit.

## Strategy

Each algorithm is tested in four main ways:

1. **Statement examples.** The examples from the assignment are used as a baseline.
2. **Edge cases.** Empty and single-element lists, repeated values, negative numbers, sorted and reversed lists, and extreme `Int` values are tested.
3. **Reference comparison.** The results are compared with an independent reference, such as library `sorted` or an O(n²) brute-force solution. The pseudo-random inputs use fixed seeds, so the tests can be reproduced.
4. **Large inputs.** Lists with 10⁵ to 10⁶ elements are used to check stack safety and make sure counters do not overflow `Int`.

Helper functions such as merge, partition and strip are public and tested separately. This makes it easier to identify which part of the algorithm is causing a failure.

## Problem 1 — `InversionCountSuite`

| Scenario | Input | Expected |
| -------- | ----- | -------- |
| Statement example 1 | `2, 3, 9, 2, 9` | 2 |
| Statement example 2 (strictly decreasing) | `9, 7, 5, 3` | 6 |
| Single element / sorted / two elements | `5` / `1,2,3,4,5` / `2,1` | 0 / 0 / 1 |
| All equal (equal values are not inversions) | `4, 4, 4, 4` | 0 |
| Duplicates mixed with a smaller value | `2, 2, 1` | 2 |
| Negative numbers | `-1, -5, 3, -2` | 3 |
| Odd and even lengths | `3, 1, 2` / `4, 3, 2, 1` | 2 / 6 |
| `merge` counts cross pairs and returns the sorted list | `[1,4,6]` + `[2,3,7]` | `[1,2,3,4,6,7]`, 4 |
| `merge` with an empty side, equal elements, and all-greater left | various | see suite |
| `mergeSort` returns the sorted list and count | `3, 1, 2` / `5,4,3,2,1` | `[1,2,3]`, 2 / `[1..5]`, 10 |
| Large descending list (n = 100000) must not overflow `Int` | `100000 … 1` | n(n−1)/2 as `Long` |

## Problem 2 — `QuickSortSuite`, `QuickSort3Suite`, `PseudoRandomSuite`

| Scenario | Applies to | Expected |
| -------- | ---------- | -------- |
| Empty list and single element | both sorts | unchanged |
| Sorted, reversed, repeated and negative inputs | both sorts | increasing order |
| Statement example 2 (`4,4,1,9,4,1,4,4`) | `quickSort3` | `1,1,4,4,4,4,4,9` |
| `partition2`: pivot goes to the greater-or-equal part and order is kept | `QuickSort` | see suite |
| `partition3`: three parts (`<`, `=`, `>`), all elements equal to the pivot, accumulators respected, and no elements lost | `QuickSort3` | see suite |
| Same result for any seed and same result for the same seed | both sorts | identical output |
| Agreement with library `sorted` on 2000 pseudo-random values | both sorts | identical output |
| `quickSort3` agrees with 2-way `quickSort` on data with few distinct values | `QuickSort3` | identical output |
| Sorted and reversed lists of 100000 elements | both sorts | no `StackOverflowError` |
| One million equal elements; 300000 elements with 5 distinct values | `QuickSort3` | correct length, first and last |
| `PseudoRandom`: deterministic, no fixed point, `indexBelow` always in range, and reaches every value in a small range | `PseudoRandom` | see suite |

## Problem 3 — `ClosestPointsSuite`

| Scenario | Input | Expected |
| -------- | ----- | -------- |
| Statement example 1 | `(0,0), (3,4)` | 5.0 |
| Statement example 2 | `(0,0), (3,4), (1,1)` | 1.4142 |
| Same coordinates | `(2,2), (2,2)` | 0.0 |
| Vertical line / horizontal line | same x / same y | smallest gap |
| Negative coordinates | `(-5,-5), (-1,-2), (-4,-9), (-8,-1)` | 4.1231 |
| Result rounded to four decimals | `(0,0), (1,1)` | 1.4142 |
| Input order does not matter | list and its reverse | same result |
| Extreme `Int` values | `(MinValue,0), (MaxValue,0)` | 4294967295.0 |
| Fewer than two points, or an invalid point | `[(1,1)]`, `[]`, `[(1,2,3),(4,5)]` | `IllegalArgumentException` |
| Closest pair crosses the dividing line | `(0,0),(4,50),(9,100),(10,2)` | 10.198 |
| Crossing pair among many points on both sides | two columns plus `(9,150),(11,151)` | 2.2361 |
| Reference comparison | 50 small inputs, 30 dense inputs with repeated coordinates, 500 wide-range points | same as brute force |
| Large inputs | 100000 collinear points; 200000 random points | 10.0; no stack overflow |
| Helpers: `coordinate`, `distanceSquared`, `bruteForce`, `mergeByAxis`, `sortByAxis`, `stripPoints`, `scanStrip`, `closestRec` | unit cases | see suite |

## Shared helpers — `ListUtilsSuite`

`size`, `reverse`, `reverseAppend`, `append`, `splitAt` and `extractAt` are tested with empty lists, boundary cases (`n = 0`, `n = length`, `n > length`, and out-of-range indexes), order preservation, and different element types.

One test runs all of them on one million elements to make sure they are stack safe.