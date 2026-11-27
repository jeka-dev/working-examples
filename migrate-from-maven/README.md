# Simple Java project to demo migrating from Maven

For now, `pom` plugin can only output Jeka dependency code to the console.

It is required that a *pom.xml* file exists at the root of the project.

The plugin reads the pom to get dependencies declared in, 
and generates jeka corresponding code on the console.

It needs Maven (`mvn`) installed, as JeKa runs `mvn help:effective-pom`.

To run, just execute :
```shell
jeka maven: migrateDeps
```

Or use the shorthand defined in [jeka.properties](jeka.properties):
```shell
jeka ::migrate
```

