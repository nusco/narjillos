package org.nusco.narjillos;

import org.apache.commons.cli.*;
import org.nusco.narjillos.analysis.DNAAnalyzer;
import org.nusco.narjillos.analysis.DNAExporter;
import org.nusco.narjillos.experiment.Experiment;
import org.nusco.narjillos.experiment.ExperimentHistoryEntry;
import org.nusco.narjillos.experiment.HistoryLog;
import org.nusco.narjillos.genomics.DNA;
import org.nusco.narjillos.persistence.ExperimentLoader;
import org.nusco.narjillos.persistence.PersistentDNALog;
import org.nusco.narjillos.persistence.PersistentHistoryLog;

import java.io.IOException;
import java.util.List;

/**
 * The "lab" program. It reads data from an experiment and does analysis and
 * conversions.
 */
class Lab {

	public static void main(String[] args) throws IOException {
		Options options = new Options();
		options.addOption("?", "help", false, "print this message");
		options.addOption("d", "dna", true, "print DNA (takes a DNA id)");
		options.addOption("D", "dnastats", true, "print DNA stats (takes a DNA id)");
		options.addOption("g", "germline", true, "print DNA germline (takes a DNA id)");
		options.addOption("p", "primary", false, "print id of primary (most successful) DNA");
		options.addOption("s", "stats", false, "print current statistics");
		options.addOption("h", "history", false, "output history as CSV");
		options.addOption("G", "germlines-count", false, "print number of living germlines");
		options.addOption("c", "csv", false, "output ancestry as CSV ('-Xmx' for memory)");
		options.addOption("n", "nexus", false, "output ancestry as NEXUS ('-Xss' for deep stack)");

		try {
			CommandLine commandLine;
			try {
				commandLine = new DefaultParser().parse(options, args);
			} catch (ParseException e) {
				printHelpText(options);
				return;
			}

			if (args.length == 0 || args[0].startsWith("-") || commandLine == null || commandLine.hasOption("?")) {
				printHelpText(options);
				return;
			}

			String databaseFile = args[0];
			Experiment experiment = ExperimentLoader.load(databaseFile, true);
			HistoryLog historyLog = new PersistentHistoryLog(experiment.getId());
			DNAAnalyzer dnaAnalyzer = new DNAAnalyzer(new PersistentDNALog(experiment.getId()));

			if (commandLine.hasOption("dna"))
				System.out.println(getDna(dnaAnalyzer, commandLine.getOptionValue("dna")));
			else if (commandLine.hasOption("dnastats"))
				System.out.println(getDNAStats(dnaAnalyzer, commandLine.getOptionValue("dnastats")));
			else if (commandLine.hasOption("germline"))
				dumpGermline(dnaAnalyzer, commandLine.getOptionValue("germline"));
			else if (commandLine.hasOption("primary"))
				System.out.println(getPrimaryDNAId(dnaAnalyzer));
			else if (commandLine.hasOption("stats"))
				System.out.println(historyLog.getLatestEntry());
			else if (commandLine.hasOption("history"))
				dumpHistory(historyLog);
			else if (commandLine.hasOption("germlines-count"))
				System.out.println(dnaAnalyzer.getNumberOfLivingGermlines());
			else if (commandLine.hasOption("csv"))
				System.out.print(new DNAExporter(dnaAnalyzer).toCSVFormat());
			else if (commandLine.hasOption("nexus"))
				System.out.println(new DNAExporter(dnaAnalyzer).toNEXUSFormat());
			else
				printHelpText(options);
		} catch (RuntimeException e) {
			System.out.println(e.getMessage());
		}
	}

	private static String getDNAStats(DNAAnalyzer dnaAnalyzer, String dnaId) {
		return dnaAnalyzer.getDNAStatistics(getDna(dnaAnalyzer, dnaId));
	}

	private static void dumpGermline(DNAAnalyzer dnaAnalyzer, String dnaId) {
		DNA dna = getDna(dnaAnalyzer, dnaId);
		List<DNA> germline = dnaAnalyzer.getGermline(dna);
		for (DNA ancestor : germline)
			System.out.println(ancestor);
	}

	private static long getPrimaryDNAId(DNAAnalyzer dnaAnalyzer) {
		DNA dna = dnaAnalyzer.getMostSuccessfulDna();
		validateDna(dna);
		return dna.getId();
	}

	private static void dumpHistory(HistoryLog historyLog) {
		System.out.println(ExperimentHistoryEntry.toCsvHeader());
		for (ExperimentHistoryEntry stat : historyLog.getEntries())
			System.out.println(stat);
	}

	private static DNA getDna(DNAAnalyzer dnaAnalyzer, String id) {
		Long dnaId = Long.parseLong(id);
		DNA dna = dnaAnalyzer.getDna(dnaId);
		validateDna(dna);
		return dna;
	}

	private static void validateDna(DNA dna) {
		if (dna == null)
			throw new RuntimeException("DNA not found");
	}

	private static void printHelpText(Options commandLineOptions) {
		new HelpFormatter().printHelp("lab <experiment_file.exp> <options>", commandLineOptions);
	}
}
