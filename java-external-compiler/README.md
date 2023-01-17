# Simple Java project with build class based on template.

This project demonstrates how to use *Eclipse* compiler for building the project.

## How to build

### From command line
To build the project, execute :
```shell
./jekaw project#clean project#pack
```

### From IDE

Execute `Build#cleanPack` class found at [build/def/ClassicBuild.java](jeka/def/ClassicBuild.java).

If you want to produce test coverage analysis with [Jacoco](http://eclemma.org/jacoco/), execute :
```shell
./jekaw @dev.jeka:jacoco-plugin project#test jacoco# 
```
If you want to produce test coverage and sonarQube analysis [SonarQube](http://www.sonarqube.org/), execute :
```shell
./jekaw @dev.jeka:jacoco-plugin @dev.jeka:sonarqube-plugin project#test jacoco# sonarqube#run
```

