# Spring-Boot Application hosted in jeka-src

Run a Spring-Boot application from sources.

The idea is to code the [Spring-Boot application](jeka-src/app) entirely is *jeke-src*.

Everything, concerning non-prod (build, tests) are located under *_dev* package and won't be shipped with the application.

## Setup IDE

```shell
jeka intellij#iml
```

Execute app :
 ```shell
 jeka #runJar
 ```

Execute Test :
```shell
jeka #test
```

## Docker

Additionally, the [script](jeka-src/Script.java) includes methods to build, run and stop a Docker image of the application.
This requires to have a Docker daemon running on the host machine.

Build Image :
```shell
jeka docker#build
```

Run the Image :
```shell
jeka docker#run
```




