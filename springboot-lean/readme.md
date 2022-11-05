# Simple Springboot project built without build class 

Projects may be built without any build code if there is no too specific tasks.

Both commands and properties are defined in [local.properties](./jeka/local.properties) file.

Dependencies are declared in [project-dependencies.txt](./jeka/project-dependencies.txt) file.

## How to build

`jekaw :build` to  create a bootable jar from scratch.

`jekaw :build_quality` to  create a bootable jar from scratch and analysing code+coverage via Sonarqube.



