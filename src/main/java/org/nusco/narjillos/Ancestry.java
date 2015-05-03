package org.nusco.narjillos;

import java.io.IOException;
import java.util.List;

import org.nusco.narjillos.core.physics.Vector;
import org.nusco.narjillos.core.things.Energy;
import org.nusco.narjillos.core.utilities.NumberFormat;
import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.ecosystem.Experiment;
import org.nusco.narjillos.genomics.DNA;
import org.nusco.narjillos.genomics.GenePool;
import org.nusco.narjillos.serializer.Persistence;

/**
 * A utility program that reads a gene pool from a file, identifies the most
 * successfull DNA in the pool, and prints out its entire ancestry - from the
 * first randomly generates ancestor onwards.
 */
public class Ancestry {

	public static void main(String[] args) throws IOException {
		if (args.length == 0) {
			System.out.println("Usage: ancestry <experiment_file.exp>");
			System.exit(0);
		}
		printAncestry(args[0]);
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

		reportCreature(mostSuccessfulDNA);

		for (DNA dna : ancestry)
			System.out.println(" " + dna);
	}

	private static void reportCreature(DNA dna) {
		Narjillo specimen = new Narjillo(dna, Vector.ZERO, 90, Energy.INFINITE);
		System.out.println("Typical successful creature:");
		System.out.println("  Number of organs   => " + specimen.getOrgans().size());
		System.out.println("  Adult mass         => " + NumberFormat.format(specimen.getBody().getAdultMass()));
		System.out.println("  Energy to children => " + NumberFormat.format(specimen.getBody().getEnergyToChildren()));
		System.out.println("  Egg interval       => " + specimen.getBody().getEggInterval());
		System.out.println("  Egg velocity       => " + specimen.getBody().getEggVelocity());
		System.out.println();
	}
}
