package org.nusco.narjillos;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.nusco.narjillos.core.physics.Vector;
import org.nusco.narjillos.core.things.Energy;
import org.nusco.narjillos.core.utilities.NumberFormat;
import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.ecosystem.Experiment;
import org.nusco.narjillos.ecosystem.ExperimentStats;
import org.nusco.narjillos.genomics.DNA;
import org.nusco.narjillos.genomics.GenePool;
import org.nusco.narjillos.genomics.GenePoolExporter;
import org.nusco.narjillos.genomics.GenePoolStats;
import org.nusco.narjillos.serializer.Persistence;

/**
 * The "laboratory" program. It reads data from an experiment and outputs it in
 * various formats.
 * 
 * At the moment, it only does ancestry analysis: it reads the gene pool,
 * identifies the most successfull DNA in the pool, and prints out its entire
 * ancestry - from the first randomly generates ancestor onwards.
 */
public class Lab {

	public static void main(String[] args) throws IOException {
		Options options = new Options();
		options.addOption("h", "help", false, "print this message");
		options.addOption("d", "dna", true, "print DNA (takes a DNA id)");
		options.addOption("dnastats", true, "print DNA stats (takes a DNA id)");
		options.addOption("a", "ancestry", true, "print DNA ancestry (takes a DNA id)");
		options.addOption("primary", false, "print id of primary (most successful) DNA");
		options.addOption("s", "stats", false, "print current statistics");
		options.addOption("history", false, "output history in CSV format");
		options.addOption("ancestry", false, "output ancestry in CSV format");
		options.addOption("nexus", false, "output ancestry in NEXUS format (needs deep Java stack)");

		CommandLine commandLine;
		try {
			commandLine = new BasicParser().parse(options, args);
        } catch(ParseException e) {
        	printHelpText(options);
	        return;
		} catch (RuntimeException e) {
			System.out.println(e.getMessage());
			return;
		}

		if (args.length == 0 || args[0].startsWith("-") || commandLine == null || commandLine.hasOption("h")) {
	    	printHelpText(options);
			return;
		}
		
		String experimentFile = args[0];
		Experiment experiment = Persistence.loadExperiment(experimentFile);
		GenePool genePool = experiment.getGenePool();

		if (commandLine.hasOption("d") || commandLine.hasOption("stats")) {
			System.out.println(new GenePoolStats(genePool));
			System.out.println();
			printConversions(experiment);
			return;
		}

		if (commandLine.hasOption("d")) {
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

		if (commandLine.hasOption("a")) {
			for (DNA dna : getAncestry(genePool, commandLine.getOptionValue("a")))
				System.out.println(dna);
			return;
		}
		
		if (commandLine.hasOption("primary")) {
			DNA dna = genePool.getMostSuccessfulDNA();
			if (dna == null)
				System.out.println("DNA not found");
			else
				System.out.println(dna.getId());
			return;
		}
		
		if (commandLine.hasOption("ancestry")) {
			System.out.print(new GenePoolExporter(genePool).toCSVFormat());
			return;
		}
		
		if (commandLine.hasOption("history")) {
			System.out.println(ExperimentStats.getCSVHeader());
			for (ExperimentStats experimentStats : experiment.getHistory())
				System.out.println(experimentStats.toCSVLine());
			return;
		}
		
		if (commandLine.hasOption("nexus")) {
			System.out.println(new GenePoolExporter(genePool).toNEXUSFormat());
			return;
		}

    	printHelpText(options);
	}

	private static void printConversions(Experiment experiment) {
		Set<Narjillo> narjillos = experiment.getEcosystem().getNarjillos();
		Map<String, Integer> conversionsToNumberOfSpecimen = new LinkedHashMap<>();
		for (Narjillo narjillo : narjillos) {
			String conversion = "" + narjillo.getBreathedElement() + "->" + narjillo.getByproduct();
			if (conversionsToNumberOfSpecimen.containsKey(conversion))
				conversionsToNumberOfSpecimen.put(conversion, conversionsToNumberOfSpecimen.get(conversion) + 1);
			else
				conversionsToNumberOfSpecimen.put(conversion, 1);
		}
		
		System.out.println("Conversions:");
		LinkedList<String> conversions = new LinkedList<>(conversionsToNumberOfSpecimen.keySet());
		Collections.sort(conversions);
		for (String conversion : conversions)
			System.out.println("  " + conversion + ": " + conversionsToNumberOfSpecimen.get(conversion));
	}

	private static List<DNA> getAncestry(GenePool genePool, String dnaId) {
		DNA dna = getDNA(genePool, dnaId);
		return genePool.getAncestry(dna);
	}

	private static String getDNAStatistics(GenePool genePool, String dnaId) {
		DNA dna = getDNA(genePool, dnaId);
		
		if (dna == null)
			return "Cannot retrieve DNA";

		Narjillo specimen = new Narjillo(dna, Vector.ZERO, 90, Energy.INFINITE);
		StringBuffer result = new StringBuffer();
		result.append("Number of organs   => " + specimen.getOrgans().size() + "\n");
		result.append("Adult mass         => " + NumberFormat.format(specimen.getBody().getAdultMass()) + "\n");
		result.append("Energy to children => " + NumberFormat.format(specimen.getBody().getEnergyToChildren()) + "\n");
		result.append("Egg interval       => " + specimen.getBody().getEggInterval() + "\n");
		result.append("Egg velocity       => " + specimen.getBody().getEggVelocity() + "\n");
		return result.toString();
	}

	private static DNA getDNA(GenePool genePool, String dnaId) {
		return genePool.getDna(new Long(Long.parseLong(dnaId)));
	}

	private static void printHelpText(Options commandLineOptions) {
		new HelpFormatter().printHelp("lab <experiment_file.exp> <options>", commandLineOptions);
	}
}