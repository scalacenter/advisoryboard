---
date: December 2019
accepted: true
status: active
---

# SCP-021: Zinc improvements

## Proposer

Proposed by Eugene Yokota, Lightbend, November 2019

## Abstract

We propose enhancements to the core of the compilation toolchain, Zinc. Zinc provides Scala compilation services, including incremental compilation, to build tools and IDEs. We think the next step in Zinc's evolution is closer coordination with Scala compilers. Ultimately incremental compilation should become a supported feature of the language, similar to how we treat collections, which could entail better testing and language evolution considering the impact on incremental compilation.

Some of the outlined tasks may be implemented by Lightbend Scala team directly, but in collaboration with Scala Center engineers, and using the Center as a coordination point for community and stakeholder interests.

Zinc started its life as a module in sbt, outside of Scala. Closer coordination between the Scala compilers becomes more important to respond to growing demand for better performance (see also SCP-015) and different use cases such as build caching.

As the ecosystem prepares for Scala 3.x, we should ensure that Zinc continues to function with the new compiler.

## Proposal

- Improve Scala 2.x compilation performance and correctness
- Ensuring that Zinc continues to work with Scala 3
- Work toward a design solution that achieves the above

## Discussion

The following is a recommendation of activities that could take place provided as a guideline to give ideas about the proposed work. This is not required this as an acceptance criterion for the proposed work.

### Benchmark incremental compilation

We suggest to add incremental compilation performance to the current [compiler benchmark][benchmark] to [quantify][dashboard] performance improvements.

Suggested tests:
- A single module with minimum library dependencies:
  - Comparison of clean build using sbt vs scalac to measure the overhead of added phases.
  - Incremental build twice without source changes to measure the overhead of no-op.
  - Incremental build with source changes (add classes, add methods, change methods etc).
- A single module with a big library dependencies:
  - Comparison of clean build using sbt vs scalac to measure the overhead of added phases.
  - Incremental build twice without sources but with an added library. This is relevant because Zinc performs classpath lookup for all library-associated classes to validate all library JARs.
  - Incremental build with source changes (add classes, add methods, change methods etc).
- Multiple modules "core" and "app" with minimum library dependencies:
  - Comparison of clean build using sbt vs scalac to measure the overhead of added phases.
  - Incremental build twice without source changes to measure the overhead of no-op.
  - Incremental build with source changes to "core" (add classes, add methods, change methods etc).
- Two single modules "core" and "app" that uses library dependencies to depend on "core":
  - Comparison of clean build using sbt vs scalac to measure the overhead of added phases.
  - Incremental build twice without source changes to measure the overhead of no-op.
  - Incremental build with source changes to "core" (add classes, add methods, change methods etc) to emulate binary change.

### Fixing under-compilations

There are known pain points in Scala 2.x in terms of under-compilation:

- Package objects
- Wildcard imports
- Macros
- Implicit search
- Default arguments
- Name shadowing
- Compile-time constants [zinc#227][227]

The suggested task is to add tests of incremental compilation in scala/scala repository and Scala 3.x, and provide fixes where possible.

### Understanding Scala 3 impact to Zinc

In this report the following questions should be answered:

- How do Scala 3’s features affect incremental compilation? In other words, what are the different source-to-source dependencies in Scala 3.x?
- In each of the scenario, how does source change affect recompilation and/or binary substitution.
- Are there language changes we can make to improve incremental compilation performance/correctness?

### Replace Zinc API info

During compilation, Zinc creates API info by adding _Extract API_ phase (in a real-world setup this was observered to be approximately 5% of the compilation time; your mileage may vary). Although the information is useful to sbt (for both incremental compilation and reflective detection), there's an overlap of data with pickles / TASTY that seems wasteful.

One possible solution is Scala 2.x uses Scala pickles, Scala 3.x uses TASTY both providing the feature currently provided by Zinc API info (API hashing, reflective querying).

### Propagating Zinc changes to build tools and Scala 3.x

Currently Scala 3.x contains its own compiler bridge. As we make changes to Zinc either as bug patches or in a more major way in the future, we should make sure to bring it along so they do not lag behind.

Similarly, we should make sure to bring downstream build tools along so they do not lag behind. Here are some of the build tools that use Zinc:

- sbt
- Bloop
- Gradle via Scala plugin
- Maven via scala-maven-plugin
- Mill
- Pants

## Cost

3 full-time engineer month to coordinate and participate in the design and prototyping of a solution for Zinc support across Scala versions. At least the design aspect should be done in collaboration with the relevant maintainers.

On the condition that the initial design has been completed, additional 12 full-time engineering months for the implementation and maintenance tasks.

## Timescales

The initial design should start sooner rather than later.

## Background

#### Under-compilation

Both in local development and in CI (continuous integration) servers, Scala code is often built using one of build tools that internally uses Zinc. The incremental compilation implemented by Zinc works conceptually as follows:

1. During the first compilation, class-to-class relationships and signature info are recorded.
2. During the second compilation onwards, initial sources are invalidated based on the timestamp change (or the file watcher); and Zinc tries to calculate the minimal set of transitively invalidated sources based on the relationship analysis and heuristics.

Invalidating more sources than necessary leads to unnecessary work, called *over-compilation*; invalidating fewer source than necessary can lead to invalid programs, and we call them *under-compilation*.

There are certain language features that leads to gotcha situation in incremental compilation. For example, using wildcard import you might be able to trick name hashing to under compile.

#### Components

The following diagram describes the general relationships between the relevant components.

```
+----------------------------------------------------+
|                   build tool                       |
+---+------------------------------------------------+
    |
    |
+---v------+          +------------------------------+
|          |          | compiler-interface           |
|          +---------->                              |
|          |          +------------------^-----------+
|          +------+                      |
|  Zinc    |      |                      |
|          |  +---v--------+    +--------+-----------+
|          |  |   Analysis <----+ compiler-bridge    |
|          |  |   file     |    |                    |
|          |  +------------+    +--------+-----------+
|          |                             |
|          |                             |
|          |                    +--------v-----------+
|          |                    | Scala compiler     |
+----------+                    +--------------------+
```

1. Build tools communicate only with Zinc. Zinc exposes a method called [`compile(...)`][1] and everything else is opaque to the build tools.
2. To abstract over the Scala compiler implementations Zinc talks to the compilers via compiler-interface. We are moving this out of Zinc to [sbt/compiler-interface][2].
3. A specific implementation of compiler-interface for a Scala compiler is called a compiler bridge. In Zinc 1.3, this resides in sbt/zinc, but we are planning to move that into scala/scala. See [scala/scala#8531][8531].
4. During the compilation, the compiler-bridge generates metadata called `Analysis` file, which contains API info etc. This is later used by Zinc for incremental compilation. The content of `Analysis` file is meant to be an implementation detail of Zinc.
5. As part of standardization, my hope is that build tools would acquire new capability to reflectively query for signatures via Zinc.

  [18]: https://github.com/scalacenter/advisoryboard/blob/ad92b6cb946d17031c367a4f479f5764b4f36b38/proposals/018-converging-214-30.md
  [227]: https://github.com/sbt/zinc/issues/227
  [559]: https://github.com/sbt/zinc/issues/559
  [benchmark]: https://github.com/scala/compiler-benchmark
  [dashboard]: https://scala-ci.typesafe.com/grafana/dashboard/db/scala-benchmark?orgId=1
  [1]: https://github.com/sbt/zinc/blob/v1.3.1/internal/compiler-interface/src/main/java/xsbti/compile/IncrementalCompiler.java
  [2]: https://github.com/sbt/compiler-interface
  [8531]: https://github.com/scala/scala/pull/8531
