# Simple Java project with build class based on template.

This project aims at providing a jar file library that capitalize entire strings (all word of the input string are capitalized).

The project has a dependency on *Guava* and on *Junit4* for testing.

The [build class](jeka/def/Build.java) declares transitive dependencies and specifies module naming.

## How to build

### From command line
Open a shell at the root of the project execute `jekaw`. <br/>
If you want to produce test coverage analysis with [Jacoco](http://eclemma.org/jacoco/), execute `jekaw jacoco#`. <br/>
If you want to produce test coverage and sonarQube analysis [SonarQube](http://www.sonarqube.org/), execute `jekaw jacoco# sonar#verify`.

### From IDE
Execute `Build` class found at [build/def/Build.java](jeka/def/Build.java) or <br/>

