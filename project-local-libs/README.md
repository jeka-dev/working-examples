# Project depending on local libraries

This project shows how JeKa builds a project whose third-party jars are stored in the repository, with no build code.
The jars lie in [libs](libs), one folder per scope: `libs/compile` for the code, `libs/test` for the tests.
JeKa adds them to the matching classpath. [jeka.properties](jeka.properties) only activates the `project` KBean (`@project=`).

## How to build

Build the jar in `jeka-output/`, after cleaning it:
```shell
jeka project: pack -c
```

Run the tests with [JaCoCo](https://www.jacoco.org/jacoco/) coverage (report in `jeka-output/jacoco`):
```shell
jeka -cp=dev.jeka:jacoco-plugin jacoco: project: test
```

Add a [SonarQube](https://www.sonarsource.com/products/sonarqube/) analysis (needs a SonarQube server):
```shell
jeka -cp=dev.jeka:jacoco-plugin,dev.jeka:sonarqube-plugin project: test jacoco: sonarqube: run
```

## Links

- [Build projects](https://jeka-dev.github.io/jeka/tutorials/build-projects/)
- [project KBean](https://jeka-dev.github.io/jeka/reference/kbeans-project/)
