# AI Usage Log

This document records the use of generative AI during the development of Integrative Assignment 1 for Computational and Discrete Structures II. 

### 1. Understanding Divide and Conquer — September 28, 2026 (Claude)

- **Own content provided:** The assignment requirements involving divide-and-conquer algorithms, recursive problem solving, and asymptotic complexity analysis.
- **Prompt:** Me puedes explicar como funciona divide y venceras y cuales son los pasos que debe seguir un algoritmo que utiliza esta tecnica. Quiero entenderlo bien antes de empezar el proyecto.
- **Result and use:** Claude explained the three main stages of divide and conquer: dividing the original problem into smaller subproblems, solving them recursively, and combining their results. The explanation also distinguished between the cost of recursive calls and the additional work performed during the combination stage. This information helped establish the theoretical foundation for the project. The concepts were related to the three required problems to identify which parts of each algorithm needed to be analyzed to justify its complexity.

### 2. Understanding the Master Theorem — September 28, 2026 (Claude)

- **Own content provided:** The need to analyze recursive algorithms and determine their asymptotic time complexity using recurrence relations.
- **Prompt:** Como se obtiene la recurrencia de un algoritmo divide y venceras y como puedo resolverla con el metodo maestro basico. Explicamelo con ejemplos sencillos y dime en que casos no se puede aplicar.
- **Result and use:** Claude explained how to express an algorithm as a recurrence of the form T(n) = aT(n/b) + f(n), identifying the number of recursive calls, the size of the subproblems, and the work performed outside recursion. It also discussed the importance of comparing f(n) with n^(log_b(a)) and checking the conditions required by each case of the theorem. This helped clarify why algorithms with two recursive calls on halves of the input and linear merging work usually have n log n complexity. The explanation was used as a reference for studying and documenting the recurrences, while the final classification had to be justified for each algorithm individually.

### 3. Understanding Inversions in an Array — September 29, 2026 (Claude)

- **Own content provided:** The definition of an inversion as a pair of positions i and j where i is less than j but the element at i is greater than the element at j.
- **Prompt:** Que es una inversion en una lista y por que contar todas las inversiones con dos ciclos puede ser muy lento. Como se puede aprovechar merge sort para hacerlo mas eficiente.
- **Result and use:** Claude explained the difference between counting inversions with a brute-force approach and counting them while merging sorted halves. The key idea was that when an element from the right half is smaller than the current element from the left half, it forms an inversion with the remaining elements in the left half. This observation helped identify how the algorithm could avoid checking every possible pair. The explanation was used to understand the algorithm's design and the reason for its expected n log n complexity. The implementation and the handling of equal elements still required independent review and testing.

### 4. Comparing Two-Way and Three-Way Quicksort — September 29, 2026 (Claude)

- **Own content provided:** The project required comparing a standard two-way Quicksort implementation with a three-way version, including performance on inputs containing repeated values.
- **Prompt:** Cual es la diferencia entre quicksort de dos vias y de tres vias. Explicame que pasa con los elementos iguales al pivote y por que eso puede cambiar tanto la complejidad en algunas entradas.
- **Result and use:** Claude explained how two-way partitioning can repeatedly place equal values into recursive subproblems, depending on the partition strategy, while three-way partitioning separates elements into values smaller than, equal to, and greater than the pivot. Elements equal to the pivot can then be excluded from further recursive calls. This provided a theoretical reason to test both algorithms using random values and inputs with few distinct values. The explanation also helped distinguish average-case behavior from worst-case behavior, avoiding the assumption that three-way partitioning is always faster regardless of the input distribution.

### 5. Understanding the Closest Pair of Points Problem — September 30, 2026 (Claude)

- **Own content provided:** The requirement to find the closest pair of points in a plane using divide and conquer with a target complexity of n log n.
- **Prompt:** Necesito entender el problema de los puntos mas cercanos usando divide y venceras. Explicame como se divide por x, como se calcula la distancia de cada mitad y para que sirve la franja del medio.
- **Result and use:** Claude described the general algorithm: organize the points by their x-coordinate, split them into two halves, recursively find the minimum distance in each half, and use the smaller distance as the initial best distance. It then explained why points near the dividing line must also be considered, since a closer pair may contain one point from each half. The importance of maintaining an ordering by y-coordinate was discussed as part of making the strip comparison efficient. This information was used to understand the algorithm's structure before evaluating the implementation and its correctness conditions.

### 6. Functional Programming Restrictions in Scala — September 30, 2026 (Claude)

- **Own content provided:** Course restrictions requiring functional Scala, immutable lists, recursion, pattern matching, and the avoidance of mutable variables, imperative loops, and prohibited collection operations.
- **Prompt:** Para esta tarea tengo que usar scala funcional puro, con listas inmutables, recursion, pattern matching y tailrec. Que errores de diseño deberia evitar para no terminar haciendo programacion imperativa sin darme cuenta.
- **Result and use:** Claude explained how functional programming approaches state changes through new values rather than mutation and how recursive functions can replace imperative loops. It also discussed the role of pattern matching in processing lists and the conditions needed for tail-recursive functions. These explanations helped establish a checklist for reviewing the implementation against the course restrictions. The suggestions were considered in the context of the assignment, especially where a particular Scala feature might conflict with the instructor's rules.

### 7. Planning the Repository Structure — October 1, 2026 (Claude)

- **Own content provided:** The assignment scope included InversionCount, QuickSort2, QuickSort3, ClosestPoints, shared list utilities, automated tests, experimental measurements, plots, and written documentation.
- **Prompt:** Estoy organizando el repo de una tarea en scala. Tengo cuatro algoritmos si cuento quicksort 2 y 3 por separado, pruebas con munit, experimentos, datos y documentacion. Que estructura de carpetas y que responsabilidades deberia tener cada clase para que todo quede organizado y sea facil de mantener.
- **Result and use:** Claude suggested separating algorithm implementations, test code, experiment infrastructure, generated data, analysis scripts, and documentation. It also explained why algorithm classes should focus on their own logic while experiment runners should handle input generation, measurement, and result storage. This guidance helped define the responsibilities of the main parts of the repository and reduced the risk of mixing experimental code with algorithm implementations. The proposed organization was adapted to the existing project, and the final directory names and class boundaries were determined according to the actual build configuration and assignment requirements.

### 8. Designing Test Cases — October 1, 2026 (Claude)

- **Own content provided:** The four algorithm implementations and the requirement to demonstrate correctness through automated tests using MUnit.
- **Prompt:** Ayudame a pensar una checklist de pruebas para inversion count, quicksort 2, quicksort 3 y closest points. Quiero saber que casos normales, extremos y especiales deberia probar y por que.
- **Result and use:** Claude proposed categories of tests that could reveal different kinds of errors. For inversion counting, important cases included sorted input, reversed input, duplicates, and small lists. For Quicksort, the discussion included repeated values, already sorted inputs, reverse ordering, and inputs with few distinct values. For ClosestPoints, the relevant cases included small point sets, ties in the minimum distance, and points whose closest pair crosses the dividing line. The response was used as a checklist for reviewing test coverage. Each suggested case still needed an appropriate expected result and had to be checked against the behavior required by the assignment.

### 9. Understanding MUnit and Test Assertions — October 1, 2026 (Claude)

- **Own content provided:** The project uses Scala with MUnit, and the test suite must verify algorithm correctness rather than only successful execution.
- **Prompt:** Como deberia organizar los tests con munit en scala para que cada prueba compruebe algo importante. Explicame como escoger los resultados esperados y como evitar pruebas que realmente no detecten errores.
- **Result and use:** Claude explained the role of test assertions and the importance of selecting inputs that distinguish a correct implementation from a potentially incorrect one. It also highlighted the value of testing edge cases independently rather than relying only on large random inputs. This helped clarify the difference between testing that a function runs and testing that it returns the correct result. The guidance was used when reviewing the test design, while the expected values and actual behavior of each algorithm remained subject to independent verification.

### 10. Designing a Fair Performance Experiment — October 2, 2026 (Claude)

- **Own content provided:** The experimental component required execution-time measurements, repeated runs, multiple input sizes, and comparisons against theoretical complexity.
- **Prompt:** Para la parte experimental quiero comparar los tiempos de los algoritmos. Como deberia hacer las mediciones para que la comparacion sea justa. Explicame lo de repetir ejecuciones, calentamiento del JIT y que informacion deberia guardar.
- **Result and use:** Claude explained that execution times can vary because of runtime warm-up, garbage collection, operating-system activity, and other sources of noise. It recommended using consistent conditions, repeating measurements, and separating input generation from the measured operation when the objective is to compare algorithm execution time. It also discussed recording the input size, input distribution, execution time, and environment information so that the results could be interpreted later. These ideas informed the experimental methodology, including the decision to perform repeated runs and discard the first run. The final methodology was selected according to the project's constraints, and the limitations of the measurements had to be considered when interpreting the graphs.

### 11. Choosing Input Distributions — October 2, 2026 (Claude)

- **Own content provided:** The planned input distributions were random, sorted, reversed, and few_distinct for sorting-related algorithms, plus point sets for ClosestPoints.
- **Prompt:** Que tipos de entradas deberia generar para comparar los algoritmos y que informacion aporta cada una. Me interesa entender por que random, sorted, reversed y pocos valores distintos pueden mostrar comportamientos diferentes.
- **Result and use:** Claude explained that input distribution can influence the partition sizes and recursive behavior of Quicksort, even when the input size is the same. Random inputs provide a general comparison, sorted and reversed inputs help expose sensitivity to ordering, and few_distinct inputs reveal the effect of repeated values. The response helped justify why one input category would not be sufficient for a meaningful comparison. The selected categories were used as a basis for planning the experiments. The actual conclusions still needed to be drawn from measured timings rather than assumed from theory alone.

### 12. Planning the Performance Graphs — October 2, 2026 (Claude)

- **Own content provided:** The experiment needed graphs comparing observed execution times with theoretical growth functions and a way to assess which growth model best described the measurements.
- **Prompt:** Quiero generar graficas en python con los tiempos de los algoritmos y compararlos con n, n log n y n cuadrado. Explicame como deberia organizar los datos, que deberia tener cada grafica y para que sirve usar escala log-log.
- **Result and use:** Claude explained how to structure measurement data so that each algorithm and input distribution could be compared consistently. It discussed the purpose of plotting measured points alongside fitted curves and theoretical reference functions. The log-log scale was described as a useful way to visualize growth across several orders of magnitude, although the interpretation still depends on the data and the model being fitted. This information guided the planning of the plotting script and the graph layout. The final implementation of the script and the selection of the fitted model had to be checked against the available measurements.

### 13. Organizing Experiment Data and Results — October 3, 2026 (Claude)

- **Own content provided:** The project needed reusable input files, timing measurements, environment details, fitted-model results, and generated images.
- **Prompt:** Como deberia organizar los archivos de data y results para no mezclar las entradas con los tiempos y las graficas. Tambien quiero poder generar las graficas otra vez sin tener que repetir todas las mediciones.
- **Result and use:** Claude recommended separating generated inputs, raw timing results, environment metadata, model-fitting outputs, and graph files. It explained why preserving the raw measurements is useful: the analysis can be repeated without running the algorithms again, and changes to the plotting script do not require regenerating the original data. This guidance helped establish a reproducible workflow in which data generation, benchmarking, analysis, and visualization were separate steps. The organization was adapted to the repository's existing directories and scripts.

### 14. Reviewing the ClosestPoints Complexity — October 4, 2026 (Claude)

- **Own content provided:** The ClosestPoints strategy used recursive solutions for the two halves and a central strip of points ordered by y-coordinate. The expected target was O(n log n).
- **Prompt:** Estoy revisando el algoritmo de closest points y quiero comprobar si la idea de ordenar por x, dividir, resolver las mitades y revisar la franja realmente mantiene n log n. Que errores de diseño podrian hacer que termine siendo mas lento.
- **Result and use:** Claude explained that the complexity depends not only on dividing the input but also on how the strip is constructed and processed. Repeatedly sorting all points inside each recursive call or comparing every pair in the strip could introduce additional work and invalidate the intended complexity. The response helped identify which operations deserved special attention during code review. The algorithm's correctness and performance still had to be evaluated using the implementation, tests, and complexity argument.

### 15. Checking Recurrence-Based Complexity Arguments — October 5, 2026 (Claude)

- **Own content provided:** Draft complexity explanations for recursive algorithms, including the recurrence T(n) = 2T(n/2) + Θ(n) for algorithms that solve two half-sized subproblems and perform linear combination work.
- **Prompt:** Tengo esta recurrencia T(n) = 2T(n/2) + theta(n). Explicame como justificar el resultado con el metodo maestro y que deberia revisar para no usarlo mal en el informe.
- **Result and use:** Claude explained how to identify the parameters a, b, and f(n), compare f(n) with n^(log_b(a)), and verify which case of the Master Theorem applies. This helped organize the mathematical justification for the algorithms whose recurrence has two recursive calls on half-sized inputs and linear extra work. The explanation was used as a guide for writing the complexity documentation, but the recurrence itself had to be established from each algorithm's actual structure rather than copied without justification.

### 16. Improving English Comments in the Documentation — October 6, 2026 (ChatGPT)

- **Own content provided:** An existing Markdown document with English comments and technical descriptions from the project.
- **Prompt:** Naturaliza los comentarios, mantenlos en ingles pero menos signos y mas sencillo.
- **Result and use:** ChatGPT suggested simpler English wording while retaining the intended technical meaning. The goal was to make the documentation more readable and less formal without losing important algorithmic details. The revised wording was reviewed before use to ensure that terms related to recursion, complexity, and correctness remained accurate. The existing content served as the basis for the edits rather than being replaced by an unrelated explanation.

### 17. Understanding the Experimental Results — October 7, 2026 (Claude)

- **Own content provided:** The experiment included several input distributions, execution-time measurements, and comparisons between two-way and three-way Quicksort.
- **Prompt:** Cuando ya tenga los tiempos de ejecucion como puedo saber si los resultados coinciden con lo que dice la teoria. Explicame como interpretar los exponentes empiricos y que limitaciones deberia mencionar si tengo pocos puntos por curva.
- **Result and use:** Claude explained that empirical growth estimates can help compare observed behavior with theoretical expectations, but that a fitted exponent is not proof of a complexity class. It also discussed how the number of measurements, input variability, and runtime effects can influence the estimated growth. This helped establish a more cautious approach to interpreting experimental data. The explanation was used to plan the discussion of results and limitations, while numerical claims had to come from the actual experiment outputs.

### 18. Understanding Git Branches and Merging Changes — October 7, 2026 (Claude)

- **Own content provided:** The repository used multiple branches for algorithm development, and the changes needed to be integrated without losing existing work.
- **Prompt:** Estoy trabajando con varias ramas en git y necesito pasar los cambios de una rama a develop. Explicame la diferencia entre merge y cherry-pick y como puedo revisar que no vaya a perder cambios.
- **Result and use:** Claude explained that merge integrates the history of a branch, whereas cherry-pick applies selected commits to another branch. It also emphasized checking the current branch, the working tree, and the commit history before integrating changes. This helped clarify which Git workflow was appropriate for changes that belonged together. The commands were treated as operational guidance, and the repository status had to be checked before performing any operation that could affect the working tree.

### 19. Improving the Complexity Explanations — October 8, 2026 (ChatGPT)

- **Own content provided:** Draft explanations comparing growth functions and discussing the applicability of the basic Master Theorem.
- **Prompt:** Naturaliza esta respuesta para que se entienda mas facil, pero conserva la idea matematica y no cambies la conclusion.
- **Result and use:** ChatGPT helped simplify the wording of the explanation while retaining its central mathematical argument. The result was used as a language-editing reference rather than as independent proof of correctness. The recurrence, comparison functions, and conclusion still needed to be checked to ensure that the simplified explanation did not omit an important condition or make an unsupported claim.

### 20. Reviewing the Proofs by Induction — October 9, 2026 (Claude)

- **Own content provided:** Draft proof documentation organized around base cases, induction hypotheses, and inductive steps for the project's algorithms.
- **Prompt:** Estoy escribiendo proofs.md y quiero revisar si mis demostraciones por induccion estan bien justificadas. Que deberia comprobar en el caso base, la hipotesis inductiva y el paso inductivo.
- **Result and use:** Claude explained the purpose of each component of an inductive proof and how the inductive hypothesis must support the correctness of the recursive calls. It also highlighted the need to connect the behavior of the algorithm with the property being proved, instead of merely repeating the algorithm's steps. This helped establish a review checklist for the proof document. The final proofs had to be checked against the actual implementations and the assumptions used in each argument.

### 21. Retrieving a Remote Git Branch — October 10, 2026 (ChatGPT)

- **Own content provided:** The project was hosted on GitHub, and a new remote branch contained changes that were not yet available in the local branch list.
- **Prompt:** Estoy trabajando en github y se creo una rama nueva en la que se hicieron cambios. Como la traigo a remoto y como puedo empezar a trabajar con ella desde mi computador.
- **Result and use:** ChatGPT explained the distinction between fetching remote branch information and checking out a local branch that tracks the remote branch. This helped clarify why a branch visible on GitHub may not immediately appear in the local repository. The guidance was used to understand the workflow for updating remote references and accessing the new branch. The branch name and repository state still needed to be checked before executing the commands.

### 22. Final Review Checklist for the Repository — October 10, 2026 (Claude)

- **Own content provided:** The project structure included algorithm source files, MUnit tests, experiment runners, generated data, timing results, Python plotting scripts, graphs, and Markdown documentation.
- **Prompt:** Hazme una checklist para revisar mi tarea integradora antes de entregarla. Quiero revisar el codigo funcional, los tests, las complejidades, las pruebas por induccion, los experimentos, las graficas y la documentacion. Dime que debo comprobar en cada parte.
- **Result and use:** Claude organized the review into several areas: compliance with functional programming restrictions, correctness of algorithm implementations, coverage of automated tests, mathematical justification of complexity, quality of inductive proofs, reproducibility of the experiments, and consistency between the data, plots, and written conclusions. It also highlighted the importance of checking the repository instructions and the commands required to run the project. This checklist was intended to guide the final review without replacing it. Each item needed to be verified against the actual source files, test results, generated outputs, and assignment rubric.

## General Reflection

Generative AI was used primarily as a learning and review tool. The interactions focused on understanding algorithms, evaluating design choices, planning test cases, organizing the repository, learning Git workflows, interpreting empirical measurements, and improving technical writing.

Claude was the main tool for theoretical explanations and project-planning questions, while ChatGPT was used occasionally for editing documentation and clarifying development workflows. AI suggestions were not treated as sufficient evidence of correctness. Algorithm implementations needed to be tested, complexity claims needed mathematical justification, and experimental conclusions needed to be supported by the collected measurements.
