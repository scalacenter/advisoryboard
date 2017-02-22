# Prioritize and collate sbt/scala/dbuild/community-build issues for community projects

## Proposer

Proposed by @BennyHill (and @larsrh), Typelevel, 2017-02-22

## Abstract

TBD

## Proposal

We would like to propose that the Scala Center maintain a community driven list of the open sbt/scala/dbuild/community-build issues that they would like priority given too. This could be as simple as a GitHub-driven list, but we would like to also suggest that the real value the SC could add is identifying issues that impact many projects and the general Scala user experience, even if the individual issues themselves are not heavily voted for.

Indeed, this could also progress to having "Meta Issues", groups of issues from many projects that on their own do not seem important for the individual projects, but are when viewed together, across projects.

Note that this does not stipulate that the SC actually do the work for the issues, but rather help focus issues that might otherwise get lost. By highlighting such, perhaps the community are motivated to do the work, perhaps some SC board members have Lightbend (LB) subscriptions and lobby other LB subscribers to vote for them.

Examples of issues that are are spread across projects include:

- Defining custom community builds (CB)
  - Sharing configurations from other CB's
  - Getting shared configs via scaladex
  - Running then locally (via catalysts-docker)
  - Promoting these tools - if a project does not know a solution exists or is fit for a certain task, they will not use it. 
- Pattern matching vals in sbt
  - An old issue, advocated for fixing by many - see  https://github.com/sbt/sbt/issues/2290
https://github.com/sbt/sbt/issues/1661 - but perhaps not a high priortiy for business aligned projects
  - relates to "-Y rangepos" option in scalac. So the SBT fix also requires a scalac fix. 
  - The general issue here has also been a recurring issue in scoverage, probably the main test coverage tool for scala.
- Cross building projects
  - This is not a standard SBT feature
  - Scala.js added cross project support for JVM/JS
  - scala-native has now taken this over, extending the Scala.js implementation
  - Typelevel Scala added its own platform type
  - One method of implementing this is using  "Pattern matching vals in sbt"
  - Adding support of multiple version of libraries makes the current API almost unusable - who should be responsible for this? (How code is maintained for different library versions is a separate concern)
- Rewriting scala code to support multiple library versions
  - scala-meta and scalafix can be leveraged, and are in [catz-cradle](https://github.com/BennyHill/catz-cradle)
  - custom community builds could test rewrites on existing projects, perhaps via scaladex
  - Difficult to implement and test as SBT does not have full support for cross building....


## Cost

We expect this work to consist mostly of triaging and keeping track of issues; also responding to community feedback. We estimate 0–0.25 FTE. 

## Timescales

Continuous.

## References

* [typelevel/general#62](https://github.com/typelevel/general/issues/62)
* [typelevel/general#45](https://github.com/typelevel/general/issues/45)
