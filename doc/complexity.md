# Complexity analysis

Notation: `n` is the number of elements (or points). `log` is base 2.

## Cost of the shared helpers (`ListUtils`)

All of them are tail recursive (constant stack) and walk the list once.

| Function | Time | Extra space |
| -------- | ---- | ----------- |
| `size`, `reverse` | Θ(n) | Θ(n) for `reverse`, Θ(1) for `size` |
| `append(a, b)` | Θ(\|a\|) | Θ(\|a\|) |
| `splitAt(l, k)` | Θ(min(k, n)) | Θ(k) |
| `extractAt(l, i)` | Θ(n) | Θ(n) |

The cost of any `ListUtils` call on a list of size `m` is therefore Θ(m). This is the
`Θ(n)` term of the recurrences below.

---

## Problem 1 — Number of inversions (`InversionCount`)

`mergeSort(l)`:

1. computes `size(l)` and splits `l` in two halves: Θ(n);
2. calls itself on each half;
3. `merge` of the two sorted halves, counting cross inversions: Θ(n)
   (each step of `mergeAcc` consumes one element).

**Recurrence**

```
T(n) = 2·T(n/2) + c·n      for n ≥ 2
T(0) = T(1) = c₀
```

(For odd `n` the halves are ⌊n/2⌋ and ⌈n/2⌉; the solution is the same.)

**Solution (recursion tree).** Level `i` has 2ⁱ subproblems of size n/2ⁱ, so each level costs
`2ⁱ · c·n/2ⁱ = c·n`. There are `log n` levels above the leaves, and the `n` leaves cost
`c₀·n`:

```
T(n) = c·n·log n + c₀·n = Θ(n log n)
```

**Check with the Master theorem.** `a = 2`, `b = 2`, `f(n) = Θ(n) = Θ(n^(log_b a))`
→ case 2 → `T(n) = Θ(n log n)`.

**Result:** time **Θ(n log n)** in every case (best, average and worst), versus
Θ(n²) for the naive pair check. Space: Θ(n) for the lists, recursion depth `log n`.
The counter is a `Long`, because the maximum is n(n−1)/2.

---

## Problem 2 — Quick sort with 3-way partition (`QuickSort3`)

Each call picks a pseudo-random pivot (`size` + `extractAt`: Θ(n)), runs `partition3` over
the remaining `n − 1` elements (Θ(n)), and recurses only on the **smaller** and the
**greater** parts. The elements equal to the pivot are final and are not sorted again.
If `k` elements are smaller and `r` are greater, then `k + r ≤ n − 1`.

**General recurrence**

```
T(n) = T(k) + T(r) + c·n       with k + r ≤ n − 1
T(0) = T(1) = c₀
```

**Worst case.** The pivot is always the minimum or the maximum of distinct values
(`k = 0`, `r = n − 1`):

```
T(n) = T(n−1) + c·n  ⇒  T(n) = c·(n + (n−1) + … + 2) + c₀ = Θ(n²)
```

With a random pivot this happens with negligible probability, and it no longer depends on
the input order (a sorted list is not a bad case).

**Best case (distinct values, balanced pivot).**

```
T(n) = 2·T(n/2) + c·n  ⇒  Θ(n log n)       (same solution as Problem 1)
```

**Average case (distinct values, random pivot).** Every rank `i = 0…n−1` is equally likely
for the pivot, so

```
T(n) = (1/n) · Σ_{i=0}^{n−1} [ T(i) + T(n−1−i) ] + c·n = (2/n) · Σ_{i=0}^{n−1} T(i) + c·n
```

Multiply by `n`, and subtract the same identity for `n − 1`:

```
n·T(n) − (n−1)·T(n−1) = 2·T(n−1) + c·(2n − 1)
n·T(n) = (n+1)·T(n−1) + c·(2n − 1)
T(n)/(n+1) = T(n−1)/n + c·(2n − 1)/(n·(n+1)) ≤ T(n−1)/n + 2c/(n+1)
```

Unrolling, `T(n)/(n+1) ≤ T(1)/2 + 2c·(H_{n+1} − 3/2)`, where `H_m = 1 + 1/2 + … + 1/m = Θ(log m)`.
Hence `T(n) = O(n log n)`, and since a comparison sort needs Ω(n log n),
**T(n) = Θ(n log n)** on average.

**Repeated values (the reason for the 3-way partition).** Let `d` be the number of distinct
values. All elements equal to the pivot leave the recursion at once, so the recursion behaves
as a sort of `d` distinct keys: expected time **O(n log d)**.
In particular, if all elements are equal, `k = r = 0` and

```
T(n) = Θ(n)      (a single partition)
```

**Comparison with the 2-way baseline (`QuickSort`).** The 2-way partition sends equal
elements to the greater side, so with all elements equal `k = 0`, `r = n − 1` at every call:
`T(n) = T(n−1) + c·n = Θ(n²)`, with recursion depth `n`. This is the case the assignment
asks to improve.

**Space.** Θ(n) for the lists; the recursion depth is O(log n) in expectation (O(n) in the
worst case).

| Case | `QuickSort3` | `QuickSort` (2-way) |
| ---- | ------------ | ------------------- |
| Distinct values, average | Θ(n log n) | Θ(n log n) |
| Distinct values, worst | Θ(n²) (negligible probability) | Θ(n²) (negligible probability) |
| All values equal | Θ(n) | Θ(n²) |
| `d` distinct values, expected | O(n log d) | up to Θ(n²) |

---

## Problem 3 — Closest points (`ClosestPoints`)

`closestDistance` first sorts the points by x with a merge sort: the same recurrence as
Problem 1, **Θ(n log n)**. Then `closestRec(sortedByX, n)` runs the divide and conquer:

1. base case `n ≤ 3`: `bruteForce` and sort by y: Θ(1);
2. `splitAt` in two halves: Θ(n);
3. two recursive calls on `n/2` points (they also return their points sorted by y);
4. `mergeByAxis` of the two y-sorted halves: Θ(n) (this is the "sort by y" step, done by
   merging instead of sorting again);
5. `stripPoints`, keeping the points within distance `d` of the middle line: Θ(n);
6. `scanStrip`: Θ(n), see below.

**Why the scan is linear.** In the strip, sorted by y, `compareAhead` stops as soon as the
y-distance is not smaller than `d`. The points compared with `p` lie in a rectangle of size
`d × 2d`. Every pair within a half is at distance ≥ `d`, so at most 8 points fit in that
rectangle, and `p` is compared with at most 7 others. The scan costs Θ(7·n) = Θ(n).

**Recurrence**

```
T(n) = 2·T(n/2) + c·n      for n > 3
T(n) = c₀                  for n ≤ 3
```

**Solution.** Same recursion tree as Problem 1: `log n` levels of cost `c·n` each plus
Θ(n) at the leaves, so `T(n) = Θ(n log n)` (Master theorem, case 2).

**Total:** initial sort Θ(n log n) + `closestRec` Θ(n log n) = **Θ(n log n)**.

If the strip were sorted by y at every call (as the assignment literally describes), the
recurrence would be `T(n) = 2·T(n/2) + Θ(n log n)`, which solves to Θ(n log² n). Merging the
two y-sorted halves avoids that extra factor, and it is the only deviation from the
description in the statement.

The naive pairwise comparison is Θ(n²).

**Space.** Θ(n) for the lists; recursion depth `log n`.

---

## Summary

| Problem | Algorithm | Recurrence | Time |
| ------- | --------- | ---------- | ---- |
| 1 | `InversionCount.mergeSort` | `T(n) = 2T(n/2) + Θ(n)` | Θ(n log n) |
| 2 | `QuickSort3.quickSort3` | `T(n) = T(k) + T(r) + Θ(n)`, `k + r ≤ n−1` | Θ(n log n) average, Θ(n²) worst, Θ(n) all equal |
| 3 | `ClosestPoints.closestDistance` | `T(n) = 2T(n/2) + Θ(n)` + initial sort | Θ(n log n) |   