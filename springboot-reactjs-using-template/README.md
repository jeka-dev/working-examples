# Springboot+reactjs project built using an external template

### Setup IDE

```shell
jeka intellij: sync
```

### Build

Help on template KBean:
```shell
jeka template: --doc
```

Create a bootable jar, containing the client app,:
```shell
jeka pack
```

Deploy the application in Docker then run end-to-end tests:
```shell
jeka template: e2e
```

Run Sonarqube analysis, on both Java and JS:
```shell
jeka template: sonar
```

Run the bootable jar:
```shell
jeka runJar
```

Create a Docker image:
```shell
jeka docker: build
```

Create a Spring-Boot native Docker image:
```shell
jeka docker: buildNative
```

Once the image is built built, we can run end-to-end tests on the Docker image
```shell
jeka template: e2e
```

For CI/CD, we can run `jeka pack template: e2e sonar` for instance.



