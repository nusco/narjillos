package org.nusco.narjillos.embryogenesis.bodyplan;

/**
 * The instructions in the body plan "program".
 */
public enum BodyPlanInstruction {
	// Stop building the current limb. If you're on the main limb (the one
	// where the head is), then stop building the whole body and ignore any
	// remaining segments.
	STOP,

	// Add this segment to the current limb and continue with the next segment.
	CONTINUE,

	// Branch this segment into two separate limbs, and continue building the
	// first of them. Once the first limb is fully built, proceed with the
	// second.
	BRANCH,

	// Branch this segment into two separate limbs, one of which is the mirror
	// image of the other.
	MIRROR,
}
