package org.nusco.narjillos.shared.utilities;


public class Configuration {

	// physics
	public static final double PHYSICS_ENERGY_SCALE = 1.0 / 75_000_000_000L;
	public static final double PHYSICS_VISCOSITY_KICK_IN_VELOCITY = 300;
	public static final double PHYSICS_COLLISION_DISTANCE = 60;

	// genes
	public static final double MUTATION_RATE = 0.067;
	public static final int GENE_MUTATION_RANGE = 15;
	
	// creatures
	public static final double CREATURE_MAX_LIFESPAN = 30_000;
	public static final double CREATURE_MAX_ENERGY_TO_INITIAL_ENERGY = 5;
	public static final double CREATURE_BLUE_FIBERS_EXTRA_PUSH = 0.5;
	public static final double CREATURE_BASE_WAVE_FREQUENCY = 0.01;
	public static final double CREATURE_WAVE_BEAT_RATIO = 2;
	public static final double CREATURE_SKEWING_VELOCITY_RATIO = 0.1;
	public static final double CREATURE_LATERAL_VIEWFIELD = 135;

	// organs
	public static final int ORGAN_MINIMUM_LENGTH_AT_BIRTH = 5;
	public static final int ORGAN_MINIMUM_THICKNESS_AT_BIRTH = 1;
	public static final double ORGAN_GROWTH_RATE = 0.01;

	// experiment
	public static final int EXPERIMENT_SAMPLE_INTERVAL = 10000;

	// ecosystem
	public final static int ECOSYSTEM_SIZE = 40_000;
	public static final int ECOSYSTEM_INITIAL_NUMBER_OF_FOOD_PIECES = 400;
	public static final int ECOSYSTEM_INITIAL_NUMBER_OF_EGGS = 300;
	public static final int ECOSYSTEM_MAX_FOOD_PIECES = 600;
	public static final int ECOSYSTEM_FOOD_RESPAWN_AVERAGE_INTERVAL = 100;

	// eggs
	public static final double EGG_RADIUS = 25;
	public static final int EGG_INCUBATION_TIME = 500;
	public static final int EGG_INITIAL_ENERGY = 25_000;
	public static final int EGG_MIN_INCUBATION_TIME = 400;
	public static final int EGG_MAX_INCUBATION_TIME = 800;

	// food
	public static final double FOOD_RADIUS = 7;
	public static final double FOOD_ENERGY = 30_000;

}
