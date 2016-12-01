# SCP-009: Improve user experience for builds that use only direct dependencies

## Proposer

Proposed by Stu Hood, Eugene Burmako - Twitter - November 2016

## Abstract

The most commonly discussed angle of attack for improving the Scala build experience is improving the speed of the compiler. Instead, this document addresses a different angle: improving the experience of using only "direct" dependencies in builds, in order to improve cache hit rates and unblock scaling multiple remote invocations of the compiler.

A "direct" (also known as "strict") dependency is a dependency that is specifically mentioned in the build definition for some compilation unit. "direct" dependencies are thus differentiated from "transitive" dependencies (which are collected by walking the transitive closure of direct dependencies).

## Background

### Minimization of compilation classpaths

In comparison with a more common project-centric workflow that involves a number of coarse-grained projects and intra-project incremental compilation via sbt, Twitter's build workflow relies on many more (tens of thousands) fine-grained modules and inter-project incrementality. This workflow is enabled by Pants, our open-source build tool that understands a multitude of languages including Java, Scala, Python and others. A similar fine-grained approach is used in other popular build tools (Blaze/Bazel, Buck) and companies (Google, Facebook, Foursquare, Square, Medium and others).

One benefit of having a large number of modules is the ability to perform parallel builds, distributing independent build tasks between  different workers in a build cluster. This has a potential to significantly improve the build experience for large projects.

Naturally, the viability of such a distributed build hinges on the amount of data sent over the network. This becomes a real problem for large projects, because in such setting it is not uncommon for a module to have hundreds or thousands of transitive dependencies. Therefore, it is critical for us to minimize compilation classpaths.

### Dropping transitive dependencies from compilation classpaths

On our quest to minimize compilation classpaths, we observe that it is often unnecessary to have the entire runtime classpath during compilation.

Let’s consider an application `C` that depends on a library `B` that, in turn, depends on a library `A`. Let’s suppose that `C` uses definitions provided by `B`, but it doesn’t explicitly reference definitions from `A`. In this document, we will then say that `B` is a direct dependency of `C`, and `A` is a transitive dependency of `C`. For example:

```scala
// A.scala
class A

// B.scala
class B {
  println(new A)
}

// C.scala
object C extends App {
  println(new B)
}
```

In this example, even though C requires A.class at runtime, it doesn’t need A.class to compile. We can verify that by running the following compiler invocations.

```
$ scalac -d a A.scala
$ scalac -d b -cp a B.scala
$ scalac -d c -cp b C.scala
$ echo $?
0
```

Unfortunately, even though scalac generally does not require the entire transitive classpath of a library to compile it, these requirements are not formally defined, and in many cases are non-intuitive. This uncertainty makes it difficult to accurately declare the direct dependencies of a library.

```scala
// A.scala
class A

// B.scala
class B extends A

// C.scala
object C extends App {
  println(new B)
}
```

In this updated example, `B` no longer uses `A` in its constructor, but instead subclasses it. Even though, `C` still doesn't use `A` directly, we now have to provide `A` on the compilation classpath, otherwise an error occurs.

```
$ scalac -d a A.scala

$ scalac -d b -cp a B.scala

$ scalac -d c -cp b C.scala
error: missing or invalid dependency detected while loading class file 'B.class'.
Could not access type A in package <empty>,
because it (or its dependencies) are missing. Check your build definition for
missing or conflicting dependencies. (Re-run with `-Ylog-classpath` to see the problematic classpath.)
A full rebuild may help if 'B.class' was compiled against an incompatible version of <empty>.
one error found
```

We can observe that the Scala compiler: 1) exhibits unintuitive behavior when faced with seemingly minor code changes,
2) produces confusing error messages that don't provide instructions how to fix underlying errors. This proposal aims at improving user experience in these areas.

## Statement

In order to enable builds that operate only on direct dependencies, absence of a transitive dependency from the classpath should be treated as a common and expected case in scalac/dotc, and should have a well polished user experience.

## Proposal

A few potential concrete outcomes of this effort, in descending order based on how useful they would be to users, and to developers of Scala build-tooling:

1. Improvements to the user experience of stub errors, centered around the `Statement`: that they are an expected common case, rather than a rare, unexpected, or fatal condition.
2. Reduction of the number of cases that result in stub errors... ie, allowing more usecases that currently result in a stub error to successfully compile, and thus allowing for fewer direct dependencies.
3. A compiler flag to require `import` statements for all symbols used during compilation (including those not otherwise mentioned in the source). For symmetry with `-Ywarn-unused-imports`, this option might potentially be called `-Ywarn-undeclared-imports`. It would primarily assist with making the transition from transitive to direct dependencies, rather than helping to maintain a build that is already using direct dependencies. (as suggested by [@posco](twitter.com/posco) and [@adriaanm](https://twitter.com/adriaanm))
4. An expansion of the Scala Language Specification to list all cases in which a symbol from another compilation unit must be present on the classpath, including: 1) subclassing, 2) return types of superclasses' public methods, 3) direct reference, 4) etc.

## Cost

Although this proposal is bounded in scope (it is possible to call it "done" when one or more of the above proposals are implemented), it is purposely open-ended with regard to strategy in order to give the implementers flexibility to work with the core team, and to use their best judgement.

Depending on the resulting strategy, this proposal might cost:
- 3 to 6 person-months invested in items `1` and `2`
- 1 to 3 person-months to implement item `3`
- 3 to 9 person-months to improve the spec for item `4`

## Timescales

Ideally, action on this proposal would occur sometime within the next 6 to 9 months (from November 2016), in order to align with Twitter internal efforts toward build graph improvements (which will hopefully include enabling usage of only direct dependencies).
