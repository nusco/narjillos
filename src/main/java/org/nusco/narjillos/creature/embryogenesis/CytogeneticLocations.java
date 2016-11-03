package org.nusco.narjillos.creature.embryogenesis;

/**
 * The encoding of phenotipic characters in a chromosome's genes.
 * <p>
 * Note that some locations are pleiotropic: they encode different
 * characters, depending on whether they're in the head or a non-head body
 * part. (Strictly speaking, this is not what "pleiotropic" really means -
 * but I couldn't resist using the word).
 */
interface CytogeneticLocations {

	// IN ALL BODY SEGMENTS:
	// The instruction and parameter in the body plan program
	static final int BODY_PLAN_INSTRUCTION = 0;
	static final int BODY_PLAN_PARAMETER = 1;
	// The segment's geometry
	static final int LENGTH = 2;
	static final int THICKNESS = 3;

	// IN HEAD ONLY:
	// The fiber components
	static final int RED = 4;
	static final int GREEN = 5;
	static final int BLUE = 6;
	// How fast the creature moves
	static final int METABOLIC_RATE = 7;
	// The ratio between the negative and positive wave semiperiods
	static final int WAVE_BEAT_RATIO = 8;
	// The atmospheric element that the creature produces when breathing
	static final int BYPRODUCT = 9;
	// How much of its own energy the creature transfers to an egg
	static final int ENERGY_TO_CHILDREN = 10;
	// The initial velocity of the creature's egg
	static final int EGG_VELOCITY = 11;
	// The average interval between laying eggs
	static final int EGG_INTERVAL = 12;
	static final int _UNUSED_1 = 13;

	// IN BODY PARTS ONLY:
	// The fiber shift components
	static final int RED_SHIFT = 4;
	static final int GREEN_SHIFT = 5;
	static final int BLUE_SHIFT = 6;
	// The angle at rest with the previous body segment
	static final int ANGLE_TO_PARENT = 7;
	// The segment's wave delay
	static final int DELAY = 8;
	// How wide the segment's movement is
	static final int AMPLITUDE = 9;
	// How much the segment skews its movement when the creature's target is
	// off to the side
	static final int SKEWING = 10;
	static final int _UNUSED_2 = 11;
	static final int _UNUSED_3 = 12;
	static final int _UNUSED_4 = 13;
}
