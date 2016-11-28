# SCP-011: Collaborative redesign and implementation of Scala 2.13's collections library.

## Proposer

Proposed by Lightbend, November 2016

Credits:
* initial version: Stefan Zeiger
* feedback: Adriaan Moors, Seth Tisue, Dale Wijnand

## Abstract

The Scala collections library underwent a major redesign in Scala 2.8. While the current design proved to be successful in many areas, several pain points have become apparent over the years which indicate fundamental issues with the design that cannot be removed through gradual minor changes. This prompted the request for [strawman proposals](https://github.com/lampepfl/dotty/issues/818) for a new design in the context of Dotty. We propose that the Scala Center work on the implementation of this new design for the Scala 2.13 standard library.

## Proposal

Lightbend will contribute a significant amount of engineering time to lead and help implement the project with the goal of integrating the library into Scala 2.13. Due to the size of the effort and the large number of interested parties, the Scala Center should also be involved in the design and implementation of the new collections library. We expect the following activities as part of the project:

- Design the new library, based on the current strawman proposals and the design goals.
- Solicit and incorporate feedback from the community and commercial users.
- Help community contributors get started, assist in the code review process.
- Port the current collection implementations to the new design.
- Benchmark the performance of the new library vs the old one.
- Define and implement a migration strategy.

Explicit goals of the new design are:
- Minimize migration effort from Scala 2.12's collections. 
- To the largest extent possible, reuse the current implementation and its test suite, as they have been hardened by production use over many years.
- Simpler API and implementation and a more maintainable code base.
- More API separation between mutable, immutable and parallel collections. The current tight integration turned out to be more confusing than helpful.
- Better integration of lazy collections: Fully lazy streams; New approach to views as reified iterator operations.
- Better integration with Java 8 streams and/or parallel collections for high-performance parallel operations.
- Better performance through specialization and other optimizations.
- Easier to extend with custom collection implementations.
- Compatible with Scala 2.13, with a clear migration path to or compatibility with Dotty.

Non-goals:
- Rewriting collection implementations from scratch.
- Cleanup just for cleanup -- all changes should be motivated by the key design goals above.
- Full source compatibility. Common usage of public members should not be affected (except for recompilation). We do expect to break compatibility for inheritance clients. Breaking changes will be documented with a migration guide that motivates the change and explains how to compensate for it.

More details on design goals can be found in the [strawman proposals ticket](https://github.com/lampepfl/dotty/issues/818).

## Cost

This is a substantial effort, core to the Scala 2.13 roadmap. We propose 2 full-time Scala Center engineers spend 80% of their time on this project for its duration.

## Timescales

The project should be completed by the end of 2017, in time for the last Scala 2.13.0 milestone.
