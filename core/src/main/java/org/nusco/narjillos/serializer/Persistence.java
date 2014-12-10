package org.nusco.narjillos.serializer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.nusco.narjillos.experiment.Experiment;
import org.nusco.narjillos.genomics.DNA;
import org.nusco.narjillos.genomics.GenePool;

public class Persistence {

	public static Experiment loadExperiment(String id) {
		Experiment result = JSON.fromJson(load(id + ".exp"), Experiment.class);
		result.timeStamp();
		return result;
	}

	public static GenePool loadGenePool(String id) {
		Path filePath = Paths.get(id + ".gpl");
		if (!Files.exists(filePath)) {
			System.out.println("Warning: no .gpl file found. Continuing with empty genepool.");
			return new GenePool();
		}
		return JSON.fromJson(load(id + ".gpl"), GenePool.class);
	}

	public static DNA loadDNA(String fileName) {
		return new DNA(load(fileName));
	}

	private static String load(String fileName) {
		try {
			byte[] encoded = Files.readAllBytes(Paths.get(fileName));
			return new String(encoded, Charset.defaultCharset());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void save(Experiment experiment, GenePool genePool) {
		String experimentFileName = experiment.getId() + ".exp";
		String tempExperimentFileName = experimentFileName + ".tmp";
		
		String ancestryFileName = experiment.getId() + ".gpl";
		String tempAncestryFileName = ancestryFileName + ".tmp";

		try {
			writeToFile(tempExperimentFileName, JSON.toJson(experiment, Experiment.class));
			writeToFile(tempAncestryFileName, JSON.toJson(genePool, GenePool.class));

			// do this quickly to minimize the chances of getting
			// the two files misaligned
			moveFile(tempExperimentFileName, experimentFileName);
			moveFile(tempAncestryFileName, ancestryFileName);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static void moveFile(String source, String destination) throws IOException {
		Path filePath = Paths.get(destination);
		if (Files.exists(filePath))
			Files.delete(filePath);
		Files.move(Paths.get(source), filePath);
	}

	private static void writeToFile(String fileName, String content) throws IOException, FileNotFoundException {
		PrintWriter out = new PrintWriter(fileName);
		out.print(content);
		out.close();
	}
}
