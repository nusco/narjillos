#Narjillos Backlog

## Technical Issues
> goal: make Narjillos easy to run on the latest Java

    I came back to the project after a few years, and found that the code has many issues (mostly system-related more than strictly code-related) compiling and running on recent versions of the Java stack.

* Define run configurations
  To run the main program, the support programs and the tests in IntelliJ.

* Define distribution procedure
  Redefine the distribution inside IntelliJ and release an update for modern Java.

+ Reintroduce Build system
  Just not Gradle or Maven, that have proven absolutely painful. Maybe old-fashioned Ant, or Rake w. JRuby?

+ Reintroduce Travis Build
  Once I have a build system, I can have this again.

* Find the bug that allows creatures to stay around after death
  Try removing all energy sources except for food: food_density_per_block, initial_element_level and catalyst_level at 0. That configuration triggers the bug.

## Collision Detection
> goal: complex interactions

    See: https://developer.mozilla.org/en-US/docs/Games/Techniques/2D_collision_detection  
    Maybe also see: http://number-none.com/blow/papers/practical_collision_detection.pdf  

* Broad CD pass to find candidate colliders--
  Use it for food collisions for now.--
  Spatial hashing.
  Maybe use bounding boxes or even oriented bounding boxes (https://en.wikipedia.org/wiki/Minimum_bounding_box_algorithms).
  Maybe even apply a convex hull algorithm (https://en.wikipedia.org/wiki/Minimum_bounding_box_algorithms). Then, a good approach is to find a dividing line as suggested in practical_collision_detection.pdf.
  Importantly, this story removes the concept of "outer space" and makes collisions work anywhere.  
  Make it visualizable.

+ Ultra-broad CD pass to remove isolated creatures-
  Use Manhattan distance (axis-aligned bounding boxes).
  Maybe I'm already doing it? Does it make a difference?

* Fix vector math  
  See TODOs and FIXMEs, in particular around vector projections.  
  This is probably the cause of sketchy narjillo-to-food collision detection  

* Fine organ-based CD pass--

* Use "speed boxes" instead of bounding boxes  
  As described in https://developer.mozilla.org/en-US/docs/Games/Techniques/2D_collision_detection  
  Probably calculate them by over-boxing previous and current position's b-boxes.  
  This should make collision detection independent of max speed, so remove "safety speed" code
  if the approach works.  

* Progressively damage out-of-bounds Narjillos
  Identifying things should work up to a limited distance.  
  Now that Outer Space is gone, damage creatures progressively for getting farther beyond
  the expected boundaries.

+ Avoid square roots in vectors by comparing to squared distances  
  In other words, never do a square root (in particular, in Vector#length()) - instead,
  keep things squared throughout. I doubt this is worth it, because my ceiling tests
  indicate that this optimization would gain maybe 20% speed but probably make the code
  harder to reason about. Still something worth considering after the main collision
  detection algorithm is done, because by that time it will be easier to judge impact.  

* Optimize collision detection  

##Instincts
>goal: complex interactions  

    Narjillos decide where to go based on their attraction/repulsion to other creatures and things.

    This is another example of complex interactions. It might result in interesting behaviors and  
    "smart" species. It's also a precondition for Predators.

    I didn't decide how to do this yet. It may be weights-based.

    Problem: what happens if narjillos keep hitting the edges while escaping something?  
    Maybe build in edge-avoidance istinct?  

* Species identification  
  Probably based on DNA SimHashes? Or fibers?  

* Instinct genes  
  How? Probably just a set of SimHashes and weights?
  Maybe a small set of curves with multiplies and shifters, all determined by the genes?  

+ Attach instincts to food  
  Maybe every Thing must be SimHashed?

* Decide target direction based on instincts  

- Visualize instinct directions  
- Visualize instincts on other creatures when following a narjillo  


##Predators
>goal: complex interactions  

    Narjillos can eat each other.

    This is a complex direct interaction that might pave the road to an arms race amongst species.
    I should consider removing food by default after this is implemented.

* Narjillos collide with other narjillos  
  This should work in a generalized way.  
  (However, see "Narjillos hurt other narjillos" story for an approach that might reduce collision
  detection to an intersection of vectors).  

* Narjillos hurt other narjillos  
  ...stealing their energy when they "hit" them.
  Probably introduce the concept of "spikes" in the body and consider  
  the tangential movement of leaf segments as an attack. Spikes would  
  hurt more than blunt segments. The mouth might be spiky by default.

+ Show damage on narjillos  
  "Bubbles" effect?  

+ Remove the concept of ecosystem size  
  Replace with something else. Right now it's only used for graphics (viewport size),
  spawn area, and maybe to damage narjillos too far from the center.

- Narjillos eat eggs  


##Flexible Genes
>goal: specialized creatures  

    More qualities of the creatures are determined by genes instead of being hard-coded.

* Chromosome swapping mutation  
* Chromosome removal mutation  
* Chromosome duplication mutation  
* Chromosome duplication with mirroring mutation  
+ Separated frequency configuration for different types of mutations  

+ Max lifespan is genetically determined  
  within a limit  

- Lateral viewfield is genetically determined  
- Growth rate is genetically determined  
  maybe? (Consumes energy?)  

- Egg incubation time is genetically determined  
  maybe. (Makes sense if egg contains green fibers)  

- Adult body size is genetically determined  

+ Gene mutation rate itself is determined by genes
  This is a very interesting concept, and something that I should think about carefully.


##Brains
>goal: complex interactions  

    Still just an idea. Do the Predators theme and then consider this.  

* Creatures have neural networks for brains  


##Eye Candy
>goal: nice user experience  

    Smoother, nicer graphics.

* Graphics scaling for performance    
  Render in background at half resolution, then scale up for retina displays.  
* Proportional mousewheel scrolling  
  Right now it uses fixed-step zoomIn/zoomOut.  
+ Disable effects automatically when frame rate drops  
+ Optimize graphics  
+ Limit panning to central area  
  Maybe with some margin.  
  Alternately, find a way to make the background solid everywhere (without impacting zoom performance).  
+ Change background color when panning far from center  
  Does this make sense in conjunction with zooming, etc?  
- Independent creature eye pupils  
+ Smoother contours when zooming in infrared mode  
- Show heat cloud when zooming out in infrared mode  
- Skip quickly over less interesting creatures in Demo Mode  
+ Command-line argument to start without visual effects  


##Species Analysis
>goal: understand what is happening in the dish  

    Use clustering algorithms to automatically count the number of "species".

* Count species with simple clustering algorithm  
  Levenshtein-based  

* "Next in cluster" and "Next cluster" buttons while following  
  Without this, it becomes very hard to understand how the creatures are clustered.  
  Also make this work while in demo mode.

* Continuous clustering  
  Needs a fast clustering process. Obvious way: cache Levenshtein distances.  
  Compare clustering performance with Levenshtein and SimHash distances.  
  Also look for other ways to optimize.  

* Track number of species clusters in history  
  Or during lab analysis if too slow.  

- Track composition of species clusters in history  

- Advanced clustering algorithm  
  Based on Shannon's theory to evaluate gene distribution.  
  See "Identifying Species by Genetic Clustering" (Murdock, Yaeger) and "The Stochastic QT–Clust Algorithm" (Scharl, Leisch).  

- Track genera/species/mutations separately  
  They are just different cluster radii.  

- Track history separately for separate species  
  It's interesting to look at things like average generation for different species, instead of
  one number for the whole gene pool.  


##Advanced Germline Browser
>goal: understand what is happening in the dish  

*  Show narjillo stats while browsing germline  
   Pick a few:
   Breathing cycle, adult mass, cumulative body angles, energy to children, metabolic  
   rate, number of organs (atrophic and not), body plan program,  
   total delay, total amplitude, total skewing, total fiber shift, egg velocity, egg interval.  

+ Start browser directly from inside Lab program  

* Automatic phenotype diff  
  Values that changed are marked in red.  

+ Show energy efficiency amongst stats  

+ Command-line help in germline application  

- More visible particles  
- Infinite particles texture  
- Different background color from regular program  

- Automatic DNA diff  

- DNA diff hint  
  Tells which characteristic the gene is controlling.  


##Lab Analysis
>goal: understand what is happening in the dish  

    Track historical data in experiment for analysis.

+ Modify "g" to use the same scripts as distribution  
+ Document opportunity to use JAVA_OPTS, LAB_OPTS and so on to pass arguments to scripts  

* Save current configuration to experiment file  
  Issue warning if continuing the experiment with a different configuration.
  Maybe offer a command-line switch to override existing configuration?

* Output germline statistics  
  Energy to children, total mass, etc.  

+ Stabilize memory consumption  
  Right now the Lab program might run out of memory on a large experiment.  
  Avoid bulk-loading stuff off the database.  

+ Fail with explicit error if running ancestry/history analysis on a file without history  

+ Warning in case of conflicting command-line arguments of main apps  
  Like -s and experiment file used together
  Also issue better error messages in case of conflict

+ Generate lab script for packaged distribution  

+ Measure speed of population centroid movement  
  See Burtsev, "Measuring the Dynamics of Artificial Evolution".
  This seems to be a good way to measure evolution speed, that is otherwise a problem.
  But be aware that I tried something similar (with Levenshtein distance of current dominant
  organism from previous generations), and it didn't really work. The junk DNA seems to dominate
  over meaningful changes.

+ Show graph of differences in phase space of population centroid movement  
  To spot (and hopefully avoid) cycles in evolution. See Burtsev, "Measuring the Dynamics of Artificial Evolution".

+ Measure longest dead branch in Lab  

+ Measure distance of common ancestor in Lab and history  

+ Show graph of phenogenealogic tree  
  See "Sexual Selection, Resource Distribution, and Population Size in Synthetic Sympatric Speciation".
  (Woehrer, Hougen, Schlupp). I need one (or more) 1-dimensional phenotypic trait for these graphs to make sense.

+ Measure creature/dish efficiency  
  Somehow. Right now, I don't really know what "efficiency" really means :)  

- Start Lab utility with deep stack  
  Otherwise it overflows when exporting huge phylogenetic trees.
  I tried to do this in Gradle, but I failed (it works in Eclipse).
  It's documented in the command-line help, so not really important.

+ Issue warning when running on CPU with the "wrong" word size
  We don't use strictfp, because it harms performance. So you could get non-deterministic results if you run
  on a CPU with a different flotpoint precision than the expected one.  


##Packaged Application
>goal: nice user experience  

    Download-and-run user experience.

* Native Mac app
  See https://bitbucket.org/infinitekind/appbundler (from Cipster)

* Allow multiple configurations  
  With a -c switch. Also have a default configuration

+ Load narjillos.yaml (or .narjillos.yaml) from home, if present, instead of config.yaml  

+ Native Windows app  

- Run in a browser  

+ Fix permissions on distribution startup scripts  
  Apparently, the scripts have problem starting in Ubuntu? Check. If not true, then remove this story  


##Realistic Physics
>goal: specialized creatures  

    More realistic behavior of body to avoid body shapes that "exploit" the current naive physics.

+ Fix "tail wiggles dog" effect  
  if it's still there - I'm not sure

+ Rotation inertia  
  but check comments in physics engine - it may break previous assumptions  

+ Translation inertia  
  but check comments in physics engine - it may break previous assumptions  

+ Limit rotation speed  
  is this a good idea?  

- Realistic viscosity  
  is this a good idea?  

- Viscosity per segment  
  is this a good idea?  

+ Simpler senescence mechanism  
  the current one feels too complicated for its own good.


##Seasons
>goal: faster evolution  

    Cyclically vary the amount of food that spawns.

    Studies show that evolution works best if there are enough resources (food),
    but not too many. The problem is that it's hard to know what "enough but
    not too many" means. So I want to try this: food amount is cyclical. I'm
    hoping that along the way from "almost starving" to "economy of
    abundance", the system will hit a few evolutionary sweet spots.

* Seasons  

* Configurable seasonal cycle  
  Max, min and period in config.yaml  


##GUI
>goal: nice user experience  

    Make the program accessible to people who don't like to learn keyboard shortcuts.

* Status bar  
* Save/load experiment from menu  
+ Optionally save when quitting application  
+ Command menus for light, speed, and so on  
* Start new experiment from menu  
+ "About"/"Help" menus  
* Show atmosphere composition  
- Speed widget  
- "Screensaver mode"  
  no menus or status bar

- Light switches for normal/infrared light  
+ Show historical data  
- Show graphs for historical data  
+ View stats for followed narjillo  
  age, energy, radius, times eaten, genome...  

- Tutorial  
+ In-app configuration panel  


##Sexual Reproduction
>goal: different species  

    Creatures mate and generate mixed-DNA offsprings.

    A lot of things to decide here. do I really need this stuff to get speciation? Probably  
    not – so I'll leave it as a low priority for now.

* Basic Sexual Reproduction  
  just to set up for Assortative Mating  

* Encourage Speciation  
  Either via assortative mating, or by making DNAs that are too different sterile.  

+ Species clustering control reproductive success  
  maybe. (to keep species apart)  

- Diploid creatures  
  maybe. would this be useful?


##Intuitive Navigation
>goal: nice user experience  

    Make it easier to move around the dish in a graphical run.

    I should also test navigation with a first-time user.

+ "Next" command during demo/following  
+ "Previous" command during demo/following  
+ Visual effect when tracking/untracking  
- Mini-map  


##Multi-dish World
>goal: distributed experiments

* Migrations  

* Save to remote database  
  Requires switching to a different driver than SQLite.  


##Crazy Ideas

- Demiurge (an entity that dynamically tweaks the environment to maximize evolutionary speed)  
