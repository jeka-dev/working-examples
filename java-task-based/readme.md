# Simple Java project with build class designed as a ANT file.

This project aims at providing a jar file library that capitalize entire strings (all word of the input string are capitalized).

The project has a dependency on *Guava* and on *Junit4* for testing.

This project does not respect any layout convention. Its [build class](jeka/def/Build.java) explicitly describes the whole build as we do with ANT.

## How to build

### From command line
Open a shell at the root of the project execute `jekaw`. <br/>
If you want the junit test to be run in a forked process, execute `jekaw compile junit -forkTest=true `. <br/>
If you want to only produce the jar without running tests, execute `jekaw compile jar`.

### From IDE
Execute `Build` class found at [build/def/Build.java](jeka/def/Build.java) or <br/>

