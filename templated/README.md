# Templated Project
This project showcases, how we can easily re-use build definition across several project.

Here, we reuse a build definition (we call it *template*) defined in an [external project](https://github.com/jeka-dev/template-examples/blob/master/src/main/java/dev/jeka/examples/templates/SpringBootTemplateBuild.java).

The template is designed to build Spring-Boot project, optionally containing a ReactJs template.

This includes tests with coverage, Sonarqube analysis, ReactJs packaging (if present) and bootable jar creation.

For this, this project has to define only what is specific (application name, dependencies, Spting-Boot and Java version),
the build template will handle all the rest parts.


## Build

This springboot project is built using [JeKa](https://jeka.dev) and its [Springboot plugin](https://github.com/jeka-dev/jeka/tree/master/plugins/dev.jeka.plugins.springboot).

To build with SonarQube analysis and produce a bootable jar, execute :
```shell
./jekaw #buildQuality
```

To run the bootable jar built by `project#pack`, execute :
```shell
./jekaw #runJar
```

