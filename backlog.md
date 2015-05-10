#===================
# Narjillos Backlog
#===================

##Advanced Body Plans
* Reintroduce mutations that insert/delete chromosomes
* "Skip" instruction in body plan
* "Jump" instruction in body plan
* "Call" instruction in body plan
* "Loop" instruction in body plan
* Duplicate organs during mutation instead of mirroring them (to favour emergent complexity)
* Different shapes for body segments

##Species Tracker
* Clustering algorithm to count species
* Report number of species on the console (or do it when analizing ancestry if too slow)

##Self-Regulating Ecosystem
* Atmosphere chemistry
* Narjillos produce a molecule (based on visible body qualities - metabolic rate?)
* Narjillos consume a molecule (faster reproduction? lower energy expenditure?)
* Ecological Niches (tweak chemistry to encourage speciation)
* (did I get sympatric speciation yet? if not, then maybe I need sexual reproduction)

##Dish Edges
* Kill narjillos who touch outer space
* Limit panning to inner space (with some margin)
* Auto-scroll viewport to stay within inner space
* (Remove the concept of outer space altogether)

##Predators (Complex Interactions 1)
* Fix sketchy collision detection (is this a bug?)
* Rewrite collision detection to be independent of max speed
* Narjillos eat other narjillos (pick target at random)
* Remove food?

##Ancestry Browser
* Ancestry Browser
* DNA diff during ancestry analysis

##Fibers
* Re-activate green and blue fibers
* Red fibers damage the attacker

##Istincts (Complex Interactions 2)
* Identifiable species (DNA hashes)
* Istinct genes: love, fear, hunger
* Visualize istinct directions
* Visualize istincts on other creatures when following a narjillo
* Decide ideal direction based on istinct genes
* Narjillos follow istinct direction (plus food?)

##Intuitive Navigation
* Visual effect when tracking/untracking
* Position map?
* (Test navigation with first-time user)

##Gaming UI
* Status bar
* Speed widget
* Light switches for normal/infrared light
* Historical experiment stats (average lifespan, number of creatures, ...)
* View stats for followed narjillo (age, energy, radius, times eaten, genome...)
* "About"/"Help" menus
* Save/load experiment from menu
* Tutorial

##Packaged Application
* Native Mac app
* Native Windows app
* Run in a browser
* Package Java runtime with command-line program?

##Sexual Reproduction
* (a lot of things to decide. do I really need this stuff to get speciation? leave it for later in case I don't)
* Basic Sexual Reproduction (just to set up for Assortative Mating)
* Assortative Mating (to encourage speciation)
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

##Grab Bag
* Fix memory leak
* Skip quickly over less interesting creatures in Demo Mode
* Show heat cloud when zooming out in infrared mode
* Simpler senescence mechanism
* Report conflicts in command-line arguments (like -s and file used together)
* Command-line argument to start without visual effects
* Narjillos eat eggs
* Smoother contours when zooming in infrared mode
* Encode CPU floatpoint precision in the experiment id (rather than use strictfp)
* Conservation of energy in entire dish (with sun to give more energy)

##Epic Goals
* Complex Interactions
* Self-Regulating Ecosystem
* Ecological Niches
* Disruptive Selection?
* Sexual Reproduction
* Assortative Mating
* Sympatric Speciation
* Asymmetrical Arms Races
* Broad Usability
* The "Wow" factor?

##Crazy Ideas
* Multiple environments in multiple processes (with migration)
* Neural networks for brains
* Demiurge
