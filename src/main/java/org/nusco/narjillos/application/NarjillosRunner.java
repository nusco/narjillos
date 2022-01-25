package org.nusco.narjillos.application;

import org.nusco.narjillos.core.configuration.Configuration;
import org.nusco.narjillos.core.utilities.Version;

/**
 * The entry point to the "narjillos" program.
 */
public class NarjillosRunner {

	public static void main(String... args) throws Exception {
		CommandLineOptions options = CommandLineOptions.parse(false, args);
		if (options == null)
			System.exit(1);

		if (options.isFast())
			runWithoutGraphics(options);
		else
			MainNarjillosApplication.main(args);
	}

	private static void runWithoutGraphics(CommandLineOptions options) {
		String applicationVersion = Version.read();
		final PetriDish dish = new PetriDish(applicationVersion, options, Configuration.ECOSYSTEM_BLOCKS_PER_EDGE_IN_EXPERIMENT * 1000);

		Runtime.getRuntime().addShutdownHook(new Thread(dish::terminate));

		while (dish.tick())
			;
		System.exit(0);
	}
}
