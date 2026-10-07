# Integradora CyDII

**Team:** The Drink Team
**Course:** Computación y Estructuras Discretas II

## Team members

| Name           | GitHub          |
| -------------- | --------------- |
| Santiago Campo | simplysanticamp |
| Jose Oñate     | Onatejose       |

## Overview

Three divide and conquer problems solved in pure functional Scala 3: immutable lists,
recursion, pattern matching and `@tailrec`. No `var`, loops or higher-order functions
in the algorithms.

| Problem | Algorithm | Complexity | Code |
| ------- | --------- | ---------- | ---- |
| 1. Number of inversions | Modified merge sort | Θ(n log n) | `InversionCount.scala` |
| 2. Improving quick sort | Randomized quick sort, 3-way partition | Θ(n log n) average | `QuickSort3.scala` (`QuickSort.scala` is the 2-way baseline) |
| 3. Closest points | Divide and conquer closest pair | Θ(n log n) | `ClosestPoints.scala` |

Helpers: `ListUtils.scala` (generic list functions) and `PseudoRandom.scala` (pure
pseudo-random pivot selection).

## Repository structure

```
├── build.sbt
├── README.md
├── doc/    # test design, proofs, complexity, experiments report, AI log
└── src/
    ├── main/scala/
    └── test/scala/