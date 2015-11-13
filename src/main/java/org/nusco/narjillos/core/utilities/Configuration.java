package org.nusco.narjillos.core.utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class Configuration {

	private static Map<String, Map<String, Object>> data = loadConfigurationData();
	
	@SuppressWarnings("unchecked")
	private static Map<String, Map<String, Object>> loadConfigurationData() {		
		File configurationFile = locateConfigurationFile();
		try {
			return (Map<String, Map<String, Object>>) new Yaml().load(new FileReader(configurationFile));
		} catch (FileNotFoundException e) {
			fail("cannot find a configuration file (config.yaml) in the current working directory");
			return null;
		} catch (Exception e) {
			fail("cannot parse the config.yaml file: " + e.getMessage());
			return null;
		}
	}

	private static File locateConfigurationFile() {
		// First, look in the working folder
		File defaultPath = new File(System.getProperty("user.dir") + "/config.yaml");
		if (defaultPath.exists())
			return defaultPath;
			
		// The working folder mechanism is unreliable in a Mac app bundle,
		// so try an alternative. The "narjillos.home" system variable
		// is set manually in the Info.plist file of the packaged app.
		return new File(System.getProperty("narjillos.home") + "/config.yaml");
	}
	
	private static double getDouble(String configSection, String configKey) {
		Object result = get(configSection, configKey);
		try {
			if (result instanceof Double)
				return (Double) result;
			return new Double((int) result);
		} catch (ClassCastException e) {
			fail("\"" + configSection + ":" + configKey + "\" in config.yaml is not a number");
			return 0;
		}
	}
	
	private static int getInt(String configSection, String configKey) {
		Object result = get(configSection, configKey);
		try {
			return (int) result;
		} catch (ClassCastException e) {
			fail("\"" + configSection + ":" + configKey + "\" in config.yaml is not an integer number");
			return 0;
		}
	}
	
	private static Object get(String configSection, String configKey) {
		Map<String, Object> section = data.get(configSection);
		if (section == null) {
			fail("cannot find section \"" + configSection + "\" in config.yaml");
		}
		Object result = section.get(configKey);
		if (result == null) {
			fail("cannot find value \"" + configSection + ":" + configKey + "\" in config.yaml");
		}
		return result;
	}

	private static void fail(String errorMessage) {
		System.out.println("Error: " + errorMessage);
		System.exit(1);
	}

	// physics
	public static final double PHYSICS_VISCOSITY_KICKIN_VELOCITY = getDouble("physics", "viscosity_kickin_velocity");
	public static final double PHYSICS_COLLISION_DISTANCE = getDouble("physics", "collision_distance");
	public static final double PHYSICS_ENERGY_EXPENSE_PER_JOULE = getDouble("physics", "energy_expense_per_joule");
	public static final double PHYSICS_METABOLIC_CONSUMPTION_POW = getDouble("physics", "metabolic_consumption_pow");

	// dna
	public static final double DNA_MUTATION_RATE = getDouble("dna", "mutation_rate");
	public static final double DNA_MUTATION_RANGE = getDouble("dna", "mutation_range");
	public static final int DNA_NUMBER_OF_CHROMOSOMES = getInt("dna", "number_of_chromosomes");

	// creatures
	public static final double CREATURE_MAX_LIFESPAN = getDouble("creature", "max_lifespan");
	public static final double CREATURE_MATURE_AGE = getDouble("creature", "mature_age");
	public static final double CREATURE_MAX_ENERGY_TO_INITIAL_ENERGY = getDouble("creature", "max_energy_to_initial_energy");
	public static final double CREATURE_BASE_WAVE_FREQUENCY = getDouble("creature", "base_wave_frequency");
	public static final double CREATURE_BASE_SKEWING_VELOCITY = getDouble("creature", "base_skewing_velocity");
	public static final double CREATURE_LATERAL_VIEWFIELD = getDouble("creature", "lateral_viewfield");
	public static final double CREATURE_SEED_ENERGY = getDouble("creature", "seed_energy");
	public static final double CREATURE_MIN_ENERGY_TO_CHILDREN = getDouble("creature", "min_energy_to_children");

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
	public static final int ECOSYSTEM_FOOD_RESPAWN_AVERAGE_INTERVAL_PER_BLOCK = getInt("ecosystem", "food_respawn_average_interval_per_block");
	public static final int ECOSYSTEM_UPDATE_FOOD_TARGETS_INTERVAL = getInt("ecosystem", "update_food_targets_interval");
	public static final double ECOSYSTEM_INITIAL_ELEMENT_LEVEL = getDouble("ecosystem", "initial_element_level");
	public static final int ECOSYSTEM_CATALYST_LEVEL = getInt("ecosystem", "catalyst_level");

	// experiment
	public static final int EXPERIMENT_SAMPLE_INTERVAL_TICKS = getInt("experiment", "sample_interval_ticks");
	public static final int EXPERIMENT_SAVE_INTERVAL_SECONDS = getInt("experiment", "save_interval_seconds");
}
