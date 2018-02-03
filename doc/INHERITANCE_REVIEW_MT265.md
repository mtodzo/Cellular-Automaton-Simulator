#Part 1
* An implementation decision that is encapsulating is that the Grid, which displays the actual simulation, does not know about the rules of the specific simulation, but only where each occupant resides, and its corresponding colors.
* The Simulation superclass is inherited by each specific simulation type. Each then implements its own simulation's rules for determining its the state of its cells.
* The Grid class will leave the time zero configuration open since that will be specified by the user, but the checking for neighbors feature will be closed since the definition of neighbor (adjacent/touching cell) will not change.
* An exception that could occur is that the user will attempt to place an initial cell in a position that is outside the grid coordinates. This will return an IndexOutOfBounds error and display an error message on screen for the user.
* I think the design is good because it applies inheritance and does it's best to implement DIY features. My "good" is in comparison to my Brick Breaker game which implemented very few classes and did not use inheritance, so it is a fairly low bar.

#Part 2
* The simulation classes are directly linked to the corresponding cell occupants class. The cell occupants subclasses hold their states and positions.
* These classes are not too heavily dependent on the cell occupant classes since they rely primarily on the information held by these classes rather than function implementations.
* These dependencies are can be limited by maintaining this simple structure of using mostly information retrieval.

#Part 3
use cases:
* changing the state of a specific element
* changing specifications on likelihood of tree catching fire
* Adding a new state of Bush to the Spreading Fire simulation