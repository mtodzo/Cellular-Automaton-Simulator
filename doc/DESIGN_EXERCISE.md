# Cell Society Design 

# Classes
## Simulation
This super class will be abstract and will be inherited by each of the cell simulations themselves: Game of Life, Predator-Prey, SpreadingFire, and Segregation. It will have methods reset(), getWidth(), getHeight(), checkStates, and updateStates.
##Cell Occupants 
This is another superclass that will be abstract. It represents each cell itself and contains the current state, next state, and location. Its subclasses will be the different possible objects in cells (i.e. fire, tree, shark, fish, etc). It has getters and setters for each of these.
##Subclasses
The subclasses for each of these two superclasses will inherit the parameters and override the methods in manners specific to their game settings.