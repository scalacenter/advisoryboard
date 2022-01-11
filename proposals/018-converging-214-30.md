---
date: September 2018
accepted: true
updates:
  - Completed, but in modified form: Scala 2.14 is no longer planned; Dotty is already able to consume Scala 2 artifacts; and the Center has completed a modification of Scala 2.13 to read TASTy so it can consume Scala 3 artifacts
status: completed
---

# SCP-018: Converging the Intermediate Representation of Scala 2.14 and Scala 3.0

## Proposer
Adriaan Moors, Lightbend

## Abstract
As outlined in SCP-002, the migration from Scala 2 to 3 requires a multi-pronged effort.  This proposal is about the tooling aspect: can we share an intermediate representation (TASTY) between Scala 2.14 and 3.0, so that we may have one post-type checker compiler pipeline for both versions, shared tooling such as Scaladoc, whole program linkers and (more immediately) build tools?

## Proposal
We believe we can answer this question in the affirmative, but it will require a significant investment. Independently from this proposal, the commonalities of Scala 2.14 and 3.0 will be large, but some language features will have to be approximated (e.g., existentials in 2.14, union types in 3.0).

We propose a collaboration between all interested parties (at least EPFL, Scala Center and Lightbend) to further evolve TASTY into a format that can be a shared target of the Scala 2.14 and 3.0 type checkers, so that the remainder of the pipeline could be shared. This is the ultimate goal, with a useful compromise available to us: only a subset of either language could compile to the shared subset of TASTY. We assume this still covers a large percentage of Scala code bases out there. We could offer separate tooling to remove the unsupported (deprecated) parts from Scala 2 code bases.

The benefits of binary-compatibility by construction between Scala 2 and 3 are numerous, with a more gradual bootstrapping of the eco-system, a longer supported life cycle for Scala 2.14 due to reduced cost (2 front-ends, but only one shared back-end and tooling to maintain).

The risk is mostly to the 2.14 release: this is an ambitious plan. Perhaps there are easier ways to accomplish the higher-level goal of reducing the gap between 2.14 and 3.0.

## Cost
Work is already underway at EPFL to adapt the Scala 2 compiler to emit TASTY, but a concerted effort is needed to complete the work in time for the 2.14 cycle. Ideally, the Scala Center should hire a compiler engineer to work on this task close to full time for a year. Lightbend will dedicate significant engineering time as well.

## Timescales
2.14 development is slated to begin in Jan 2019, with a release by late 2020, early 2021.
