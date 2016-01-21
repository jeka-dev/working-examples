# Multi-techno Html5 project

This project aims at giving an example of how to build a project containing both Java and HTML5 projects.<br/>
The idea is that all sub-projects are built with tooling that fits naturally with project technology (Jerkar for Java project, [Grunt](gruntjs.com) for Html5), and the overall build is achieved with Jerkar.<br/> <br/>
Note that is not the only way to build such project with Jerkar : you could build HTML5 project with Jerkar as well, but we want to showcase how external build technology can be integrated with Jerkar.

The functionalty of this showcase project a "magic formula" : it just computes a number according an hard-coded function (called "magic formula") and an input number. 

* [core](./core) project actually do compute the "magic formula". Ituses in turn the [utility](./utility) project to accomplish some parts of the computing.

* [client-swing](./client-swing) project offers a Swing graphical interface for computing magic formula.

* [web](./web) project exposes the "magic formula" on http server.

* [client-html5](./client-html5) project offers an web2 interface for computing magic formula. 

* [master](./master) project is a project containing build instruction to build all together.


![map](master/capture.png)
