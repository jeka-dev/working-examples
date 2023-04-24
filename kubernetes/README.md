# A pringboot + mongoDb project deployed on Kubernetes

This demo is based on [this Kubernetes tutorial](https://learnk8s.io/spring-boot-kubernetes-guide),

It consists in a Springboot web app backed by a MongoDB database.

This project showcases how we can automate Kubernetes deployment in multi-environments, using 
pure Java technologies.

## Prerequisite

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

Also, Docker Desktop embeds a k8s cluster, so you can use it if you don't have one yet installed on your machine.

## Build the Docker image

The image is build using [jib](https://github.com/GoogleContainerTools/jib/tree/master/jib-core), and does not require 
to have a Docker daemon installed on your machine.

Execute :
```
./jekaw :image
```
`:image` is a command shortcut defined in *local.properties* file. This actually clean, compile, test the
application (`project#ctest`) prior to build the Docker image and publish it (`kube#buildImage`).

Go [here](http://localhost:5000/v2/knote-java/tags/list) to check that your image has been actually deployed on your registry.

## Deploy on your local Kubernetes cluster

To build and interact with Kubernetes cluster, we use [Fabric8io Kubernetes Client](https://github.com/fabric8io/kubernetes-client).

To deploy or update the cluster with the built resources, execute : 
```shell
./jeka kube#apply
```

You can also build the application and deploy it in a row using :
```shell
./jekaw kube#pipeline
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

## Manage multiple environments