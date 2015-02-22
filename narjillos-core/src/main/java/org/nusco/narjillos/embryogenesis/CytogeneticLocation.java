package org.nusco.narjillos.embryogenesis;

/**
 * The encoding of phenotipic characters in a chromosome's genes.
 */
interface CytogeneticLocation {
	// Some locations (such as 4 and 5) are pleiotropic: they encode different
	// characters, depending on whether they're in the head or a body segment.
	// (Strictly speaking, this is not what "pleiotropic" means - but I couldn't
	// resist using the word).

	// The building instruction in the body plan program.
	static final int BODY_PLAN_INSTRUCTION = 0;

	// Reserved for later use.
	static final int _RESERVED = 1;

	// The body segment's geometry.
	static final int LENGTH = 2;
	static final int THICKNESS = 3;

	// (Only for the head chromosome): how fast the creature moves.
	static final int METABOLIC_RATE = 4;

	// How fast the body segment reacts on the movement command.
	static final int DELAY = 4;

	// (Only for the head chromosome): how much of its own energy the creature
	// transfers to an egg.
	static final int PERCENT_ENERGY_TO_CHILDREN = 5;

	// How wide the body segment's movement is.
	static final int AMPLITUDE = 5;

	// The angle at rest with the previous body segment.
	static final int ANGLE_TO_PARENT = 6;

	// How much the segment skews its movement when the creature's target is off
	// to the side.
	static final int SKEWING = 7;

	// The fiber components of the segment.
	static final int RED = 8;
	static final int GREEN = 9;
	static final int BLUE = 10;

	// The fiber shift components of the segment.
	static final int RED_SHIFT = 8;
	static final int GREEN_SHIFT = 9;
	static final int BLUE_SHIFT = 10;
}
