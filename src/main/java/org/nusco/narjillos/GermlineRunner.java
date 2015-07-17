package org.nusco.narjillos;

import org.nusco.narjillos.application.GermlineApplication;

/**
 * The entry point to the "germline" program.
 */
public class GermlineRunner extends GermlineApplication {

	public static void main(String... args) throws Exception {
		setProgramArguments(args);
		launch(args);
	}
}
