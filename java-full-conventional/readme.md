# Simple Java project without build class.

This project aims at providing a jar file library that capitalize entire strings (all word of the input string are capitalized).

The project has a dependency on *Guava* and on *Junit4* for testing. Libraries are hosted in a folder locally in the project and are not managed transitively. 

This project respects fully convention so no build class is required :
* sources lie under *src/main/java*
* test sources lie under *src/test/java*
* 3rd party libraries needed for compilation lie under *build/libs/compile*
* 3rd party libraries needed for tests lie under *build/libs/test*


## How to build

### From command line
Open a shell at the root of the project execute `jekaw project#cleanPack`. <br/>
To produce a fat jar, execute `jekaw project#pack -project#pack.fatJar=true`. <br/>
To produce test coverage analysis with [Jacoco](http://eclemma.org/jacoco/), execute `jekaw project#test jacoco#`. <br/>
To produce test coverage and sonarQube analysis [SonarQube](http://www.sonarqube.org/), <br/> execute `jekaw project#test project#test jacoco# sonarqube#verify -sonar.host.url=http://my.sonar.host:8080`.

### From IDE
Exectute the class `dev.jeka.core.tool.Main` within this project. 
