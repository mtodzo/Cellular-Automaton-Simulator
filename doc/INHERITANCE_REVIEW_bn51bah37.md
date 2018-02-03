### Part 1:
1. Our UI components both hide the creation of the grid visualization from the other classes as well as the creation of the various buttons, sliders, etc. 
2. Ben has a grid class that instantiates a subclass of a Container object, which is an abstract base class that specifies behaviors for drawing the changing grid object itself. One particular implementation of this class is a SquareContainer, which draws a grid made up of square shapes. This could be extended to handle different types of shapes in the future. Belanie does not currently have any inheritance hierarchies but thinks Ben’s idea is very smart and would like to implement something similar.
3. Ben suggests that the polymorphism present in his implementation follows from the inheritance hierarchy present above: since the abstract Container class specifies an abstract behavior called drawContainer(), this method can be called on any subclass, which is an instance of polymorphism. Belanie has not set up an inheritance relationship yet so she does not currently have polymorphism, but would like to implement it in order to take care of the size and shape of simulation cells. 
4. Belanie is reading in .properties files as well as .xml files in order to set buttons and create arrays of CellOccupants, so the exceptions that may arise are all related to bad input. This could include files that are formatted incorrectly or missing files. She will handle these errors by throwing an exception that indicates that the file is invalid. Ben seconds much of what Belanie has said and is handling his exceptions similarly.
5. Belanie thinks that her design is good, because it does not require interaction with other classes except in the update call during the game loop. In this way, it is not greatly affected by errors in other portions of the program. Ben agrees with all of this and feels his program has similar strong suits; in addition, certain components of the UI are flexible, and it would be easy to add new buttons, change the style of any component, and update the text displayed on screen.

### Part 2:
1. For both of us, the only interaction we have with other classes is an update call on the grid’s display, based on the rules logic for a certain simulation (which are contained in separate modules in the codebase). This makes our classes relatively decoupled.
2. Our dependencies are based on the other class’s behaviors not their implementations. We merely expect the update call to provide an updated grid and rely on its internal behavior to do so.
3. We feel as though our dependency is minimized.
4. Ben feels as though, since he currently only has one implementation of the abstract base class, there is nothing to compare or have in common. The reason the inheritance hierarchy is necessary is in anticipation of future requirements. Belanie does not currently have superclasses and subclasses in her implementation but would like to implement a similar structure to Ben.

### Part 3:
1. Use Cases:

 	* A user wants to pause the simulation
	* A user wants to change to a different simulation
	* A user wants to add a new file to their simulation
	* A user wants to change the number of cells in their grid.
	* A use wants to step through the simulation.
2. Belanie is most excited to work on adding functionality to the buttons. Ben is most excited to see his work come together with his teammates’, when they merge their code together and get a first version up and running.
3. Belanie is most worried about the feature that involves opening and uploading new configurations for simulations. I’m most worried about handling the logic that walks the user through the simulation one step at a time.
