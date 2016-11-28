# SCP-[000]: Improve Compilation Scalability

## Proposer

Proposed by Stu Hood, Twitter, November 2016

## Abstract

The most commonly discussed angle of attack for improving the Scala build experience is improving the speed of the compiler. Instead, this document addresses a different angle: ensuring that it is possible to scale multiple, parallel (even remote) invocations of the compiler.

When building very large Scala projects, the presence/necessity of transitive build dependencies on the compile classpath represents a very real limit on the ability to distribute compilation: shipping 2000 transitive dependencies to a cluster of remote compiler instances is significantly more challenging than shipping 20 direct dependencies. Additionally, over-declaring dependencies causes cache keys for build artifacts to be invalidated more often than is strictly necessary.

scalac generally does not require the entire transitive classpath of a library to compile it, but its requirements are not formally defined, and in many cases are non-intuitive. This uncertainty makes it difficult to accurately declare the direct/"strict" dependencies of a library... a generally accepted build-hygiene best practice with great benefits for scalability (and straight-line performance).

One example case (of many) where an unimported dependency is required is when a compilation unit `org.c` uses a class `A` defined in a separate compilation unit `org.a`, which implements a trait `B` defined in a compilation unit `org.b`. While compiling `org.c` with `org.a` on the classpath (but not `org.b`), scalac emits an error like the following:

    [error] missing or invalid dependency detected while loading class file 'A.class'.
    [error] Could not access term b in package org,
    [error] because it (or its dependencies) are missing. Check your build definition for
    [error] missing or conflicting dependencies. (Re-run with `-Ylog-classpath` to see the problematic classpath.)
    [error] A full rebuild may help if 'A.class' was compiled against an incompatible version of org.

Another recent example of this type of mismatch is found in [scala/scala-dev#275](https://github.com/scala/scala-dev/issues/275).

## Proposal

This document proposes formalizing (and ideally, tightening) scalac/dotc's requirements around the presence of transitive dependencies.

Here are a few potential concrete outcomes of this effort, in descending order based on how useful they would be to users, and to developers of Scala build-tooling:

1. A tightening of the compiler's requirements such that only symbols explicitly referenced in `import` statements in a `.scala` file are required on the classpath during compilation.
2. A dependency story that is sufficiently simplified to allow for a scalac/dotc integrated tool that extracts all direct symbol dependencies of a Scala source file *without* a classpath. Such a tool would be the holy grail for Scala compilation scalability, as it would (given a lookup table from symbol to classpath entry) allow for very-fine-grained, semi-automatic dependency inference and build graph partitioning.
3. An expansion of the Scala Language Specification to list all cases in which a symbol from another compilation unit must be present on the classpath, including: 1) subclassing, 2) return types of superclasses' public methods, 3) direct reference, 4) etc.
4. Changes to scalac/dotc internal APIs to make expansion of transitive symbols explicit, in order to bias compiler developers against unnecessary transitive walks.
5. A compiler flag to require `import` statements for all symbols used during compilation (including those not otherwise mentioned in the source). This would primarily assist with making the transition from transitive to direct dependencies, rather than helping to maintain a build that is already using direct dependencies. (via [@posco](twitter.com/posco))

## Cost

Although this proposal is bounded in scope (it is possible to call it "done" when one or more of the above proposals are implemented), it is purposely open-ended with regard to strategy in order to give the implementers flexibility to work with the core team, and to use their best judgement.

Depending on the resulting strategy, this proposal could involve anywhere from 1 person-month to implement only item `3` or item `5`, to 6 person-months to implement some combination of the remaining items.

## Timescales

Ideally, action on this proposal would occur sometime within the next 6 to 9 months (from November 2016), in order to align with Twitter internal efforts toward build graph improvements (which will hopefully include enabling usage of only strict/direct dependencies).
