# Aid the ecosystem in upgrading to sbt 1.0.x

## Proposer

Proposed by Lars Hupel.

## Abstract

Scala 2.10 used to (and still has, to some extent), huge gravitas in the Scala ecosystem. To large parts, this was due to Spark and sbt supporting only 2.10. Spark has migrated to 2.11, and sbt 1.0.x is already there (the RCs are binary compatible).

Most Scala libraries (as far as I can tell), cross-build against at least 2.10, 2.11, and 2.12. Some even added support for 2.13, which means _four_ major Scala versions.

One one hand, this is a significant improvement over the state of the art before 2.10: It was unthinkable to support so many versions simultaneously with as little changed code.

On the other hand, it is also a maintenance burden. Good cross-compatibility also means that progress is hindered. Libraries are unable to exploit newer features from, say, 2.11.x (_partial unification_ aka SI-2712 comes to mind).

Dropping Scala 2.10 support and moving the sbt ecosystem forward to sbt 1.0.x are intertwined issues with combined benefits. It means version switches in build files (e.g. for Scala modules like _scala-xml_) or compatibility libraries (e.g. _macro-compat_) can go away. In almost all cases I’ve seen, this was used to distinguish between 2.10 and 2.11+. It also ensures we're not eternally stuck on a Scala version released many years ago.

## Proposal

The Scala Center should devote time to

* analyse the usage of sbt plugins "in the wild",
* prioritize plugins according to usage,
* send pull requests updating to sbt 1.0.x (e.g. using the ongoing work on [sbtfix](https://github.com/scalacenter/sbtfix)), and
* if necessary, push releases, in case the maintainer(s) are unresponsive

## Cost & Timescales

This is an "open-ended" proposal.
I recommend 0.25 FTE working on this for 3 months, which should already make significant progress. 
