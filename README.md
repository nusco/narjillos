#Narjillos

Narjillos are artificial creatures that mutate and evolve in a virtual pond, under the pressure of natural selection.
You can observe them through a microscope. ![Take a peek](docs/title.jpg&raw=true).

This project owes essentially all of its good ideas to Jeffrey Ventrella's GenePool (see credits).

## Goal

The ultimate goal of Narjillos is to trigger an evolutionary arms race: two or more distinct "species" of creatures becoming more and more complex by leapfrogging over each other. If this ever happens, the resulting species will look like "good design" to human eyes.

##How to run it

To put a Petri dish full of Narjillos under your microscope, you need Java 8 and git:

    git clone https://github.com/nusco/narjillos.git
    cd narjillos
    ./gradlew build
    petri

Move the microscope around with your mouse and cursor keys.

## Credits

Narjillos is strongly inspired by Jeffrey Ventrella's beautiful [GenePool](http://www.swimbots.com). "Strongly inspired" is an understatement here---Narjillos started its life as a feeble attempt to clone GenePool and bask in its greatness.

Narjillos has a different goal than GenePool (exploring arms races rather than exploring sexual selection), and a different strategy (eventually I want to give Narjillos simple neural networks for brains). GenePool is not open source, so I wrote my own code to toy with. However, you should definitely check out GenePool and Ventrella's other works if you like the concept.

And of course, I'd never have thought of anything like this if I hadn't been an avid reader of Richard Dawkins' books. "The Selfish Gene" is a life changer. If you haven't read it, stop whatever you're doing and buy a copy now.
