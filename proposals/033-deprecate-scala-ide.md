---
date: August 2024
---

# Deprecate Eclipse Scala IDE

## Proposer

Proposed by Zainab Ali, Community Representative, 14th May 2024.

## Abstract

The Eclipse Scala IDE integration at https://scala-ide.org/ is no longer maintained. Its last release was in 2018.

However, it is the first search result for "Scala IDE" on prominent search engines, so is arguably the most discoverable IDE for newcomers. This can be seen on [duckduckgo](https://duckduckgo.com/?t=ffab&q=scala+IDE&ia=web).

This results a poor experience for new Scala users.

## Proposal

A deprecation notice sould be added to the Scala IDE website. This should recommend Eclipse users to use Metals. For example:
```
The Scala IDE project is no longer maintained. The recommended IDE integration for Eclipse is now Metals. Follow [this guide](link-to-guide) to set it up.
```

A guide for setting up Eclipse with Scala should be added to [Metals](https://scalameta.org/metals/docs/) or the [Getting started](https://docs.scala-lang.org/getting-started/index.html#with-an-ide) page.

A `noindex` mechanism should be added to `scala-ide.org` to prevent search engines from listing it.

## Cost

In level of difficulty I estimate the following order (from easiest to
hardest):

1. Adding a deprecation notice to the Scala IDE website.
2. Adding a noindex mechanism to `scala-ide.org` .
3. Adding guidance on Eclipse setup to Metals and the Getting Started page.

## Timescales

It is assumed that the Scala Center has ownership over the Scala IDE project, or contact with the project organizers.

If so, all tasks could be done quickly.
