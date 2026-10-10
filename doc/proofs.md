# Correctness proofs

The proofs are based on structural induction. The recursive functions always work with smaller lists, so the induction is mainly based on the size and structure of the list. Problem 3 contains the auxiliary lemmas for `splitAt` (Lemma 0) and `mergeByAxis`, which the other two problems use.

## Problem 1 — Number of inversions

### Notation

- For a list `l`, `inv(l)` is the number of pairs of positions `i < j` with `l(i) > l(j)`.
  Equal elements are not an inversion.
- For two lists `A` and `B`, `cross(A, B)` is the number of pairs `(b, c)` with `b ∈ A`, `c ∈ B`
  and `b > c`. It depends only on the *multisets* of `A` and `B`, not on the order of their
  elements.
- "Sorted" means non-decreasing order, and `l ≈ l'` means that `l'` is a permutation of `l`.

### Specification

`mergeSort(l)` returns `(s, c)` where `s` is a sorted permutation of `l` and `c = inv(l)`.

### Lemma 1 — `mergeAcc` (and `merge`)

Let `L` and `R` be sorted lists, `acc` a list such that `reverse(acc)` is sorted and every element
of `acc` is `≤` every element of `L` and of `R`, and `k` a counter. If `leftSize = |L|`, then

```text
mergeAcc(L, leftSize, R, acc, k) = (S, k + cross(L, R))
```

where `S` is a sorted permutation of `reverse(acc) ++ L ++ R`.

**Proof.** Induction on `|L| + |R|`. The invariant `leftSize = |L|` holds at the start (`merge`
calls it with `size(left)`) and is preserved by the recursive calls, as shown below.

- **Base case.** If `L = Nil`, the function returns `reverseAppend(acc, R)` and `k`. This list is
  `reverse(acc) ++ R`, which is sorted by the hypothesis on `acc` and `R`, and `cross(Nil, R) = 0`.
  If `R = Nil`, the result is `reverseAppend(acc, L)` and `k`, and `cross(L, Nil) = 0`. Both cases
  satisfy the statement.
- **Inductive hypothesis.** The statement holds for every call with a smaller `|L| + |R|`.
- **Inductive step.** Let `L = a :: L'` and `R = b :: R'`.
    - *Case `a ≤ b`.* The function calls `mergeAcc(L', leftSize − 1, R, a :: acc, k)`. Since `R` is
      sorted, `b ≤ every element of R`, so `a ≤ every element of R`: `a` is greater than no element
      of `R`, and therefore `cross(L, R) = cross(L', R)`. The invariant `leftSize − 1 = |L'|` holds. The new
      accumulator still satisfies the hypothesis: `a ≤ b ≤ R`, and `a ≤ L'` because `L` is sorted.
      By the inductive hypothesis, the result is `(S, k + cross(L', R)) = (S, k + cross(L, R))`.
    - *Case `a > b`.* The function calls `mergeAcc(L, leftSize, R', b :: acc, k + leftSize)`.
      Since `L` is sorted, `b < a ≤ every element of L`, so `b` forms an inversion with all `|L|` elements of `L`
      and `cross(L, R) = |L| + cross(L, R')`. The new accumulator satisfies the hypothesis: `b < L`, and `b ≤ R'` because `R` is
      sorted. By the inductive hypothesis, the result is
      `(S, (k + |L|) + cross(L, R')) = (S, k + cross(L, R))`.

In both cases `S` is a sorted permutation of the same elements, because only the head of `L` or
of `R` moved to `acc`. ∎

**Corollary.** `merge(L, R)`, with `acc = Nil` and `k = 0`, returns a sorted permutation of
`L ++ R` and `cross(L, R)`.

### Theorem — `mergeSort`

For every list `l`, `mergeSort(l) = (s, inv(l))` with `s` a sorted permutation of `l`.

**Proof.** Strong induction on `n = |l|`.

- **Base case (`n ≤ 1`).** For `Nil` or a one-element list, the function returns `(l, 0)`. The list
  is sorted and has no pair of positions, so `inv(l) = 0`.
- **Inductive hypothesis.** The statement holds for every list shorter than `n`.
- **Inductive step (`n ≥ 2`).** `splitAt(l, ⌊n/2⌋)` returns `(left, right)` with `left ++ right = l`
  (Lemma 0 in the Problem 3 section). Since `n ≥ 2`, we have `1 ≤ ⌊n/2⌋ ≤ n − 1`, so both halves
  are non-empty and **strictly shorter** than `l`. By the inductive hypothesis:
    - `sortedLeft` is a sorted permutation of `left` and `leftCount = inv(left)`;
    - `sortedRight` is a sorted permutation of `right` and `rightCount = inv(right)`.

  Every pair of positions `i < j` of `l` is of exactly one of three kinds: both in `left`, both in
  `right`, or `i` in `left` and `j` in `right` (positions of `left` come before those of `right`).
  Hence

  ```text
  inv(l) = inv(left) + inv(right) + cross(left, right)
  ```

  Since `cross` depends only on the multisets and `sortedLeft ≈ left`, `sortedRight ≈ right`, we
  have `cross(left, right) = cross(sortedLeft, sortedRight)`. By the Corollary of Lemma 1,
  `merge(sortedLeft, sortedRight)` returns a sorted permutation `s` of `sortedLeft ++ sortedRight`
  (that is, of `l`) and `crossCount = cross(sortedLeft, sortedRight)`. The function returns
  `(s, leftCount + rightCount + crossCount) = (s, inv(l))`. ∎

`countInversions(l) = mergeSort(l)._2 = inv(l)`. The counter is a `Long` because `inv(l)` can be as
large as `n(n − 1)/2`.

---

## Problem 2 — Quick sort with 3-way partition

### Notation

"Sorted" means non-decreasing order. We write `l ≈ l'` when `l'` is a permutation of `l` (same
elements with the same multiplicities), and `S ⊎ E ⊎ G` for the multiset union.

### Lemma 1 — `partition3`

`partition3(L, p, S, E, G)` returns `(S', E', G')` such that

- `S' ≈ S ++ [x ∈ L : x < p]`,
- `E' ≈ E ++ [x ∈ L : x = p]`,
- `G' ≈ G ++ [x ∈ L : x > p]`,

where `[x ∈ L : P(x)]` are the elements of `L` that satisfy `P`. (The order inside each part is not
specified.)

**Proof.** Induction on `|L|`.

- **Base case.** If `L = Nil`, the function returns `(S, E, G)`, and no element of `L` is added.
- **Inductive hypothesis.** The statement holds for any accumulators and any list shorter than `L`.
- **Inductive step.** Let `L = h :: T`. Exactly one of the three conditions `h < p`, `h = p`,
  `h > p` is true.
    - If `h < p`, the call is `partition3(T, p, h :: S, E, G)`. By the inductive hypothesis, the
      result is `(h :: S ++ [x ∈ T : x < p], E ++ [x ∈ T : x = p], G ++ [x ∈ T : x > p])`, and since
      `h` is the only element of `L` that is not in `T`, this is the statement for `L`.
    - The cases `h = p` and `h > p` are the same, adding `h` to `E` or to `G`. ∎

**Corollary.** `partition3(L, p, Nil, Nil, Nil)` splits `L` into three lists with all the elements
`< p`, `= p` and `> p`, and no element of `L` is lost or duplicated.

### Lemma 2 — `extractAt`

If `0 ≤ i < |l|`, then `extractAt(l, i) = Some((x, rest))` with `x = l(i)`, `|rest| = |l| − 1` and
`x :: rest ≈ l`.

**Proof.** `splitAt(l, i)` returns `(front, back)` with `front ++ back = l` and `|front| = i`
(Lemma 0 in the Problem 3 section). Since `i < |l|`, `back` is non-empty, `back = x :: back'`,
and `x = l(i)`. `append(front, back')` is `front ++ back'`, which is `l` without the element at
position `i`. ∎

### Lemma 3 — `PseudoRandom.indexBelow`

For any `seed` and any `bound > 0`, `indexBelow(seed, bound)` is in `[0, bound)`. This holds
because the value is `(next(seed) >>> 1) % bound`: the unsigned shift makes the number
non-negative, and the remainder by `bound` is smaller than `bound`. ∎

### Theorem — `quickSort3`

For every list `l` and **every** seed, `quickSort3(l, seed)` is a sorted permutation of `l`.

(The seed is quantified in the statement because the recursive calls use different seeds.)

**Proof.** Strong induction on `n = |l|`.

- **Base case (`n ≤ 1`).** For `Nil` or a one-element list, the function returns the list itself,
  which is sorted and is a permutation of itself.
- **Inductive hypothesis.** For every list shorter than `n` and every seed, `quickSort3` returns a
  sorted permutation of it.
- **Inductive step (`n ≥ 2`).** By Lemma 3 the index is in `[0, n)`, and by Lemma 2
  `extractAt` returns `Some((pivot, rest))` with `|rest| = n − 1` and `pivot :: rest ≈ l`.
  (The `None` branch is unreachable.) By the Corollary of Lemma 1,
  `partition3(rest, pivot, Nil, Nil, Nil) = (smaller, equal, greater)` where every element of
  `rest` is in exactly one of the three lists:
    - every element of `smaller` is `< pivot`;
    - every element of `equal` is `= pivot`;
    - every element of `greater` is `> pivot`.

  Because `|smaller| ≤ n − 1` and `|greater| ≤ n − 1`, the inductive hypothesis applies to
  `smaller` with `leftSeed` and to `greater` with `rightSeed`: `sortedSmaller` is a sorted
  permutation of `smaller` and `sortedGreater` is a sorted permutation of `greater`. The list
  `equal` is **not** sorted again: it does not need to be, because all its elements are equal.

  The function returns `sortedSmaller ++ (pivot :: equal) ++ sortedGreater` (`append` is
  concatenation). We check the two properties.

    1. **Sorted.** Each of the three blocks is sorted: the first and the third by the inductive
       hypothesis, and the middle one because all its elements are equal to `pivot`. Moreover,
       every element of the first block is `< pivot`, which is the value of the middle block, and
       every element of the third block is `> pivot`. Therefore the concatenation is sorted.
    2. **Permutation.** The elements of the result are
       `smaller ⊎ {pivot} ⊎ equal ⊎ greater`, which is `pivot :: rest ≈ l`.

  ∎

**Termination.** The recursive calls are made on `smaller` and `greater`, both strictly shorter
than `l`, so the recursion ends. The elements equal to the pivot never re-enter the recursion;
this is exactly the improvement over the 2-way partition, where the elements equal to the pivot
go to one side and a list with all elements equal produces a recursive call of size `n − 1`.

---

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