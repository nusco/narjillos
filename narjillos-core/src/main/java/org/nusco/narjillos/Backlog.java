package org.nusco.narjillos;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

/**
 * Utility program to print the first prioritized items in the backlog.
 */
public class Backlog {
	public static void main(String[] args) throws IOException {
		final String BACKLOG_FILE = "backlog.md";
		List<String> lines = loadEntries(BACKLOG_FILE);

		System.out.println("=================");
		System.out.println("Narjillos Backlog");
		System.out.println("=================\n");
		print(lines, getNumberOfEntriesToPrint(args));
		System.out.println();
	}

	private static List<String> loadEntries(final String BACKLOG_FILE) throws IOException {
		return Files.readAllLines(Paths.get(BACKLOG_FILE), Charset.forName("UTF-8"));
	}

	private static int getNumberOfEntriesToPrint(String[] args) {
		return args.length > 0 ? Integer.parseInt(args[0]) : Integer.MAX_VALUE;
	}

	private static void print(List<String> lines, int numberOfEntriesToPrint) {
		int items = 0;
		for (String line : lines) {
			System.out.println(line);
			if (line.startsWith("*"))
				items++;
			if (items > numberOfEntriesToPrint)
				return;
		}
	}
}
