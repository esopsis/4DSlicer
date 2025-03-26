This code is based off of some code for generating 3D terrain by NeHe Productions which can be found here: https://nehe.gamedev.net/tutorial/beautiful_landscapes_by_means_of_height_mapping/16006/

Steps to run:

1. Import the Lesson 34 jar in Eclipse
2. Replace InputHandler.java and Renderer.java with my versions of the files, and also add MaxPoint.java and Tank.java
3. Download the SWT libraries (rather than use the library supplied with Eclipse).
4. Download the JOGL libraries.
5. Extract all of the .jar and .dll files in both SWT and JOGL packages to a common library folder, e.g. c:\libraries.
6. In Eclipse, in your java project, open the project properties. Click on the Libraries tab, click on the Add External Jars button, go to the c:\libraries folder, and choose all of the .jar files. For the current versions of both toolkits, there should be 3 jar files (swt.jar, jogl.jar, gluegen-rt.jar). This will allow the project to compile SWT and JOGL without any errors relating to library dependencies.
7. Now we want to run the application. Create a run profile as usual, but to the Arguments tab for the application in the VM arguments you need to define the location of the library using the flag -Djava.library.path=C:\Libraries
The application should find the libraries correctly now, and should run!

(For convenience sake, I have already included a .zip file with the SWT and JOGL libraries in a common zip folder.  Skip steps 3-5 and extract the libraries folder to c:\)

To play around with what I have, put your hands on the home row of your keyboard.  Try keys A, Q, S, D, E, F, J, K, I, L, ;, and P.

In this case, generally the left hand controlls rotation along 3 axes, whereas the right hand controlls movement along the same 3 axes.  Keys can be changed in the InputHandler.java file, for instance, one can also set things up so that the left hand could control 3D movement, and the right hand 4D movement.
