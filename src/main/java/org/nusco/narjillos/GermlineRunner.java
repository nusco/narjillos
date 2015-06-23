package org.nusco.narjillos;

import org.nusco.narjillos.application.GermlineApplication;


/**
 * The entry point to the germline program.
 */
public class GermlineRunner extends GermlineApplication {

	public static String[] programArguments = new String[0];

	@Override
	protected String[] getProgramArguments() {
		return programArguments;
	}

	public static void main(String... args) throws Exception {
		programArguments = args;
		launch(args);
	}
}
