# Single project hosting a Kotlin Springboot and reactJs application

This project contains a Kotlin Springboot application and a ReactJs application (hosted in 'client' dir).

Jeka builds both server and client application. 

No installation of node-js is required : Jeka install required node versions transparently.

Kotlin support is still experimental, so Kotlin compilation has been entirely coded within build classes. 
It gives an idea about how to customise Jeka for integrating specific technology. 

## Setup IDE

```shell
jeka intellij: iml --force
```

Generates iml

## How to build

To make a clean build for production (a springboot jar file embedding the client), execute :
```shell
jeka -c project: pack
``` 

To run the jar built with previous command:
```shell
jeka project: runJar
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

