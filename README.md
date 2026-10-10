# Integradora CyDII — Integrative Assignment 1

**Team:** The Drink Team
**Course:** Computación y Estructuras Discretas II, 2026-2

## Team members

| Name           | GitHub                                                |
| -------------- | ----------------------------------------------------- |
| Santiago Campo | [simplysanticamp](https://github.com/simplysanticamp) |
| Jose Oñate     | [Onatejose](https://github.com/Onatejose)             |

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
├── doc/
│   ├── test-design.md
│   ├── proofs.md
│   ├── complexity.md
│   ├── experiments-report.md
│   └── ai-log.md
├── results/                    # timings.csv and environment.txt of the experiments
└── src/
    ├── main/scala/
    │   ├── Algorithms/         # the three problems and their helpers
    │   └── Experiments/        # input generation, timing and the experiment runner
    └── test/scala/
```

## Usage

Requires JDK 17+ and [sbt](https://www.scala-sbt.org/).

```bash
sbt compile
sbt test
```

### Experiments

```bash
sbt "runMain Experiments.Main quick"   # sizes up to 10^4, to check that everything works
sbt "runMain Experiments.Main full"    # all sizes, up to 10^6
```

Each size is run 10 times, the first run is discarded and the rest are averaged.
Times are written to `results/timings.csv` and the machine description to
`results/environment.txt`. Generated inputs are cached in `data/`.

## Documentation

See [`doc/`](doc/): [test design](doc/test-design.md), [proofs](doc/proofs.md),
[complexity](doc/complexity.md), [experiments report](doc/Experiment-report.md) and
[AI usage log](doc/Ai-log.md).
