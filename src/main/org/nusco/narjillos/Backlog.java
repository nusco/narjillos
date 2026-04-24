package org.nusco.narjillos;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A utility program to print the top of the backlog. Just an ugly Java hack
 * until backlog.txt has its own Python utilities.
 * (See <a href="https://github.com/nusco/backlog.txt">current backlog</a>).
 */
public class Backlog {

	private static final String ANSI_RED = "\u001B[31m";

	private static final String ANSI_GREEN = "\u001B[32m";

	private static final String ANSI_YELLOW = "\u001B[33m";

	private static final String ANSI_CYAN = "\u001B[36m";

	private static final String ANSI_RESET = "\u001B[0m";

	public static void main(String[] args) throws IOException {
		final String BACKLOG_FILE = "backlog.md";
		List<Feature> features = toFeatures(loadEntries(BACKLOG_FILE));

		int numberOfUserStories = getNumberOfUserStories(features);
		String header = "Narjillos Backlog (" + numberOfUserStories + " user stories in " + features.size() + " features)";
		System.out.println(header + ".");
		System.out.println("(Look in backlog.md for details).\n");

		int featuresToPrint = getNumberOfFeaturesToPrint(args, features);
		for (int i = 0; i < featuresToPrint; i++)
			System.out.print(features.get(i).toString() + "\n");
		if (featuresToPrint < features.size())
			System.out.println("...\n");
	}

	private static int getNumberOfUserStories(List<Feature> features) {
		return features.stream()
			.mapToInt(LinkedList::size)
			.sum();
	}

	private static List<Feature> toFeatures(List<String> lines) {
		ArrayList<Feature> result = new ArrayList<>();
		LinkedList<String> linesQueue = new LinkedList<>(lines);
		while (!linesQueue.isEmpty()) {
			String line = linesQueue.pop();
			if (isFeature(line))
				result.add(new Feature(line));
			else {
				if (!result.isEmpty()) {
					Feature currentMarketFeature = result.get(result.size() - 1);
					currentMarketFeature.add(line);
				}
			}
		}
		return result;
	}

	private static boolean isFeature(String line) {
		return line.startsWith("##");
	}

	private static boolean isUserStory(String line) {
		return line.startsWith("*") || line.startsWith("+") || line.startsWith("-");
	}

	private static boolean isComment(String line) {
		if (line.isEmpty())
			return true;

		return !isFeature(line) && !isUserStory(line);
	}

	private static List<String> loadEntries(final String BACKLOG_FILE) throws IOException {
		List<String> lines = Files.readAllLines(Paths.get(BACKLOG_FILE), StandardCharsets.UTF_8);
		return lines.stream()
			.filter(line -> !isComment(line))
			.collect(Collectors.toList());
	}

	private static int getNumberOfFeaturesToPrint(String[] args, List<Feature> marketFeatures) {
		if (args.length == 0)
			return marketFeatures.size();

		String arg = args[0];

		if (arg.equals("top"))
			return 1;

		if (arg.equals("all"))
			return marketFeatures.size();

		int requiredNumber = Integer.parseInt(arg);
		return Math.min(requiredNumber, marketFeatures.size());
	}

	@SuppressWarnings("serial")
	static class Feature extends LinkedList<String> {

		public final String name;

		public Feature(String name) {
			this.name = name.replaceAll("#", "").trim();
		}

		@Override
		public String toString() {
			StringBuilder result = new StringBuilder();
			result.append(ANSI_GREEN + name + ANSI_RESET + "\n");
			for (String userStory : this) {
				String storyName = userStory
					.replaceFirst("^\\*", "")
					.replaceFirst("^\\+", "")
					.replaceFirst("^-", "")
					.trim();
				result.append(toColor(userStory) + "    " + storyName + ANSI_RESET + "\n");
			}
			return result.toString();
		}

		private String toColor(String userStory) {
			if (userStory.startsWith("*"))
				return ANSI_RED;
			else if (userStory.startsWith("+"))
				return ANSI_YELLOW;
			else
				return ANSI_CYAN;
		}
	}
}
