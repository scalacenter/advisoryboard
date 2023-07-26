---
date: June 15 2023
accepted: true
status: completed
---

# Ensure reachability of Scala websites

## Proposer

Proposed by Lukas Rytz and Seth Tisue, Lightbend, June 15 2023

## Abstract

As per [this GitHub ticket](https://github.com/scala/scala-lang/issues/1456) opened in January 2023, scala-lang.org and at least some of the other websites under this domain are unreachable from some VPNs (Virtual Private Networks).

We propose that the Scala Center take steps to ensure that all of the Scala websites are as broadly reachable and browseable from as much of the internet as reasonably possible, even for VPN users.

## Proposal

It's apparent from the number of people commenting on the ticket that many users are hitting this issue.

Seth has also seen many users complain about it on Discord and on the Scala Users forum.

As seen in the comments on the ticket, the root cause is that at least some of our websites are hosted inside EPFL and EPFL blocks some VPNs at the university level.

Fabien Salvi at the Scala Center has already done some experimental work with hosting the main website outside of EPFL. For example, an experimental mirror of the main website is already online at [https://www3.scala-lang.org](https://www3.scala-lang.org).

Can EPFL be convinced to exempt our websites from their VPN ban? If so, that would resolve the issue.

If EPFL cannot be convinced, then we suggest that all of our websites be hosted outside of EPFL.

## Cost

Unknown. We are asking the Center to provide an estimate.

## Timescales

The problem has already gone on for at least half a year, perhaps longer. We are hoping that the issue could be resolved before the fall quarter begins, so that the websites are usable by students studying Scala.
