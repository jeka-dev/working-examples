# A Set of JeKa Project Examples

This repository contains several independent projects built with [JeKa](https://jeka-dev.github.io/jeka/).
Each directory is a project: run its commands from that directory, with [JeKa installed](https://jeka-dev.github.io/jeka/installation/).
JeKa downloads the JDK it needs.

The [jeka.properties](jeka.properties) file at the root sets the JeKa and Java versions for all of them.
A project can override them in its own `jeka.properties`.

| Code Sample | Description |
|-------------|-------------|
| [Task Based](./java-task-based) | Build a project with ANT-style tasks, written in Java |
| [Project Simple](./project-simple) | Build a project with no build code |
| [Project with Local Libs](./project-local-libs) | Build a project depending on jars stored in the repository |
| [Maven Migration](./migrate-from-maven) | Generate JeKa dependencies from a Maven `pom.xml` |
| [Springboot API](./springboot-api) | Configure a Spring Boot project with a KBean |
| [Springboot Props](./springboot-props) | Build a Spring Boot project using properties only |
| [Springboot Base](./springboot-base) | Write a Spring Boot application as a script, in *jeka-src* |
| [Kotlin Springboot ReactJs](./springboot-kotlin-reactjs) | Integrate Kotlin and Node.js |
| [Springboot ReactJs using a build convention](./springboot-reactjs-using-template) | Reuse a build convention published as a jar |
| [Multi Modules](./springboot-multi-modules) | Drive several modules from a master build |
| [Kubernetes](./kubernetes) | Build a Spring Boot + MongoDB project and deploy it to Kubernetes |

Note: you can open this entire repository in IntelliJ IDEA, with the [JeKa plugin](https://plugins.jetbrains.com/plugin/13489-jeka).
