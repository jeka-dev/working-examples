# Simple Java project without build class.

This project aims at providing a jar file library that capitalize entire strings (all word of the input string are capitalized).

The project has a dependency on *Guava* and on *Junit4* for testing.

This project respect fully convention so no build class is required :
* sources lie under *src/main/java*
* test sources lie under *src/test/java*
* 3rd party libraries needed for compilation lie under *build/libs/compile*
* 3rd party libraries needed for tests lie under *build/libs/test*


## How to build

### From command line
Open a shell at the root of the project execute `jerkar`. <br/>
If you want to produce a fat jar, execute `jerkar -pack.fatJar=true`. <br/>
If you want to produce test coverage analysis with [Jacoco](http://eclemma.org/jacoco/), execute `jerkar jacoco#`. <br/>
If you want to produce test coverage and sonarQube analysis [SonarQube](http://www.sonarqube.org/), execute `jerkar jacoco# sonar#verify`.

### From IDE
Exectute the class `org.jerkar.tool.Main` within this project. 
