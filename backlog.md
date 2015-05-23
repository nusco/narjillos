#Narjillos Backlog

Narjillos is my experimental project. I use it to try a lot of different things, and then I apply what I learn to other projects.
Now I'm looking for an efficient, low-tech way to manage a backlog, so let's put it to the test in Narjillos. :) I'm testing both the basic concepts ("Is this way of working flexible and simple?") and the tools ("Is it technically easy to manage the backlog with this format?"). If this works, then I might turn it into its own project ("backlog.txt", kinda like "todo.txt" for backlogs).

The Backlog is divided into _Features_, each composed of _User Stories_.

The formatting is important, because the tools rely on it to understand this document. Here is the format:

* The format is a dialect of Markdown, so everything that works in Markdown works here. (In particular, double spaces at the end of a line force a newline).
* The first line is ignored by the tools.
* Features start with one or more # characters.
* User Stories start with x characters (under their Feature).
* Everything else is a comment.

Here are some commands you can use:

    g backlog            prints the whole backlog (in color)
    g backlog all        like "g backlog"
    g backlog top        prints the topmost priority feature
    g backlog N          prints the topmost N features

Here is the meaning of different types of User Stories inside a Feature:

* "Red" Stories (starting with xxx) are Mandatory. We cannot deliver a Feature if we don't do all of them.
* "Yellow" Stories (starting with xx) are Linear. The more of these we implement, the more value the Feature has.
* "Blue" Stories (starting with x) are Exciters. They don't add huge value, but they make the Feature more compelling.

Features are sorted by highest priority first, but their priorities shift all the time. Once the Mandatory stuff is done, a Feature might slip down into lower priorities.

##Species Analysis

    Use clustering algorithms to automatically count the number of different "species"  
    in the ecosystem.

xxx Count species with simple clustering algorithm (Levenshtein-based)  
xx Cache distances for fast continuous clustering  
xx Compare clustering performance with Levenshtein and SimHash distances  
xxx Track number of species clusters in history (or during lab analysis if too slow)  
x Track composition of species clusters in history  
x Track multiple cluster groups with different sizes (genera/species/mutations)  


##Basic Lab Analysis

    Track historical data in experiment for offline analysis in a spreadsheet.

xxx Track historical data together with ancestry: eggs/creatures/food/clusters/etc.  
xxx Run analysis with --history to dump a CSV file of history  
xx Fail with explicit error if running ancestry analysis on a file without ancestry  
x Generate lab script for packaged distribution  
xx Warning in case of conflicting command-line arguments of main apps (like -s and experiment file used together)  


##Self-Regulating Ecosystem

    Encourage disruptive evolution, which should result in sympatric speciation.
    If I don't get sympatric speciation after implementing this, then maybe I need  
    something more. Sexual reproduction, maybe?

xxx Atmosphere chemistry  
xxx Narjillos produce a molecule (based on visible body qualities - metabolic rate?)  
xxx Narjillos consume a molecule (faster reproduction? lower energy expenditure?)  
xxx Ecological Niches (tweak chemistry to encourage speciation)  
xx Local atmosphere (atmosphere composition is a map instead of a global value)  
xx Atmosphere composition "heat map"  


##Intuitive Navigation

    Make it easier to move around the dish in a graphical run.
    I should also test navigation with a first-time user.

xx Next/previous commands during demo  
xx Visual effect when tracking/untracking  
x Mini-map  


##GUI

    Make the program accessible to people who don't like to learn keyboard shortcuts.

xxx Status bar  
xxx Save/load experiment from menu  
xx Optionally save when quitting application  
xx Command menus for light, speed, and so on  
xxx Start new experiment from menu  
xx "About"/"Help" menus  
x Speed widget  
x "Screensaver mode" (no menus or status bar)  
x Light switches for normal/infrared light  
xx Show historical data  
x Show graphs for historical data  
xx View stats for followed narjillo (age, energy, radius, times eaten, genome...)  
x Tutorial  
xx In-app configuration panel  


##Advanced Body Plans

    Give evolution more power to shape interesting creature bodies.

xxx "Loop" instruction in body plan  
xx "Jump" instruction in body plan  
xx "Call" instruction in body plan  
x Duplicate organs during mutation instead of mirroring them (to favour emergent complexity)  
x Different shapes for body segments  


##Dish Edges

    Constrain the "world" inside defined edges.
    Right now, things can be placed anywhere, but only the central area of the dish is  
    space-partitioned. The rest is "outer space". If we limit the world, we can make it  
    all space-partitioned. The current space partitioning is OK-ish, but it would make  
    it hard and expensive to implement generalized collision detection. At the moment,  
    this is not a problem, because the only collision detection I have is "mouth colliding  
    with food". But in the future, I want to have anything collide with anything.
    After implementing this, I can probably remove the concept of "outer space" (the area outside the space-partitioned center of the dish).

xxx Kill narjillos who touch outer space  
xx Limit panning to inner space (with some margin)  
xx Auto-scroll viewport to stay within inner space  


##Seasons

    Cyclically vary the amount of food that spawns.
    Studies show that evolution works best if there are enough resources (food), but not too many.
    The problem is that it's hard to know what "enough but not many" means. So I want to try this:
    food amount is cyclical. I'm hoping that along the way from "almost starving" to "economy of
    abundance", the system will hit a few evolutionary sweet spots.

xxx Seasons  
xxx Seasonal cycle max, min and period can be configured in config.yaml  


##Eye Candy

    Smoother, nicer graphics.

x Moving mouths  
x Independent eye pupils  
xx Optimize graphics  
xx Simpler senescence mechanism  
xx Smoother contours when zooming in infrared mode  
x Show heat cloud when zooming out in infrared mode  
x Skip quickly over less interesting creatures in Demo Mode  
xx Command-line argument to start without visual effects  


##Packaged Application

    Download-and-run user experience.

xxx Native Mac app  
xx Native Windows app  
xx Load narjillos.yaml (or .narjillos.yaml) from home, if present, instead of config.yaml  
x Run in a browser  
xx Fix permissions on distribution startup scripts (has problem starting in Ubuntu?)  


##Predators (Complex Interactions 1)

    Narjillos can eat each other.
    This is a complex direct interaction that might pave the road to an arms race amongst species.
    I should consider removing food by default after this is implemented.

xxx Fix sketchy collision detection (is this a bug?)  
xx Rewrite collision detection to be independent of max speed  
xxx Narjillos eat other narjillos (pick target at random)  
x Narjillos eat eggs  


##Fibers

    Creature specialize in different survival strategies depending on the color of their organs.
    This is another factor that can encourage disruptive evolution and speciation.

xxx Re-activate green fibers ("plant fibers", get energy from environment)  
xxx Re-activate blue fibers ("runner fibers", generate more push when moving)  
xx Add red fibers ("killer fibers", damage collided creatures)  


##Ancestry Browser

    Analyze DNA ancestry in separate program.
    Takes a chain of DNA ancestors and dynamically shows specimen on the screen, with forward/back  
    buttons and the like.

xxx Ancestry Browser  
xx "Back" button  
x DNA diff during ancestry analysis  


##Istincts (Complex Interactions 2)

    Narjillos decide where to go based on their attraction/repulsion to other creatures and things.
    This is another example of complex interactions. It might result in interesting behaviors and  
    "smart" species.

xxx Species identification (based on DNA hashes?)  
xxx Istinct genes: love, fear, hunger  
x Visualize istinct directions  
x Visualize istincts on other creatures when following a narjillo  
xxx Decide ideal direction based on istinct genes  
xx Attach istincts to all things – in particular, food  


##Advanced Lab Analysis

    Science-level analysis of experimental data.

xx Track advanced experimental data together with ancestry: avg lifetime/avg descendants/etc.  
xx Measure creature/dish efficiency (somehow)  
x Advanced clustering algorithm (based on Shannon's theory to evaluate gene distribution)  
xx Advanced ancestry analysis (study related papers)  
xx Store historical data in a database rather than in memory  
xx Optionally move entire persistence to a database  
x Encode CPU floatpoint precision in the experiment id (rather than use strictfp)  


##Sexual Reproduction

    Creatures mate and generate mixed-DNA offsprings.
    A lot of things to decide here. do I really need this stuff to get speciation? Probably  
    not – so I'll leave it as a low priority for now.

xxx Basic Sexual Reproduction (just to set up for Assortative Mating)  
xxx Assortative Mating (to encourage speciation)  
xx Species clustering control reproductive success? (to keep species apart)  
x Diploid creatures?  


##Realistic Physics

    More realistic behavior of body to avoid body shapes that "exploit" the current naive physics.

xx Fix "tail wiggles dog" effect  
xx Rotation inertia (but check comments in physics engine - it may break previous assumptions)  
xx Translation inertia (see above)  
xx Limit rotation speed?  
x Realistic viscosity?  
x Viscosity per segment?  


##Flexible Genes

    More qualities of the creatures are determined by genes instead of being hard-coded.

xx Wave beat ratio is genetically determined  
xx Max lifespan is geneticaly determined (within a limit)  
x Lateral viewfield is genetically determined  
x Growth rate is genetically determined? (Takes energy?)  
x Egg incubation time is genetically determined? (Makes sense if egg contains green fibers)  
x Adult body size is genetically determined  


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
