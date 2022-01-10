---
date: December 2018
accepted: true
updates:
  - see https://www.scala-lang.org/2019/10/17/dependency-management.html
status: completed
---

# SBT transitive dependency conflicts management improvements

## Proposer

Proposed by Julien Tournay, Spotify, November 2018

## Abstract

In its current state, SBT only offers limited options when it comes to detecting and fixing conflicts in versions of transitive dependencies. This is a very common issue for projects depending on Java libraries, as some libraries are extremely common in the Java ecosystem (netty, guava, grpc, etc.) and a particular build may transitively depend on a dozen different versions of a given library. This leads to exceptions such as `NoSuchMethodException` being thrown at runtime, and there’s no way to statically check that dependencies are mutually compatible.

Furthermore, while SBT does have a [conflict management](https://www.scala-sbt.org/1.x/docs/Library-Management.html#Conflict+Management) configuration, some of its possible values only work if dependencies are published on an Ivy repository. Given that the vast majority of libraries are published on Maven repositories, the setting is effectively useless.

The process of finding out which dependencies are problematic, understanding why a particular version was selected and fixing the conflicts is mostly undocumented. It can for example be pretty tricky to know which JAR pulls `io.netty.SomeClass` given that a project can transitively pull 5 or 6 different dependencies with `groupid`  `io.netty`.

It should be noted that this particular issue creates a lot of frustration for developers coming from Java and used to Maven because:

- Maven and SBT do not resolve dependencies in the same way.
- Maven being more common than SBT, dependencies issues are more likely to be detected and fixed / documented for Maven than SBT.
- Maven has [Maven Enforcer](https://maven.apache.org/enforcer/maven-enforcer-plugin/) which can be used to detect dependency conflicts and enforce a resolution strategy

### A work on `ConflictManager.strict`

`ConflictManager.strict` does currently work (with Ivy at least), but it also has pretty serious drawbacks:

1. When a conflict on library `A` is detected, the user has to force the version of `A` using `dependencyOverrides`. If later in time a newer version of `A` is required (for example bc. a new dependency `B` that depends on `A` is introduced), the "old" version of `A` will still have priority, which may introduce a runtime issue again. Over time, it becomes hard to maintains a clean dependency tree.
1. When a specific version of `A` is forced, it changes the dependency tree (because `A` is also pulling dependencies transitively) which in turn creates new conflicts. As a consequence you have to override the version of library `C`, which creates new conflicts... Rinse and repeat. Even if you go through the tedious process of fixing all conflicts manually, upgrading any dependency or introducing a new dependency will force you to do that work all over again because it also changes the dependency tree (see point 1.).

### related work

- [Add Coursier dependency resolution in SBT](https://github.com/sbt/sbt/pull/4430)
- [Support `ConflictManager.strict` similar to Ivy in Coursier](https://github.com/coursier/coursier/issues/349)

## Proposal

Here are a few potential solutions to the issues, in ascending order of complexity:

1. Publish documentation about how SBT solves dependencies, and how to detect and fix conflicts.
1. Fix `ConflictManager` so that it works consistently for every dependency independently of the repository hosting them. If it is impossible, `ConflictManager` should be deprecated and replaced with a consistent solution.
1. Ideally, there should be a way to statically analyse the set of dependencies selected by SBT and detect incompatibilities without executing the code.

## Cost & Timescales

The cost and timescales largely depends on the chosen scope. Fixing the documentation is a an easy process and should not take more than a few hours while creating static analysis tooling may take a few months
