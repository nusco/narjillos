package org.nusco.narjillos.embryogenesis;

interface Gene {
	static final int UNUSED = 0;
	static final int LENGTH = 1;
	static final int THICKNESS = 2;
	static final int METABOLIC_RATE_OR_DELAY = 3;
	static final int PERCENT_ENERGY_TO_CHILDREN_OR_HUE = 4; // TODO: these are both used in head. split them.
	static final int ANGLE_TO_PARENT = 5;
	static final int AMPLITUDE = 6;
	static final int SKEWING = 7;
	static final int UNUSED_2 = 8;

}
