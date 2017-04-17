# Improve Resources for Learning SBT

## Proposer

Proposed by Jon Pretty, Chairperson.

FIXME: Need to decide whether I can make a proposal directly, or should go
through a community representative.

## Abstract

The majority of Scala open-source and commercial projects use SBT to build, and
whilst it is not the only viable build tool available for Scala, its ubiquity
has made it a de-facto standard in the Scala ecosystem.

Despite being called the "Simple Build Tool", it is a complex tool which offers
users a lot of power.  However, its learning curve is such that all but the
simplest builds require developers to invest some time into understanding an
abstraction which (despite its intentions) can appear unintuitive, or worse,
counterintuitive, even to experienced Scala developers.

The SBT website offers extensive documentation, presented in the style of a
"user guide" and StackOverflow offers answers to many SBT-related questions.
While useful, these may not suit all developers, many of whom may learn better
from more interactive teaching methods.

Many existing resources are also biased towards more immediate solutions to
specific problems, often with copy/paste code fragments, and while exposure to
these will, over the long-term, gradually enhance a developer's capabilities
with SBT, they are an inefficient way to improve a developer's *understanding*
of SBT, or to offer a path for a user to become an advanced SBT developer.

## Proposal

The Scala Center should use its extensive internal experience of SBT to improve
the learning experience for Scala developers looking to learn SBT or improve
their existing understanding.

As much as possible, this should take advantage of existing freely-available
SBT resources, but the key deliverable should be some form of learning guide or
course (as the Scala Center decides most appropriate, but herein referred to as
a "course") which takes learners through the steps necessary to become a
capable SBT developer with a deep understanding of the tool, its domain model,
and the motivation for its design.

Some interactive learning, in which learners develop real SBT buildfiles should
be a requisite of the course.

## Cost

Developing an eight-hour course should take approximately eight full-time weeks
for one developer, of which the first two weeks should be dedicated to
consulting with the Scala community on the best approach, and to clearly
defining the scope of the course.

The ongoing costs of maintaining the course should not exceed one
developer-week per year, but the maintenance should not be overlooked.

## Timescales

The course should be developed within three months of the release of SBT 1.0.


