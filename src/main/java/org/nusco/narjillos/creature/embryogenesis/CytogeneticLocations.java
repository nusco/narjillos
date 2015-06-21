package org.nusco.narjillos.creature.embryogenesis;

/**
 * The encoding of phenotipic characters in a chromosome's genes.
 */
interface CytogeneticLocations {
	// Some locations (such as 4 and 5) are pleiotropic: they encode different
	// characters, depending on whether they're in the head or a non-head body
	// part. (Strictly speaking, this is not what "pleiotropic" really means -
	// but I couldn't resist using the word). In those cases, the comments
	// specify whether the gene expresses that behavior in the head or in a
	// body part.

	// The building instruction in the body plan program
	static final int BODY_PLAN_INSTRUCTION = 0;

	// The atmospheric element that the creature consumes
	static final int CONSUMED_ELEMENT = 1;

	// The body segment's geometry
	static final int LENGTH = 2;
	static final int THICKNESS = 3;

	// In head: how fast the creature moves
	static final int METABOLIC_RATE = 4;

	// In body part: how fast the body segment reacts on the movement command
	static final int DELAY = 4;

	// In head: how much of its own energy the creature transfers to an egg
	static final int ENERGY_TO_CHILDREN = 5;

	// In body parts: how wide the body segment's movement is
	static final int AMPLITUDE = 5;

	// In head: the initial velocity of the creature's egg
	static final int EGG_VELOCITY = 6;

	// In body parts: the angle at rest with the previous body segment
	static final int ANGLE_TO_PARENT = 6;

	// In head: the average interval between laying eggs
	static final int EGG_INTERVAL = 7;

	// In body parts: how much the body part skews its movement when the
	// creature's target is off to the side
	static final int SKEWING = 7;

	// In head: the fiber components
	static final int RED = 8;
	static final int GREEN = 9;
	static final int BLUE = 10;

	// In body parts: the fiber shift components
	static final int RED_SHIFT = 8;
	static final int GREEN_SHIFT = 9;
	static final int BLUE_SHIFT = 10;
}
