# Single project hosting a Kotlin Springboot and reactJs application

This project contains a Kotlin Springboot application and a ReactJs application (hosted in 'client' dir).

Jeka builds both server and client application. 

No installation of node-js is required : Jeka install required node versions transparently.

The whole build is configured in [jeka.properties](jeka.properties): the `kotlin`, `nodeJs` and `springboot` plugins.
The `SPRING` preset of the `kotlin` KBean adds the Kotlin *allopen* compiler plugin, needed by Spring.

## Setup IDE

```shell
jeka intellij: iml --force
```

Generates iml

## How to build

To make a clean build for production (a springboot jar file embedding the client), execute :
```shell
jeka pack
``` 

To run the jar built with previous command:
```shell
jeka runJar
```

Browse http://localhost:8080 to open the main page once the jar is running.

Build a native Docker image:
```shell
jeka docker: buildNative
```

Execute the image:
```shell
docker run --rm -p 8080:8080 native-springboot-kotlin-reactjs:latest 
```

______________
## Development

Client application has been scaffolded using ReactJs tool according the tutorial mentioned below.

## Testimonial

This example application as been created from [this tutorial](https://developer.okta.com/blog/2020/01/13/kotlin-react-crud)

