# SCP-002: Migration path to Dotty for Scalac users

## Proposer

Proposed by Timothy Perrett and Jonathan Perry, Verizon and Goldman Sachs
respectively, 30th June 2016.

## Abstract

The forthcoming release of Dotty leaves a large question mark over how existing
users of Scala will migrate to what is essentially, Scala 3.0. This currently
has a large unknown factor for users of Scalac; not knowing the time or cost of
any potential upgrade. Moreover, without a path to migrate existing code bases,
the Scala community could be facing a similar split that was experienced by
Python with 2.x vs 3.x, which everyone agrees is undesirable and not in the
best interests of the overall Scala community.

## Proposal

In order to mitigate the potential risk to users of Scalac with regards to the
migration, we propose a multi-pronged effort:

1. The invested entities behind Dotty make a concerted effort to have existing
Scalac codebases be entirely compatible with Dotty, such that a migrating user
has zero (or at least minimal) work required to migrate. This could be achieved
either via altering Scalac to be Dotty compliant or altering Dotty / Dotty
bridge to accept byte code produced from existing 2.x Scalac.

1. We request and encourage that EPFL/Scala Center/Lightbend specifically
enumerate any deltas vs scalac, such that a migrating user would be able to
understand the cost of upgrading to Dotty explicitly, before embarking on any
such migration.

1. Many commercial entities would likely feel more at ease about a migration if
a path existed for said entities to pay for certain features - or bugs - to be
corrected in a timely manner, in order to migrate their code without source
changes. What might this look like and whom would the commercial entity doing
the work be? (Ownership of Dotty between EPFL and Lightbend is not clear)

## Cost

Costs unknown by proposers, as extensive knowledge of the internals of both
Scalac and Dotty are required to make fair estimate.

## Timescales

It would be good if this proposal was available shortly after any official
release of Dotty, as the probability of fragmenting the community - Python
style - increases exponentially month over month, once Dotty is released (i.e.
libraries migrate to Dotty, and abandon their Scalac users).

