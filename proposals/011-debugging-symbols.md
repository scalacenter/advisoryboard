# Debugging Position Information

## Proposer

Proposed by Lars Hupel, written by Sam Halliday, April 2017

## Abstract

A major criticism of Scala is that the debugging experience is poor
compared to Java. The main reason for this is because Scala's
representation as JVM bytecode is not always intuitive. Although
visual debuggers (Scala IDE, IntelliJ and ENSIME) are able to hide
much of the *demangling* detail from the developer, there remains a
great deal of ambiguity regarding the block of code that is executing.

## Proposal

We need a way to refer to *range positions* when a block of code is
executing, not just a line in a source file. To solve this, we propose
using virtual line numbers in the debugging section of the JVM
bytecode. These virtual line numbers could index a lookup to
actual range positions.

For example, the following code

```scala
foo.map { f => f.toString }.filter(_.length > 10)
```

could produce a lookup (embedded in the `.class` file) similar to

```
1 0-3
2 8-26
3 34-48
```

and the closures could refer to a line number of this file, which is
then parsed to recover the range position that is currently executing.
It is worth noting that `scala.js` source maps were defined with
column information in mind and produce good stack traces and stepping.

A potential encoding for this lookup is the
[Strata](https://docs.oracle.com/javase/7/docs/jdk/api/jpda/jdi/com/sun/jdi/Location.html#strata).
However, the related problem of *setting* a breakpoint may not be
addressed, making it preferable to use a custom lookup table despite
the pollution to stacktraces.

The current workaround is to write each block on a separate line,
which is far from ideal and does not address stepping problems in
pattern matches or `for` comprehensions:

```scala
foo
  .map { f => f.toString }
  .filter(_.length > 10)
```

In addition, we need a better way of resolving *which* source file is
being debugged. The Java Debugger Interface (JDI) provides the binary
package name and source filename, but not the relative path to the
source file. e.g. the JDI `com/foo/Foo.scala` refers to the package
and source that is being debugged, but the file may well be in
`bar/Foo.scala` and unrelated to the binary package.

The Scala team has proposed
[JSR-45 compliance](https://github.com/scala/scala-dev/issues/3), a
standard encoding for:

- line numbers of multiple source files within one `.class` file
- path of source file

which would complete the requirements to dramatically improve the
debugging experience in all visual debuggers.

## Cost

No additional costs beyond Scala Center resourcing and overheads.

## Timescales

A full treatment, including integration in IDEs, is estimated at 6
months for somebody familiar with the compiler code. However, should
that be too much for the appetite of the advisory board, a Proof of
Concept (inserting `RangePosition` in the strata / custom binary blob)
is achievable with 2 months.
