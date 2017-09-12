# SCP-015: Improving performance and profiling of Zinc

## Proposer

Proposed by James Belsey - Morgan Stanley - September 2017

### Abstract

[SCP-010] has added flags and tooling to better understand the compiler
behaviour in user projects.

Understanding what the compiler is doing and where time is being spent allows
users to structure their projects for optimal speed, and communicate this data
to compiler engineers so that they can optimize neglected or unknown scenarios.

Bulk compiler data is important to understand slow compile times.

However, it does not give us any insight into [incremental compilation](zinc),
another key component of developers' workflow. Incremental compilations is used
to support rapid recompilation of changes during a typical development
write-compile-test cycle.

Supporting correctness in the compilation is also key, we can see two key
failure modes which significantly affect the user experience, over and under
compilation.  In over compilation, the incremental compiler does unnecessary
work, compiling units which were not affected by the changes, leading to slower
performance. Under compilation is worse, leading to situations where errors are
reported that disappear under full compilation due to an incomplete set.  Both
of these situation lead to poor user experience and we need to build tooling to
help identify and improve these situations.

### Proposal

We propose to enhance incremental compilation in a similar fashion that

SCP-010 has done for

standard compilation. The idea is to improve profiling and performance of
[Zinc](zinc) —Scala's incremental compiler— aiming at:

1. Understanding the incremental compilers performance profile.
2. Making incremental compilation faster; and,
3. Allowing Scala users to optimize for faster development write-compile-test cycles.

We can break this into several related work items:

#### Deriving profile metadata after every incremental compile

After the realization of this proposal, Zinc should provide profiling metadata
that allows users to analyze the behaviour of incremental compiles throughout a
developer session (e.g. one day).

Similar to SCP-010 statistics, this profiling metadata should be accessible in
both human and machine readable forms so that larger scale analysis can be
performed.

This profile metadata captures information such as:

 - Number of compilation units updated
 - Number of compilation units recompiled
 - Number of classes affected/methods affected
 - An explanation for why a source file was recompiled for every change
 - Total time of recompilation (including different cycles)
 - Portion of time spent in the build tool (e.g. sbt/zinc) vs the compiler itself
 - The impact of the full compilation threshold (i.e. when the incremental
   compiler gives up and falls back to a full compilation).

#### Validation

We believe that this work should include validation tooling to help ensure the
stability and effectiveness of the incremental compiler. This tooling would
provide useful both for finding bugs (e.g. when cases where Zinc undercompiles)
and also in investigating and assessing performance improvements.

It would be an incredibly powerful tool to have the ability to replay a git
repository through the incremental compiler, i.e. replaying each commit of a
repository through the incremental compiler and capturing the statistics. Such
a feature would be useful for:

 - Evaluating the effectiveness of the incremental compiler.
 - Finding places where the project structure can be improved.
 - Allowing compiler maintainers to profile and optimise the behaviour of the
   incremental compiler.

Following recompilation you should capture the

 - false negative - those classes that should have been compiled
 - false positive - those classes that were recompiled, but were unchanged

This will require a way to analyse the builds at higher level than pure
bytecode.

Another idea would be to use this tooling to ensure that the results of
incremental compilation and straight compilation produce the same results.  It
is not uncommon to see instances where incremental compilation produces an
error, but a full recompile clears the error. 

Having a way to replicate and fix this kind of issues would do much to improve
perceived performance of compilation.

#### Performance improvements to improve the incremental compiler

We see several ways of making Zinc faster, overall:

1. Making structural changes to the incremental compiler to **reduce its
   compilation overhead**.

   After Scala 2.12.3 has been released with important performance
   improvements, optimizing Zinc's internals becomes more important. We believe
   that optimizing Zinc's internals could help reduce the overhead of
   incremental compilation, assessed to be around 5% to 15% of total compile
   times.  Such optimizations are [Merge API and
   dependency](https://github.com/sbt/zinc/issues/248) and [compile
   incrementally within jars](https://github.com/sbt/zinc/issues/305). Another
   idea is to delay the derivation of incremental compilation's metadata after
   code generation. This last idea, while feasible and impactful, has yet to be
   confirmed practical by Zinc maintainers.

2. **Researching which aspects of the Scala programming language impact**
   incremental compilation the most.

   For example, it is known that incremental compilation is heavily affected by
   project structure.  Leaving the compiler to infer public signatures or
   having too many public methods and fields can slow down incremental
   compilation.  A small change can have large knock on effects forcing
   unnecessary recompilations of many source files. Understanding the behaviour
   and impact of this and other properties like cyclic dependencies or name
   distribution can improve user understanding, code structure and effective
   performance, helping to reduce incremental compilation.

## Cost

Unknown at this stage.

## Timescales

Unknown at this stage.

[SCP-010]: https://github.com/scalacenter/advisoryboard/blob/master/proposals/010-compiler-profiling.md
[zinc]: https://github.com/sbt/zinc
