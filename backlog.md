#===================
# Narjillos Backlog
#===================

* Run smaller ecosystem when starting with "narjillo" (the large one is too demanding on slow computers)

##Smart Reproduction
* Firing eggs costs energy
* Egg firing distance is controlled by genes

##Predators (Complex Interactions 1)
* Fix sketchy collision detection (is this a bug?)
* Rewrite collision detection to be independent of max speed
* Narjillos eat other narjillos (pick target at random)
* Red fibers damage the attacker
* Remove food?

##Self-Regulating Ecosystem
* Elements in environment
* Narjillos produce a molecule (based on visible body qualities - use DNA hashes?)
* Narjillos consume a molecule (faster reproduction? lower energy expenditure?)
* Ecological Niches (tweak chemistry to encourage speciation)

##Istincts (Complex Interactions 2)
* Identifiable species (DNA hashes)
* Istinct genes: love, fear, hunger
* Visualize istinct directions
* Visualize istincts on other creatures when following a narjillo
* Decide ideal direction based on istinct genes
* Narjillos follow istinct direction (plus food?)

##Dish Edges
* Kill narjillos who touch outer space
* Limit panning to inner space (with some margin)
* Auto-scroll viewport to stay within inner space
* (Remove the concept of outer space altogether)

##Intuitive Navigation
* Visual effect when tracking/untracking
* Scale zoom based on creature size and window size when following
* Position map?
* (Test navigation with first-comer)

##Species Tracker
* Clustering algorithm to count species (any algo to do this with variable num of clusters *and* variable-length DNA?)
* Report number of species on the console (or do it when analizing ancestry if too slow)

##Sexual Reproduction
* (a lot of things to decide. do I really need this stuff to get speciation? leave it for later in case I don't)
* Basic Sexual Reproduction (just to set up for Assortative Mating)
* Assortative Mating (this should get me speciation)
* Species clustering control reproductive success? (to keep species apart)
* Diploid creatures? (as a nice-to-have)

##Realistic Physics
* Fix "tail wiggles dog" effect?
* Rotation inertia (but check comments in physics engine - it may break previous assumptions)
* Translation inertia (see above)
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
