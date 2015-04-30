package org.nusco.narjillos;

import org.nusco.narjillos.application.CommandLineOptions;
import org.nusco.narjillos.application.PetriDish;
import org.nusco.narjillos.core.utilities.Configuration;
import org.nusco.narjillos.serializer.Persistence;

/**
 * The entry point to "experiment".
 */
public class ExperimentRunner {
	public static void main(String... args) {
		CommandLineOptions options = CommandLineOptions.parse(args);
		if (options == null)
			System.exit(1);
		
		String applicationVersion = Persistence.readApplicationVersion();
		final PetriDish lab = new PetriDish(applicationVersion, options, Configuration.ECOSYSTEM_BLOCKS_PER_EDGE_IN_EXPERIMENT * 1000);

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				lab.terminate();
			}
		});

		while (lab.tick())
			;
		System.exit(0);
	}
}
