package org.nusco.narjillos.embryogenesis;

interface CytogeneticLocation {
	static final int BODY_PLAN = 0;
	static final int _RESERVED = 1;
	static final int LENGTH = 2;
	static final int THICKNESS = 3;
	
	// pleiotropic location
	static final int METABOLIC_RATE = 4;
	static final int DELAY = 4;
	
	// pleiotropic location
	static final int PERCENT_ENERGY_TO_CHILDREN = 5;
	static final int AMPLITUDE = 5;

	static final int ANGLE_TO_PARENT = 6;
	static final int SKEWING = 7;
	static final int HUE = 8;
}
