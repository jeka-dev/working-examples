# jerkar-examples

This repository holds examples of projects built with Jerkar 0.7.0-SNAPSHOT

You can import it directly in your IDE. For Intellij user, all the metafiles are already present in VCS so all comes 
configured out-of-the-box.

For now provided examples are :

* [Java task based](./java-task-based) : A simple Java project with build class designed as a set of tasks (ala Ant).

* [Java full conventional](./org.jerkar.examples-java-full-conventional) : Similar project as above but no build class is required as, here, the project respect fully conventions. 

* [Html5 multi-techno](./html5-multi-techno) : A multi-project containing Java jar projects, Java web project and an HTML5 project.

To make it works on Intellij, execute `jerkar idea#generateFiles` at the root of the project. This will generate iml files along modules.xml.

## Other examples

You'll find other example by watching projects built with Jerkar :

* [Jerkar itself](https://github.com/jerkar/jerkar) : This project demonstrate how you can embedds jar libs in source repositories, build HTML documentation based on markdown files, deploy on OSSRH repositories. This project contains also sample module containing several Jerkar build classes.

* [jerkar.github.io-source](https://github.com/jerkar/jerkar.github.io-sources) : Not a Java project. This project aims at building the [Jerkar HTML site](http://project.jerkar.org/) from templates using [JBake](https://jbake.org/). It also involve Git tasks automation.

* [Jerkar springboot plugin](https://github.com/jerkar/spring-boot-plugin) : This project demonstrates how to make a plugin for Jerkar. 
 
