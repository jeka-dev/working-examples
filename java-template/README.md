# Simple Java project with build class based ontemplate.

This project aims at providing a jar file library that capitalize entire strings (all word of the input string are capitalized).

The project has a dependency on *Guava* and on *Junit4* for testing.

## How to build

### From command line
Open a shell at the root of the project execute `jerkar`. 
If you want to produce test coverage analysis with [Jacoco](http://eclemma.org/jacoco/), execute `jerkar jacoco#`.
If you want to produce test coverage and sonarQube analysis [SonarQube](http://www.sonarqube.org/), execute `jerkar jacoco# sonar#verify`.

### From IDE
Exectute the class ´org.jerkar.tool.Main´ within this project or directly run the [Build](./build/def/Build.java) class. 
