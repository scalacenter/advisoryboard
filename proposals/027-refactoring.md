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
2. Refactoring often involves build changes, but Scalafix is unaware about that.
3. Driving the Scalafix is limited to CLI or sbt.

## Proposal

- Define Build Server Protocol for common refactoring operations.
- Implement a reference implementation using Scalafix.
- Implement a reference UI

## Discussion

Generally speaking, IDEs such as IntelliJ and Metals have intuitive user experience of refactoring operations such as renaming and moving the packages around. This makes us think that Build Server Protocol might be a good fit; however, we probably do not want to require full language indexing either so they can be used in CI and/or at the corporate monorepo scale.

In Language Server Protocol, there are [Rename Request][lsp-rename] and [Code Action Request][lsp-car], which might be considered for adopting as-is. Similarly, we might take advantage of the work that's been done in [The Language Server Index Format (LSIF)][lsif] as an optional step to narrow down the refactoring targets without invoking the language server or build tools.

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

`patch.json` should include [Language Server Index Format (LSIF)][lsif-java] compatible symbol so we can narrow down the targets from app1 ~ app100, down to a subset that contains the reference to `core.A#foo(Int)`.

Next, I can run some tool that would make a request to a running build server using `patch.json`. The expected result is that all reference to `core.A#foo(Int)` are renamed to `core.A#bar(Int)`.

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

This action will be captured as a JSON file - `patch.json`. Just as rename, `patch.json` should include LSIF-compatible symbol so we can narrow down the targets from app1 ~ app100, down to a subset that contains the reference to `core.A#foo(Int)`.

Next, I can run some tool that would make a request to a running build server using `patch.json`. The expected result is that all reference to `core.A#foo(Int)` are renamed to `core2.A#foo(Int)`, and dependency to `core2` target is added in the build file.

## Cost and timescales

We expect overall effort to require one ~ three months effort, depending on the coordination required with interested parties.

We estimate that given six months, a single engineer should be able to make substantial progress.

  [lsp-rename]: https://microsoft.github.io/language-server-protocol/specifications/specification-current/#textDocument_rename 
  [lsp-car]: https://microsoft.github.io/language-server-protocol/specifications/specification-current/#textDocument_codeAction
  [lsif]: https://code.visualstudio.com/blogs/2019/02/19/lsif 
  [lsif-java]: https://sourcegraph.github.io/lsif-java/
