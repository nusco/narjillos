This is my backlog for the Narjillo project. It's divided in sections by "type of user". Inside each sections, features are roughly prioritized.
I generally work on the high-priority features, but I pick the "user" that I want to be depending on my mood of the day.



understand what happens with {032_004_218_209_168_210}{235_005_030_179_208_073}{149_028_102_041_033_239}
emerging at 930000 in experiment 6f157d39f3f048c864600730048981409c840661:1148303094 - they seem to be alive,
but they are invisible in a normal petri dish. what's happening? they took over the pond (50+ of them)

same with {230_003_090_211_124_158}, in experiment 6f157d39f3f048c864600730048981409c840661:1954960563 - emerging quite soon at tick 80000.

these seem to be the 0-length, 0-mass creatures. constrain length and mass to 0.1 or less?

###Physicist

* Uniform measure units (instead of had-hoc clipping and multiplying all over the place)

###Speed Demon

* Return Vector.ZERO when constructing a vector just like it (if worth it -measure!)
* Space Partitioning for fast searches of pond
* Optimize physics engine
* Optimize infrared mode
* Use fast math library

## Computer Dummy

* Externalize all configurable parameters to a JSON file
* Save Petri dish
* Load Petri dish

###Genetist

* Each body segment's amplitude is genetically determined
* Narjillo's skewing is genetically determined
* Whole-body size gene
* View Narjillo's evolutionary path
* Bullet-proof genome with a Chromosome class (for documentation, tool building and checking min and max values, for avoiding divisions by 0 and other potential issues)
* DNA analyzer program
* DNA diff program (based on DNA analyzer)
* Sexual reproduction
* Sexual reproduction is optional
* Prevent DNA strands that are too different from mixing to favor speciation? (maybe bad idea b/c of deleterious mutations in small population)

###Ostetrician

* Eggs
* "Egg-firing"
* Additional cost for long-distance egg-firing
* Distance of firing is determined by genes
* Narjillos spends energy to reproduce
* Narjillos decide how much energy to give to descendants

###Lab Technician

* Fix bug with non-repeatable experiments
* Lock on single Narjillo
* Bench with stats
* "Real Time" and "Fast" slider
* Light switch
* "Paused" setting in speed slider
* Select creature
* Export creature
* "Most Prolific" button
* View creature energy

###Environmentalist

* Narjillos eat each other
* Plants (food that evolves)
* Gamma rays (cause mutation)
* Beta rays (cause old age and death)
* Controllable food rate
* Controllable food calories

###Ethologist

* Narjillo updates food item when not in range anymore

###Artist

* Give a sense of movement when following a locked-on Narjillo
* Birth animation
* Speckles in liquid (or some other effect)

##Neurologist

* Brains with behaviours

##Daydreamer

* Trigger evolutionary arms race
