This is my backlog for the Narjillo project. It's divided in sections by "type of user". Inside each sections, features are roughly prioritized.
I generally work on the high-priority features, but I pick the "user" that I want to be depending on my mood of the day.

## Computer Dummy

* Don't require Gradle to start the programs (only to build them)
* Externalize all configurable parameters to a JSON file
* Save dish
* Load dish

###Physicist

* Think hard about wave doubling - do it on the "going forward / coming back" instead of the "positive/negative"?
* Real rotation
* Tweak energy consumption

###Genetist

* Each body segment's amplitude is genetically determined
* Whole-body size gene
* Bullet-proof genome with a Chromosome class (for documentation, tool building and checking min and max values, for avoiding divisions by 0 and other potential issues)
* Smooth organ evolution to and from atrophy

###Ostetrician

* Eggs
* "Egg-firing"
* Additional cost for long-distance egg-firing
* Distance of firing is determined by genes
* Narjillos spends energy to reproduce
* Narjillos decide how much energy to give to descendants

###Lab Technician

* Examine Species
* Bench with stats
* "Real Time" and "Fast" slider
* Light switch
* "Paused" setting in speed slider
* Select creature
* Export creature
* "Most Prolific" button
* View creature energy

###Speed Demon

* Normalize organ angle during rotation (right now rotations are weird - see comment in BodySegment.java)
* Change nerves to propagate a scalar instead of a vector (that never really worked, and it lent to duplicated calculations - see BodySegment.java)
* Space Partitioning optimization
* Cached food image?

###Environmentalist

* Narjillos eat each other
* Plants (food that evolves)
* Gamma rays (cause mutation)
* Beta rays (cause old age and death)
* Controllable food rate
* Controllable food calories

###Ethologist

* Narjillo updates food item when not existing anymore
* Narjillo updates food item when not in range anymore

###Artist

* Smooth overzoomed effects (right now they kill visible framerate, not reported one)
* Detailed death animation
* Birth animation
* Narjillos always overlap food (food is in background)
* Speckles in liquid (or some other effect)

##Daydreamer

* DNA analyzer program
* DNA diff program (based on DNA analyzer)
* Sexual reproduction
* Sexual reproduction is optional
* Brains with behaviours
* Prevent DNA strands that are too different from mixing to favor speciation? (maybe bad idea b/c of deleterious mutations in small population)
