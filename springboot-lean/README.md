# Simple Springboot project built without build class 

Projects may be built without any build code if there is no too specific tasks.

Both commands and properties are defined in [local.properties](./jeka/local.properties) file.

Dependencies are declared in [project-dependencies.txt](./jeka/project-dependencies.txt) file.

## How to build

To create a bootable jar from scratch, execute :

```shell
./jekaw :build
``` 

To create a bootable jar from scratch and analysing code+coverage via Sonarqube, execute :

```shell
./jekaw :build_quality
```


