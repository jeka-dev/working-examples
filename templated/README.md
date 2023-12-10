# Templated Project
This project showcases, how we can easily re-use build definition across several project.

Here, we reuse a build definition (we call it *template*) defined in an [external project](https://github.com/jeka-dev/template-examples).
Source code of this template is available [here](https://github.com/jeka-dev/template-examples/blob/master/src/main/java/dev/jeka/examples/templates/SpringBootTemplateBuild.java).

The template is designed to build Spring-Boot project, optionally containing a ReactJs template.

This includes tests with coverage, Sonarqube analysis, ReactJs packaging (if present) and bootable jar creation.

For this, the project has to define only what is specific (application name, dependencies, Spting-Boot and Java version),
the build template will handle all the remaining parts.

The whole build definition lies in [local.properties file](./jeka/local.properties) 
and in [project-dependencies.txt](./jeka/project-dependencies.txt) for application dependencies.


## Build

This springboot project is built using [JeKa](https://jeka.dev), [JeKa Springboot plugin](https://github.com/jeka-dev/jeka/tree/master/plugins/dev.jeka.plugins.springboot)
and [JeKa NodeJs plugin](https://github.com/jeka-dev/jeka/tree/master/plugins/dev.jeka.plugins.nodejs) 

To create a bootable jar, containing the client app, and execute SonarQube analysis on both java and js, execute :
```shell
./jekaw #packQuality
```

To run the bootable jar built in previous step, execute :
```shell
./jekaw #runJar
```

