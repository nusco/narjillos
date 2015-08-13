package org.nusco.narjillos;

import org.nusco.narjillos.application.DNABrowserApplication;

/**
 * The entry point to the "dnabrowser" program.
 */
public class DNABrowserRunner extends DNABrowserApplication {

	public static void main(String... args) throws Exception {
		setProgramArguments(args);
		launch(args);
	}
}
