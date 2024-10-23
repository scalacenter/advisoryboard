---
date: May 2024
accepted: true
status: completed
---

# Provide guidance on choosing between Scala LTS and Next

## Proposer

Proposed by Zainab Ali, Community Representative, 14th May 2024.

## Abstract

There is no clear guidance on how to choose between Scala LTS or Scala Next. This has lead to some open source projects adopting Next where LTS is recommended. Examples include [xebiafunctional/fetch](https://github.com/xebia-functional/fetch/commit/60b26afb242b1e248615c811f2c23a51d6cab6fe), [kovstas/fs2-throttler](https://github.com/kovstas/fs2-throttler/commit/e265f6d585aa55d101ddd5e631abb940fccce084) and [cquiroz/scala-java-locales](https://github.com/cquiroz/scala-java-locales/issues/490).

The most prominent guidance in [the 2022 blog post on compatibility plans](https://www.scala-lang.org/blog/2022/08/17/long-term-compatibility-plans.html#the-future-best-practices) is outdated. [The 3.4.0 release notes](https://www.scala-lang.org/blog/2024/02/29/scala-3.4.0-and-3.3.3-released.html#-so-which-version-should-i-update-to) have better guidance, but are hard to discover. 

## Proposal

The Scala website should include more guidance on choosing Scala versions. For example:
 - There could be instructions on the [download page](https://www.scala-lang.org/download/) on choosing between LTS and Next.
 - There could be an entry in the [migration guide](https://docs.scala-lang.org/scala3/guides/migration/compatibility-intro.html) on which version to choose.
 - The [2022 blog post on compatibility plans](https://www.scala-lang.org/blog/2022/08/17/long-term-compatibility-plans.html#the-future-best-practices) could include a link to recent best practices.
Ideally, there should be a link to guidance wherever Next or LTS are mentioned.

Developers actively upgrading their Scala versions are unlikely to check the website, but do check other sources.
 - There should be a link to guidance in the [release notes](https://github.com/scala/scala3/releases/tag/3.4.1).
 - [Scala Steward's](https://github.com/scala-steward-org/scala-steward) PRs for Scala version upgrades could include explicit guidance and instructions on [pinning LTS](https://github.com/scala-steward-org/scala-steward/blob/main/docs/faq.md#how-can-version-updates-be-controlled).
 - Build tools such as mill, sbt, and scala-cli could potentially inform the user if a jar is configured for publishing with a Next version. 

## Cost

In level of difficulty I estimate the following order (from easiest to
hardest):

1. Updating guidance on the Scala website.
2. Updating Scala Steward's PR summary.
3. Including guidance in build tool publishing steps.

## Timescales

Updating guidance on the Scala website and release notes could be done quickly. As developers are currently in the process of adopting `3.4`, the earlier this guidance is available, the better.
