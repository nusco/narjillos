#===================
# Narjillos Backlog
#===================

##Basic Lab Analysis
* Make ancestry analysis an option of the lab program (--ancestry)
* Fail with explicit error if running ancestry analysis on a file without ancestry
* Track basic experimental data together with ancestry: eggs/creatures/food/etc.
* Run analysis with --history to dump a CSV file of history
* Generate lab script for packaged distribution
* Count species with simple clustering algorithm
* Track genera/species/mutations separately (different cluster size)
* Report number of species on the console/history (or during lab analysis if too slow)

##Self-Regulating Ecosystem
* Atmosphere chemistry
* Narjillos produce a molecule (based on visible body qualities - metabolic rate?)
* Narjillos consume a molecule (faster reproduction? lower energy expenditure?)
* Ecological Niches (tweak chemistry to encourage speciation)
* (did I get sympatric speciation yet? if not, then maybe I need sexual reproduction)

##Advanced Body Plans
* "Jump" instruction in body plan
* "Call" instruction in body plan
* "Loop" instruction in body plan
* Duplicate organs during mutation instead of mirroring them (to favour emergent complexity)
* Different shapes for body segments

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
* Species identification (based on DNA hashes?)
* Istinct genes: love, fear, hunger
* Visualize istinct directions
* Visualize istincts on other creatures when following a narjillo
* Decide ideal direction based on istinct genes
* Narjillos follow istinct direction (plus food?)
* Atmosphere map with local chemistry

##Intuitive Navigation
* Forward/Back commands during demo
* Visual effect when tracking/untracking
* Mini map?
* (Test navigation with first-time user)

##Gaming UI
* Status bar
* Save/load experiment from menu
* Optionally save when quitting application
* Start new experiment from menu
* "About"/"Help" menus
* Speed widget
* Light switches for normal/infrared light
* Historical experiment stats (average lifespan, number of creatures, ...)
* View stats for followed narjillo (age, energy, radius, times eaten, genome...)
* Tutorial
* In-app configuration

##Advanced Lab Analysis
* Track advanced experimental data together with ancestry: avg lifetime/avg descendants/etc.
* Measure creature/dish efficiency (somehow)
* Advanced clustering algorithm (based on Shannon's theory to evaluate gene distribution)
* Advanced ancestry analysis (study related papers)
* Store historical data in a database rather than in memory
* Optionally move entire persistence to a database
* Encode CPU floatpoint precision in the experiment id (rather than use strictfp)

##Packaged Application
* Native Mac app
* Native Windows app
* Run in a browser
* Fix permissions on distribution startup scripts (has problem starting in Ubuntu)
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
* Independent eye pupils
* Load narjillos.yaml (or .narjillos.yaml) from home, if present, instead of config.yaml
* Change mouth graphics?
* Optimize graphics again
* Simpler senescence mechanism
* Report conflicts in command-line arguments (like -s and file used together)
* Smoother contours when zooming in infrared mode
* Show heat cloud when zooming out in infrared mode
* Skip quickly over less interesting creatures in Demo Mode
* Command-line argument to start without visual effects
* Narjillos eat eggs
* Conservation of energy in entire dish (with an external source of energy)

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
