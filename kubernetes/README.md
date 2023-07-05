# A Springboot + mongoDb project deployed on Kubernetes

This demo is based on [this Kubernetes tutorial](https://learnk8s.io/spring-boot-kubernetes-guide),

It consists in a Springboot web app backed by a MongoDB database.

This project showcases how we can automate Kubernetes deployment in multi-environments, using 
pure Java technologies.

## prerequisite

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

## Build the Docker image

The image is build using [jib](https://github.com/GoogleContainerTools/jib/tree/master/jib-core), and does not require 
to have a Docker daemon installed on your machine.

Execute :
```shell
./jekaw :image
```
`:image` is a command shortcut defined in [local.properties file](jeka/local.properties). This actually clean, compile, test the
application (`project#test`) prior to build the Docker image and publish it (`kube#buildImage`).

Go [here](http://localhost:5000/v2/knote-java/tags/list) to check that your image has been actually deployed on your registry.

## Deploy on your local Kubernetes cluster

To build and interact with Kubernetes cluster, we use [Fabric8io Kubernetes Client](https://github.com/fabric8io/kubernetes-client).

To deploy or update the cluster with the built resources, execute : 
```shell
./jekaw kube#apply
```

You can also build the application and deploy it in a row using :
```shell
./jekaw kube#buildAllAndApply
```

Prior testing, you must forward the port from Kubernetes cluster to your local machine :
```shell
./jekaw kube#portForward
```

To display all methods/properties available on this project, execute the following command, 
it reflects the content of the `Kube` class.
```shell
./jekaw kube#help
```

Now you can access to your local application by [clicking here](http://localhost:8080/)

## How does it work ?

Jeka builds (compiles and tests) the Springboot application using [Jeka Springboot 
plugin](https://github.com/jeka-dev/jeka/tree/master/plugins/dev.jeka.plugins.springboot).

A [Kube KBean](jeka/def/kube/Kube.java) defines the entry points to interact with command-line 
or the IDE. This BBean delegates the tasks to following classes :
- [Images](jeka/def/kube/Images.java) : Produces the container image.
- [Resources](jeka//def/kube/Resources.java) : Defines an object model of the Kubernetes resources to deploy.
- [Patch](jeka/def/kube/Patch.java) : Defines the variants to apply according deployment environment (K8s resources and container registry).

## The image

The image is built using *Jib* technology, so we don't need a local Docker deamon to build the image.

The `Ìmages` class needs 2 parameters to build image :
- The registry to deploy the build image. Note that it is also possible to build image as file (tarball) or  
  to deploy it on a docker daemon but as k8s require a registry to fetch the image, this intermediate stage in not relevant.
- The `SpringbootJkBean` that contains all info to actually bbuid the image (location of classes and libs, and the main class name).


## The Kubernetes Resources

Class `Resources`define an object model of the k8s resources to deploy. The model instance is construct 
from yaml static resources. This class proposes methods to :
- modify the model conveniently (setters)
- render the model in yaml format

The model is based on an library which works with immutable object, making deep structure modification
a little bit tedious. This is mitigated with the usage of setter methods to wrap common modifications.

## The Patches to deal with multi environment

Each patch instance describe the modifications to apply for a given environment.
In this pattern, patches are defined relatively from another to minimize the the change description.

For simplicity sake, here, we use a single k8s cluster but each environment deploys to a distinct namespace.

In *local*, the application run on a single pod while in higher environment, it deploys on 2 replicas.

## The Kube KBean

The `Kube` KBean defines methods that :
- Developers may need during development to set up the k8s resources properly.
- CI/CD tool need to actually build and deploy resources.

The target environment can be selected by mentioning the `kube#env=STATGING` option in the command line. 

For example, the following command displays the k8s resources as they will be deployed in PROD environment.
```shell
./jekaw kube#showResources kube#env=PROD
```

In addition, the `Kube` KBean offers the `pipeline`method that does compile, test and deploy in a row.

# Conclusion

This pattern allows a Java project to deal with Kubernetes, using simple Java technologies only.

The benefit is that developers deal with a statically typed (yet simple) model, that they can easily edit and debug 
with their familiar tools.










