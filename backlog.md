#Narjillos Backlog

###Species Analysis

Use clustering algorithms to automatically count the number of different "species" in the ecosystem.

* Count species with simple clustering algorithm
  > do this next  
  > test_parameter : 42  
  
  This one should use Levenshtein distances.

+ Cache distances for fast continuous clustering
+ Compare clustering performance with Levenshtein and SimHash distances
* Track number of species clusters in history (or during lab analysis if too slow)
- Track composition of species clusters in history
- Track multiple cluster groups with different sizes (genera/species/mutations)


###Basic Lab Analysis

Track historical data in experiment for offline analysis in a spreadsheet.

* Track historical data together with ancestry: eggs/creatures/food/clusters/etc.
* Run analysis with --history to dump a CSV file of history
+ Fail with explicit error if running ancestry analysis on a file without ancestry
- Generate lab script for packaged distribution
+ Warning in case of conflicting command-line arguments of main apps (like -s and experiment file used together)


###Self-Regulating Ecosystem

Encourage disruptive evolution, which should result in sympatric speciation.

If I don't get sympatric speciation after implementing this, then maybe I need
something more. Sexual reproduction, maybe?

* Atmosphere chemistry
* Narjillos produce a molecule (based on visible body qualities - metabolic rate?)
* Narjillos consume a molecule (faster reproduction? lower energy expenditure?)
* Ecological Niches (tweak chemistry to encourage speciation)
+ Local atmosphere (atmosphere composition is a map instead of a global value)
+ Atmosphere composition "heat map"


###Intuitive Navigation

Make it easier to move around the dish in a graphical run.

I should also test navigation with a first-time user.

+ Next/previous commands during demo
+ Visual effect when tracking/untracking
- Mini-map

###GUI

Make the program accessible to people who don't like to learn keyboard shortcuts.

* Status bar
* Save/load experiment from menu
+ Optionally save when quitting application
+ Command menus for light, speed, and so on
* Start new experiment from menu
+ "About"/"Help" menus
- Speed widget
- "Screensaver mode" (no menus or status bar)
- Light switches for normal/infrared light
+ Show historical data
- Show graphs for historical data
+ View stats for followed narjillo (age, energy, radius, times eaten, genome...)
- Tutorial
+ In-app configuration panel


###Advanced Body Plans

Give evolution more power to shape interesting creature bodies.

* "Loop" instruction in body plan
+ "Jump" instruction in body plan
+ "Call" instruction in body plan
- Duplicate organs during mutation instead of mirroring them (to favour emergent complexity)
- Different shapes for body segments


###Dish Edges

Constrain the "world" inside defined edges.

Right now, things can be placed anywhere, but only the central area of the dish is space-partitioned. The rest is "outer space". If we limit the world, we can make it all space-partitioned. The current space partitioning is OK-ish, but it would make it hard and expensive to implement generalized collision detection. At the moment, this is not a problem, because the only collision detection I have is "mouth colliding with food". But in the future, I want to have anything collide with anything.

After implementing this, I can probably remove the concept of "outer space" (the area outside the space-partitioned center of the dish).

* Kill narjillos who touch outer space
+ Limit panning to inner space (with some margin)
+ Auto-scroll viewport to stay within inner space


###Seasons

Cyclically vary the amount of food that spawns.

Studies show that evolution works best if there are enough resources (food), but not too many. The problem is that it's hard to know what "enough but not many" means. So I want to try this: food amount is cyclical. I'm hoping that along the way from "almost starving" to "economy of abundance", the system will hit a few evolutionary sweet spots.

* Seasons
* Seasonal cycle max, min and period can be configured in config.yaml


###Eye Candy

Smoother, nicer graphics.

- Moving mouths
- Independent eye pupils
+ Optimize graphics
+ Simpler senescence mechanism
+ Smoother contours when zooming in infrared mode
- Show heat cloud when zooming out in infrared mode
- Skip quickly over less interesting creatures in Demo Mode
+ Command-line argument to start without visual effects


###Packaged Application

Download-and-run user experience.

* Native Mac app
+ Native Windows app
+ Load narjillos.yaml (or .narjillos.yaml) from home, if present, instead of config.yaml
- Run in a browser
+ Fix permissions on distribution startup scripts (has problem starting in Ubuntu?)


###Predators (Complex Interactions 1)

Narjillos can eat each other.
This is a complex direct interaction that might pave the road to an arms race amongst species.
I should consider removing food by default after this is implemented.

* Fix sketchy collision detection (is this a bug?)
+ Rewrite collision detection to be independent of max speed
* Narjillos eat other narjillos (pick target at random)
- Narjillos eat eggs


###Fibers

Creature specialize in different survival strategies depending on the color of their organs.
This is another factor that can encourage disruptive evolution and speciation.

* Re-activate green fibers ("plant fibers", get energy from environment)
* Re-activate blue fibers ("runner fibers", generate more push when moving)
+ Add red fibers ("killer fibers", damage collided creatures)


###Ancestry Browser

Analyze DNA ancestry in separate program.
Takes a chain of DNA ancestors and dynamically shows specimen on the screen, with forward/back
buttons and the like.

* Ancestry Browser
+ "Back" button
- DNA diff during ancestry analysis


###Istincts (Complex Interactions 2)

Narjillos decide where to go based on their attraction/repulsion to other creatures and things.

This is another example of complex interactions. It might result in interesting behaviors and "smart" species.

* Species identification (based on DNA hashes?)
* Istinct genes: love, fear, hunger
- Visualize istinct directions
- Visualize istincts on other creatures when following a narjillo
* Decide ideal direction based on istinct genes
+ Attach istincts to all things – in particular, food


###Advanced Lab Analysis

Science-level analysis of experimental data.

+ Track advanced experimental data together with ancestry: avg lifetime/avg descendants/etc.
+ Measure creature/dish efficiency (somehow)
- Advanced clustering algorithm (based on Shannon's theory to evaluate gene distribution)
+ Advanced ancestry analysis (study related papers)
+ Store historical data in a database rather than in memory
+ Optionally move entire persistence to a database
- Encode CPU floatpoint precision in the experiment id (rather than use strictfp)


###Sexual Reproduction

Creatures mate and generate mixed-DNA offsprings.

A lot of things to decide here. do I really need this stuff to get speciation? Probably not – so I'll leave it as a low priority for now.

* Basic Sexual Reproduction (just to set up for Assortative Mating)
* Assortative Mating (to encourage speciation)
+ Species clustering control reproductive success? (to keep species apart)
- Diploid creatures?


###Realistic Physics

More realistic behavior of body to avoid body shapes that "exploit" the current naive physics.

+ Fix "tail wiggles dog" effect
+ Rotation inertia (but check comments in physics engine - it may break previous assumptions)
+ Translation inertia (see above)
+ Limit rotation speed?
- Realistic viscosity?
- Viscosity per segment?


###Flexible Genes

More qualities of the creatures are determined by genes instead of being hard-coded.

+ Wave beat ratio is genetically determined
+ Max lifespan is geneticaly determined (within a limit)
- Lateral viewfield is genetically determined
- Growth rate is genetically determined? (Takes energy?)
- Egg incubation time is genetically determined? (Makes sense if egg contains green fibers)
- Adult body size is genetically determined


---


*Project Goals*

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

*Some crazy ideas which I might never get around to*

* Multiple environments in multiple processes (with migration)
* Creatures have neural networks for brains
* Demiurge (an entity that dynamically tweaks the environment to maximize evolutionary speed)
