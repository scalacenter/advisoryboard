---
date: November 2021
status: awaiting revision
---

## Proposer

Proposed by Eugene Yokota, Twitter, November 1st, 2021

## Abstract

We propose enhancements to the tooling ecosystem around refactorings.

While Scalafix has been successful in automatic rewrites, there a number of challenges:

1. The rule writing is not accessible outside of the core library author persona.
2. Driving the Scalafix is limited to CLI or sbt.
3. Refactoring often involves build changes, but Scalafix is unaware about that.

## Proposal

We generally propose enhancement to refactoring tooling that improves 2 axes:
1. Usability
2. Scalability (scale to multiple GitHub repos or corporate monorepo)

We want to communicate the ideas around the proposal, but not over-specify the solution space,
so the following discussion and use case sections should be interpreted as non-normative illustration.

## Discussion

Generally speaking, IDEs such as IntelliJ and Metals have intuitive user experience
of refactoring operations such as renaming and moving the packages around.
We would like to keep that user experience, but make it scale to multiple GitHub repos, or large monorepo,
which the IDEs will not be able to open easily.

For the front-end, we can envision:
- creating a web UI
- coordiate with IDEs to use them directly as UI

For the back-end, we can envision:
- use Scalafix
- coordinate with build tools to automate editing of build files, esp to add/remove dependencies

### Use Case: Renaming

Let's say we use VS Code + Metals as UI, and consider a monorepo as follows:

```
- core/src/main/scala
- app1/src/main/scala
- app2/src/main/scala
...
- app100/src/main/scala
```

In this scenario, we will only open core/ subdirectory using VS Code. There might be a "Batch Rename…" option where I can rename a method `core.A#foo(Int)` to `core.A#bar(Int)`. This action will be captured as a JSON file - `patch.json`.

Next, I can call Scalafix or run some tool that would make a request to a running build server using `patch.json`. The expected result is that all reference to `core.A#foo(Int)` are renamed to `core.A#bar(Int)`.

### Use Case: Moving

In another example, let's say we would like to factor out `core.A` class to another target altogether and create `core2` package:

```
- core/src/main/scala
- core2/src/main/scala
- app1/src/main/scala
- app2/src/main/scala
...
- app100/src/main/scala
```

This action will be captured as a JSON file - `patch.json`.

Next, I can run some tool that would make a request to a running build server using `patch.json`. The expected result is that all reference to `core.A#foo(Int)` are renamed to `core2.A#foo(Int)`, and dependency to `core2` target is added in the build file.

### Notes

In Language Server Protocol, there are [Rename Request][lsp-rename] and [Code Action Request][lsp-car], which might be considered for adopting as-is.

Similarly, we might take advantage of the work that's been done in [The Language Server Index Format (LSIF)][lsif] as an optional step to narrow down the refactoring targets without invoking the language server or build tools.

## Cost and timescales

We expect overall effort to require one ~ three months effort, depending on the coordination required with interested parties.

We estimate that given six months, a single engineer should be able to make substantial progress.

  [lsp-rename]: https://microsoft.github.io/language-server-protocol/specifications/specification-current/#textDocument_rename 
  [lsp-car]: https://microsoft.github.io/language-server-protocol/specifications/specification-current/#textDocument_codeAction
  [lsif]: https://code.visualstudio.com/blogs/2019/02/19/lsif 
  [lsif-java]: https://sourcegraph.github.io/lsif-java/
