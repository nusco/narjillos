package org.nusco.narjillos;

import java.io.IOException;
import java.util.List;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.nusco.narjillos.core.physics.Vector;
import org.nusco.narjillos.core.things.Energy;
import org.nusco.narjillos.core.utilities.NumberFormatter;
import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.experiment.Experiment;
import org.nusco.narjillos.experiment.ExperimentHistoryEntry;
import org.nusco.narjillos.experiment.HistoryLog;
import org.nusco.narjillos.genomics.DNA;
import org.nusco.narjillos.genomics.GenePool;
import org.nusco.narjillos.genomics.GenePoolExporter;
import org.nusco.narjillos.persistence.ExperimentLoader;
import org.nusco.narjillos.persistence.PersistentHistoryLog;

/**
 * The "lab" program. It reads data from an experiment and outputs it in
 * various formats.
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
		GenePool genePool = experiment.getGenePool();

		if (commandLine.hasOption("stats")) {
			HistoryLog history = new PersistentHistoryLog(experiment.getId());
			System.out.println(history.getLatestEntry());
			return;
		}

		if (commandLine.hasOption("dna")) {
			DNA dna = getDNA(genePool, commandLine.getOptionValue("d"));
			if (dna == null)
				System.out.println("DNA not found");
			else
				System.out.println(dna);
			return;
		}

		if (commandLine.hasOption("dnastats")) {
			System.out.println(getDNAStatistics(genePool, commandLine.getOptionValue("dnastats")));
			return;
		}

		if (commandLine.hasOption("germline")) {
			for (DNA dna : getAncestry(genePool, Long.parseLong(commandLine.getOptionValue("germline"))))
				System.out.println(dna);
			return;
		}

		if (commandLine.hasOption("primary")) {
			DNA dna = genePool.getMostSuccessfulDna();
			if (dna == null)
				System.out.println("DNA not found");
			else
				System.out.println(dna.getId());
			return;
		}

		if (commandLine.hasOption("csv")) {
			System.out.print(new GenePoolExporter(genePool).toCSVFormat());
			return;
		}

		if (commandLine.hasOption("history")) {
			System.out.println(ExperimentHistoryEntry.toCsvHeader());
			HistoryLog database = new PersistentHistoryLog(experiment.getId());
			for (ExperimentHistoryEntry stat : database.getEntries())
				System.out.println(stat);
			return;
		}

		if (commandLine.hasOption("nexus")) {
			System.out.println(new GenePoolExporter(genePool).toNEXUSFormat());
			return;
		}

		printHelpText(options);
	}

	private static List<DNA> getAncestry(GenePool genePool, long dnaId) {
		return genePool.getAncestryOf(dnaId);
	}

	private static String getDNAStatistics(GenePool genePool, String dnaId) {
		DNA dna = getDNA(genePool, dnaId);

		if (dna == null)
			return "Cannot retrieve DNA";

		Narjillo specimen = new Narjillo(dna, Vector.ZERO, 90, Energy.INFINITE);
		StringBuilder result = new StringBuilder();
		result.append("Number of organs   => ").append(specimen.getOrgans().size()).append("\n");
		result.append("Adult mass         => ").append(NumberFormatter.format(specimen.getBody().getAdultMass())).append("\n");
		result.append("Wave beat ratio    => ").append(NumberFormatter.format(specimen.getBody().getWaveBeatRatio())).append("\n");
		result.append("Energy to children => ").append(NumberFormatter.format(specimen.getBody().getEnergyToChildren())).append("\n");
		result.append("Egg interval       => ").append(specimen.getBody().getEggInterval()).append("\n");
		result.append("Egg velocity       => ").append(specimen.getBody().getEggVelocity()).append("\n");
		return result.toString();
	}

	private static DNA getDNA(GenePool genePool, String dnaId) {
		return genePool.getDna(Long.parseLong(dnaId));
	}

	private static void printHelpText(Options commandLineOptions) {
		new HelpFormatter().printHelp("lab <experiment_file.exp> <options>", commandLineOptions);
	}
}
