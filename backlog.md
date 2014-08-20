This is my backlog for the Narjillo project.
It's divided in sections by "type of user". Inside each sections, features are roughly prioritized.
I generally work on the high-priority features, but I pick the "user" that tickles my fancy each day.

###Physicist

* Smooth, inertial targeting or target hysteresis (to avoid vibration when target is on opposite side) °°°
* Fix rotation-related flickering °°°
* Introduce viscosity (to improve the poor creatures' mobility) °°°
* Tweak rotation vs. translation °°°
* Find a good way to deal with atrophy (see FIXME) °°
* Tweakable mass constant °
* Bug: PetriDish can miss notifications from pond (in particular, Narjillos dying when they spawn with very low or no energy) °°
* Check measure units: why do rotations/translations/energy require such wildly different multipliers? °
* Remove "tail wiggling dog" effect (requires conservation of MoI during update in vacuum)

###Speed Demon

* Optimize physics engine (in particular look at calculation of center of mass) °°°
* Only update Organ cache when necessary °°°
* Space Partitioning for fast thing searches °°
* Find out why even a petri dish of {0} creatures slows down (apparently creatures become "heavier" to tick)
* Space Partitioning runs in a separate thread
* Make FPS parametric to improve TPS on slower machines

###Gamer

* Try scrolling/panning commands and speeds under Linux °°
* Parameterize scroll/pan speeds °
* Externalize all configurable parameters to JSON files
* Save Petri dish
* Load Petri dish

###Genetist

* Narjillo's skewing is genetically determined °°
* Whole-body size gene
* View Narjillo's evolutionary path
* Mutation rate is controlled by genes (this is what happens in nature. tends to freeze evolution?)
* Narjillo's color is an expression of its behaviour (amplitude, phase, etc)
* DNA analyzer program
* DNA diff program (based on DNA analyzer)
* Sexual reproduction
* Sexual reproduction is optional
* Prevent DNA strands that are too different from mixing to favor speciation? (maybe bad idea b/c of deleterious mutations in small population)

###Ostetrician

* Eggs °°
* "Egg-firing" °
* Additional cost for long-distance egg-flinging °
* Distance of firing is determined by genes °
* Narjillos spends energy to reproduce
* Narjillos decide how much energy to give to descendants

###Lab Technician

* Fix non-repeatable experiments (also check across "experiment" and "petri") °°°
* Slow motion setting °°
* "Paused" setting °°
* Lock on single Narjillo °
* Bench with stats
* "Real Time" and "Fast" slider
* Light switch
* Select creature
* Export creature
* "Most Prolific" button
* View creature energy
* Make food shine at a distance? or in infrared view?

###Ecologist

* Spawn Narjillos with random direction (to avoid "swarming in same direction" effect on some races - or, do I want to keep it?)
* Conservation of energy in entire pond (with sun to give more energy)
* Distribute things in a circle instead of a square
* Multiple environments in multiple processes (with migration, still deterministic - but how?)
* Plants (food that evolves)
* Gamma rays (cause mutation)
* Beta rays (cause old age and death)
* Controllable food rate
* Controllable food calories

###Ethologist

* Narjillos eat each other °°
* Narjillo updates food item when not in range anymore
* "Attracted to" gene (uses other Narjillo's colors to select most desirable mate)
* "Fears" gene (like "attracted to")
* "Wants to eat" gene (like "attracted to")

###Artist

* Better death animation (right now the bending is not visible) °
* Don't make eye transparent (it makes it hard to gauge energy; make it opaque or give it a white background) °
* Fade out mouth/eye during death °
* Birth animation °
* Smooth out body during death in infrared
* Check multithreading for reactive animations and maximum reactivity
* Give a sense of movement when following a locked-on Narjillo
* Speckles in liquid (or some other effect)

##Neurologist

* Brains with behaviours

##Daydreamer

* Trigger evolutionary arms race
