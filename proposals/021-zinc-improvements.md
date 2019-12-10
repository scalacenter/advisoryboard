# SCP-021: Zinc improvements

## Proposer

Proposed by Eugene Yokota, Lightbend, November 2019

## Abstract

We propose enhancements to the core of the compilation toolchain, Zinc. Zinc provides Scala compilation services, including incremental compilation, to build tools and IDEs. We think the next step in Zinc's evolution is closer coordination with Scala compilers. Ultimately incremental compilation should become a supported feature of the language, similar to how we treat collections, which could entail better testing and language evolution considering the impact on incremental compilation.

Some of the outlined tasks may be implemented by Lightbend Scala team directly, but in collaboration with Scala Center engineers, and using the Center as a coordination point for community and stakeholder interests.

Zinc started its life as a module in sbt, outside of Scala. Closer coordination between the Scala compilers becomes more important to respond to growing demand for better performance (see also SCP-015) and different use cases such as build caching.

As the ecosystem prepares for Scala 3.x, we should ensure that Zinc continues to function with the new compiler.

## Background

Both in local development and in CI (continuous integration) servers, Scala code is often built using one of build tools that internally uses Zinc. The incremental compilation implemented by Zinc works conceptually as follows:

1. During the first compilation, class-to-class relationships and signature info are recorded.
2. During the second compilation onwards, initial sources are invalidated based on the timestamp change (or the file watcher); and Zinc tries to calculate the minimal set of transitively invalidated sources based on the relationship analysis and heuristics.

Invalidating more sources than necessary leads to unnecessary work, called *over-compilation*; invalidating fewer source than necessary can lead to invalid programs, and we call them *under-compilation*.

There are certain language features that leads to gotcha situation in incremental compilation. For example, using wildcard import you might be able to trick name hashing to under compile.

## Expected outcome

- Improved correctness
  - By bringing compiler implementation specific details into Scala compiler, we expect better maintenance and test coverage over time. This applies to Dependency tracking phase.
  - When heuristics are used, there's a trade-off between over- and under-compilation (currently it focuses on local development). We should provide a knob to adjust this for CI usages.
- Improved Zinc performance
  - By Scala compiler and Zinc cooperating, there might be opportunities for speedup and memory usage reduction. Concretely, Zinc API Extraction phase (which can be 5% of compilation time; your mileage may vary) could potentially be removed if we could reuse the information from an existing signature information.

## Proposal

1. Benchmark incremental compilation
2. Fixing under-compilations
3. Standardization of signature and outline types
4. Propagating of Zinc to build tools

### Benchmark incremental compilation

We propose to add incremental compilation performance to the current [compiler benchmark][benchmark] to [quantify][dashboard] performance improvements.

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

The proposed task is to add tests of incremental compilation in scala/scala repository, and provide fixes where possible. In a situation where heuristics might be used consider providing "strict" mode where it biases towards correctness and over-compilation.

### Standardization of signature info


Here are some of the use cases for signature information (also known as API info):

- Separate compilation by Scalac.
- Incremental compilation by Zinc.
- Reflective detection of test suites by sbt.
- Build pipelining by starting the subproject builds earlier. (outline typing allows further parallelism)

Today, the public signature information is represented as various forms:

- Semanticdb
- Scala pickle in `*.class` file
- Zinc API info
- TASTY

During compilation, Zinc creates API info by adding _Extract API_ phase (in a real-world setup this was observered to be approximately 5% of the compilation time; your mileage may vary). Although the information is useful to sbt (for both incremental compilation and reflective detection), there's an overlap of data with pickles / TASTY that seems wasteful.

Given the utility of signature information, we should standardize the representation of the signature information across Scala 2.13 and Scala 3.x and provide it via Zinc. Depending on the use case, different representations may have different degrees of details and/or performance characteristics for some operations.

Here is the scoring criteria for the standard signature info:

- Can query for all types that extends some type X.
- Can query for all types that extends with annotation Y.
- Store hash values for public signatures and private signatures.
- Represent normal (post-typer) public signature info for Scala 2.13.
- Represent normal (post-typer) public signature info for Scala 3.x.

- Support deserialization back into the symbol table for separate compilation in Scala 2.13.
- Support deserialization back into the symbol table for separate compilation in Scala 3.x.

One possible solution is Scala 2.x uses Scala pickles, Scala 3.x uses TASTY both providing the feature currently provided by Zinc API info (API hashing, reflective querying). In that case, Lightbend Scala team will work on Scala pickles, and Scala Center could work on TASTY.

Side note: [SCP-018][18] proposed that Scala 2.14 and 3.0 both emit TASTY post-typer, "so that we may have one post-type checker compiler pipeline for both versions." This proposal is less ambitious in this aspect since it targets current 2.13 and 3.x series each having their own backends. It is similar in the sense that it calls for a standard signature representation that is able to express both Scala 2.13 type system and Scala 3.x type system. The goal of 021 is to share tooling logic for separate compilation, incremental compilation, etc.

### Propagating Zinc changes to build tools

Here are some of the build tools that use Zinc:

- sbt
- Bloop
- Gradle via Scala plugin
- Maven via scala-maven-plugin
- Mill
- Pants

As we make changes to Zinc either as bug patches or in a more major way in the future, we should make sure to bring the various downstream build tools along so they do not lag behind. The proposed task is to send pull requests to the downstream build tools.

## Cost

#### Benchmark incremental compilation

Two part-time engineer months to add incremental compilation benchmarks to the existing compiler benchmarks.

#### Fixing under-compilations

Three full-time engineer months for the initial investigation of adding scripted tests to Scala 2.x and 3.x, and providing low-hanging fixes.

Three additional full-time engineer months for more in-depth fixes or workarounds such as "strict" mode based on the initial findings.

#### Standardization of signature info

One part-time engineer month to coordinate initial design meetings. Six full-time engineer months to extend an existing signature representation as a replacement of Zinc API info.

#### Propagating Zinc changes to build tools

Three part-time engineer months to propagate new Zinc implementation to downstream build tools.

## Timescales

#### Benchmark incremental compilation

This could start any time. To confirm performance speedup it would be beneficial to have some benchmarks prior to performance related works.

#### Fixing under-compilations

This is dependent on Lightbend completing the task of migrating the compiler bridge to scalac, which should be done by early 2020.

#### Standardization of signature info

It would be nice to start the initial design meetings soon. There's no blocker for that. If the design of TASTY is affected, this task needs to be addressed before Scala 3.0 becomes final.

#### Propagating Zinc changes to build tools

This task would take place after new Zinc comes out.

  [18]: https://github.com/scalacenter/advisoryboard/blob/ad92b6cb946d17031c367a4f479f5764b4f36b38/proposals/018-converging-214-30.md
  [227]: https://github.com/sbt/zinc/issues/227
  [559]: https://github.com/sbt/zinc/issues/559
  [benchmark]: https://github.com/scala/compiler-benchmark
  [dashboard]: https://scala-ci.typesafe.com/grafana/dashboard/db/scala-benchmark?orgId=1
