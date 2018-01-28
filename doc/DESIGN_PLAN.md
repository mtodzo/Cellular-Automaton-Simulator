### Introduction

We are trying to write a program that animates a 2D grid CA simulation. We want to be able to model a variety of cellular automaton (CA) replication and destruction scenarios, i.e. game of life and spreading fire. The program should be flexible enough to adapt to different styles of simulation, different original configuration, and different configuration update rules. Our primary design goals are to make the program easily adaptable so that new simulations and rules can be configured quickly and easily. Initially, we want our program to handle the four base simulations and make it easy for the user to input new configurations. Some open aspects of our primary architecture will be setting simulation parameters and switching simulations. Some closed aspects of our primary architecture will be applying rules to movement of cells, to updating the states of specific cell occupants, and to moving a simulation to the next generation. Later on, we would hope to extend our flexibility to include a variety of simulation types and other forms of user input. We also hope to use inheritance to improve the quality of our code with DIY and DRY design concepts.

### Overview
This section serves as a map of your design for other programmers to gain a general understanding of how and why the program was divided up, and how the individual parts work together to provide the desired functionality. As such, it should describe specific components you intend to create, their purpose with regards to the program's functionality, and how they collaborate with each other. It should also include a picture of how the components are related (these pictures can be hand drawn and scanned in, created with a standard drawing program, or screen shots from a UML design program). This section should discuss specific classes, methods, and data structures, but not individual lines of code.
### User Interface

### Design Details

### Design Considerations

### Team Responsibilities