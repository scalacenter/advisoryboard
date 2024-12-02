# Artifact publishing

## Proposer
Proposed by Eugene Yokota, community delegate, December 2024

## Abstract

We propose enhancements to the tooling ecosystem around artifact publishing..

There have been a few changes to the landscape of Scala artifact publishing in 2024. First was Sonatype's [change to account management](https://central.sonatype.org/news/20241007_ossrh_accounts_visible_in_portal/) and general release of [Portal Publisher API](https://central.sonatype.org/publish/publish-portal-api/). Second is Maven BOM (bill-of-material) enhancements in [Coursier 2.1.17](https://github.com/coursier/coursier/releases/tag/v2.1.17).

Unlike the library dependency resolution, which has been unified mostly to Coursier, these emerging tasks related to the publishing side are worked on in various build tools independently, including sbt 1.x, sbt 2.x, Scala CLI, Mill, Gradle, and Bazel.

Some of the outlined tasks may be implemented by contributors who are already working on the solutions, but in collaboration with Scala Center engineers, and using the Center as a coordination point for community and stakeholder interests.

## Proposal

Define or identify a common library for artifact publishing.

Here are some of the use cases.

### Use case 1: publishLocal

Support equivalent to sbt's publishLocal task, which is implemented as publishing to an Ivy layout repository located on disk. Supporting publication of Ivy layout is useful not only for backward compatibility, but also for companies using internal Artifactory.

### Use case 2: publishM2

Support equivalent to sbt's publishM2 task, which is implemented as publishing to a Maven layout repository located on disk.

### Use case 3: publishSigned

Support equivalent to sbt's publishSigned task, which is implemented as publishing to a Maven layout repository that is remotely located, after GPG signing the artifacts.

### Use case 4: Publish to Sonatype

There are multiple APIs to publish to Sonatype. Plain HTTP, Rest API, and Portal Publisher API.
Credits to Taro Saito for his work on sbt-sonatype, and David Doyle for https://github.com/lumidion/sonatype-central-client. Publishing support should include both snapshots and release versions.

### Use case 5: POM properties

Support custom properties into the POM files, which helps the library consumers. For example, sbt adds the URL of the ScalaDoc API location, version scheme information, etc.

### Use case 6: Creation of BOM

Credits to Alexandre Archambault and contributors for their work on Coursier and on-going BOM-related enhancements.

Common publishing library should support the creation of Maven BOM (bill-of-material). For example, <https://repo1.maven.org/maven2/org/apache/spark/spark-parent_2.13/3.5.3/spark-parent_2.13-3.5.3.pom>. The POM contains dependencyManagement section.

```xml
<dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>org.apache.spark</groupId>
      <artifactId>spark-tags_${scala.binary.version}</artifactId>
      <version>${project.version}</version>
    </dependency>
    ....
```

This transitively dictates the spark-tags version, when they appear in the dependency graph.

### Use case 7: Version-less dependency
Once a project can produce BOM, then it no longer needs to specify the version number for the dependencies tracked in BOM.

```xml
<dependency>
  <groupId>org.apache.spark</groupId>
  <artifactId>spark-tags_2.13</artifactId>
  <scope>compile</scope>
</dependency>
```

## Cost and timescale

We expect overall effort to require three ~ six months effort, depending on the coordination required with interested parties.

We estimate that given six months, a single engineer should be able to make substantial progress.
