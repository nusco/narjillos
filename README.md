#Narjillos

Narjillos are artificial creatures that mutate and evolve in a virtual pond, under the pressure of natural selection.
You can observe them through a microscope.

![Take a peek](/doc/narjillos.jpg).

This project owes almost everything to Jeffrey Ventrella's GenePool (see credits).

## Goal

The ultimate goal of Narjillos is to trigger an evolutionary arms race: two or more distinct "species" of creatures becoming more and more complex by leapfrogging over each other. If this ever happens, the resulting species will look like "good design" to human eyes.

##How to run it

To put a Petri dish full of Narjillos under your microscope, you need Java 8 and git. To install Narjillos:

    git clone https://github.com/nusco/narjillos.git
    cd narjillos
  
  Then run with the _petri_ command:
  
    petri

The first run will also build and test the system, so it will take some time.

##How to control it

Move around with your mouse: **click anywhere** to center, **double-click** to auto-zoom. Zoom in/out by **scrolling**. You can also move the microscope with your **cursor keys**.

To turn from high speed to real time and back, press **S**.

To turn the lamp on/off, press **L**. With the lamp off, high speed is much faster (because the program doesn't need to render the creatures).

If you have trouble distinguishing the shapes of Narjillos, try infrared light. You can toggle it by pressing **I**.

## Credits

Narjillos was inspired by [Jeffrey Ventrella](http://en.wikipedia.org/wiki/Jeffrey_Ventrella)'s beautiful [GenePool](http://www.swimbots.com). "Inspired" is an understatement: Narjillos started its life as a GenePool clone. Narjillos has a different goal than GenePool (exploring arms races rather than exploring sexual selection), and it adopts different strategies (eventually I want to give Narjillos simple neural networks for brains). However, you should definitely check out GenePool and Ventrella's [other works](http://www.ventrella.com/) if you like the concept of Narjillos.

And of course, I'd never have thought of anything like this if I hadn't been an avid reader of [Richard Dawkins](http://en.wikipedia.org/wiki/Richard_Dawkins)' books. [The Selfish Gene](http://www.amazon.com/The-Selfish-Gene-Richard-Dawkins/dp/0192860925) can be a life-changing book. If you haven't read it, stop whatever you're doing and buy a copy now.
