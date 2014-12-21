package org.nusco.narjillos;

import java.io.IOException;
import java.util.List;

import org.nusco.narjillos.experiment.Experiment;
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
		printAncestry(args[0]);
	}

	private static void printAncestry(String experimentFile) throws IOException {
		System.out.println(">Reading file \"" + experimentFile + "\"...");
		Experiment experiment = Persistence.loadExperimentWithGenePool(experimentFile);
		GenePool genePool = experiment.getGenePool();

		System.out.println("  > Current gene pool size: " + genePool.getCurrentPoolSize());
		System.out.println("  > Historical gene pool size: " + genePool.getHistoricalPoolSize());

		System.out.println(">Identifying most successful DNA...");
		DNA mostSuccessfulDNA = genePool.getMostSuccessfulDNA();

		System.out.println(">Extracting ancestry...");
		List<DNA> ancestry = genePool.getAncestry(mostSuccessfulDNA);

		for (DNA dna : ancestry)
			System.out.println(" " + dna);
	}
}
