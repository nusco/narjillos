##Backlog

This backlog is organized by "type of user". Inside each sections, features are roughly prioritized.
I generally work on the high-priority features, but I pick the "user" that tickles my fancy each day.

###Physicist

* Viscosity for very fast speeds (makes the current watchdog unnecessary) °
* Rewrite collision detection to be independent of max speed

###Speed Demon

* Pre-store angle when creating polar vector °°
* Lazily reverse-calculate x and y in polar vectors °°
* Optimize physics engines by passing angles instead of recalculating angles °°
* Fix code for growing organs and bodies (currently weird and probably worth optimizing) °°

###Gamer

* Real arguments parsing (use --no-persistence together with other switches) °
* Externalize configurable parameters to YAML °
* Command-line argument to disable graphic effects on slow computers
* Downloadable build
* Tutorial
* Nice status bar
* GUI
* Windows start batch
* Try scrolling/panning commands under Linux
* Check speed under Linux

###Genetist

* Ancestry Browser
* "Skip" instruction in body plan
* "Repeat" instruction in body plan
* DNA diff during ancestry analysis
* Separate RGB color genes
* Whole-body size gene

###Ethologist

* Narjillos eat eggs
* Narjillos eat each other

###Lab Technician

* Single zipped savefile for both experiment and genepool °°
* View locked narjillo (age, energy, times eaten, genome...)
* Check git commit on experiment startup
* Automatically purge experiment temp file
* Automatically recover missing experiment from temp file
* Encode CPU floatpoint precision in the experiment id (instead of using strictfp)

###Ecologist

* Conservation of energy in entire dish (with sun to give more energy)
* Multiple environments in multiple processes (with migration)
* Plant-like creatures instead of food (minimal energy expenditure? photosyntesis?)

###Artist

* Give a sense of movement when following a locked-on Narjillo (speckles in liquid?) °°
* Light on/off visual and sound effects
* Smoother contours when zooming in infrared mode
* Make food more visible in normal view
* Make food more visible in infrared view

###Theologist

* Demiurge

##Neurologist

* Brains

##Daydreamer

* Trigger evolutionary arms race
