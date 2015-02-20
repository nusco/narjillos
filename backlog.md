=================
Narjillos Backlog
=================

#Solid Persistence
* Serialize ancestry and experiment together in one file, not two separate files
* Automatically recover missing experiment from temp file
* Automatically purge experiment temp file
* Start without ancestry by default (to avoid OutOfMemory errors in the long run)
* "--serious" option to start with persistence, ancestry, and large heap

--Release 0.2.0

#Plants and Animals
* Encode color shift instead of hue
* Blue fibers give extra push
* Egg firing
* Egg firing distance is controlled by genes
* Green fibers get energy from the environment
* Externalize configurable parameters to YAML

#Intuitive Navigation
* Test track/untrack mouse commands with real user
* Limit panning inside space area
* Make central area of dish visible at a distance
* Visual effect when tracking/untracking

#Predators
* Fix sketchy collision detection
* Rewrite collision detection to be independent of max speed
* Narjillos point at other narjillos
* Narjillos eat other narjillos
* Red fibers damage the attacker
* Identifiable species (DNA hashes?)
* Decide direction based on other creatures' species

#Realistic Physics
* Rotation inertia
* Translation inertia
* Limit rotation speed?
* More realistic viscosity?
* Viscosity per segment?

#Advanced Body Plans
* Duplicate organs during mutation instead of mirroring them (to favour emergent complexity)
* "Skip" instruction in body plan
* "Repeat" instruction in body plan
* Whole-body size gene

#Ancestry Browser
* Ancestry Browser
* DNA diff during ancestry analysis

#Gaming UI
* Zoo mode (switch from narjillo to narjillo)
* Status bar
* Tutorial
* GUI
* Historical experiment stats (average lifespan, number of creatures, ...)
* View stats for followed narjillo (age, energy, times eaten, genome...)
* "About"/"Help" menus
* Save/load experiment from menu
* Visualize different speed settings
* Light on/off visual and sound effects
* Smoother contours when zooming in infrared mode
* Make food more visible in normal view
* Make food more visible in infrared view

#Packaged Application
* Native Mac app
* Native Windows app
* Run in a browser
* Package Java runtime with command-line program?

* Remove food
* Adaptive graphics (disable effects when framerate plummets)
* Narjillos eat eggs
* Encode CPU floatpoint precision in the experiment id (instead of using strictfp)
* Conservation of energy in entire dish (with sun to give more energy)
* Multiple environments in multiple processes (with migration)
* Different shapes for body segments
* Demiurge
* Brains
