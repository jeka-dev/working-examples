# Build Spring-Boot Project by just Setting Properties

JeKa can build projects without using any build code, if there is no too specific tasks. 
Here, we build a Springboot project with SonarQube analysis and Jacoco coverage.

Both commands and properties are defined in [local.properties](./jeka/local.properties) file.

Dependencies are declared in [project-dependencies.txt](./jeka/project-dependencies.txt) file.

## How to build

To create a bootable jar from scratch, execute :
```shell
./jekaw project#cleanPack
``` 

To create a bootable jar from scratch and analysing code+coverage via Sonarqube, execute :
```shell
./jekaw :packQuality
```

## How to run

```shell
./jekaw project#runJar
```
