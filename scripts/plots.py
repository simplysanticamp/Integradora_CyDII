"""Curve fitting and plots for the experiments of the assignment (Part 2).

Reads  results/timings.csv  (written by Experiments.Main) and produces:

  * doc/img/<algorithm>.png                  one figure per algorithm: data, data fitting
                                             curve and theoretical complexity as reference
  * doc/img/comparison_algorithms.png        fitted curves of the algorithms on one graph
  * doc/img/comparison_quicksort_variants.png  2-way vs 3-way quick sort, per scenario
  * results/fits.csv                         parameters and errors of every fitted model

Usage, from the project root:

    pip install -r scripts/requirements.txt
    python scripts/plots.py
"""
import csv
import math
from collections import defaultdict
from pathlib import Path

import matplotlib

matplotlib.use("Agg")
import matplotlib.pyplot as plt
import numpy as np
from scipy.optimize import nnls

ROOT = Path(__file__).resolve().parent.parent
CSV_PATH = ROOT / "results" / "timings.csv"
FITS_PATH = ROOT / "results" / "fits.csv"
IMG_DIR = ROOT / "doc" / "img"

# Sizes below this value are drawn but not used to fit: with a few microseconds per run the
# JIT warm-up and the timer resolution dominate the measurement.
FIT_MIN_N = 1000

# Candidate functions for the data fitting curve (the model is  a * f(n) + b,  a >= 0, b >= 0).
MODELS = {
    "n": lambda n: n,
    "n log n": lambda n: n * np.log2(n),
    "n^2": lambda n: n ** 2,
}

# Theoretical complexity of every algorithm/scenario (see doc/complexity.md).
THEORY = {
    ("quicksort_2way", "random"): "n log n",
    ("quicksort_2way", "sorted"): "n log n",
    ("quicksort_2way", "reversed"): "n log n",
    ("quicksort_2way", "few_distinct"): "n^2",
    ("quicksort_3way", "random"): "n log n",
    ("quicksort_3way", "sorted"): "n log n",
    ("quicksort_3way", "reversed"): "n log n",
    ("quicksort_3way", "few_distinct"): "n",
    ("inversion_count", "random"): "n log n",
    ("inversion_count", "sorted"): "n log n",
    ("inversion_count", "reversed"): "n log n",
    ("closest_points", "points"): "n log n",
}

TITLES = {
    "quicksort_2way": "Quick sort, 2-way partition",
    "quicksort_3way": "Quick sort, 3-way partition",
    "inversion_count": "Number of inversions",
    "closest_points": "Closest points",
}

# Scenario shown for every algorithm in the comparison between algorithms.
MAIN_SCENARIO = {
    "quicksort_2way": "random",
    "quicksort_3way": "random",
    "inversion_count": "random",
    "closest_points": "points",
}

COLORS = {
    "quicksort_2way": "tab:red",
    "quicksort_3way": "tab:blue",
    "inversion_count": "tab:green",
    "closest_points": "tab:purple",
}


def load_timings(path):
    """Returns {(algorithm, scenario): (sizes, mean_ms)} with the sizes in increasing order."""
    data = defaultdict(dict)
    with open(path, newline="") as handle:
        for row in csv.DictReader(handle):
            data[(row["algorithm"], row["scenario"])][int(row["n"])] = float(row["mean_ms"])
    result = {}
    for key, points in data.items():
        sizes = sorted(points)
        result[key] = (
            np.array(sizes, dtype=float),
            np.maximum(np.array([points[n] for n in sizes]), 1e-6),
        )
    return result


def fit_model(sizes, times, f):
    """Least squares fit of  a*f(n) + b  with a, b >= 0, minimizing the *relative* error.

    Relative weights are needed because the times span six orders of magnitude; without them
    only the largest sizes would count.
    """
    scale = f(sizes[-1])
    matrix = np.column_stack([f(sizes) / scale / times, 1.0 / times])
    (a_scaled, b), _ = nnls(matrix, np.ones(len(sizes)))
    a = a_scaled / scale
    prediction = a * f(sizes) + b
    rel_rmse = math.sqrt(np.mean(((times - prediction) / times) ** 2))
    sst = np.sum((times - times.mean()) ** 2)
    r2 = 1.0 - np.sum((times - prediction) ** 2) / sst if sst > 0 else float("nan")
    return {"a": a, "b": b, "rel_rmse": rel_rmse, "r2": r2}


def fit_all(sizes, times):
    """Fits every candidate model on the sizes >= FIT_MIN_N; returns {name: fit} and the best name."""
    mask = sizes >= FIT_MIN_N
    fits = {name: fit_model(sizes[mask], times[mask], f) for name, f in MODELS.items()}
    best = min(fits, key=lambda name: fits[name]["rel_rmse"])
    return fits, best


def exponent(sizes, times):
    """Slope of the log-log regression: the empirical exponent k in  t ~ n^k."""
    mask = sizes >= FIT_MIN_N
    return float(np.polyfit(np.log(sizes[mask]), np.log(times[mask]), 1)[0])


def curve(fit, name, grid):
    return fit["a"] * MODELS[name](grid) + fit["b"]


def fit_grid(sizes):
    """Grid over the range that was used to fit (sizes >= FIT_MIN_N)."""
    return np.logspace(math.log10(FIT_MIN_N), math.log10(sizes[-1]), 200)


def fit_label(name, fit):
    return f"fit: {name}  (rel. error {fit['rel_rmse'] * 100:.0f}%)"


def draw_panel(ax, sizes, times, fits, best, theory_name, color):
    grid = np.logspace(math.log10(sizes[0]), math.log10(sizes[-1]), 200)
    used = sizes >= FIT_MIN_N
    ax.plot(sizes[used], times[used], "o", color=color, label="measured (used in fit)")
    ax.plot(sizes[~used], times[~used], "o", mfc="none", color=color, alpha=0.6,
            label="measured (not used in fit)")
    ax.plot(fit_grid(sizes), curve(fits[best], best, fit_grid(sizes)), "-", color="black", lw=1.8,
            label=fit_label(best, fits[best]))
    f_theory = MODELS[theory_name]
    anchor = times[-1] / f_theory(sizes[-1])
    ax.plot(grid, anchor * f_theory(grid), "--", color="gray", lw=1.6,
            label=f"theory: Θ({theory_name}), scaled to the largest n")
    ax.set_xscale("log")
    ax.set_yscale("log")
    ax.grid(True, which="both", alpha=0.25)
    ax.legend(fontsize=7, loc="upper left")


def algorithm_figure(algorithm, data, all_fits):
    scenarios = [s for (a, s) in data if a == algorithm]
    count = len(scenarios)
    cols = 2 if count == 4 else min(count, 3)
    rows = math.ceil(count / cols)
    fig, axes = plt.subplots(rows, cols, figsize=(6.2 * cols, 4.6 * rows), squeeze=False)
    for index, scenario in enumerate(scenarios):
        ax = axes[index // cols][index % cols]
        sizes, times = data[(algorithm, scenario)]
        fits, best = all_fits[(algorithm, scenario)]
        draw_panel(ax, sizes, times, fits, best, THEORY[(algorithm, scenario)], COLORS[algorithm])
        ax.set_title(f"input: {scenario}", fontsize=10)
        ax.set_xlabel("input size n")
        ax.set_ylabel("mean time (ms)")
    for index in range(count, rows * cols):
        axes[index // cols][index % cols].axis("off")
    fig.suptitle(f"{TITLES[algorithm]}: experimental vs. theoretical complexity", fontsize=12)
    fig.tight_layout()
    fig.savefig(IMG_DIR / f"{algorithm}.png", dpi=150)
    plt.close(fig)


def comparison_algorithms(data, all_fits):
    fig, ax = plt.subplots(figsize=(8, 5.5))
    for algorithm, scenario in MAIN_SCENARIO.items():
        if (algorithm, scenario) not in data:
            continue
        sizes, times = data[(algorithm, scenario)]
        fits, best = all_fits[(algorithm, scenario)]
        grid = fit_grid(sizes)
        ax.plot(sizes, times, "o", ms=4, alpha=0.35, color=COLORS[algorithm])
        ax.plot(grid, curve(fits[best], best, grid), "-", lw=2, color=COLORS[algorithm],
                label=f"{TITLES[algorithm]} ({scenario}): {best}")
    ax.set_xscale("log")
    ax.set_yscale("log")
    ax.grid(True, which="both", alpha=0.25)
    ax.set_xlabel("input size n")
    ax.set_ylabel("mean time (ms)")
    ax.set_title(f"Fitted experimental complexities of the algorithms (fit on n >= {FIT_MIN_N})")
    ax.legend(fontsize=8)
    fig.tight_layout()
    fig.savefig(IMG_DIR / "comparison_algorithms.png", dpi=150)
    plt.close(fig)


def comparison_quicksort(data, all_fits):
    scenarios = ["random", "sorted", "reversed", "few_distinct"]
    fig, axes = plt.subplots(2, 2, figsize=(12, 9), squeeze=False)
    for index, scenario in enumerate(scenarios):
        ax = axes[index // 2][index % 2]
        for algorithm in ("quicksort_2way", "quicksort_3way"):
            if (algorithm, scenario) not in data:
                continue
            sizes, times = data[(algorithm, scenario)]
            fits, best = all_fits[(algorithm, scenario)]
            grid = fit_grid(sizes)
            ax.plot(sizes, times, "o", ms=4, alpha=0.35, color=COLORS[algorithm])
            ax.plot(grid, curve(fits[best], best, grid), "-", lw=2, color=COLORS[algorithm],
                    label=f"{TITLES[algorithm]}: {best}")
        ax.set_xscale("log")
        ax.set_yscale("log")
        ax.grid(True, which="both", alpha=0.25)
        ax.set_title(f"input: {scenario}", fontsize=10)
        ax.set_xlabel("input size n")
        ax.set_ylabel("mean time (ms)")
        ax.legend(fontsize=8)
    fig.suptitle("2-way vs. 3-way quick sort (fitted curves)", fontsize=12)
    fig.tight_layout()
    fig.savefig(IMG_DIR / "comparison_quicksort_variants.png", dpi=150)
    plt.close(fig)


def write_fits(data, all_fits):
    with open(FITS_PATH, "w", newline="") as handle:
        writer = csv.writer(handle)
        writer.writerow(["algorithm", "scenario", "theory", "model", "a", "b", "rel_rmse_pct", "r2",
                         "is_best", "empirical_exponent"])
        for (algorithm, scenario), (fits, best) in all_fits.items():
            sizes, times = data[(algorithm, scenario)]
            k = exponent(sizes, times)
            for name, fit in fits.items():
                writer.writerow([algorithm, scenario, THEORY[(algorithm, scenario)], name,
                                 f"{fit['a']:.6e}", f"{fit['b']:.6e}", f"{fit['rel_rmse'] * 100:.2f}",
                                 f"{fit['r2']:.5f}", name == best, f"{k:.3f}"])


def print_summary(data, all_fits):
    print("| algorithm | scenario | theory | best fit | rel. error | R^2 | exponent |")
    print("| --- | --- | --- | --- | --- | --- | --- |")
    for (algorithm, scenario), (fits, best) in all_fits.items():
        sizes, times = data[(algorithm, scenario)]
        fit = fits[best]
        print(f"| {algorithm} | {scenario} | {THEORY[(algorithm, scenario)]} | {best} | "
              f"{fit['rel_rmse'] * 100:.1f}% | {fit['r2']:.4f} | {exponent(sizes, times):.2f} |")


def main():
    IMG_DIR.mkdir(parents=True, exist_ok=True)
    data = load_timings(CSV_PATH)
    all_fits = {key: fit_all(sizes, times) for key, (sizes, times) in data.items()}
    for algorithm in TITLES:
        if any(a == algorithm for (a, _) in data):
            algorithm_figure(algorithm, data, all_fits)
    comparison_algorithms(data, all_fits)
    comparison_quicksort(data, all_fits)
    write_fits(data, all_fits)
    print_summary(data, all_fits)
    print(f"\nImages in {IMG_DIR}, fit table in {FITS_PATH}")


if __name__ == "__main__":
    main()