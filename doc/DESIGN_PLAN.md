### Introduction

We are trying to write a program that animates a 2D grid CA simulation. We want to be able to model a variety of cellular automaton (CA) replication and destruction scenarios, i.e. game of life and spreading fire. The program should be flexible enough to adapt to different styles of simulation, different original configuration, and different configuration update rules. Our primary design goals are to make the program easily adaptable so that new simulations and rules can be configured quickly and easily. Initially, we want our program to handle the four base simulations and make it easy for the user to input new configurations. Some open aspects of our primary architecture will be setting simulation parameters and switching simulations. Some closed aspects of our primary architecture will be applying rules to movement of cells, to updating the states of specific cell occupants, and to moving a simulation to the next generation. Later on, we would hope to extend our flexibility to include a variety of simulation types and other forms of user input. We also hope to use inheritance to improve the quality of our code with DIY and DRY design concepts.

### Overview

We are planning on having one class, two superclasses, and 12 subclasses. The Setup class will deal with the GUI and will handle user input. The Simulation superclass will be an abstract class that will provide methods for checking and updating the status of cell occupants as well as getters and setters for the size of the simulation. The subclasses of Simulation will be GameOfLife, Segregation, PredatorPrey, and SpreadingFire. The CellOccupant superclass will be an abstract class that will provide methods for getting and setting current state, next state, current location, and next location for various cell occupants. The subclasses of CellOccupant will be Shark, Fish, Cells, Community1, Community2, Tree, Fire, and Blank.

![UML](IMG_0848.jpeg)

Each simulation has specified CellOccupant objects and a Simulation object that will provide the proper rules for updating cells. 

### User Interface

This section describes how the user will interact with your program (keep it very simple to start). It should describe the overall appearance of program's user interface components and how users interact with these components (especially those specific to your program, i.e., means of input other than menus or toolbars). It should also include one or more pictures of the user interface (these pictures can be hand drawn and scanned in, created with a standard drawing program, or screen shots from a dummy program that serves as a exemplar). Finally, it should describe any erroneous situations that are reported to the user (i.e., bad input data, empty data, etc.). This section should go into as much detail as necessary to cover all your team wants to say.

### Design Details

### Design Considerations

### Team Responsibilities