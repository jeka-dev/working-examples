# Spring Boot project configured with a KBean

This Spring Boot application is built by JeKa, with its build configured in Java: the [Build](jeka-src/Build.java) KBean
adds the plugins with `@JkDep` (`springboot`, `jacoco`, `sonarqube`), declares the dependencies, and sets the
project options. [jeka.properties](jeka.properties) makes `project` the default KBean, and initializes `Build` (`@build=on`).

## Build

Create the bootable jar, after cleaning the output:
```shell
jeka pack -c
```

Run the tests:
```shell
jeka test
```

Run the bootable jar, then open http://localhost:8080 (it answers `Hello World`):
```shell
jeka runJar
```

Run a SonarQube analysis (needs a SonarQube server):
```shell
jeka sonarqube: run
```

## Links

- [Spring Boot plugin](https://github.com/jeka-dev/jeka/tree/master/plugins/plugins.springboot)
- [KBeans](https://jeka-dev.github.io/jeka/reference/kbeans/)
