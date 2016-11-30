# SCP-[000]: Minimization of compilation classpaths

## Proposer

Proposed by Stu Hood, Eugene Burmako - Twitter - November 2016

## Abstract

The most commonly discussed angle of attack for improving the Scala build experience is speeding up the compiler. Instead, this proposal addresses a different angle: minimizing compilation classpaths to enable distributed builds.

## Minimization of compilation classpaths

Our build workflow at Twitter is centered around a monorepo. Instead of keeping internal projects in separate repositories, we merge them into one repository (hence the name “monorepo”), significantly reducing the overhead associated with evolving independent repositories.

In comparison with a more common workflow that involves a number of coarse-grained projects and intra-project incremental compilation via sbt, our build workflow relies on many more (tens of thousands) fine-grained modules and inter-project incrementality. This workflow is enabled by Pants, our open-source build tool that understands a multitude of languages including Java, Scala, Python and others.

One benefit of having a large number of modules is the ability to perform parallel builds, distributing independent build tasks between  different workers in a build cluster. This has a potential to significantly improve the build experience for large projects.

Naturally, the viability of such a distributed build hinges on the amount of data sent over the network. This becomes a real problem in our setting, because it is not uncommon for a module to have hundreds of transitive dependencies. Therefore, it is critical for us to minimize compilation classpaths.

## Dropping transitive dependencies from compilation classpaths

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

TBD

## Cost

TBD

## Timescales

Ideally, action on this proposal would occur sometime within the next 6 to 9 months (from November 2016), in order to align with Twitter internal efforts toward build graph improvements (which will hopefully include enabling usage of only direct dependencies).
