package org.nusco.narjillos;

import org.nusco.narjillos.application.MainNarjillosApplication;

/**
 * The entry point to "narjllos".
 */
public class NarjillosRunner extends MainNarjillosApplication {

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
