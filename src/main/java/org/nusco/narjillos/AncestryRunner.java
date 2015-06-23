package org.nusco.narjillos;

import org.nusco.narjillos.application.AncestryApplication;


/**
 * The entry point to the ancestry program.
 */
public class AncestryRunner extends AncestryApplication {

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
