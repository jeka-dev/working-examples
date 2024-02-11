# My project

## Build

This springboot project is built using [JeKa](https://jeka.dev) and its [Springboot plugin](https://github.com/jeka-dev/jeka/tree/master/plugins/dev.jeka.plugins.springboot).

To create a bootable jar, execute :
```shell
jeka project#pack -co
```

Launch Sonarqube analysis, execute :
```shell
jeka sonarqube#run
```

To run the bootable jar built by `project#pack`, execute :
```shell
jeka project#runJar
```

