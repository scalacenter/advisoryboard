# sbt Community repository

## Proposer

Proposed by Eugene Yokota, community delegate

## Abstract

We propose a plan to manage sbt Community repository.

In 2021, JFrog sunsetted Bintray, and since then they've generously sponsored Scala ecosystem by providing us an Artifactory instance. When Eugene created the "Artsy" instance he licensed it under Scala Center, but generally both parties have been hands-free since it's been operating good enough as a Bintray replacement. This proposal formalizes the management of the artifacts hosted in the repo, and re-evaluate the technical implementations.

## Details

Some details of what we currently host on Artsy is documented in <https://eed3si9n.com/bintray-to-jfrog-artifactory-migration-status-and-sbt-1.5.1>:

- Read-only access to sbt plugins sbt-plugin-releases
- Read/write hosting of Linux installers for DEB and RPM

Artsy instance has gone down twice since 2021, and this is a reminder that we should be prepared to not have sponsored instance at some point.

## Proposal

- Take a backup of the read-only repo (likely using https://jfrog.com/help/r/jfrog-cli/jfrog-cli)
- Take periodic backups of the read/write repos
- Retain the ability to reproduce read-only sbt plugin hosting
- Retain the ability to reproduce Linux installer hosting
- Estimate annual cost of running either on our own
- Coordinate with the community to republish/transfer plugins to Maven Central
