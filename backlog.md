##Backlog

This backlog is organized by "type of user". Inside each sections, features are roughly prioritized.
I generally work on the high-priority features, but I pick the "user" that tickles my fancy each day.

###Physicist

* Increase precision of fast arctangent °°
* Switch to correct vector math algorithms °°
  (see TODO in class Vector for an example)
* Remove "crazy rotation" effect °°
  (see "{037_033_009_240_163_114_226_093}{070_235_165_209_037_163_103_156}{070_041_062_065_112_221_235_088}{224_046_028_223_162_019_227_001})
* Watchdog for excessive speed (results in warning) °
* Smooth, inertial targeting

###Speed Demon

* Synchronize cuncurrent access in parallel ticking of narjillos °°°
  (also double-check parallel code)
* Optimize physics engine °
* Check speed under Linux
* Make FPS parametric to improve TPS on slower machines

###Gamer

* Externalize all configurable parameters to JSON files
* Try scrolling/panning commands under Linux
* Parameterize scroll/pan speeds

###Genetist

* Iterations in body plan °°°
* Find a good way to deal with atrophy °°
* Ancestry browser °°
* DNA diff during ancestry analysis °
* "Skip next" instruction in body plan °
* Separate RGB color genes
* Whole-body size gene

###Ethologist

* Genetically determined asymmetric skewing strategies °°
* Eggs °
* "Egg-firing" °
* Additional cost for long-distance egg-flinging °
* Distance of firing is determined by genes °

###Lab Technician

* Make CPU floatpoint precision a part of the experiment's id (instead of using strictfp) °
* Check git commit on experiment startup °
* Lock on single narjillo °
* Dump isolated narjillos to file (instead of the console)
* Real arguments parsing (use --no-persistence together with other switches)
* Zipped persistence files
* View narjillo stats (age, energy, times eaten...)
* Make food visible in normal view
* Make food visible in infrared view

###Ecologist

* Bug: PetriDish can miss notifications from pond (in particular, Narjillos dying when they spawn with very low or no energy) °°
  (This might not be the case anymore, but a good chance to check multithreading around the Ecosystem)
* Conservation of energy in entire pond (with sun to give more energy)
* Multiple environments in multiple processes (with migration)
* Plant-like creatures instead of food (minimal energy expenditure? photosyntesis?)

###Ethologist

* Narjillos are born small and then grow up °
* Narjillos eat each other
* "Fears" gene
* "Wants to eat" gene
* Obstacles and reactive sense system

###Artist

* Birth animation °
* Light on/off visual and sound effects °
* Better death animation (right now the bending is not visible) °
* Fade out mouth/eye during death °
* Smoother contours when zooming in infrared mode
* Give a sense of movement when following a locked-on Narjillo (speckles in liquid?)

##Neurologist

* Brains with behaviours

##Daydreamer

* Trigger evolutionary arms race
