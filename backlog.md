##Backlog

This backlog is organized by "type of user". Inside each sections, features are roughly prioritized.
I generally work on the high-priority features, but I pick the "user" that tickles my fancy each day.

###Physicist

* Fix flickering °°

  _creature to check: {090_065_154_010_089_080_141}{067_085_239_111_061_007_013}{139_098_147_209_088_160_176}{115_115_132_022_062_227_036}{224_247_251_216_231_232_137}{067_117_115_107_223_148_243}{018_017_212_069_242_128_180}{076_036_057_179_008_130_025}{219_245_210_188_115_122_020}_

* Smooth, inertial targeting or target hysteresis (to avoid skewing flickering when target is on opposite side) °°
* Remove "tail wiggling dog" effect (requires conservation of MoI during update in vacuum) °°
* Realistic radius calculation (for better moment of inertia) °°

###Speed Demon

* Optimize physics engine (in particular look at calculation of center of mass) °°°
* Only update Organ cache when necessary °°°
* Space Partitioning for fast thing searches °°
* Make FPS parametric to improve TPS on slower machines

###Gamer

* Update documentation
* Try scrolling/panning commands and speeds under Linux
* Externalize all configurable parameters to JSON files
* Parameterize scroll/pan speeds
* Save Petri dish
* Load Petri dish

###Genetist

* View Narjillo's evolutionary path °°
* Find a good way to deal with atrophy (see FIXME) °°
* Narjillo's skewing is genetically determined °°
* Whole-body size gene
* Mutation rate is controlled by genes (this is what happens in nature. tends to freeze evolution?)
* DNA diff during ancestry analysis

###Etologisty

* Eggs °°
* "Egg-firing" °
* Additional cost for long-distance egg-flinging °
* Distance of firing is determined by genes °
* Narjillos spend energy to reproduce
* Narjillos decide how much energy to give to descendants

###Lab Technician

* Unify experiment and petri (make it easy to turn graphics on off with a "light switch) °°°
* Slow motion setting °°
* "Pause" setting °°
* Lock on single Narjillo °
* Bench with stats
* "Real Time" and "Fast" slider
* Select creature
* Export creature
* "Most Prolific" button
* View creature energy
* Make food visible in normal and/or infrared view

###Ecologist

* Bug: PetriDish can miss notifications from pond (in particular, Narjillos dying when they spawn with very low or no energy) °°
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
* "Fears" gene
* "Wants to eat" gene

###Artist

* Better death animation (right now the bending is not visible) °
* Fade out mouth/eye during death °
* Birth animation °
* Smooth out body during death in infrared
* Give a sense of movement when following a locked-on Narjillo (speckles in liquid?)

##Neurologist

* Brains with behaviours

##Daydreamer

* Trigger evolutionary arms race
