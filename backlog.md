#Narjillos Backlog

Narjillos is my experimental project. I use it to try a lot of different things, and then I apply what I learn to other projects. In this case, I'm using it to experiment with the backlog.txt format (https://github.com/nusco/backlog.txt).

For now, I'm using a quick Java utility to process the backlog. Here are some commands you can use:

    g backlog            prints the whole backlog (in color)
    g backlog all        like "g backlog"
    g backlog top        prints the topmost priority feature
    g backlog N          prints the topmost N features

##Basic Lab Analysis
>goal: understand what is happening in the dish  

    Track historical data in experiment for analysis.

* Track history data together with ancestry
  creatures/food/etc.  

* Dump history to CSV  
  Run analysis with --history to dump a CSV file of history.  
  This should be good for analysis in a spreadsheet.

* Measure evolution speed  
  This seems to be hard. The mutation rate solutions seems useless. Any help from existing papers?

+ Measure extinctions  
  Assuming they are important, and they can be measured effectively. Check existing papers.

+ Measure min/max generations in Lab and history  
+ Measure longest dead branch in Lab 
+ Measure distance of common ancestor in Lab and history  
+ Measure standard deviation of gene pool in Lab and history    

+ Fail with explicit error if running ancestry analysis on a file without ancestry  
+ Warning in case of conflicting command-line arguments of main apps  
  like -s and experiment file used together  

- Generate lab script for packaged distribution  

- Start Lab utility with deep stack  
  Otherwise it overflows when exporting huge phylogenetic trees.
  I tried to do this in Gradle, but I failed (it works in Eclipse).
  It's documented in the help, so not really important.


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


##Species Analysis
>goal: multiple species  
   
    Use clustering algorithms to automatically count the number of "species".

* Count species with simple clustering algorithm  
  Levenshtein-based  

* "Next in cluster" and "Next cluster" buttons while following  
  Without this, it becomes very hard to understand how the creatures are clustered.  
  Also apply during demo.

* Continuous clustering  
  Needs a fast clustering process. Obvious way: cache Levenshtein distances.  
  Compare clustering performance with Levenshtein and SimHash distances.  
  Also look for other ways to optimize.  

* Track number of species clusters in history  
  Or during lab analysis if too slow.  
  
- Track composition of species clusters in history  

- Track genera/species/mutations separately  
  They are just different cluster radiuses.  


##Ecological Niches
>goal: multiple species  

    Use different kinds of food to encourage sympatric speciation.
    
    If I don't get sympatric speciation after implementing this, then try the
    Self-Regulating Ecosystem feature.
    
* Different kinds of food  
  probably over a 1- or 2-dimensional continuum. (1-dimensional is easier to graph,
  2-dimensional has more space for niches).
  for example, red to green, and all the intermediate colors.
  
* Narjillos consume food based on their body shape  
  how exactly? I need to come up with some kind of 1-dimensional relationship here.
  Maybe body size? or color?
  
* Narjillos select food  
  this could be tricky. how does a creature decide that food is "good enough"?
  most likely, they pick the food that they "like" best. but what if better 
  food appears nearby? there is a risk that a creature will starve
  while trying to reach better and better food.
  another totally different approach is mixing this with Geographical Food
  Distribution (a separate story), and having narjillos have a favourite direction
  (genetically determined) that they use to look for food instead of a favourite
  type of food. Each Narjillos would eat any kind of food, but only find nutrients
  in the "right" kind. Narjillos who tend towards the wrong direction will likely
  die.
  
+ Phenogenealogical trees  
  Like in the paper by Woehrer, Hougen and Schlupp. They are a great way to understand
  what is happening and how species are diverging.
  
+ Geographical food distribution  
  For example, red is higher up in the culture, green is lower down.


##Intuitive Navigation
>goal: nice user experience  

    Make it easier to move around the dish in a graphical run.
    
    I should also test navigation with a first-time user.

+ "Previous" command during demo/following  
+ Visual effect when tracking/untracking  
- Mini-map  


##GUI
>goal: nice user experience  

    Make the program accessible to people who don't like to learn keyboard shortcuts.

* Status bar  
* Save/load experiment from menu  
+ Optionally save when quitting application  
+ Command menus for light, speed, and so on  
* Start new experiment from menu  
+ "About"/"Help" menus  
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


##Dish Edges
>goal: complex interactions  

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
  
* Narjillos eat other narjillos  
  pick target at random  
  
- Narjillos eat eggs  


##Eye Candy
>goal: nice user experience  

    Smoother, nicer graphics.

- Independent eye pupils  
+ Optimize graphics  
+ Smoother contours when zooming in infrared mode  
- Show heat cloud when zooming out in infrared mode  
- Skip quickly over less interesting creatures in Demo Mode  
+ Command-line argument to start without visual effects  


##Packaged Application
>goal: nice user experience  

    Download-and-run user experience.

* Native Mac app
  See https://bitbucket.org/infinitekind/appbundler (from Cipster)
    
+ Native Windows app  
+ Load narjillos.yaml (or .narjillos.yaml) from home, if present, instead of config.yaml  
- Run in a browser  
+ Fix permissions on distribution startup scripts  
  Apparently, the scripts have problem starting in Ubuntu? Check. If not true, then remove this story  


##Advanced Body Plans
>goal: specialized creatures  

    Give evolution more power to shape interesting creature bodies.

* "Loop" instruction in body plan  
+ "Jump" instruction in body plan  
+ "Call" instruction in body plan  
- Duplicate organs during mutation instead of mirroring them  
  To favour emergent complexity. There are studies who say it would, at least.  
    
- Different shapes for body segments  


##Fibers
>goal: specialized creatures  

    Creature specialize in different survival strategies depending on the color of their organs.
    
    This is another factor that can encourage disruptive evolution and speciation.

* Green fibers  
  "Plant fibers", get energy from environment.  
  Reactivate (I disabled them in config).  
  
* Re-activate blue fibers  
  "Runner fibers", generate more push when moving.  
  Reactivate (I disabled them in config).  
    
+ Add red fibers  
  "Killer fibers", damage collided creatures  
  These are new.


##Ancestry Browser
>goal: ancestry browser  

    Analyze DNA ancestry in separate program.
    
    Takes a chain of DNA ancestors and dynamically shows specimen on the screen, with forward/back  
    buttons and the like.

* Ancestry Browser  
+ "Back" button  
- DNA diff during ancestry analysis  


##Istincts
>goal: complex interactions  

    Narjillos decide where to go based on their attraction/repulsion to other creatures and things.
    
    This is another example of complex interactions. It might result in interesting behaviors and  
    "smart" species.

* Species identification  
  Probably based on DNA SimHashes?  
  
* Istinct genes: love, fear, hunger  
- Visualize istinct directions  
- Visualize istincts on other creatures when following a narjillo  
* Decide ideal direction based on istinct genes  
+ Attach istincts to all things – in particular, food  


##Advanced Lab Analysis
>goal: understand what is happening in the dish  

    Science-level analysis of experimental data.

* Store historical data in a database rather than in memory  
+ Move entire persistence to a database  
  I have to try, but this might not even make sense. It might destroy performance or reliability.

+ Track advanced experimental data together with ancestry  
  avg lifetime/avg descendants/etc.  
  
+ Measure creature/dish efficiency  
  somehow. for now, I don't know what this is :)  
  
- Advanced clustering algorithm  
  based on Shannon's theory to evaluate gene distribution. see related study.  
  
+ Advanced ancestry analysis  
  study related papers  

- Encode CPU floatpoint precision in the experiment id  
  rather than use strictfp, that harms performance  


##Self-Regulating Ecosystem
>goal: specialized creatures  

    Encourage speciation by creating a self-enforcing reciprocal need between creatures.
    
    This feature should leverage disruptive evolution to push narjillos to specialize.
    
    If I still don't get sympatric speciation after implementing this, then maybe I need  
    something even more radical. Sexual reproduction with assortative mating, maybe?

* Atmosphere chemistry  
* Narjillos produce a molecule  
  based on visible body qualities - metabolic rate?  

* Narjillos consume a molecule  
  faster reproduction? lower energy expenditure?
    
* Ecological Niches  
  tweak chemistry to encourage speciation
    
+ Local atmosphere  
  atmosphere composition is a map instead of a global value  
  
+ Atmosphere composition "heat map"  


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
