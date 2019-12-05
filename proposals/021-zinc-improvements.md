# SCP-021: Zinc improvements

## Proposer

Proposed by Eugene Yokota, Lightbend, November 2019

## Abstract

We propose enhancements to the core of the compilation toolchain, Zinc. (Zinc provides Scala compilation services, including incremental compilation, to build tools and IDEs.)  We think the next step in Zinc's evolution is to bring its compiler-specific logic into the Scala compiler itself, so that incremental compilation becomes a supported feature of the language, rather than a feature that's tacked on externally.

Some of the outlined tasks may be implemented by Lightbend Scala team directly, but in collaboration with Scala Center engineers, and using the Center as a coordination point for community and stakeholder interests.

Zinc started its life as a module in sbt, outside of Scala. Closer coordination between the Scala compilers (Scala 2.x "nsc", dotc, Twitter rsc) becomes more important to respond to growing demand for better performance (see also SCP-015) and different use cases such as build caching.

As the ecosystem prepares for Scala 3.x, we should ensure that Zinc continues to function with the new compiler.

## Proposal

1. Fixing under-compilations
3. Standardization of signature and outline types
3. Propagating of Zinc to build tools

### Fixing under-compilations

The incremental compilation implemented by Zinc works conceptually as follows:

1. During the first compilation, class-to-class relationships and signature info are recorded.
2. During the second compilation onwards, initial sources are invalidated based on the timestamp change (or the file watcher); and Zinc tries to calculate the minimal set of transitively invalidated sources based on the relationship analysis and heuristics.

Invalidating more sources than necessary would lead to unnecessary work, called *over-compilation*; invalidating fewer source than necessary could lead to invalid programs, and we call them *under-compilation*. There are known pain points in Scala 2.x in terms of under-compilation:

- Package objects
- Wildcard imports
- Macros
- Implicit search
- Default arguments
- Name shadowing
- Compile-time contants [zinc#227][227]

The proposed task is to add tests of incremental compilation in scala/scala repository, and provide fixes where possible.

### Standardization of signature and outline types

In the Scala tooling ecosystem, the notion of outline types is emerging as a key technique. In a nutshell, outline types are public signatures of a given code base scraped without going through the full typer (missing return types, super etc might still need to be resolved).

Here are some of the cases for signature information (also known as API info):

- Separate compilation by Scalac.
- Incremental compilation by Zinc.
- Reflective detection of test suites by sbt.
- Build pipelining by starting the subproject builds earlier. (outline typing allows further parallelism)

Today, the public signature information is represented as various forms:

- Semanticdb
- Scala pickle in `*.class` file
- Zinc API info
- TASTY

During compilation, Zinc creates API info by adding _Extract API_ phase, which takes up approximately 5% of the compilation time. Although the information is useful to sbt (for both incremental compilation and reflective detection), there's an overlap of data with pickles / TASTY that seems wasteful.

Given the utility of signature information, we should standardize the representation of the signature information across Scala 2.13 and Scala 3.x and provide it via Zinc. Depending on the use case, different representations may have different degrees of details and/or performance characteristics for some operations.

Here is the scoring creteria for the standard signature info:

- Can query for all types that extends some type X.
- Can query for all types that extends with annotation Y.
- Store hash values for public signatures and private signatures.
- Represent normal (post-typer) public signature info for Scala 2.13.
- Represent normal (post-typer) public signature info for Scala 3.x.
- Represent outline type (pre-typer) public signature info for Scala 2.13.
- Represent outline type (pre-typer) public signature info for  Scala 3.x.
- Support deserialization back into the symbol table for separate compilation in Scala 2.13.
- Support deserialization back into the symbol table for separate compilation in Scala 3.x.

Side note: [SCP-018][18] proposed that Scala 2.14 and 3.0 both emit TASTY post-typer, "so that we may have one post-type checker compiler pipeline for both versions." This proposal is less ambitious in this aspect since it targets current 2.13 and 3.x series each having their own backends. It is similar in a sense that it calls for a standard signature representation that is able to express both Scala 2.13 type system and Scala 3.x type system. The goal of 021 is to share tooling logic for separate compilation, incremental compilation, etc.

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

Unknown at this stage.

## Timescales

Unknown at this stage.

  [18]: https://github.com/scalacenter/advisoryboard/blob/ad92b6cb946d17031c367a4f479f5764b4f36b38/proposals/018-converging-214-30.md
  [227]: https://github.com/sbt/zinc/issues/227
  [559]: https://github.com/sbt/zinc/issues/559
