# Spring-Boot Application hosted in jeka-src

Run a Spring-Boot application from sources.

## Setup IDE

```shell
jeka intellij: iml
```

Execute app :
 ```shell
 jeka runJar
 ```

Execute Test :
```shell
jeka test
```

Build Docker Image :
```shell
jeka docker: build
```

Run Docker Image :
```shell
jeka docker: run
```

The idea is to code the [Spring-Boot application](jeka-src/app) entirely is *jeke-src*.

Everything, concerning non-prod (build, tests) are located under *_dev* package and won't be shipped with the application.






