package org.nusco.narjillos;

import java.io.IOException;
import java.util.List;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.nusco.narjillos.analysis.DNAAnalyzer;
import org.nusco.narjillos.analysis.DNAExporter;
import org.nusco.narjillos.experiment.Experiment;
import org.nusco.narjillos.experiment.ExperimentHistoryEntry;
import org.nusco.narjillos.experiment.HistoryLog;
import org.nusco.narjillos.genomics.DNA;
import org.nusco.narjillos.persistence.ExperimentLoader;
import org.nusco.narjillos.persistence.PersistentDNALog;
import org.nusco.narjillos.persistence.PersistentHistoryLog;

/**
 * The "lab" program. It reads data from an experiment and outputs it in various
 * formats.
 *
 * At the moment, it only does ancestry analysis: it reads the gene pool,
 * identifies the most successful DNA in the pool, and prints out its entire
 * ancestry - from the first randomly generates ancestor onwards.
 */
public class Lab {

	public static void main(String[] args) throws IOException {
		Options options = new Options();
		options.addOption("?", "help", false, "print this message");
		options.addOption("d", "dna", true, "print DNA (takes a DNA id)");
		options.addOption("D", "dnastats", true, "print DNA stats (takes a DNA id)");
		options.addOption("g", "germline", true, "print DNA germline (takes a DNA id)");
		options.addOption("p", "primary", false, "print id of primary (most successful) DNA");
		options.addOption("s", "stats", false, "print current statistics");
		options.addOption("h", "history", false, "output history in CSV format");
		options.addOption("c", "csv", false, "output ancestry in CSV format");
		options.addOption("n", "nexus", false, "output ancestry in NEXUS format (needs deep Java stack)");

		CommandLine commandLine;
		try {
			commandLine = new BasicParser().parse(options, args);
		} catch (ParseException e) {
			printHelpText(options);
			return;
		} catch (RuntimeException e) {
			System.out.println(e.getMessage());
			return;
		}

		if (args.length == 0 || args[0].startsWith("-") || commandLine == null || commandLine.hasOption("?")) {
			printHelpText(options);
			return;
		}

		String databaseFile = args[0];
		Experiment experiment = ExperimentLoader.load(databaseFile);
		DNAAnalyzer dnaAnalyzer = new DNAAnalyzer(new PersistentDNALog(experiment.getId()));
		HistoryLog historyLog = new PersistentHistoryLog(experiment.getId());


		if (commandLine.hasOption("history")) {
			System.out.println(ExperimentHistoryEntry.toCsvHeader());
			for (ExperimentHistoryEntry stat : historyLog.getEntries())
				System.out.println(stat);
			return;
		}

		if (commandLine.hasOption("stats")) {
			System.out.println(historyLog.getLatestEntry());
			return;
		}

		if (commandLine.hasOption("primary")) {
			DNA dna = dnaAnalyzer.getMostSuccessfulDna();
			if (dna == null) {
				System.out.println("DNA not found");
				System.exit(-1);
			}
			System.out.println(dna.getId());
			return;
		}

		if (commandLine.hasOption("dna")) {
			DNA dna = getDna(dnaAnalyzer, commandLine.getOptionValue("dna"));
			System.out.println(dna);
			return;
		}

		if (commandLine.hasOption("dnastats")) {
			DNA dna = getDna(dnaAnalyzer, commandLine.getOptionValue("dnastats"));
			System.out.println(dnaAnalyzer.getDNAStatistics(dna));
			return;
		}

		if (commandLine.hasOption("germline")) {
			DNA dna = getDna(dnaAnalyzer, commandLine.getOptionValue("germline"));
			List<DNA> germline = dnaAnalyzer.getGermline(dna);
			for (DNA ancestor : germline)
				System.out.println(ancestor);
			return;
		}

		if (commandLine.hasOption("csv")) {
			System.out.print(new DNAExporter(dnaAnalyzer).toCSVFormat());
			return;
		}

		if (commandLine.hasOption("nexus")) {
			System.out.println(new DNAExporter(dnaAnalyzer).toNEXUSFormat());
			return;
		}

		printHelpText(options);
	}

	private static DNA getDna(DNAAnalyzer dnaAnalyzer, String id) {
		Long dnaId = Long.parseLong(id);
		DNA dna = dnaAnalyzer.getDna(dnaId);
		if (dna == null) {
			System.out.println("DNA not found");
			System.exit(1);
		}
		return dna;
	}

	private static void printHelpText(Options commandLineOptions) {
		new HelpFormatter().printHelp("lab <experiment_file.exp> <options>", commandLineOptions);
	}
}
