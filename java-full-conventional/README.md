# Simple Java project with build class based on template.

This project demonstrates how to build a project without any configuration/code.<br/>
Third party libraries are stored locally in dedicated folder according their scope.

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

Expand the Jeka Tool Window to **java-full-conventional | Classpath KBeans | project | pack...**


