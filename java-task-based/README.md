# Simple Java project with build class designed as a ANT file.

This project aims at providing a jar file library that capitalize entire strings (all word of the input string are capitalized).

The project has a dependency on *Guava* and on *Junit4* for testing.

This project does not respect any layout convention. Its [build class](jeka/def/Tasks.java) explicitly describes the whole build as we do with ANT.

## Setup IDE

```shell
jeka intellij: iml
```

## How to build

### From command line

If you want to only produce the jar without running tests, execute :
```shell
jeka compile jar
```

If you want the junit test to be run in a forked process, execute :
```shell
jeka compile junit forkTest=true
```

To full build the project, execute
```shell
jeka build
```

### From IDE
Execute `Tasks` class found at [build/def/Tasks.java](jeka/def/Tasks.java) or <br/>

