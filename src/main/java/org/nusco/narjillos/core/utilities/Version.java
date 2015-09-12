package org.nusco.narjillos.core.utilities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Version {

	public static String read() {
		try {
			return Files.readAllLines(Paths.get("version")).get(0);
		} catch (IOException e) {
			return "0.0.0";
		}
	}
}
