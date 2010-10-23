# lurgee

A java framework for building abstract strategy games. Includes applet and console implementations of three games using the framework: reversi, connect four and nine men's morris.

## Building

You will need:

* j2sdk 1.5.0 or higher
* ant 1.6 or higher
* junit 3.8.1 or higher
* proguard 3.0 or higher (optional - to shrink/obfuscate the binaries)

Settings you might need to change in ant.properties:

* junit-jar should point to junit.jar on your local system
* proguard-jar should point to proguard.jar on your local system (optional)

Useful ant targets:

* clean - removes all the directories created by other targets
* build - compiles the code in each sub-project in the correct order
* doc - generates javadocs for the complete source excluding the unit tests
* alldoc - generates javadocs for the complete source including the unit tests

Useful ant targets for each of the sub-projects:

* clean - removes all the directories created by other targets
* build - builds the console app and the applet
* build-console-app - compiles the code for the console application.
* run-console-app - launches the console application from ant
* build-applet - compiles the code and builds a distribution jar file for the applet
* doc - generates javadocs for the project excluding the unit tests.
* alldoc - generates javadocs for the project including the unit tests.
* test - runs the unit tests for the project.
* obfuscate - shrink and obfuscate the distribution

## Copyright

Copyright (c) 2006-2010 Michael Patricios. See mit-license.txt for details.