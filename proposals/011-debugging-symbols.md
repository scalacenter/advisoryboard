# Debugging Position Information

## Proposer

Proposed by Sam Halliday, on behalf of the Scala tooling community,
March 2017.

## Abstract

A major criticism of Scala is that the debugging experience is very
poor compared to Java. The main reason for this is because Scala's
representation as JVM bytecode is not always intuitive. Although
visual debuggers (Scala IDE, IntelliJ and ENSIME) are able to hide
much of the *demangling* detail from the developer, there remains a
great deal of ambiguity regarding the block of code that is being
executed.

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

and the various closures could refer to a line number of this file,
which is then parsed to recover the range position that is currently
executing.

Note that it may be enough to use positions within the `.class`
format, as a visual debugger can infer the closing Position using an
interactive compiler.

In addition, we need a better way of resolving *which* source file is
being debugged. The Java Debugger Interface (JDI) provides the binary
package name and source filename, but not the relative path to the
source file. e.g. the JDI `com/foo/Foo.scala` refers to the package
and source that is being debugged, but the file may well be located in
`bar/Foo.scala` and unrelated to the binary package.

The Scala team has proposed
[JSR-45 compliance](https://github.com/scala/scala-dev/issues/3), a
standard encoding for:

- line numbers of multiple source files within one `.class` file
- path of source file

which would complete the requirements to dramatically improve the
debugging experience in all visual debuggers.

## Cost

??? An estimate of expected costs (if any) associated with implementing this
proposal, including both one-off and maintenance costs, if applicable. If the
costs are not known, uncertain, variable or dependent on other factors, this
should be described in this section.

## Timescales

??? An estimate of expected time required to implement this proposal. If the time
is not known, uncertain, variable or dependent on other factors, this should be
described in this section.
