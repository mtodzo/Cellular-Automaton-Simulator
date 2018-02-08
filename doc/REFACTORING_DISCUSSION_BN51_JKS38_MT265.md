### Duplication Refactoring

We do not have a whole lot of duplication aside from some minor errors in using string literals instead of constant values in the Setup class and using two loops instead of one in the WrapAroundGrid class. We decided not to change the portion in Setup where the program recommended that we replace some string literals with constants, however these string literals were referring to the .properties file, so we felt as though it was not necessary to change them.

### Checklist Refactoring

Communication:
We replaced "magic numbers" with well-named constants. Some of the "magic numbers" that are currently in occupant configuration settings are going to be changed later so that the user can decide the values. Regarding unused inputs or variables, we will go through our classes and remove them at the very end.

Flexibility:
We moved some functionality in DisplayGrid into a different method to make the code more readable and we made sure to keep the left side in our ArrayList declarations more general.
* To Do - we will continue to refactor our different occupant classes in order to make methods shorter and more readable.
* To Do - we will refactor the createButtons method in Setup to get rid of some of the duplicate code for creating different buttons, but we haven't quite figured out how we want to do that yet.

Modularity:
In order to reduce the dependencies in the Setup class we are going to make a Button class that handles creating buttons and their functionality. Also, we are still working on the XML class so we will fix the modularity issues after it is functioning.

### General Refactoring

We decided to create an interface in order to protect the Grid class.