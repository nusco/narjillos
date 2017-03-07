package org.nusco.narjillos.creature.embryogenesis;

/**
 * The encoding of phenotypic characters in a chromosome's genes.
 * <p>
 * Note that some locations are pleiotropic: they encode different
 * characters, depending on whether they're in the head or a non-head body
 * part. (Strictly speaking, this is not what "pleiotropic" really means -
 * but I couldn't resist using the word).
 */
interface CytogeneticLocations {

	// IN ALL BODY SEGMENTS:
	// The instruction and parameter in the body plan program
	int BODY_PLAN_INSTRUCTION = 0;
	int BODY_PLAN_PARAMETER = 1;
	// The segment's geometry
	int LENGTH = 2;
	int THICKNESS = 3;

	// IN HEAD ONLY:
	// The fiber components
	int RED = 4;
	int GREEN = 5;
	int BLUE = 6;
	// How fast the creature moves
	int METABOLIC_RATE = 7;
	// The ratio between the negative and positive wave semiperiods
	int WAVE_BEAT_RATIO = 8;
	// The atmospheric element that the creature produces when breathing
	int BYPRODUCT = 9;
	// How much of its own energy the creature transfers to an egg
	int ENERGY_TO_CHILDREN = 10;
	// The initial velocity of the creature's egg
	int EGG_VELOCITY = 11;
	// The average interval between laying eggs
	int EGG_INTERVAL = 12;
	int _UNUSED_1 = 13;

	// IN BODY PARTS ONLY:
	// The fiber shift components
	int RED_SHIFT = 4;
	int GREEN_SHIFT = 5;
	int BLUE_SHIFT = 6;
	// The angle at rest with the previous body segment
	int ANGLE_TO_PARENT = 7;
	// The segment's wave delay
	int DELAY = 8;
	// How wide the segment's movement is
	int AMPLITUDE = 9;
	// How much the segment skews its movement when the creature's target is
	// off to the side
	int SKEWING = 10;
	int _UNUSED_2 = 11;
	int _UNUSED_3 = 12;
	int _UNUSED_4 = 13;
}
