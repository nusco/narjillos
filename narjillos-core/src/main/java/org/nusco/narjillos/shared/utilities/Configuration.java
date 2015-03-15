package org.nusco.narjillos.shared.utilities;


public class Configuration {

	// physics
	public static final double PHYSICS_VISCOSITY_KICK_IN_VELOCITY = 300;
	public static final double PHYSICS_COLLISION_DISTANCE = 60;

	// genes
	public static final double GENE_MUTATION_RATE = 0.067;
	public static final double GENE_MUTATION_RANGE = 15;
	
	// creatures
	public static final double CREATURE_MAX_LIFESPAN = 100_000;
	public static final double CREATURE_MATURE_AGE = 5_000;
	public static final double CREATURE_MAX_ENERGY_TO_INITIAL_ENERGY = 5;
	public static final double CREATURE_BLUE_FIBERS_EXTRA_PUSH = 0;
	public static final double CREATURE_GREEN_FIBERS_EXTRA_ENERGY = 0;
	// two reasonable values for when I want to experiment with fibers
//	public static final double CREATURE_BLUE_FIBERS_EXTRA_PUSH = 0.5;
//	public static final double CREATURE_GREEN_FIBERS_EXTRA_ENERGY = 4.0 / 1_000_000;
	public static final double CREATURE_BASE_WAVE_FREQUENCY = 0.01;
	public static final double CREATURE_WAVE_BEAT_RATIO = 2;
	public static final double CREATURE_BASE_SKEWING_VELOCITY = 0.1;
	public static final double CREATURE_LATERAL_VIEWFIELD = 135;
	public static final double CREATURE_MIN_PERCENT_ENERGY_TO_CHILDREN = 0.2;
	public static final double CREATURE_MIN_ENERGY_TO_LAY_EGG = 10_000;

	// energy
	public static final double ENERGY_PER_JOULE = 1.0 / 75_000_000_000L;
	public static final double ENERGY_OF_SEED_CREATURES = 25_000;
	public static final double ENERGY_PER_FOOD_ITEM = 30_000;
	public static final double ENERGY_METABOLIC_CONSUMPTION_POW = 1.5;

	// organs
	public static final double ORGAN_MINIMUM_LENGTH_AT_BIRTH = 5;
	public static final double ORGAN_MINIMUM_THICKNESS_AT_BIRTH = 1;
	public static final double ORGAN_GROWTH_RATE = 0.01;

	// experiment
	public static final int EXPERIMENT_SAMPLE_INTERVAL_TICKS = 10000;
	public static final int EXPERIMENT_SAVE_INTERVAL_SECONDS = 600;

	// ecosystem
	public final static int ECOSYSTEM_SIZE = 40_000;
	public static final double ECOSYSTEM_INITIAL_EGGS_DENSITY_PER_1000_SQUARE_POINTS = 0.18;
	public static final double ECOSYSTEM_INITIAL_FOOD_DENSITY_PER_1000_SQUARE_POINTS = 0.2;
	public static final double ECOSYSTEM_MAX_FOOD_DENSITY_PER_1000_SQUARE_POINTS = 0.35;
	public static final double ECOSYSTEM_FOOD_RESPAWN_AVERAGE_INTERVAL_PER_1000_SQUARE_POINTS = 0.003;
	public static final int ECOSYSTEM_UPDATE_FOOD_TARGETS_INTERVAL = 1000;

	// eggs
	public static final double EGG_RADIUS = 25;
	public static final int EGG_MIN_INCUBATION_TIME = 400;
	public static final int EGG_MAX_INCUBATION_TIME = 800;
	public static final double EGG_MASS = 0.1;
	public static final double EGG_MIN_VELOCITY = 0.1;
	public static final double EGG_VELOCITY_DECAY = 0.98;

	// food
	public static final double FOOD_RADIUS = 7;
}
