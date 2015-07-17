#Narjillos Backlog

Narjillos is my experimental project. I use it to try a lot of different things, and then I apply what I learn to other projects. In this case, I'm using it to experiment with the backlog.txt format (https://github.com/nusco/backlog.txt).

For now, I'm using a quick Java utility to process the backlog. Here are some commands you can use:

    g backlog            prints the whole backlog (in color)
    g backlog all        like "g backlog"
    g backlog top        prints the topmost priority feature
    g backlog N          prints the topmost N features

---

##Prepare for Madison+ Presentation
>goal: have a good, clear presentation in Madison  

* Update documentation  
* Prepare a "find a new name" initiative  
* Auto-play in Germline Browser  
* Keyframes in Germline Browser  
* Identify interesting germline for demo  
* Identify interesting history for demo  
  (Temporarily remove chemistry?)  
* Slow motion in germline viewer  
* Fix "viewport shift" bug in germline viewer  
* Rotate food in germline viewer  


##Germline Browser
>goal: understand what is happening in the dish  

    Analyze DNA germline in separate program.
    This is useful to prepare my presentation at Madison+ Ruby.
    
    Takes a chain of DNA ancestors and dynamically shows specimen on the screen, with forward/back  
    buttons and the like.

*  Show narjillo stats while browsing germline  
   Pick a few:
   Breathing cycle, adult mass, cumulative body angles, energy to children, metabolic  
   rate, number of organs (atrophic and not), bodyplan program,  
   total delay, total amplitude, total skewing, total fiber shift, egg velocity, egg interval.  

* Automatic phenotype diff  
  Values that changed are marked in red.  

+ Start browser directly from inside Lab program  

+ Show energy efficiency amongst stats  

+ Command-line help in germline application  

- More visible particles  
- Infinite particles texture  
- Different background color from regular program  

- Automatic DNA diff  


##Istincts
>goal: complex interactions  

    Narjillos decide where to go based on their attraction/repulsion to other creatures and things.
    
    This is another example of complex interactions. It might result in interesting behaviors and  
    "smart" species. It's also a precondition for Predators.
    
    I didn't decide how to do this yet. It may be weights-based.

* Species identification  
  Probably based on DNA SimHashes? Or fibers?  
  
* Istinct genes  
  How? Probably just a set of SimHashes and weights?

+ Attach istincts to food  
  Maybe every Thing must be SimHashed?

* Decide target direction based on istincts  

- Visualize istinct directions  
- Visualize istincts on other creatures when following a narjillo  


##Predators
>goal: complex interactions  

    Narjillos can eat each other.
    
    This is a complex direct interaction that might pave the road to an arms race amongst species.
    I should consider removing food by default after this is implemented.

* Fix sketchy collision detection  
  is this a bug?  

+ Rewrite collision detection to be independent of max speed  
  This can be almost as simple as it is now, but we should probably aim for generalized
  collision detection on all organs. Here are a few links from Cipster:
  http://www.dyn4j.org
  http://www.toptal.com/game/video-game-physics-part-ii-collision-detection-for-solid-objects
  http://blog.sklambert.com/html5-canvas-game-2d-collision-detection
  http://number-none.com/blow/papers/practical_collision_detection.pdf
  
  However, see "hurt" story for an approach that might reduce collision detection to  
  an intersection of vectors.

* Narjillos hurt other narjillos  
  ...stealing their energy when they "hit" them.
  Probably introduce the concept of "spikes" in the body and consider  
  the tangential movement of leaf segments as an attach. Spikes would  
  hurt more than blunt segments. The mouth might be spikey by default.

- Narjillos eat eggs  


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
  They are just different cluster radiuses.  

- Track history separately for separate species  
  It's interesting to look at things like average generation for different species, instead of
  one number for the whole gene pool.  


##Advanced Body Plans
>goal: specialized creatures  

    Give evolution more power to shape interesting creature bodies.

* Less constraining mirroring  
  It should be possible to mirror an organ without necessarily mirroring the entire subtree.  
    
* "Loop" instruction in body plan  
+ "Jump" instruction in body plan  
+ "Call" instruction in body plan  
- Duplicate organs during mutation instead of mirroring them  
  To favour emergent complexity. There are studies who say it would, at least.  
    
- Different shapes for body segments  


##Database Persistence
>goal: understand what is happening in the dish  

    For advanced analysis of experimental data.
    This also removes the memory cap on extremely long experiments.  

* Store historical data in a database rather than in memory  
  Amongst other things, this fixes the OutOfMemory problem on extremely long experiments.
  
+ Move entire persistence to a database  
  Consider this, but be aware that it might effect usability, performance or reliability.  
  So I might decide to stick with files.  
  
+ Save events instead of current state  
  Then have an external program, possibly in Ruby, to process stats

+ Save to remote database  
  If the DB is configurable, then it's easier to run experiments in the cloud.


##Lab Analysis
>goal: understand what is happening in the dish  

    Track historical data in experiment for analysis.

* Save current configuration in experiment file
  Issue warning if continuing the experiment with a different configuration.
  Maybe offer a command-line switch to override existing configuration?

+ Fail with explicit error if running ancestry/history analysis on a file without history  

+ Warning in case of conflicting command-line arguments of main apps  
  like -s and experiment file used together  

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


##Dish Edges
>goal: nice user experience  

    Constrain the "world" inside defined edges.
    
    Right now, things can be placed anywhere, but only the central area of the dish is  
    space-partitioned. The rest is "outer space". If we limit the world, we can make it  
    all space-partitioned. The current space partitioning is OK-ish, but it would make  
    it hard and expensive to implement generalized collision detection. At the moment,  
    this is not a problem, because the only collision detection I have is "mouth colliding  
    with food". But in the future, I want to have anything collide with anything.
    After implementing this, I can probably remove the concept of "outer space" (the area outside the space-partitioned center of the dish).

* Kill narjillos who touch outer space  
+ Limit panning to inner space  
  with some margin.  
  
+ Auto-scroll viewport to stay within inner space  


##Eye Candy
>goal: nice user experience  

    Smoother, nicer graphics.

- Independent eye pupils  
+ Optimize graphics  
+ Smoother contours when zooming in infrared mode  
- Show heat cloud when zooming out in infrared mode  
- Skip quickly over less interesting creatures in Demo Mode  
+ Command-line argument to start without visual effects  


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


##Sexual Reproduction
>goal: different species  

    Creatures mate and generate mixed-DNA offsprings.
    
    A lot of things to decide here. do I really need this stuff to get speciation? Probably  
    not – so I'll leave it as a low priority for now.

* Basic Sexual Reproduction  
  just to set up for Assortative Mating  

* Assortative Mating  
  to encourage speciation  

+ Species clustering control reproductive success  
  maybe. (to keep species apart)  
  
- Diploid creatures  
  maybe. is it useful?


##Intuitive Navigation
>goal: nice user experience  

    Make it easier to move around the dish in a graphical run.
    
    I should also test navigation with a first-time user.

+ "Previous" command during demo/following  
+ Visual effect when tracking/untracking  
- Mini-map  


##Realistic Physics
>goal: specialized creatures  

    More realistic behavior of body to avoid body shapes that "exploit" the current naive physics.

+ Fix "tail wiggles dog" effect  
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
  the current one feels too complicated for its own good


##Flexible Genes
>goal: specialized creatures  
 
    More qualities of the creatures are determined by genes instead of being hard-coded.

+ Wave beat ratio is genetically determined  
+ Max lifespan is geneticaly determined  
  within a limit  
  
- Lateral viewfield is genetically determined  
- Growth rate is genetically determined  
  maybe? (Takes energy?)  
  
- Egg incubation time is genetically determined  
  maybe. (Makes sense if egg contains green fibers)  
  
- Adult body size is genetically determined  

+ Gene mutation rate itself is determined by genes
  This is a very interesting concept, and something that I should think about carefully.


##Crazy Ideas

- Multiple environments in multiple processes (with migration)  
- Creatures have neural networks for brains  
- Demiurge (an entity that dynamically tweaks the environment to maximize evolutionary speed)  
