# Simple Java project with build class based on template.

This project aims at providing a jar file library that capitalize entire strings (all word of the input string are capitalized).

The project has a dependency on *Guava* and on *Junit5* for testing.

The [build class](jeka/def/ClassicBuild.java) declares transitive dependencies and specifies module naming.

## How to build

### From command line
To build the project, execute :
```shell
./jekaw project#clean project#pack
```

If you want to produce test coverage analysis with [Jacoco](http://eclemma.org/jacoco/), execute :
```shell
./jekaw @dev.jeka:jacoco-plugin project#test jacoco# 
```
If you want to produce test coverage and sonarQube analysis [SonarQube](http://www.sonarqube.org/), execute : 
```shell
./jekaw @dev.jeka:jacoco-plugin @dev.jeka:sonarqube-plugin project#test jacoco# sonarqube#run
```

### From IDE

Execute `ClassicBuild#cleanPack` class found at [build/def/ClassicBuild.java](jeka/def/ClassicBuild.java).

