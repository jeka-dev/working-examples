# Jeka Examples

This repository holds examples of projects built with Jeka 0.8.4.RELEASE. It uses a Jeka wrapper so you don't need 
to install Jeka on your machine.

You can import it directly in your IDE. For Intellij user, all the metadata files are already present in VCS so all comes 
configured out-of-the-box.

For now provided examples are :

* [Java task based](./java-task-based) : A simple Java project with build class designed as a set of tasks (ala Ant).

* [Java full conventional](./org.jeka.examples-java-full-conventional) : Similar project as above but no build class is required as, here, the project respect fully conventions. 

* [Html5 multi-techno](./html5-multi-techno) : A multi-project containing Java jar projects, Java web project and an HTML5 project.

To make it works on Intellij, execute `jeka idea#generateFiles` at the root of the project. This will generate iml files along modules.xml.

## Other examples

You'll find other example by watching projects built with jeka :

* [Jeka itself](https://github.com/jeka/jeka) : This project demonstrate how you can embedds jar libs in source repositories, build HTML documentation based on markdown files, deploy on OSSRH repositories. This project contains also sample module containing several Jeka build classes.

* [Jeka springboot plugin](https://github.com/jeka/spring-boot-plugin) : This project demonstrates how to make a plugin for Jeka. 
 
