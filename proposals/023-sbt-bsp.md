# Build Server Protocol Support for sbt

## Proposer

Written by Justin Kaeser, JetBrains, January 2020

## Abstract

The [Build Server Protocol](https://github.com/scalacenter/bsp/blob/master/docs/bsp.md) was developed by the Scala Center with cooperation from JetBrains to ease integration of build tools with IDEs and language servers. Currently the IntelliJ Scala plugin
and Metals rely on injecting sbt plugins into user projects to export the build structure. This approach causes friction that can be addressed by supporting the Build Server Protocol directly within sbt.

## Proposal

Implement Build Server Protocol support for sbt, either as an sbt plugin initially, and migrate to sbt core once stable, or directly in sbt core.

### Rationale

Integration of IntelliJ and Metals with sbt is burdened by some overhead and friction. The IntelliJ Scala plugin injects several sbt plugins into a user's project to be able to export the project model and interact with the sbt shell. This is quite brittle and does not give a good user experience in terms of IDE integration.

Metals injects the sbt-bloop plugin to export Bloop config files, and then uses Bloop directly for compilation. Bloop already implements BSP as a server, so towards the frontend there is a good user experience. The drawback is that Bloop does not run the full sbt task graph, so anything more sophisticated than just compilation, such as source code generation or packaging, still needs to be done from sbt directly.

[BSP support is on the shortlist for sbt 1.4](https://discuss.lightbend.com/t/roadmap-for-sbt-1-4/5038/9). This proposal is to ensure the resources to make it happen.

### Inclusion in sbt core

Adding BSP support as a plugin has the drawback that either the user needs to configure their environment or projects to explicitly install the plugin, or BSP clients need to inject the plugin into the user's sbt environment, requiring tool-specific logic.

BSP is designed to enable integration between IDEs/language servers and build tools without either side needing to know specifics of each other. Native support in sbt makes this possible with the least friction for the user.

### Supported endpoints and functionality

An implementation should support at least:

* [server lifetime endpoints](https://github.com/scalacenter/bsp/blob/v2.0.0-M2/docs/bsp.md#server-lifetime)
* [workspace/buildTargets](https://github.com/scalacenter/bsp/blob/v2.0.0-M2/docs/bsp.md#workspace-build-targets-request)
* [buildTarget/sources](https://github.com/scalacenter/bsp/blob/v2.0.0-M2/docs/bsp.md#build-target-sources-request)
* [buildTarget/dependencySources](https://github.com/scalacenter/bsp/blob/v2.0.0-M2/docs/bsp.md#dependency-sources-request)
* [buildTarget/resources](https://github.com/scalacenter/bsp/blob/v2.0.0-M2/docs/bsp.md#resources-request)
* [buildTarget/compile](https://github.com/scalacenter/bsp/blob/v2.0.0-M2/docs/bsp.md#compile-request)
* [Scala extensions](https://github.com/scalacenter/bsp/blob/v2.0.0-M2/docs/bsp.md#scala)

An implementation with good feature coverage will also include:

* [sbt extension](https://github.com/scalacenter/bsp/blob/v2.0.0-M2/docs/bsp.md#sbt)
* [task notifications](https://github.com/scalacenter/bsp/blob/v2.0.0-M2/docs/bsp.md#task-notifications)
    * for compilation of individual subprojects
    * for individual sbt tasks
* [buildTarget/test](https://github.com/scalacenter/bsp/blob/v2.0.0-M2/docs/bsp.md#test-request)
    * [test notifications](https://github.com/scalacenter/bsp/blob/master/docs/bsp.md#test-notifications-1) per test suite / test

### BSP connection protocol

BSP defines a [standard way](https://github.com/scalacenter/bsp/blob/v2.0.0-M2/docs/bsp.md#bsp-connection-protocol) for clients to discover and connect to BSP servers. One way to support this from sbt is create a BSP wrapper around an sbt shell process.

### Interaction with sbt server

The [sbt server protocol](https://www.scala-sbt.org/1.0/docs/sbt-server.html), like BSP, can be viewed as an extension of LSP. It could thus be a reasonable approach to extend sbt server with BSP endpoints.

### Related work

* [BSP support for Mill](https://github.com/lihaoyi/mill/pull/664)
* [BSP support in Fury](https://github.com/propensive/fury/pull/297)
* [sbt-bloop plugin](https://github.com/scalacenter/bloop/tree/master/integrations/sbt-bloop)
* [sbt BSP plugin for Dotty proof of concept](https://github.com/dotty-staging/dotty/blob/ide-compile/sbt-dotty/src/dotty/tools/sbtplugin/DottyBSPPlugin.scala)


## Cost & timescales

* Alexandra Dima's BSP support for Mill, with almost no prior exposure to Scala or Mill, took about 2 months for a largely complete implementation
* Justin Kaeser's BSP support in Fury implements only project structure resolution and took 1-2 weeks.

I expect sbt BSP support to be more complex, but overall similar in scope.

Ideally BSP support in sbt would be ready for sbt 1.4 support, and be ready in time for IntelliJ release to support it.