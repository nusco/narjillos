#===================
# Narjillos Backlog
#===================

##Species Tracker
* Fix DNA length?
* Identifiable species (DNA hashes)
* Clustering algorithm to count species
* Report number of species on the console

##Smart Reproduction
* New rules to decide when to lay an egg (min interval + energy check)
* Firing eggs costs energy
* Egg firing distance is controlled by genes

##Chemistry
* Elements in environment (O, N)
* Narjillos produce an element (based on visible body qualities - use DNA hashes?)
* Narjillos consumes an element to reproduce faster (see above)
* Tweak chemistry to encourage speciation

##Istincts
* Istinct genes: love, fear, hunger
* Visualize istinct directions
* Visualize istincts on other creatures when following a narjillo
* Decide ideal direction based on istinct genes
* Narjillos follow istinct direction (plus food)

##Predators
* Fix sketchy collision detection (bug?)
* Rewrite collision detection to be independent of max speed
* Narjillos eat other narjillos
* Red fibers damage the attacker
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

##Sexual Reproduction
* (a lot of stuff to define)
* Species clustering control reproductive success? (to keep species apart)
* Diploid creatures?

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

##Ancestry Browser
* Ancestry Browser
* DNA diff during ancestry analysis

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

##Advanced Body Plans
* Encode bodyplan instructions with modulo instead of bit checking
* "Back" instruction in body plan
* Duplicate organs during mutation instead of mirroring them (to favour emergent complexity)
* "Skip" instruction in body plan
* "Repeat" instruction in body plan
* Different shapes for body segments

##Grab Bag
* Show things as blips when zooming out in infrared mode
* Simpler senescence mechanism
* Report conflicts in command-line arguments (like -s and file used together)
* Demo mode (switch from narjillo to narjillo)
* Narjillos eat eggs
* Smoother contours when zooming in infrared mode
* Externalize configurable parameters to YAML
* Adaptive graphics (disable effects when framerate plummets)
* Encode CPU floatpoint precision in the experiment id (rather than use strictfp)
* Conservation of energy in entire dish (with sun to give more energy)
* Fix memory leak

##Epic Goals
* Complex Interactions
* Self-Regulating Ecosystem
* Ecological Niches
* Disruptive selection?
* Sexual Reproduction
* Assortative Mating
* Sympatric Speciation
* Asymmetrical Arms Races
* Wide Usability
* The "Wow" factor?

##Big Goals Crazy Ideas
* Multiple environments in multiple processes (with migration)
* Neural networks for brains
* Demiurge
