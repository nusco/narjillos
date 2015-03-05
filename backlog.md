#===================
# Narjillos Backlog
#===================

* Fix closing application when you close the Petri window

##Plant and Animals
* New rules to decide when to lay an egg
* Egg firing costs energy (refuse to lay egg if not enough energy)
* Egg firing interval is controlled by genes
* Egg firing distance is controlled by genes

##Predators
* Fix sketchy collision detection (bug?)
* Rewrite collision detection to be independent of max speed
* Narjillos point at other narjillos
* Narjillos eat other narjillos
* Red fibers damage the attacker
* Identifiable species (DNA hashes?)
* Decide direction based on other creatures' species
* Remove food

##Dish Edges
* Kill narjillos who touch outer space
* Limit panning to inner space (with some margin)
* Auto-scroll viewport to stay within inner space
* (Remove the concept of outer space altogether)

##Intuitive Navigation
* Visual effect when tracking/untracking
* Scale zoom based on creature size and window size when following
* (Test navigation with first-comer)

##Realistic Physics
* Fix "tail wiggles dog" effect?
* Rotation inertia
* Translation inertia
* Limit rotation speed?
* Realistic viscosity?
* Viscosity per segment?

##Flexible Genes
* Wave beat ratio is genetically determined
* Max lifespan is geneticaly determined (within a limit)
* Lateral viewfield is genetically determined?
* Growth rate is genetically determined? (Takes energy?)
* Egg incubation time is genetically determined? (Makes sense if egg contains green fibers)
* Adult body size is genetically determined

##Gaming UI
* Status bar
* Speed widget
* Light switches for normal/infrared light
* Historical experiment stats (average lifespan, number of creatures, ...)
* View stats for followed narjillo (age, energy, times eaten, genome...)
* "About"/"Help" menus
* Toggle effects with less obvious combination (CMD+E, or similar)
* Save/load experiment from menu
* Tutorial

##Packaged Application
* Native Mac app
* Native Windows app
* Run in a browser
* Package Java runtime with command-line program?

##Ancestry Browser
* Ancestry Browser
* DNA diff during ancestry analysis

##Advanced Body Plans
* Encode bodyplan instructions with modulo instead of bit checking
* "Back" instruction in body plan
* Duplicate organs during mutation instead of mirroring them (to favour emergent complexity)
* "Skip" instruction in body plan
* "Repeat" instruction in body plan
* Different shapes for body segments

##Grab Bag
* Simpler senescence mechanism
* Report conflicts in command-line arguments (like -s and file used together)
* Demo mode (switch from narjillo to narjillo)
* Narjillos eat eggs
* Smoother contours when zooming in infrared mode
* Externalize configurable parameters to YAML
* Adaptive graphics (disable effects when framerate plummets)
* Encode CPU floatpoint precision in the experiment id (rather than use strictfp)
* Conservation of energy in entire dish (with sun to give more energy)

##Crazy Ideas
* Daisyworld-like ecosystem with chemicals influencing reproduction rate
* Multiple environments in multiple processes (with migration)
* Demiurge
* Brains
