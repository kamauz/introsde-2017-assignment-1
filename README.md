[INTROSDE] Exercise with Java objects, XML and JSON (assignment 1)
===============
First assignment of "Introduction to service design and engineering" course

Code information
-------------

Before talking about the code a look should be given to the used technologies in order to better understand the project organization.
The project has been developed in Java and a list of libraries has been provided to manage the operations. This set includes: **XML/XSD**, **JAXB/Jackson**, **JSON**, **XPATH**.

The organization of the source code shows two packages: a default package and a pojos package.
The first one contains the class that instantiate and executes the operations requested in the assignment, while in the second one there are the definitions of classic standard Java classes (like "ActivityPreferences", "PeopleStore", "Person"). These packages are included into a src folder and this is put beside the source XML file, the XML schema, the ivy xml file and the build xml in the root folder.


Requested task
--------------------
The assignments requests were:

- to get the people list from "people.xml"
- to get the ActivityPreference for a specific person
- to filter the ActivityPreferences basing on stardate
- to create three Java object Person instances and to convert them into a XML file (marshal)
- to create the Java objects from the generated XML file as input (unmarshal)


How run the code 
---------------------
To execute the application correctly it is possible to act in two different ways.

So to execute all the sub-exercises:

- ```ant execute.evaluation```

To execute each single exercise:

- ```ant compile```

then

 - ```ant exercise_1``` : to get the people list found in the file "people.xml"
 - ```ant exercise_2``` : to get the ActivityPreference for a specific person with id attribute equals to '5'
 - ```ant exercise_3``` : to filter the ActivityPreferences with stardate attribute > 2017-13-10
 - ```ant exercise_4``` : to create three Java object Person instances and to convert the objects into a XML file (marshal operation, the XML file generated is called "marshalled.xml")
 - ```ant exercise_5``` : to recreate the Java objects given the XML file in input (unmarshal) 
 - ```ant exercise_6``` : to generate a JSON file from the Java object Person instances
 
 For every specific operation a log of the output is shown in the terminal
