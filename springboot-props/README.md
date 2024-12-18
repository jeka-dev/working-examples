# Build Spring-Boot Project by just Setting Properties

JeKa can build projects without using any build code, if there is no too specific tasks. 
Here, we build a Springboot project with SonarQube analysis and Jacoco coverage.

Both commands and properties are defined in [local.properties](local.properties) file.

Dependencies are declared in [project-dependencies.txt](project-dependencies.txt) file.

## Setup IDE

```shell
jeka intellij: iml
```

## How to build

To create a bootable jar from scratch, execute :
```shell
jeka pack
``` 

To create a bootable jar from scratch and analysing code+coverage via Sonarqube, execute :
```shell
jeka ::packQuality
```

## How to run

```shell
jeka runJar
```
