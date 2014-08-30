package org.nusco.narjillos.creature.genetics;

import java.util.LinkedList;
import java.util.List;

import org.nusco.narjillos.shared.utilities.RanGen;

public class DNA {

	public static final double MUTATION_RATE = 0.02;
	private static final int GENE_MUTATION_RANGE = 30;

	private final Integer[] genes;

	DNA(Integer... genes) {
		this.genes = (genes.length > 0) ? clipToByteSize(genes) : new Integer[] {0};
	}

	public DNA(String dnaDocument) {
		this(new DNADocument(dnaDocument).toGenes());
	}

	public Integer[] getGenes() {
		return genes;
	}

	public DNA copy() {
		List<Integer[]> resultChromosomes = new LinkedList<>();

		DNAParser parser = new DNAParser(this);
		Chromosome nextChromosome;
		while((nextChromosome = parser.nextChromosome()) != null) {
			// skip a chromosome every now and then
			if (!isMutating())
				resultChromosomes.add(copy(nextChromosome));
			// add a chromosome every now and then
			if (isMutating())
				resultChromosomes.add(randomGenes(Chromosome.SIZE));
		}

		Integer[] resultGenes = flatten(resultChromosomes);
		return new DNA(resultGenes);
	}

	private Integer[] flatten(List<Integer[]> chromosomes) {
		List<Integer> result = new LinkedList<>();
		for (Integer[] chromosome : chromosomes)
			for (Integer gene : chromosome)
				result.add(gene);
		return result.toArray(new Integer[result.size()]);
	}

	private Integer[] copy(Chromosome chromosome) {
		Integer[] result = new Integer[Chromosome.SIZE];
		for (int i = 0; i < result.length; i++)
			result[i] = copy(chromosome.getGene(i));
		return result;
	}

	private int copy(int gene) {
		return isMutating() ? mutate(gene) : gene;
	}

	private boolean isMutating() {
		return RanGen.nextDouble() < MUTATION_RATE;
	}

	private int mutate(int gene) {
		int randomFactor = ((int) (RanGen.nextDouble() * GENE_MUTATION_RANGE * 2)) - GENE_MUTATION_RANGE;
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

	public static DNA random() {
		int size = Chromosome.SIZE * (Math.abs(RanGen.nextInt()) % 10 + 2);
		return random(size);
	}

	private static DNA random(int size) {
		Integer[] genes = randomGenes(size);
		return new DNA(genes);
	}

	private static Integer[] randomGenes(int size) {
		Integer[] genes = new Integer[size];
		for (int i = 0; i < genes.length; i++)
			genes[i] = RanGen.nextByte();
		return genes;
	}

	// Taken (only slightly adapted) from:
	// http://en.wikipedia.org/wiki/Levenshtein_distance
	public int getLevenshteinDistanceFrom(DNA other) {
		Integer[] theseGenes = getGenes();
		Integer[] otherGenes = other.getGenes();

	    if (theseGenes.length == 0) return otherGenes.length;
	    if (otherGenes.length == 0) return theseGenes.length;
	 
	    // create two work vectors of integer distances
	    int[] v0 = new int[otherGenes.length + 1];
	    int[] v1 = new int[otherGenes.length + 1];
	 
	    // initialize v0 (the previous row of distances)
	    // this row is A[0][i]: edit distance for an empty s
	    // the distance is just the number of characters to delete from t
	    for (int i = 0; i < v0.length; i++)
	        v0[i] = i;
	 
	    for (int i = 0; i < theseGenes.length; i++)
	    {
	        // calculate v1 (current row distances) from the previous row v0
	 
	        // first element of v1 is A[i+1][0]
	        //   edit distance is delete (i+1) chars from s to match empty t
	        v1[0] = i + 1;
	 
	        // use formula to fill in the rest of the row
	        for (int j = 0; j < otherGenes.length; j++)
	        {
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
	public String toString() {
		return DNADocument.toString(this);
	}
 }