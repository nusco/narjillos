package org.nusco.narjillos;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Utility program to print the first prioritized items in the backlog.
 */
public class Backlog {
	public static void main(String[] args) throws IOException {
		final String BACKLOG_FILE = "backlog.md";
		List<String> lines = loadEntries(BACKLOG_FILE);
		sortByRanking(lines);
		print(lines, getNumberOfEntriesToPrint(args));
	}

	private static List<String> loadEntries(final String BACKLOG_FILE) throws IOException {
		List<String> result = new LinkedList<>();

		List<String> lines = Files.readAllLines(Paths.get(BACKLOG_FILE), Charset.forName("UTF-8"));

		String currentUser = "";
		for (String line : lines) {
			if (line.startsWith("#"))
				currentUser = extractPaddedCurrentUser(line);
			else if (line.startsWith("*"))
				result.add(currentUser + line.replaceAll("^\\*", ""));
		}
		
		return result;
	}

	private static String extractPaddedCurrentUser(String line) {
		String user = "[" + line.replaceAll("^#+", "") + "]";
		final String padding = "                  ";
		return (user + padding).substring(0, padding.length());
	}

	private static void sortByRanking(List<String> lines) {
		lines.sort(new Comparator<String>() {

			@Override
			public int compare(String line1, String line2) {
				int line1Ranking = rank(line1);
				int line2Ranking = rank(line2);
				return line2Ranking - line1Ranking;
			}

			private int rank(String line) {
				return line.length() - line.replaceAll("°+$", "").length();
			}
		});
	}

	private static int getNumberOfEntriesToPrint(String[] args) {
		return args.length > 0 ? Integer.parseInt(args[0]) : Integer.MAX_VALUE;
	}

	private static void print(List<String> lines, int numberOfEntriesToPrint) {
		Iterator<String> linesIterator = lines.iterator();
		for (int i = 0; i < numberOfEntriesToPrint && linesIterator.hasNext(); i++)
			System.out.println(linesIterator.next());
	}
}
