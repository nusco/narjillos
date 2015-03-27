package org.nusco.narjillos.shared.utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;


public class Configuration {

	private static Map<String, Map<String, Double>> data = loadConfigurationData();
	
	@SuppressWarnings("unchecked")
	private static Map<String, Map<String, Double>> loadConfigurationData() {
		Map<String, Map<String, Double>> data = new HashMap<>();
		
		Map<String, Double> physics = new HashMap<>();
		physics.put("viscosity_kick_in_velocity", 300.0);
		physics.put("collision_distance", 60.0);
		physics.put("energy_expense_per_joule", 0.1);
		physics.put("metabolic_consumption_pow", 1.5);
		data.put("physics", physics);

		Map<String, Double> dna = new HashMap<>();
		dna.put("mutation_rate", 0.067);
		dna.put("mutation_range", 15.0);
		data.put("dna", dna);

		Map<String, Double> creature = new HashMap<>();
		creature.put("max_lifespan", 100_000.0);
		creature.put("mature_age", 5000.0);
		creature.put("max_energy_to_initial_energy", 5.0);
		creature.put("base_wave_frequency", 0.01);
		creature.put("wave_beat_ratio", 2.0);
		creature.put("base_skewing_velocity", 0.1);
		creature.put("lateral_viewfield", 135.0);
		creature.put("seed_energy", 25_000.0);
		creature.put("min_energy_to_children", 10_000.0);
		creature.put("blue_fibers_extra_push", 0.0);
		creature.put("green_fibers_extra_energy", 0.0);
//		creature.put("blue_fibers_extra_push", 0.5);
//		creature.put("green_fibers_extra_energy", 0.5 / 10_000_000);
		data.put("creature", creature);
		
		try {
			File configurationFile = findConfigurationFile();
			return (Map<String, Map<String, Double>>) new Yaml().load(new FileReader(configurationFile));
		} catch (FileNotFoundException e) {
			System.out.println("Cannot find configuration file (config.yaml)");
			System.exit(1);
			throw new RuntimeException(e); // shut up, compiler
		}
	}

	private static File findConfigurationFile() {
		return new File(System.getProperty("user.dir") + "/config.yaml");
	}
	
	private static double getDouble(String configSection, String configKey) {
		Object result = get(configSection, configKey);
		try {
			if (result instanceof Double)
				return (Double) result;
			return new Double((int) result);
		} catch (ClassCastException e) {
			System.out.println("I expected " + configSection + ":" + configKey + " to be a number. It's not.");
			System.exit(1);
			throw new RuntimeException(e);
		}
	}
	
	private static int getInt(String configSection, String configKey) {
		Object result = get(configSection, configKey);
		try {
			return (int) result;
		} catch (ClassCastException e) {
			System.out.println("I expected " + configSection + ":" + configKey + " to be an integer number. It's not.");
			System.exit(1);
			throw new RuntimeException(e);
		}
	}

	private static Object get(String configSection, String configKey) {
		Map<String, Double> section = data.get(configSection);
		if (section == null) {
			System.out.println("Cannot find section " + configSection + " in configuration. Exiting...");
			System.exit(1);
		}
		Object result = section.get(configKey);
		if (result == null) {
			System.out.println("Cannot find value " + configSection + ":" + configKey + " in configuration. Exiting...");
			System.exit(1);
		}
		return result;
	}

	// physics
	public static final double PHYSICS_VISCOSITY_KICKIN_VELOCITY = getDouble("physics", "viscosity_kickin_velocity");
	public static final double PHYSICS_COLLISION_DISTANCE = getDouble("physics", "collision_distance");
	public static final double PHYSICS_ENERGY_EXPENSE_PER_JOULE = getDouble("physics", "energy_expense_per_joule");
	public static final double PHYSICS_METABOLIC_CONSUMPTION_POW = getDouble("physics", "metabolic_consumption_pow");

	// genes
	public static final double DNA_MUTATION_RATE = getDouble("dna", "mutation_rate");
	public static final double DNA_MUTATION_RANGE = getDouble("dna", "mutation_range");
	
	// creatures
	public static final double CREATURE_MAX_LIFESPAN = getDouble("creature", "max_lifespan");
	public static final double CREATURE_MATURE_AGE = getDouble("creature", "mature_age");
	public static final double CREATURE_MAX_ENERGY_TO_INITIAL_ENERGY = getDouble("creature", "max_energy_to_initial_energy");
	public static final double CREATURE_BASE_WAVE_FREQUENCY = getDouble("creature", "base_wave_frequency");
	public static final double CREATURE_WAVE_BEAT_RATIO = getDouble("creature", "wave_beat_ratio");
	public static final double CREATURE_BASE_SKEWING_VELOCITY = getDouble("creature", "base_skewing_velocity");
	public static final double CREATURE_LATERAL_VIEWFIELD = getDouble("creature", "lateral_viewfield");
	public static final double CREATURE_SEED_ENERGY = getDouble("creature", "seed_energy");
	public static final double CREATURE_MIN_ENERGY_TO_CHILDREN = getDouble("creature", "min_energy_to_children");
	public static final double CREATURE_BLUE_FIBERS_EXTRA_PUSH = getDouble("creature", "blue_fibers_extra_push");
	public static final double CREATURE_GREEN_FIBERS_EXTRA_ENERGY = getDouble("creature", "green_fibers_extra_energy");

	// organs
	public static final double ORGAN_MINIMUM_LENGTH_AT_BIRTH = getInt("organ", "minimum_length_at_birth");
	public static final double ORGAN_MINIMUM_THICKNESS_AT_BIRTH = getInt("organ", "minimum_thickness_at_birth");
	public static final double ORGAN_GROWTH_RATE = getDouble("organ", "growth_rate");

	// eggs
	public static final double EGG_RADIUS = getInt("egg", "radius");
	public static final int EGG_MIN_INCUBATION_TIME = getInt("egg", "min_incubation_time");
	public static final int EGG_MAX_INCUBATION_TIME = getInt("egg", "max_incubation_time");
	public static final double EGG_MASS = getDouble("egg", "mass");
	public static final double EGG_MIN_VELOCITY = getDouble("egg", "min_velocity");
	public static final double EGG_VELOCITY_DECAY = getDouble("egg", "velocity_decay");

	// food
	public static final double FOOD_RADIUS = getInt("food", "radius");
	public static final double FOOD_ENERGY = getInt("food", "energy");

	// ecosystem
	public final static int ECOSYSTEM_BLOCKS_PER_EDGE_IN_APP = getInt("ecosystem", "blocks_per_edge_in_app");
	public final static int ECOSYSTEM_BLOCKS_PER_EDGE_IN_EXPERIMENT = getInt("ecosystem", "blocks_per_edge_in_experiment");
	public static final double ECOSYSTEM_EGGS_DENSITY_PER_BLOCK = getDouble("ecosystem", "eggs_density_per_block");
	public static final double ECOSYSTEM_FOOD_DENSITY_PER_BLOCK = getDouble("ecosystem", "food_density_per_block");
	public static final double ECOSYSTEM_MAX_FOOD_DENSITY_PER_1000_BLOCK = getDouble("ecosystem", "max_food_density_per_1000_blocks");
	public static final double ECOSYSTEM_FOOD_RESPAWN_AVERAGE_INTERVAL_PER_BLOCK = getInt("ecosystem", "food_respawn_average_interval_per_block");
	public static final int ECOSYSTEM_UPDATE_FOOD_TARGETS_INTERVAL = getInt("ecosystem", "update_food_targets_interval");

	// experiment
	public static final int EXPERIMENT_SAMPLE_INTERVAL_TICKS = getInt("experiment", "sample_interval_ticks");
	public static final int EXPERIMENT_SAVE_INTERVAL_SECONDS = getInt("experiment", "sample_interval_ticks");
}
