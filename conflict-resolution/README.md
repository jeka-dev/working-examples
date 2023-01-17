# Simple Java project with build class based on template.

This project demonstrates how we can configure the dependency resolver to force a failure,
if there is version mismatch in the resolved dependency tree.<br/>
To force the failure, comment the dependency exclusion line 33 of `Build` class.

The [build class](jeka/def/ClassicBuild.java) declares transitive dependencies and specifies module naming.

## How to build

### From command line
Open a shell at the root of the project execute :
```shell
./jekaw project#clean project#pack
```

### From IDE

Execute `Build#cleanPack` class found at [build/def/ClassicBuild.java](jeka/def/ClassicBuild.java).

