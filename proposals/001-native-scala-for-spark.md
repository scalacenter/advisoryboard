---
date: May 2016
accepted: true
updates:
  - deferred for later consideration
  - considered again and accepted August 2016
  - dormant no recent activity as of August 2019
status: dormant
---

# SCP-001: Native Execution of Scala/Spark via LLVM

## Proposer

Proposed by Spark Technology Center, IBM, May 9, 2016

## Abstract

We are interested in an exploration of native compilation of Scala in
the context of Big Data analytics workloads (ie, Spark) executing on
server-class systems. The repetitive nature of the workload (many
executions of the same Spark jobs) could enable effective static
compilation (including off-line profile-directed feedback) and whole
program optimization.  The primary goals of a native execution
alternative would be (1) to reduce memory footprint of the language
runtime (ie, the JVM) (2) eliminate warm up effects of JIT compilation
and (3) provide a platform to explore non-GC based approaches to
memory management (for example, region-based allocation may be a good
fit to the structure of Spark jobs).

## Proposal

The Scala Center should explore a non-JVM implementation of the Scala
language based on static compilation to native code through an
existing production compiler (for example LLVM).  In addition to
considering a non-JVM implementation of the core Scala language, the
Scala Center should also explore what subset of the Scala core class
libraries and features such as reflection and serialization would be
supportable in a non-JVM environment. The ultimate objective of the
proposal is to be able to execute substantial pieces of Scala-based
distributed middleware (ie Spark) on realistic workloads on
server-class machines. The natively compiled Spark jobs would need to
fit into a fully distributed workflow that may include other
Spark/Scala programs running on JVMs with which the natively compiled
Spark/Scala program would need to exchange data.

The work in progress by Denys Shabalin "Scala on LLVM" could provide a
starting point for this exploration.  We expect more work would be
needed to achieve the language and library coverage needed to execute
portions of realistic Spark workflows on natively compiled Scala.  We
also speculate that off-line profile-directed optimizations (eg
profile-directed devirtualization and inlining) may be needed to
achieve the desired levels of performance. To support Big Data Spark
workloads on server-class systems, the memory management subsystem of
native Scala will likely need to be capable of supporting multi-GB heaps.

## Cost

Uncertain / unknown (dependent on current maturity of "Scala on LLVM" work)

## Timescales

Uncertain, but likely a multi-year effort.
