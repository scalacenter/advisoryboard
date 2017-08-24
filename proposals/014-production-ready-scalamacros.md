# Production ready scalamacros/scalamacros

## Proposer

Proposed by 47 Degrees and SAP. Prepared with help from Eugene Burmako and Olafur Pall Geirsson.

## Abstract

Def macros and macro annotations have become an integral part of the Scala 2.x ecosystem. Well-known libraries like Play, sbt, Scala.js, ScalaTest, Shapeless, Slick, Spark, Spire and others use macros to achieve previously unreachable standards of terseness and type safety.

Unfortunately, Scala macros have also gained notoriety as an arcane and brittle technology. The most common criticisms of Scala macros concern their difficult metaprogramming API based on compiler internals as well as a lack of featureful editor support. Even five years after their introduction, traditional macros don’t expand properly in Intellij. This leads to a proliferation of spurious “red squiggle” errors, even in simple projects. As a result of these problems the language committee has decided to retire the current macro system in Scala 3.x.

During the last couple years a new macro system has been developed to support both Scala 2.x and Scala 3.x. The new system is based on the platform-independent metaprogramming API Scalameta, which is designed to be easy to use and easy to support across multiple implementations of the Scala language. The result of this multi-year effort is hosted at https://github.com/scalamacros/scalamacros and its design is explained in detail in the paper [“Two approaches to portable macros”](https://www.dropbox.com/s/2xzcczr3q77veg1/gestalt.pdf).

This repository is currently a working prototype of the new macro system that features:

- Scalameta based syntactic and semantic APIs that cross-compile against Scala 2.12, 2.13 and Dotty. The corresponding library is quite slim, being less than 500Kb in size.
- A prototype implementation of the new macro engine for Scala 2.12.3 that supports macro annotations and def macros.

No one is currently employed to work on scalamacros/scalamacros. This means that the project relies on volunteer effort to reach a production-ready status.

## Proposal

We propose that the Scala Center invests engineering resources to make scalamacros/scalamacros production ready so the community can migrate to this new macro system. This will ultimately enable better IDE support and ease migration to dotty.

Focus should be put on the following tasks:

- Finalize support for macro annotations with coverage for class/trait/object/def definitions and modifiers.
- Finish support for def macro materializers with full coverage for types. This includes support for the functionality provided by legacy whitebox macros-- specifically allowing def macros to inspect types at the macro callsite and return values of types not known before macro expansion.
- Provide documentation for using the new macro system. This includes a migration guide from legacy macros, potentially with scalafix rewrites.


## Cost

We expect each of these items to require a minimum one month effort. Combined, we expect 4 to 5 months of a full-time hired engineer.

## Timescale

The project should be completed by March 2018.
