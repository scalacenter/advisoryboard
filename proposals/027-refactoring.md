---
date: November 2021
status: dormant
updates:
  - revised March 2022
  - A working group will be created to further pull out actionable items from this proposal before any work will begin.
  - revised in June 2022 to show the outcome of the working group.
---

## Proposer

Proposed by Eugene Yokota, Twitter, November 2021

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
 
## Amendment (Chris Kipp and Julien Richard-Foy)

After the board meeting on April 8 it was agreed on that this proposal is
accepted, with the condition that a working group be created first to further
discuss the issues mentioned in here and also to pull out some more concrete
actionable items to better understand what exactly would constitute this
proposal being marked as *completed* in the future.

The working group met the 18th of May 2022. It was composed of Julien Richard-Foy (Scala Center), Sébastien Doeraene (Scala Center), Adrien Piquerez (Scala Center), Eugene Yokota (Twitter), Tomasz Godzik (VirtusLab), and Krzysztof Romanowski (VirtusLab).

The group agreed on a reasonable concrete solution to the problem of applying refactorings across multiple Scala projects from an IDE that only has a partial knowledge of the whole system (ie, it knows only one of the multiple projects).

Scalafix can be used to implement the refactorings (it already supports renaming symbols, for instance). So, we need a way to “drive” Scalafix to apply the desired refactorings in all the desired projects from an action performed in an IDE.

From the IDE side, we need to implement an action that produces a text-based description of the refactoring. E.g., for a renamed symbol:

~~~ json
{
  "rename": {
    "from": "foo.bar.Baz.quux",
    "to": "foo.bar.Baz.bah"
  }
}
~~~

Then, we need to implement a “command” in build tools (sbt, Bazel, etc.) to apply the text-based description of the refactoring to the whole project (that’s the part that drives Scalafix).

Ultimately, we could work on making the “Scalafix driver” more reusable to make it easier to support alternative build tools. For instance, by publishing as a library the logic that translates the text-based description of the refactorings into Scalafix rules. Or, by publishing a command-line tool that would take as parameters both the text-based description of the refactoring and the list of “Scala projects” (source directories, command to produce semanticdb, projects graph), and that would apply Scalafix everywhere (supporting new build-tools would be achieved by implementing a command that exports the projects of the build definition).

There is still an open question: should such a tool be available in public Metals releases? How would the “export refactoring” actions be integrated with the “usual” refactoring actions?

The estimated cost for implementing this project is one or two developer-months.

  [lsp-rename]: https://microsoft.github.io/language-server-protocol/specifications/specification-current/#textDocument_rename
  [lsp-car]: https://microsoft.github.io/language-server-protocol/specifications/specification-current/#textDocument_codeAction
  [lsif]: https://code.visualstudio.com/blogs/2019/02/19/lsif
  [lsif-java]: https://sourcegraph.github.io/lsif-java/
