# Springboot+reactjs project built using an external build convention

The build logic comes from a convention KBean published on Maven Central, defined in
[demo-build-conventions](https://github.com/jeka-dev/demo-build-conventions). [jeka.properties](jeka.properties)
only adds it to the classpath and activates it. A complete example is
[demo-build-conventions-consumer](https://github.com/jeka-dev/demo-build-conventions-consumer).

### Setup IDE

```shell
jeka intellij: sync
```

### Build

Help on the convention KBean:
```shell
jeka convention: --doc
```

Run the tests and create a bootable jar, containing the client app:
```shell
jeka test pack
```

Run the bootable jar, then open http://localhost:8080:
```shell
jeka runJar
```
