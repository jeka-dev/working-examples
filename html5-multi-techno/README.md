# Multi-techno Html5 project

This project gives an example of how to build a project containing both Java and HTML5 modules.<br/><br/>
Java projects are built with Jerkar while HTML5 is built using [Grunt](gruntjs.com) for Html5). Jerkar is used also to glue Java and HTM5 builds together in a 'master' project.<br/> <br/>
Note that is not the only way to build such project with Jerkar : you could build HTML5 project with Jerkar as well, but we want to showcase how external build technology can be integrated with Jerkar.

The functionality of this showcase project is a "magic formula" : it just computes a number according an hard-coded function (called "magic formula") and an input number. 

* [core](./core) project actually does compute the "magic formula". It uses in turn the [utility](./utility) project to accomplish some parts of the computing.

* [client-swing](./client-swing) project offers a Swing graphical interface for computing magic formula.

* [web](./web) project exposes the "magic formula" on http server.

* [client-html5](./client-html5) project offers an Html5 interface. This project is built with *Grunt*. 

* [master](./master) project contains build instruction to build all together.


![map](master/capture.png)

## How to build

You need to install **Grunt** in order to build the client-html5 part of the build.

### From command line
Open a shell in [master](./master) project and execute `jerkar`. <br/>
If you don't want to build/include [client-html5](./client-html5) project, execute `jerkar -embbedHtml5=false`.


### From IDE
Exectute the class ´Build´ found at [master/build/def/Build.java](master/jerkar/def/Build.java) or <br/>
execute `org.jerkar.tool.Main`class within the *master* project.
