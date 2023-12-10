# Spring-Boot Application as script

Run a Spring-Boot application from sources.

The idea is too code the Spring-Boot application as a regular JeKa script, so it can 
be executed from sources, without needing to be distributed as jar.
Users just need a JDK 17+ installed on host machine.

The [Application](./jeka/def/app/Application.java) class declares the dependencies needed to run the application.

 `./jekaw #run` executes the application from sources, passing by a compilation test behind the scene.
The compilation result is cached for faster execution on subsequent runs.

`./jekaw #test` executes the tests defined in *test* dir.

Production and test classpath are not segregated, which is ok for project with small-medium testing requirements.
