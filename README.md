# The Scala Center Advisory Board

## Purpose

This repository documents the process and activities of the [Scala
Center](http://scala.epfl.ch) Advisory Board, the body which makes
recommendations on the activities of the Scala Center, under the provisions of
the [Scala Industry Affiliates
Program](https://scala.epfl.ch/docs/ScalaCenterMembershipRegulations.pdf)

## Proposals for Recommendation

Proposals for recommendation may be submitted to the Advisory Board for
consideration only by

 - The Scala Center,
 - advisory board members,
 - affiliate members, or
 - community members through the community representatives,

and should be made via a pull-request adding a new Markdown file to the
[proposals](https://github.com/scalacenter/advisoryboard/tree/main/proposals)
directory.  The proposal should be assigned the next available Scala Center
Proposal (SCP) number in sequence, and the file should be named
`nnn-<description>.md`, where `nnn` is the SCP number, padded with zeroes, and
`<description>` is a concise, lower-case, dash-separated description of the
proposal.

Proposals should follow the format and sections laid out in the [template
proposal](https://github.com/scalacenter/advisoryboard/tree/main/templates/proposal.md),
and should be concise enough to fit on a single side of paper if printed out.

### Proposals status

To see the up-to-date status of a proposal see the
[proposals/README](./proposals/README.md) which holds whether or not the
proposal has been accepted, a summary of updates and the current status of all
proposals. Once a proposal has been accepted by the board it becomes a
"recommendation".

**NOTE**: This proposals/README.md file is auto-generated, so if you need to add
an update or change the status of a proposal make sure to do so in the heading
of the proposal file. The possible headings are:

```yaml
date: date proposed
accepted: true, yes, false, no
updates:
  - postponed until next meeting
  - accepted after discussion
  - Updates found at https://www.scala-lang.org/blog/
status: completed, postponed, active, etc
```

The proposals/README.md will be updated in CI when merged into `main`, but you
can also manually regenerate it with `scala-cli run bin/` from the root of this
project.

## Invitations

For reference, invitations sent to each Advisory Board representative are
included in the 
[invitations](https://github.com/scalacenter/advisoryboard/tree/main/invitations)
directory.

## Agendas

Agendas for each Advisory Board meeting shall be made available online at least
five days before the meeting takes place, and shall reside in the
[agendas](https://github.com/scalacenter/advisoryboard/tree/main/agendas)
directory, under the filename `xxx-yyyy-qz.md`, where `xxx` represents the
meeting number, `yyyy` represents the year and `z` represents the quarter (1,
2, 3 or 4).

## Minutes

The minutes of the Scala Center shall be written up by the secretary
after each meeting, and made available shortly afterwards
[on the Scala Center website](https://scala.epfl.ch/records.html).

## Contributions

Contributions *may* be made to this repository, however the documents here
represent a record of the proceedings of Advisory Board meetings, so only
immaterial changes such as spelling corrections and reformatting will be
accepted.
