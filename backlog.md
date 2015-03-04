#===================
# Narjillos Backlog
#===================

When followed narjillo died:
Exception in thread "Thread-6" java.util.ConcurrentModificationException
	at java.util.LinkedHashMap$LinkedHashIterator.nextNode(LinkedHashMap.java:711)
		at java.util.LinkedHashMap$LinkedKeyIterator.next(LinkedHashMap.java:734)
			at java.util.AbstractCollection.addAll(AbstractCollection.java:343)
				at org.nusco.narjillos.ecosystem.Ecosystem.getThings(Ecosystem.java:51)
					at org.nusco.narjillos.utilities.Locator.findThingAt_WithLabel(Locator.java:39)
						at org.nusco.narjillos.utilities.Locator.findNarjilloAt(Locator.java:32)
							at org.nusco.narjillos.utilities.ThingTracker.tick(ThingTracker.java:27)
								at org.nusco.narjillos.PetriDish$2.run(PetriDish.java:97)

##Plant and Animals
* (refactor energy management)
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
* Scale based on creature size and window size when following
* (Test navigation with first-comer)

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
* Encode CPU floatpoint precision in the experiment id (instead of using strictfp)
* Conservation of energy in entire dish (with sun to give more energy)

##Crazy Ideas
* Multiple environments in multiple processes (with migration)
* Demiurge
* Brains
