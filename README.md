#Narjillos

Narjillos are artificial creatures that mutate and evolve in a virtual microscope dish, under the pressure of natural selection. They don't have any predefined strategy for "swimming", apart from swinging their bodies around in response to an external target. Yet, they tend to evolve good swimming behaviours over time.

![Take a peek](/doc/narjillos.jpg)

##How to run an experiment

To put a Petri dish full of narjillos under your microscope, you need Java 8 and git. To install Narjillos:

    git clone https://github.com/nusco/narjillos.git
    cd narjillos
  
Then start a Petri dish with the _petri_ command:
  
    petri

The first run installs the Gradle build tool and then builds the system, so it takes some time. The following runs start faster.

##How to control the microscope

Move around with your mouse: **click anywhere** to center, **double-click** to auto-zoom, and zoom in/out by **scrolling**. You can also move the microscope with your **cursor keys**.

To turn from high speed to real time and back, press **S**.

To turn the lamp on/off, press **L**. With the lamp off, high speed is faster (because the program doesn't need to render the creatures).

If you have trouble distinguishing the shapes of narjillos, try infrared light. It makes things more visible, though not as pretty as a regular lamp. You can toggle infrared light by pressing **I**.

##What the heck are _these_?

There are three main ingredients to the Narjillos recipe: _phenotypic variation_, _selection_ and _mutation_. They sound difficult, but you probably know about all three of them already. Let me tell how they work and what's their final result.

###The three ingredients

####Phenotypic variation

When you start an experiment, the program generates a few hundreds different strands of DNA. Each DNA is a random sequence of bytes. Each byte is a "gene", and genes are collected into "chromosomes".

Each DNA strand goes through a process called "embryology", which generates the physical body of a narjillo. Each chromosome roughly generates a segment in the narjillo's body, and the genes in the chromosome dictate that segment's appearance and behavior: its color, size, angle, and so on.

The DNA is also called the _genotype_, and the physical body is also called the _phenotype_---so you can say that "the genotype generates the phenotype". Identical genotypes always generate identical phenotypes. Similar genotypes tend to generate similar phenotypes. Very different genotypes tend to generate very different phenotypes.

All the narjillos generated in the beginning are collected in a dish under your microscope. The dish is also filled with food---the blue dots. More food keeps spawning slowly as the experiment goes on.

####Selection

Narjillos try to reach the food by swimming. You can look at their "mouths" (the thin green lines) to see which direction they're trying to go. Narjillos don't think, and they don't have any sophisticated states---they just oscillate mechanically in response to the direction of their target. These oscillations push them around in the dish.

Some narjillos are decent swimmers, but most are not. You'll see many who cannot even move, or tend to move in the opposite direction they're aiming at. Those who cannot reach food will eventually get exhausted and die.

Bigger narjillos are born with more energy, but they tend to consume it faster as they push their large bodies around the dish. In general, more movement consumes more energy. The maximum energy of a narjillo also decreases with age, so even narjillos who can't move will eventually lose energy and die.

Look at a narjillo's eye color to get a clue about its energy level. Fully healthy, energized narjillos have a bright green eye, that becomes darker as they get exhausted. Also, they become transparent once they're nearing death.

(A note for Artificial Life connoisseurs: narjillos have no "fitness function". The fitter narjillos are simply the ones that happen to be better at reaching food.)

####Mutation

Each time a narjillo eats a piece of food, it spawns a "child" nearby. To do that, it makes a copy of its own genotype, and then generates a new phenotype out of it.

However, here is a twist: when the narjillo copies its genotype, it introduces random errors (called "mutations") in the copy. These errors mean that, while the copy probably resembles the original, it also has a few different genes---which result in a slightly different body shape, movement style, or color.

A child can also have entirely new (random) chromosomes, or lose some of the chromosomes of its parent. This generally results in more, or less, segments in the child's body compared to its parent. However, the same old rule applies: the child's genotype is similar to the parent's genotype, so the child is going to resemble the parent. As grandma used to say, "the apple doesn't fall far from the tree".

Phenotypic variation, Selection, Mutation... done! Now that you know about the three ingredients, let's put them together.

###The Circle of Life

Over time, narjillos who happen to be good at swimming tend to survive longer and reproduce more often than narjillos who are not as good. Because they reproduce, they generate offspring that are themselves good swimmers---but because of mutations, the offspring can be a little better, or a little worse, than their parents. The better offspring tend to survive longer and reproduce faster than the worse offspring, and the cycle continues: bad swimmers tend to go extinct, while better swimmers tend to prosper, and go on to generate even better swimmers.

We have a single word to describe this entire process: _evolution_.

After a few tens of minutes of running an experiment (at high speed), you'll likely see one "family" of related narjillos emerge and slowly take over the pond, while less successful genotypes go extinct. As you keep the program running, possibly for a few hours or days, this dominating DNA strand will subtly mutate to generate bodies that are better and better at swimming---sometimes in ways that I myself wasn't expecting.

The resulting creatures will look like they've been designed by a human intelligence intent on creating good swimmers. That's not the case. On the other hand, the more successfull narjillos are very unlikely to appear by just generating random genes from scratch: there are billions of possible genotypes, and only a few of those genotypes generate good swimmer phenotypes. This is neither a case of intelligent design, nor a case of blind luck. The narjillos are _evolving_ their ability to swim well.

## Goal

The ultimate goal of Narjillos is to trigger an _evolutionary arms race_: two or more distinct "species" of creatures becoming more and more complex by leapfrogging over each other.

That's a long way to go. In the current version, narjillos do not interact with other narjillos, apart from stealing food from each other---so an arms race can hardly happen. However, I'm having a lot of fun along the way.

## Credits

Narjillos was inspired by [Jeffrey Ventrella](http://en.wikipedia.org/wiki/Jeffrey_Ventrella)'s beautiful [GenePool](http://www.swimbots.com). "Inspired" is an understatement: Narjillos started its life as a GenePool clone. Narjillos has a different goal than GenePool (exploring arms races rather than exploring sexual selection), and it adopts different strategies. However, you should definitely check out GenePool and Ventrella's [other works](http://www.ventrella.com/) if you like the concept of Narjillos.

And of course, I'd never have thought of anything like this program if I hadn't been an avid reader of [Richard Dawkins](http://en.wikipedia.org/wiki/Richard_Dawkins). [The Selfish Gene](http://www.amazon.com/The-Selfish-Gene-Richard-Dawkins/dp/0192860925) can be a life-changing book. If you haven't read it, do yourself a favor and buy a copy now.
