package org.nusco.narjillos.genomics;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.nusco.narjillos.core.utilities.Configuration;
import org.nusco.narjillos.core.utilities.RanGen;

/**
 * A sequence of genes.
 */
public class DNA implements Iterable<Chromosome> {

	private final long id;
	
	private final Integer[] genes;

	public DNA(long id, String dnaDocument) {
		this.id = id;
		this.genes = clipGenes(new DNADocument(dnaDocument).toGenes());
	}

	public DNA(long id, Integer[] genes) {
		this.id = id;
		this.genes = clipGenes(genes);
	}

	public long getId() {
		return id;
	}

	public Integer[] getGenes() {
		return genes;
	}

	public DNA copyWithMutations(long id, RanGen ranGen) {
		List<Integer[]> resultChromosomes = new LinkedList<>();

		for (Chromosome chromosome : this) {
			// skip a chromosome every now and then
			if (!isChromosomeMutation(ranGen))
				resultChromosomes.add(copyWithMutations(chromosome, ranGen));
			// add a chromosome every now and then
			if (isChromosomeMutation(ranGen))
				resultChromosomes.add(randomGenes(Chromosome.SIZE, ranGen));
		}

		Integer[] resultGenes = flatten(resultChromosomes);
		return new DNA(id, resultGenes);
	}

	public static DNA random(long id, RanGen ranGen) {
		int size = Chromosome.SIZE * (Math.abs(ranGen.nextInt()) % 10 + 2);
		return random(id, ranGen, size);
	}

	// From: http://en.wikipedia.org/wiki/Levenshtein_distance,
	// with slight changes.
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
	public Iterator<Chromosome> iterator() {
		return new Iterator<Chromosome>() {
			private int indexInGenes = 0;

			@Override
			public boolean hasNext() {
				return indexInGenes == 0 || indexInGenes < getGenes().length;
			}

			@Override
			public Chromosome next() {
				int[] result = new int[Chromosome.SIZE];
				int index_in_result = 0;
				while(index_in_result < result.length && indexInGenes < getGenes().length) {
					result[index_in_result] = getGenes()[indexInGenes];
					index_in_result++;
					indexInGenes++;
				}
				return new Chromosome(result);
			}
		};
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

	private Integer[] clipGenes(Integer[] genes) {
		return (genes.length > 0) ? clipToByteSize(genes) : new Integer[] { 0 };
	}

	private Integer[] copyWithMutations(Chromosome chromosome, RanGen ranGen) {
		Integer[] result = new Integer[Chromosome.SIZE];
		for (int i = 0; i < result.length; i++)
			result[i] = copyWithMutations(chromosome.getGene(i), ranGen);
		return result;
	}

	private int copyWithMutations(int gene, RanGen ranGen) {
		return isMutantGene(ranGen) ? mutate(gene, ranGen) : gene;
	}

	private boolean isMutantGene(RanGen ranGen) {
		return ranGen.nextDouble() < Configuration.DNA_MUTATION_RATE;
	}

	private boolean isChromosomeMutation(RanGen ranGen) {
		return ranGen.nextDouble() < (Configuration.DNA_MUTATION_RATE / (Chromosome.SIZE * 2));
	}

	private int mutate(int gene, RanGen ranGen) {
		int randomFactor = (int) ((ranGen.nextDouble() * Configuration.DNA_MUTATION_RANGE * 2) - Configuration.DNA_MUTATION_RANGE);
		return gene + randomFactor;
	}

	private Integer[] flatten(List<Integer[]> chromosomes) {
		List<Integer> result = new LinkedList<>();
		for (Integer[] chromosome : chromosomes)
			for (Integer gene : chromosome)
				result.add(gene);
		return result.toArray(new Integer[result.size()]);
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

	private static DNA random(long id, RanGen ranGen, int size) {
		Integer[] genes = randomGenes(size, ranGen);
		return new DNA(id, genes);
	}

	private static Integer[] randomGenes(int size, RanGen ranGen) {
		Integer[] genes = new Integer[size];
		for (int i = 0; i < genes.length; i++)
			genes[i] = ranGen.nextByte();
		return genes;
	}

	public Codon[] toCodons() {
		Codon[] result = new Codon[(int) Math.ceil(genes.length / 3.0)];
		for (int i = 0; i < result.length; i++)
			result[i] = new Codon(safeGetGene(i * 3), safeGetGene(i * 3 + 1), safeGetGene(i * 3 + 2));
		return result;
	}

	private Integer safeGetGene(int i) {
		if (i >= genes.length)
			return 0;
		return genes[i];
	}
}