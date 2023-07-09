# A Springboot-mongoDb application deployed on Kubernetes

This demo is based on a Spring Boot Kubernetes tutorial available at https://learnk8s.io/spring-boot-kubernetes-guide.

It showcases a Spring Boot web application that utilizes a MongoDB database as its backend.

The main objective of this demo is to demonstrate the automation of Kubernetes deployment across multiple environments using pure Java technologies. 
The following libraries are used for this purpose:

- [jib core library](https://github.com/GoogleContainerTools/jib/tree/master/jib-core) for building container images
- [Fabric8io Kubernetes Client](https://github.com/fabric8io/kubernetes-client) Fabric8io Kubernetes Client for specifying and deploying Kubernetes resources

## How does it work ?

Jeka builds (compiles and tests) the Springboot application using [Jeka Springboot
plugin](https://github.com/jeka-dev/jeka/tree/master/plugins/dev.jeka.plugins.springboot).

The application build is specified in [local.properties file](jeka/local.properties) while
[project-dependencies.txt](jeka/project-dependencies.txt) specifies dependencies.

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
./jekaw #render #target=PROD
```

## Running the demo 


### prerequisite

*You don't have to install Jeka on your machine thanks to the Jeka Wrapper. 
Just clone this directory and execute the command line.
Nevertheless, for a better experience it recommended to install the [Jeka Plugin](https://plugins.jetbrains.com/plugin/13489-jeka) 
if you use Intellij.*

For building and deploying this application, you need two things :
- A docker registry to deploy the docker image of the application.
- A workable Kubernetes cluster.

Both can be provided by Docker Desktop. If you haven't yet a private registry deployed locally, 
you can get it by executing :
```shell
docker run -d -p 5000:5000 --restart=always --name registry registry:2
```

Docker Desktop embeds a k8s cluster. You can enable it from the Docker Desktop *Settings* panel.

This demo has been designed to run out-of-the-box with this cluster. 
You may need extra configuration for running with another one as Minikube.




### Build the Docker image

The image is build using [jib](https://github.com/GoogleContainerTools/jib/tree/master/jib-core), and does not require 
to have a Docker daemon installed on your machine.

Execute :
```shell
./jekaw #buildAndApply
```
`:build` is a command shortcut defined in [local.properties file](jeka/local.properties). This actually clean, compile, test the
application (`project#test`) prior to build the Docker image and publish it (`kube#buildImage`).

Go [here](http://localhost:5000/v2/knote-java/tags/list) to check that your image has been actually deployed on your registry.

### Deploy on your local Kubernetes cluster

To build and interact with Kubernetes cluster, we use [Fabric8io Kubernetes Client](https://github.com/fabric8io/kubernetes-client).

To deploy or update the cluster with the built resources, execute : 
```shell
./jekaw #apply
```

You can also build the application and deploy it in a row using :
```shell
./jekaw #buildAllAndApply
```

Prior testing, you must forward the port from Kubernetes cluster to your local machine :
```shell
./jekaw #portForward
```

To display all methods/properties available on this project, execute the following command, 
it reflects the content of the `Kube` class.
```shell
./jekaw #help
```

Now you can access to your local application by [clicking here](http://localhost:8080/)

### Build and deploy in multi-environment

For simplicity's sake, we are managing only 3 environments sharing the same local Kubernetes cluster and Docker registry.
Only k8s namespace, environment variables, volume size and replica count diverges from one environment to another.
- local: for development, only deployed from local machine
- staging: build and deployed from aCI tool
- prod: deployed from CI tool

#### Build and deploy in *staging* environment

To build and deploy in *staging* environment, the CI tool, just need to execute 
the following command, where `${BUIL_ID}` is an id generated from the CI tool, from where 
we can retrieve original Git commit and branch.
```
./jekaw #buildAndApply #target=STAGING #appVersion=${BUILD_ID}
```

#### Deploy in *prod* environment

In the same pipeline, the CI tool may provide a step to deploy the already
built application, in *prod* environment.

```
./jekaw #apply #target=PROD #appVersion=${BUILD_ID}
```













