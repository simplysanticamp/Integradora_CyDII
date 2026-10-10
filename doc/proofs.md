# Correctness proofs

The proofs are based on structural induction. The recursive functions always work with smaller lists, so the induction is mainly based on the size and structure of the list.

## Problem 3 — Closest points

### Notation

- A point is `p = (xₚ, yₚ)`. The squared distance between two points is `d²(p, q) = (xₚ − x_q)² + (yₚ − y_q)²`.
- For a list `S`, `δ(S)` is the minimum squared distance between two points in different positions of `S`. If two equal points appear in the list, the distance is 0. If `|S| < 2`, then `δ(S) = ∞`.
- "Sorted by x" or "sorted by y" means that the corresponding coordinate is in non-decreasing order.
- The proofs use exact real arithmetic. In the code, coordinates are converted to `Double` before subtracting, which avoids `Int` overflow. Any rounding error is only in the last few bits.

### Specification

If `S` is sorted by x and `n = |S|`, then `closestRec(S, n)` returns `(Y, m)` where:

1. `Y` contains the same points as `S`, sorted by y.
2. `m = δ(S)`.

### Lemma 0 — `splitAt`

`splitAt(l, k) = (front, back)` satisfies `front ++ back = l` and `|front| = min(k, |l|)`.

**Proof.** We use induction on `l`.

If `l = Nil` or `k = 0`, the result is `(Nil, l)`, so the property is true.

Otherwise, `splitAt(h :: t, k)` calls `splitAt(t, k−1)`. By the induction hypothesis, the two resulting lists contain all the elements of `t` in the correct order. Adding `h` to the front gives the original list `l`. Therefore, the property also holds for `h :: t`. ∎

The code uses an accumulator `front` in reverse order. In that case, the invariant is `reverse(acc) ++ front' ++ back' = l`, which can be proved in the same way.

### Lemma 1 — `mergeByAxis`

If `A` and `B` are sorted by an axis `a`, then `mergeByAxis(A, B, a)` returns a sorted permutation of `A ++ B`.

**Proof.** We prove a stronger property for `mergeAcc(A, B, a, acc)`. Assume that `A` and `B` are sorted, `reverse(acc)` is sorted, and every element in `acc` is less than or equal to every element in `A` and `B`.

We use induction on `|A| + |B|`.

- **Base case.** If one of the lists is empty, the function returns the other list together with the accumulator. Since both parts are already sorted and the accumulator contains smaller elements, the result is sorted and contains exactly the required elements.
- **Inductive step.** Suppose `A = l :: A'` and `B = r :: B'`. If `l ≤ r`, the function puts `l` into the accumulator and continues with `A'` and `B`. The conditions still hold because `A` and `B` are sorted. By the induction hypothesis, the rest of the result is correctly sorted. The case `r < l` works in the same way.

Starting with `acc = Nil` gives the required result. ∎

### Lemma 2 — `sortByAxis`

`sortByAxis(P, a)` returns a sorted permutation of `P`.

**Proof.** We use induction on the size of `P`.

- **Base case.** If `P` is empty or has one element, it is already sorted.
- **Inductive step.** For a list with at least two elements, `splitAt` divides it into two smaller lists. By the induction hypothesis, both recursive calls return sorted permutations of their respective halves. Lemma 1 shows that merging them gives a sorted permutation of the original list.

Therefore, `sortByAxis` correctly sorts the list. ∎

### Lemma 3 — `bruteForce`

`bestAgainst(p, O, best)` returns the smallest value between `best` and the squared distance from `p` to every point in `O`.

`bruteForce(P, best)` returns the smallest value between `best` and the squared distance of every pair in `P`.

**Proof.**

For `bestAgainst`, we use induction on `O`.

- If `O = Nil`, the function simply returns `best`.
- If `O = q :: O'`, it compares `best` with the distance from `p` to `q` and continues with `O'`. By the induction hypothesis, all remaining points are checked correctly.

For `bruteForce`, we use induction on `P`.

- If `P = Nil`, there are no pairs to check, so `best` is returned.
- If `P = p :: rest`, the function compares `p` with every point in `rest` and then checks the remaining list. Every pair either contains `p` or is completely inside `rest`, so all possible pairs are considered.

Therefore, `bruteForce` returns the correct minimum squared distance. ∎

### Lemma 4 — `stripPoints`

`stripPoints(P, midX, bound)` returns the points from `P` whose squared distance from `midX` is smaller than `bound`, keeping their original order.

**Proof.** We use induction on `P`.

If `P = Nil`, there are no points to keep.

For `P = p :: rest`, there are two cases. If `p` satisfies `(xₚ − midX)² < bound`, it is added to the result. Otherwise, it is skipped. By the induction hypothesis, the remaining points are handled correctly.

Therefore, the resulting list contains exactly the required points in the same order. ∎

### Lemma 5 — `scanStrip`

If `strip` is sorted by y, then `scanStrip(strip, best)` returns `min(best, δ(strip))`.

**Proof.** First, consider `compareAhead(p, rest, best)`. It checks the points after `p` and returns the smallest distance between `p` and those points, or `best` if `best` is already smaller.

We use induction on `rest`.

- **Base case.** If `rest = Nil`, there is nothing to compare, so `best` is returned.
- **Inductive step.** Let `rest = q :: tail`. Since the list is sorted by y, `y_q ≥ y_p`.

If `(y_q − y_p)² ≥ best`, then every point after `q` is even farther away in the y direction. Therefore, none of them can improve `best`, so the function can stop.

Otherwise, the function compares `p` with `q` and continues with the rest of the list. By the induction hypothesis, all remaining points are handled correctly.

Now we apply the same idea to `scanStrip`. It processes each point and uses `compareAhead` to check the possible pairs after it. By induction on the strip, every relevant pair is considered and the smallest distance is kept.

Therefore, `scanStrip(strip, best) = min(best, δ(strip))`. ∎

### Theorem — `closestRec`

If `S` is sorted by x and `n = |S|`, then `closestRec(S, n)` returns `(Y, m)` where `Y` is `S` sorted by y and `m = δ(S)`.

**Proof.** We use induction on `n`.

**Base case (`n ≤ 3`).** The function directly sorts the points by y and uses `bruteForce` to find the minimum distance. By Lemmas 2 and 3, both results are correct.

**Inductive step (`n > 3`).**

Let `k = ⌊n/2⌋`. Using `splitAt`, the list is divided into `L` and `R`. Both lists are smaller than `S` and remain sorted by x.

Let `midX` be the x-coordinate of the first point in `R`. Since `S` is sorted by x:

```text
x_p ≤ midX ≤ x_q
````

for every `p` in `L` and every `q` in `R`.

By the induction hypothesis, the recursive calls correctly return the points sorted by y and the minimum distances `mL` and `mR` for each half.

Let:

```text
best = min(mL, mR)
```

1. **The sorted list is correct.** `mergeByAxis` merges the two y-sorted halves. By Lemma 1, the result is a sorted permutation of all points in `S`.

2. **The distance is correct.** A pair of points in `S` can be completely inside `L`, completely inside `R`, or cross the dividing line. The first two cases are already covered by `best`.

For the crossing case, the algorithm creates a strip containing the points whose x-distance from `midX` is smaller than `best`. By Lemma 4, these are exactly the points that could still produce a better crossing pair.

The strip is sorted by y, so Lemma 5 shows that `scanStrip` finds the smallest distance inside it.

If a crossing pair has distance smaller than `best`, both points must be inside the strip. Therefore, the algorithm will find that pair. If no crossing pair improves `best`, then `best` is already the correct answer.

So the final result is exactly:

```text
m = δ(S)
```

Therefore, both the sorted list and the minimum distance are correct. ∎

### Theorem — `closestDistance`

For a list `P` with at least two points, `closestDistance(P)` returns the minimum Euclidean distance between two different points, rounded to four decimal places.

**Proof.** First, `sortByAxis(P, X-axis)` sorts the points by x without changing which points are in the list. By the previous theorem, `closestRec` then finds the correct minimum squared distance `δ(P)`.

Since the square root is increasing:

```text
sqrt(δ(P))
```

is the actual minimum Euclidean distance.

If two points have the same coordinates, their squared distance is 0, so the final result is also 0.

Finally, the function rounds the result to four decimal places, giving the required answer. ∎
}
