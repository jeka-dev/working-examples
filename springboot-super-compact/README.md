# Spring-Boot Application as script

Run a Spring-Boot application from sources.

The idea is too code the [Spring-Boot application](./jeka/def/app) as a regular JeKa script, so it can 
be executed from sources, without needing to be explicitly built.
Users just need a JDK 17+ installed on host machine.

The [Application](./jeka/def/app/Application.java) class declares the dependencies needed to run the application.

 `./jekaw #run` executes the application from [sources](./jeka/def/app), passing by a compilation test behind the scene.
The compilation result is cached for faster execution on subsequent runs.

`./jekaw #test` executes the tests defined in [test dir](./jeka/def/test)

Production and test classpath are not segregated, which is ok for project with small-medium testing requirements.

## Docker

Additionally, the [script](./jeka/def/Script.java) includes methods to build, run and stop a Docker image of the application.
This requires to have a Docker daemon running on the host machine.

`./jekaw #buildImage` to build the Docker image using Docker daemon.

`./jekaw #runImage` to run the Docker image in a container.

`./jekaw #runImage` to stop the container launched in previous step.

