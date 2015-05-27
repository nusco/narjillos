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
import org.nusco.narjillos.core.utilities.NumberFormat;
import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.ecosystem.Experiment;
import org.nusco.narjillos.genomics.DNA;
import org.nusco.narjillos.genomics.GenePool;
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
		options.addOption("a", "ancestry", false, "print ancestry statistics");
		options.addOption("csv", false, "output ancestry in CSV format");
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
		
		if (commandLine.hasOption("a")) {
			printAncestry(args[0]);
			return;
		}
		
		if (commandLine.hasOption("csv")) {
			printCSVTree(args[0]);
			return;
		}
		
		if (commandLine.hasOption("nexus")) {
			printNexusTree(args[0]);
			return;
		}

    	printHelpText(options);
	}

	private static void printAncestry(String experimentFile) throws IOException {
		System.out.println("> Reading file \"" + experimentFile + "\"...");
		Experiment experiment = Persistence.loadExperiment(experimentFile);
		GenePool genePool = experiment.getGenePool();

		System.out.println("  > Current gene pool size: " + genePool.getCurrentPoolSize());
		System.out.println("  > Historical gene pool size: " + genePool.getHistoricalPoolSize());

		if (genePool.getHistoricalPoolSize() == 0) {
			System.out.println(">Empty gene pool. Exiting...");
			return;
		}

		System.out.println("> Identifying most successful DNA...");
		DNA mostSuccessfulDNA = genePool.getMostSuccessfulDNA();

		System.out.println("> Extracting ancestry...");
		List<DNA> ancestry = genePool.getAncestry(mostSuccessfulDNA);

		reportAncestryCreature(mostSuccessfulDNA);

		for (DNA dna : ancestry)
			System.out.println(" " + dna);
	}

	private static void printCSVTree(String experimentFile) {
		Experiment experiment = Persistence.loadExperiment(experimentFile);
		GenePool genePool = experiment.getGenePool();
		System.out.println(genePool.toCSVFormat());
	}

	private static void printNexusTree(String experimentFile) {
		Experiment experiment = Persistence.loadExperiment(experimentFile);
		GenePool genePool = experiment.getGenePool();
		System.out.println(genePool.toNEXUSFormat());
	}

	private static void reportAncestryCreature(DNA dna) {
		Narjillo specimen = new Narjillo(dna, Vector.ZERO, 90, Energy.INFINITE);
		System.out.println("Typical successful creature:");
		System.out.println("  Number of organs   => " + specimen.getOrgans().size());
		System.out.println("  Adult mass         => " + NumberFormat.format(specimen.getBody().getAdultMass()));
		System.out.println("  Energy to children => " + NumberFormat.format(specimen.getBody().getEnergyToChildren()));
		System.out.println("  Egg interval       => " + specimen.getBody().getEggInterval());
		System.out.println("  Egg velocity       => " + specimen.getBody().getEggVelocity());
		System.out.println();
	}
	
	private static void printHelpText(Options commandLineOptions) {
		new HelpFormatter().printHelp("lab <experiment_file.exp> <options>", commandLineOptions);
	}
}