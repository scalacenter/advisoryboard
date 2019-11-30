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
2. Propagating of Zinc to build tools
3. Standardization of signature and outline types
4. Understanding Scala 3 impact to Zinc

### Fixing under-compilations

There are known pain points in Scala 2.x in terms of under-compilation:

- Package objects
- Wildcard imports
- Macros
- Implicit search
- Default arguments
- Name shadowing

Given known under-compilation issues in Zinc with regards to the above problem areas:

- Could we reuse the name hashing heuristics to invalidate code arising from these under-compilation scenarios?
- Given that the cost of under-compilation might be higher in some environments, could we introduce "strict mode" to Zinc such that it would err towards over-compilation? In the most extreme case, the "strict mode" may print a warning and invalidate any time it sees a wildcard import.
- https://github.com/sbt/zinc/issues/559

### Propagating Zinc changes to build tools

Here are some of the build tools that use Zinc:

- sbt
- Bloop
- Gradle via Scala plugin
- Maven via scala-maven-plugin
- Mill
- Pants

As we make changes to Zinc either as bug patches or in a more major way in the future, we should make sure to bring the various downstream build tools along so they do not lag behind. The proposed task is to send pull requests to the downstream build tools.

### Standardization of signature and outline types

In the Scala tooling ecosystem, the notion of outline types is emerging as a key technique. In a nutshell, outline types are public signatures of a given code base scraped without going through the full typer (missing return types, super etc might still need to be resolved).

Here are some of the cases for signature information (also known as API info):

- Company-wide code browsing (Metadoc in Twitter).
- Separate compilation by Scalac.
- Incremental compilation by Zinc.
- Build pipelining by starting the subproject builds earlier. (outline typing allows further parallelism)

Today, the public signature information is represented as various forms:

- Semanticdb
- Scala pickle in `*.class` file
- Zinc API info
- TASTY

Given the utility of signature information, we should standardize the representation of the signature information across Scala 2.13 and Scala 3.x and provide it via Zinc. Depending on the use case, different representation may have different degrees of details and/or performance characteristics for some operations.

Here are some goals for the standard signature info:

- Represent normal (post-typer) public signature info for Scala 2.13.
- Represent normal (post-typer) public signature info for Scala 3.x.
- Represent outline type (pre-typer) public signature info for Scala 2.13.
- Represent outline type (pre-typer) public signature info for  Scala 3.x.
- Support deserialization back into the symbol table for separate compilation in Scala 2.13.
- Support deserialization back into the symbol table for separate compilation in Scala 3.x.

### Understanding Scala 3 impact to Zinc

Incremental compilation is a challenging problem due to Scala’s expressive power It can be hard to predict how language changes will impact incremental compilation.

As we are evolving the language towards Scala 3, we propose to better understand the tradeoffs and possibly suggest changes to accommodate better incremental compilation.

For instance, among the source-to-source dependencies, trait inheritance might be one of the basic ones, but whether it requires recompilation or not depends on a complicated set of rules. This information is something that the compiler could expose to the user and/or Zinc to avoid over-compilation.

In this report the following questions should be answered:

- How do Scala 3’s features affect incremental compilation? In other words, what are the different source-to-source dependencies in Scala 3.x?
- In each of the scenario, how does source change affect recompilation and/or binary substitution.
- Is the compiler bridge offered by Dotty sufficiently usable in production?
- Are there language changes we can make to improve incremental compilation performance/correctness?

## Cost

Unknown at this stage.

## Timescales

Unknown at this stage.
