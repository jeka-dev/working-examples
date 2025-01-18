# Simple Java project with build class based on template.

This project demonstrates how to build a project without any configuration/code.<br/>
Third party libraries are stored locally in dedicated folder according their scope.

## How to build

### From command line
To build the project, execute :
```shell
jeka project: pack -c
```

If you want to produce test coverage analysis with [Jacoco](http://eclemma.org/jacoco/), execute :
```shell
jeka -cp=dev.jeka:jacoco-plugin jacoco: project: test  
```
If you want to produce test coverage and sonarQube analysis [SonarQube](http://www.sonarqube.org/), execute :
```shell
jeka -cp=dev.jeka:jacoco-plugin,dev.jeka:sonarqube-plugin project: test jacoco: sonarqube: run
```

### From IDE

Expand the Jeka Tool Window to **java-full-conventional | Classpath KBeans | project | pack...**


