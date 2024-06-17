# Templated Project

## Setup IDE

```shell
jeka intellij: iml
```

## Build

This springboot project is built using [JeKa](https://jeka.dev), [JeKa Springboot plugin](https://github.com/jeka-dev/jeka/tree/master/plugins/dev.jeka.plugins.springboot)
and [JeKa NodeJs plugin](https://github.com/jeka-dev/jeka/tree/master/plugins/dev.jeka.plugins.nodejs)

To create a bootable jar, containing the client app, and execute SonarQube analysis on both java and js, execute :
```shell
jeka project: pack
```

To run the bootable jar built in previous step, execute :
```shell
jeka project: runJar
```

Launch Sonarqube analysis :
```shell
jeka #runSonarqube
```

## Display Info

Help on template KBean :
```shell
jeka -cmd=
```

Help on Project KBean
```shell
jeka project: help
```

Java project info :
```shell
jeka project: info
```

## Purpose

This project showcases, how we can easily re-use build definition across several project.

Here, we reuse a build definition (we call it *template*) defined in an [external project](https://github.com/jeka-dev/template-examples).
Source code of this template is available [here](https://github.com/jeka-dev/template-examples/blob/master/src/main/java/dev/jeka/examples/templates/SpringBootTemplateBuild.java).

The template is designed to build Spring-Boot project, optionally containing a ReactJs template.

This includes tests with coverage, Sonarqube analysis, ReactJs packaging (if present) and bootable jar creation.

For this, the project has to define only what is specific (application name, dependencies, Spting-Boot and Java version),
the build template will handle all the remaining parts.

The whole build definition lies in [local.properties file](local.properties) 
and in [project-dependencies.txt](project-dependencies.txt) for application dependencies.

