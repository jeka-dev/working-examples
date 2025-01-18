# A Springboot-mongoDb application deployed on Kubernetes

This demo is based on a Spring Boot Kubernetes tutorial available at https://learnk8s.io/spring-boot-kubernetes-guide.

It showcases a Spring Boot web application that utilizes a MongoDB database as its backend.

The main objective of this demo is to demonstrate the automation of Kubernetes deployment across multiple environments using pure Java technologies. 
The following libraries are used for this purpose:

- [jib core library](https://github.com/GoogleContainerTools/jib/tree/master/jib-core) for building container images
- [Fabric8io Kubernetes Client](https://github.com/fabric8io/kubernetes-client) Fabric8io Kubernetes Client for specifying and deploying Kubernetes resources

## Setup IDE

```shell
jeka intellij: iml
```

## How does it work ?

Jeka builds (compiles and tests) the Springboot application using [Jeka Springboot
plugin](https://github.com/jeka-dev/jeka/tree/master/plugins/dev.jeka.plugins.springboot).

The application build is specified in [jeka.properties file](jeka.properties) while
[project-dependencies.txt](project-dependencies.txt) specifies dependencies.

A [Kube KBean](jeka/def/kube/Kube.java) defines the entry points to interact with command-line
or the IDE. This KBean delegates the tasks to following classes :
- [Image](jeka/def/kube/Image.java) : Produces the container image.
- [Resources](jeka//def/kube/Resources.java) : Defines an object model of the Kubernetes resources to deploy.
- [2 generic helper classes](jeka/def/kube/support) that can be externalized in a lib/plugin. They provide simple convenient methods to deal with Jib and Fabric8 api.


### The image

The image is built using Jib technology, which eliminates the need for a local Docker daemon to build the image. To build the image, you need to provide the following inputs:

- The SpringbootJkBean: This contains all the necessary information to build the image, such as the location of classes/libs and the main class name.
- An optional version for tagging the image (the default is latest).


### The Kubernetes Resources

The `Resources` class defines an object model using the Fabric8 API, which provides the following functionalities:

- Factory methods for creating `Resources` objects customized for predefined environments.
- Accessors to retrieve Kubernetes mutable and immutable resources for deployment.
- Mutators to conveniently modify Kubernetes resources, such as changing the image tag for the application container.

### The Kube KBean

The `Kube` KBean defines methods that serve two purposes:

1. Developers may need these methods during development to properly set up the Kubernetes resources.
2. CI/CD tools utilize these methods for building and deploying resources.

The target environment can be specified by using the `kube#target=STAGING` option in the command line.

For instance, the following command displays the Kubernetes resources as they will be deployed in the PROD environment.
```shell
jeka render target=PROD
```

## Running the demo

### Prerequisites

To build and deploy this application, you will need the following:

- A Docker registry to deploy the Docker image of the application.
- A functional Kubernetes cluster.

Both requirements can be fulfilled by using Docker Desktop. If you don't have a private registry deployed locally, you can obtain one by running the following command:

```shell
docker run -d -p 5000:5000 --restart=always --name registry registry:2
```

Docker Desktop includes a built-in Kubernetes cluster that can be enabled from the Docker Desktop Settings panel.

This demo is designed to run seamlessly with this cluster, but additional configuration may be required for running it with a different cluster like Minikube.

### Building the Docker image

The application image is built using [Jib](https://github.com/GoogleContainerTools/jib/tree/master/jib-core), eliminating the need for a Docker daemon on your local machine.

Execute the following command:
```shell
jeka buildAndApply
```

The `:build` command shortcut defined in the [local.properties](local.properties) file will clean, compile, and test the application (`project#test`) before building and publishing the Docker image (`kube#buildImage`).

You can check that the image has been successfully deployed to your registry by visiting [http://localhost:5000/v2/knote-java/tags/list](http://localhost:5000/v2/knote-java/tags/list).

### Deploying on your local Kubernetes cluster

To interact with the Kubernetes cluster and deploy or update resources, the [Fabric8io Kubernetes Client](https://github.com/fabric8io/kubernetes-client) is used.

To deploy or update the cluster with the built resources, execute the following command:
```shell
jeka apply
```

You can also build the application and deploy it in a single command by running:
```shell
jeka buildAllAndApply
```

Before testing, you need to forward the port from the Kubernetes cluster to your local machine:
```shell
jeka portForward
```

To display all the available methods and properties for this project, execute the following command, which reflects the content of the `Kube` class:
```shell
jeka -cmd
```

You can now access your local application by clicking [here](http://localhost:8080/).

### Building and deploying in multi-environment

For simplicity, this demo manages only three environments that share the same local Kubernetes cluster and Docker registry. Each environment may differ in the Kubernetes namespace, environment variables, volume size, and replica count.

- **local**: Used for development purposes and only deployed from the local machine.
- **staging**: Built and deployed from a CI tool.
- **prod**: Deployed from a CI tool.

#### Building and deploying in the *staging* environment

To build and deploy in the *staging* environment from a CI tool, execute the following command, where `${BUILD_ID}` is an ID generated by the CI tool that allows you to retrieve the original Git commit and branch information:

```shell
jeka #buildAndApply #target=STAGING #appVersion=${BUILD_ID}
```

#### Deploying in the *prod* environment

In the same pipeline, the CI tool may include a step to deploy the already built application in the *prod* environment:

```shell
jeka #apply #target=PROD #appVersion=${BUILD_ID}
```












