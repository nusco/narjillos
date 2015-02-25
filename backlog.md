#===================
# Narjillos Backlog
#===================

##Plants and Animals
* (refactor energy management)
* (balance energy management in the presence of green fibers)
* Egg firing costs energy
* Egg firing distance is controlled by genes
* (change policy for deciding when to fire egg)

##Predators
* Fix sketchy collision detection
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
* Test track/untrack mouse commands with first-comer
* Visual effect when tracking/untracking

##Flexible Genes
* Wave beat ratio is genetically determined
* Max lifespan is geneticaly determined (within a limit)
* Lateral viewfield is genetically determined?
* Growth rate is genetically determined? (Takes energy?)
* Egg incubation time is genetically determined? (Makes sense if egg contains green fibers)
* Adult body size is genetically determined

##Realistic Physics
* Rotation inertia
* Translation inertia
* Limit rotation speed?
* Realistic viscosity?
* Viscosity per segment?

##Gaming UI
* Status bar
* Speed widget
* Light switches for normal/infrared light
* Historical experiment stats (average lifespan, number of creatures, ...)
* View stats for followed narjillo (age, energy, times eaten, genome...)
* "About"/"Help" menus
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
* Duplicate organs during mutation instead of mirroring them (to favour emergent complexity)
* "Skip" instruction in body plan
* "Repeat" instruction in body plan
* Different shapes for body segments

##Grab Bag
* Simpler senescence mechanism
* Demo mode (switch from narjillo to narjillo)
* Narjillos eat eggs
* Smoother contours when zooming in infrared mode
* Externalize configurable parameters to YAML
* Adaptive graphics (disable effects when framerate plummets)
* Encode CPU floatpoint precision in the experiment id (instead of using strictfp)
* Conservation of energy in entire dish (with sun to give more energy)

##Crazy Ideas
* Multiple environments in multiple processes (with migration)
* Demiurge
* Brains
