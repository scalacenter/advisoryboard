---
date: September 2018
accepted: false
status: rejected
---

# SCP-019: Focusing on backwards compatibility for Scala 2.14 and Scala 3.0

## Proposer
Stu Hood, Twitter

## Abstract
[SCP-018](https://github.com/scalacenter/advisoryboard/blob/master/proposals/018-converging-214-30.md) proposed to add support for TASTY to Scala 2.14, but was somewhat open ended with regard to the concrete goals. This proposal intends to refine SCP-018 to ensure that the Scala Center’s efforts are focused specifically on a backwards compatibility story: ie, that Scala 3.0 can depend on Scala 2.14, using any feasible codegen strategy.

## Proposal
In order to ease the migration from Scala 2.14 to Scala 3.0, there are two concrete possibilities: “Scala 3.0 code may be compiled to depend on unmodified Scala 2.14 binaries” (aka `3 depends on 2`) and “Scala 2.14 code may be compiled to depend on unmodified Scala 3.0 binaries” (aka `2 depends on 3`). This proposal suggests refining SCP-018 such that the Scala Center expends any effort that it dedicates to this area on the `3 depends on 2` usecase until that integration is proven and production ready.

The motivation for this refinement is that backwards compatibility is a hallmark of the JVM ecosystem: JDK 11 code can depend on JDK 6 code, while the reverse has never been a goal (and is arguably too restrictive for language/runtime implementers). While supporting both forward and backwards compatibility would allow for the maximum velocity of migration, forwards compatibility seems like too lofty a goal.

Backwards compatibility still allows for a migration strategy where the “roots” of the dependency graph (end user applications, rather than libraries) are the first to see the UX benefits of the upgrade. This provides for a very large initial uptake, because consumers are more numerous than libraries. While it is true that libraries will not see benefit as easily, they always retain the option of publishing multiple binary copies of their library (an operation that they are already familiar with within the Scala ecosystem).

Additionally, it’s important to note that this refinement has no preference for the particular code-generation backend used to achieve the stated goal: if expanding and polishing the support in Scala 3 for consuming scala signatures / pickles is sufficient to achieve backwards compatibility, then that should be considered a viable path forward for the purposes of this refined proposal.

Finally, as an important aside: backwards compatibility is the feature what would prevent applications from being blocked on future library upgrades like the Spark upgrade: that in itself feels like enough justification to focus there.

## Acceptance Criteria
The `3 depends on 2` usecase can be considered satisfied when “Scala 3.0 code may be compiled to depend on unmodified Scala 2.14 binaries” without 1) significant performance regressions, 2) runtime errors that would usually be caught at compile time.

To validate that the acceptance criteria have been met, two concrete artifacts would be useful:

1. A large-scale compatibility study that measures a fraction of in-the-wild Scala 2 binaries that is consumable from Dotty (e.g. by expanding the Dotty community build to include additional projects with transitive dependencies on unmodified Scala 2 binaries).
2. A compatibility reference (where for relevant features of Scala 2 that can appear in pickles, the documentation says to what extent Dotty supports consuming it).

Due to the significant differences between the Scala 2 and Scala 3 compiler codebases, it is likely that some language features of Scala 2 can/should not be exposed to Scala 3. Additionally, since new Scala 3 consumers will be re-compiling their code during their upgrade, there is a slightly increased tolerance for unsupported patterns, particularly if there are consume-side workarounds. Nonetheless, the spirit of this proposal requires that a large fraction of unmodified Scala 2.14 binaries are naturally consumable from Scala 3.

## Cost
This proposal refines an existing proposal, but it also proposes concrete artifacts that might not have been in scope before.

We estimate that the concrete artifacts of this proposal represent an additional 3 person-months of effort atop what was estimated for the first proposal. But we suspect that the portions of the existing proposal that have been de-prioritized by this refinement represent at least that much effort, and that this proposal is thus neutral in terms of effort required.

## Timescales
2.14 development is slated to begin in Jan 2019, with a release by late 2020, early 2021.

