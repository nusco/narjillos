package org.nusco.narjillos.genomics;

import java.util.LinkedList;
import java.util.List;

import org.nusco.narjillos.shared.utilities.RanGen;

public class DNA {

	public static final double MUTATION_RATE = 0.03;
	private static final int GENE_MUTATION_RANGE = 30;
	private static DNAObserver observer = DNAObserver.NULL;
	
	private static long serial = 0;

	private long id; // unique for each DNA instance in the same run of the program
	private final Integer[] genes;

	public DNA(String dnaDocument) {
		this(new DNADocument(dnaDocument).toGenes());
	}

	public DNA(String dnaDocument, long id) {
		this.genes = clipGenes(new DNADocument(dnaDocument).toGenes());
		setId(id);
		DNA.observer.created(this, null);
	}

	private DNA(Integer... genes) {
		this.genes = clipGenes(genes);
		setId(serial + 1);
		DNA.observer.created(this, null);
	}

	private DNA(DNA parent, Integer... genes) {
		this.genes = clipGenes(genes);
		setId(serial + 1);
		DNA.observer.created(this, parent);
	}

	private Integer[] clipGenes(Integer[] genes) {
		return (genes.length > 0) ? clipToByteSize(genes) : new Integer[] { 0 };
	}

	private void setId(long id) {
		this.id = id;
		// This complicated mechanism guarantees that even
		// after deserializing DNA, the serial will still
		// contain the highest id in the system. (But
		// if you generate DNA and *then* deserialize
		// other DNA, then you might have duplicated
		// DNA ids in the system. I assume that you
		// will only deserialize DNA before ever creating it.
		if (id > serial)
			serial = id;
	}

	public long getId() {
		return id;
	}

	public Integer[] getGenes() {
		return genes;
	}

	public DNA copy(RanGen ranGen) {
		List<Integer[]> resultChromosomes = new LinkedList<>();

		DNAParser parser = new DNAParser(this);
		Chromosome nextChromosome;
		while ((nextChromosome = parser.nextChromosome()) != null) {
			// skip a chromosome every now and then
			if (!isChromosomeMutation(ranGen))
				resultChromosomes.add(copy(nextChromosome, ranGen));
			// add a chromosome every now and then
			if (isChromosomeMutation(ranGen))
				resultChromosomes.add(randomGenes(Chromosome.SIZE, ranGen));
		}

		Integer[] resultGenes = flatten(resultChromosomes);
		return new DNA(this, resultGenes);
	}

	private Integer[] flatten(List<Integer[]> chromosomes) {
		List<Integer> result = new LinkedList<>();
		for (Integer[] chromosome : chromosomes)
			for (Integer gene : chromosome)
				result.add(gene);
		return result.toArray(new Integer[result.size()]);
	}

	private Integer[] copy(Chromosome chromosome, RanGen ranGen) {
		Integer[] result = new Integer[Chromosome.SIZE];
		for (int i = 0; i < result.length; i++)
			result[i] = copy(chromosome.getGene(i), ranGen);
		return result;
	}

	private int copy(int gene, RanGen ranGen) {
		return isMutantGene(ranGen) ? mutate(gene, ranGen) : gene;
	}

	private boolean isMutantGene(RanGen ranGen) {
		return ranGen.nextDouble() < MUTATION_RATE;
	}

	private boolean isChromosomeMutation(RanGen ranGen) {
		return ranGen.nextDouble() < (MUTATION_RATE / (Chromosome.SIZE * 2));
	}

	private int mutate(int gene, RanGen ranGen) {
		int randomFactor = ((int) (ranGen.nextDouble() * GENE_MUTATION_RANGE * 2)) - GENE_MUTATION_RANGE;
		return gene + randomFactor;
	}

	private Integer[] clipToByteSize(Integer[] genes) {
		Integer[] result = new Integer[genes.length];
		for (int i = 0; i < result.length; i++)
			result[i] = clipToByteSize(genes[i]);
		return result;
	}

	private int clipToByteSize(int number) {
		return Math.max(0, Math.min(255, number));
	}

	public static DNA random(RanGen ranGen) {
		int size = Chromosome.SIZE * (Math.abs(ranGen.nextInt()) % 10 + 2);
		return random(size, ranGen);
	}

	private static DNA random(int size, RanGen ranGen) {
		Integer[] genes = randomGenes(size, ranGen);
		return new DNA(genes);
	}

	private static Integer[] randomGenes(int size, RanGen ranGen) {
		Integer[] genes = new Integer[size];
		for (int i = 0; i < genes.length; i++)
			genes[i] = ranGen.nextByte();
		return genes;
	}

	// Taken (only slightly adapted) from:
	// http://en.wikipedia.org/wiki/Levenshtein_distance
	public int getLevenshteinDistanceFrom(DNA other) {
		Integer[] theseGenes = getGenes();
		Integer[] otherGenes = other.getGenes();

		if (theseGenes.length == 0)
			return otherGenes.length;
		if (otherGenes.length == 0)
			return theseGenes.length;

		// create two work vectors of integer distances
		int[] v0 = new int[otherGenes.length + 1];
		int[] v1 = new int[otherGenes.length + 1];

		// initialize v0 (the previous row of distances)
		// this row is A[0][i]: edit distance for an empty s
		// the distance is just the number of characters to delete from t
		for (int i = 0; i < v0.length; i++)
			v0[i] = i;

		for (int i = 0; i < theseGenes.length; i++) {
			// calculate v1 (current row distances) from the previous row v0

			// first element of v1 is A[i+1][0]
			// edit distance is delete (i+1) chars from s to match empty t
			v1[0] = i + 1;

			// use formula to fill in the rest of the row
			for (int j = 0; j < otherGenes.length; j++) {
				int cost = (theseGenes[i] == otherGenes[j]) ? 0 : 1;
				v1[j + 1] = Math.min(Math.min(v1[j] + 1, v0[j + 1] + 1), v0[j] + cost);
			}

			// copy v1 (current row) to v0 (previous row) for next iteration
			for (int j = 0; j < v0.length; j++)
				v0[j] = v1[j];
		}

		return v1[otherGenes.length];
	}

	@Override
	public int hashCode() {
		return (int) getId();
	}

	@Override
	public boolean equals(Object obj) {
		DNA other = (DNA) obj;
		return other.getId() == getId();
	}

	@Override
	public String toString() {
		return DNADocument.toString(this);
	}

	public static void setObserver(DNAObserver dnaObserver) {
		DNA.observer = dnaObserver;
	}

	public void removeFromPool() {
		DNA.observer.removed(this);
	}
}