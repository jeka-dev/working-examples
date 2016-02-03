# Simple Java project with build class designed as a ANT file.

This project aims at providing a jar file library that capitalize entire strings (all word of the input string are capitalized).

The project has a dependency on *Guava* and on *Junit4* for testing.

This project does not respect any layout convention. Its [build class](./build/def/Build.java) explicitly describes the whole build as we do with ANT.

## How to build

### From command line
Open a shell at the root of the project execute `jerkar`. <br/>
If you want the junit test to be run in a forked process, execute `jerkar -forkTest=true`. <br/>
If you want to only produce the jar without running tests, execute `jerkar compile jar`.

### From IDE
Execute `Build` class found at [build/def/Build.java](./build/def/Build.java) or <br/>
execute `org.jerkar.tool.Main`class within this project.

