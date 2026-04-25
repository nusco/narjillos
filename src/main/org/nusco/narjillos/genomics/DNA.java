package org.nusco.narjillos.genomics;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.nusco.narjillos.core.configuration.Configuration;
import org.nusco.narjillos.core.utilities.NumGen;

/**
 * A sequence of genes.
 */
public record DNA(long id, Integer[] genes, long parentId) implements Iterable<Chromosome> {

	public static final long NO_PARENT = 0;

	public DNA(long id, String dnaDocument) {
		this(id, dnaDocument, NO_PARENT);
	}

	public DNA(long id, String dnaDocument, long parentId) {
		this(id, clipGenes(new DNADocument(dnaDocument).toGenes()), parentId);
	}

	public DNA(long id, Integer[] genes, long parentId) {
		this.id = id;
		this.genes = clipGenes(genes);
		this.parentId = parentId;
	}

	public boolean hasParent() {
		return parentId != NO_PARENT;
	}

	public DNA mutate(long id, NumGen numGen) {
		List<Integer[]> resultChromosomes = new LinkedList<>();
		for (Chromosome chromosome : this)
			if (isChromosomeMutation(numGen))
				resultChromosomes.addAll(mutateChromosome(numGen, chromosome));
			else
				resultChromosomes.add(copyChromosome(chromosome, numGen));
		Integer[] resultGenes = flattenToGenes(resultChromosomes);
		return new DNA(id, padToSameGenomeLength(resultGenes, numGen), id());
	}

	public int getSimHashedDistanceFrom(DNA other) {
		int result = 0;
		for (int i = 0; i < Codon.HASH_SIZE; i++)
			if (SimHash.calculateSimHash(this)[i] != SimHash.calculateSimHash(other)[i])
				result++;
		return result;
	}

	// From: http://rosettacode.org/wiki/Levenshtein_distance#Java,
	// with slight changes.
	public int getLevenshteinDistanceFrom(DNA other) {
		Integer[] theseGenes = genes();
		Integer[] otherGenes = other.genes();

		if (theseGenes.length == 0 || otherGenes.length == 0)
			return Math.max(theseGenes.length, otherGenes.length);

		int[] costs = new int[otherGenes.length + 1];
		// the distance is just the number of characters to delete from t
		for (int i = 0; i < costs.length; i++)
			costs[i] = i;

		for (int i = 1; i <= theseGenes.length; i++) {
			costs[0] = i;
			int nw = i - 1;
			for (int j = 1; j <= otherGenes.length; j++) {
				boolean equalGenes = theseGenes[i - 1].equals(otherGenes[j - 1]);
				int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]), equalGenes ? nw : nw + 1);
				nw = costs[j];
				costs[j] = cj;
			}
		}

		return costs[otherGenes.length];
	}

	@Override
	public Iterator<Chromosome> iterator() {
		return new Iterator<>() {

			private int indexInGenes = 0;

			@Override
			public boolean hasNext() {
				return indexInGenes == 0 || indexInGenes < genes().length;
			}

			@Override
			public Chromosome next() {
				int[] result = new int[Chromosome.SIZE];
				int index_in_result = 0;
				while (index_in_result < result.length && indexInGenes < genes().length) {
					result[index_in_result] = genes()[indexInGenes];
					index_in_result++;
					indexInGenes++;
				}
				return new Chromosome(result);
			}
		};
	}

	@Override
	public int hashCode() {
		return (int) id();
	}

	@Override
	public boolean equals(Object obj) {
		DNA other = (DNA) obj;
		return other.id() == id();
	}

	@Override
	public String toString() {
		return DNADocument.toString(this);
	}

	Codon[] toCodons() {
		Codon[] result = new Codon[(int) Math.ceil(genes.length / 3.0)];
		for (int i = 0; i < result.length; i++)
			result[i] = new Codon(safeGetGene(i * 3), safeGetGene(i * 3 + 1), safeGetGene(i * 3 + 2));
		return result;
	}

	public static DNA random(long id, NumGen numGen) {
		return new DNA(id, randomGenes(getDefaultSize(), numGen), 0);
	}

	private List<Integer[]> mutateChromosome(NumGen numGen, Chromosome chromosome) {
		if (isSkipMutation(numGen))
			return new LinkedList<>();

		// duplicate the chromosome
		List<Integer[]> resultChromosomes = new LinkedList<>();
		Integer[] copiedGenes = copyChromosome(chromosome, numGen);
		resultChromosomes.add(copiedGenes);
		resultChromosomes.add(copiedGenes);
		return resultChromosomes;
	}

	private boolean isSkipMutation(NumGen numGen) {
		return numGen.nextDouble() > 0.5;
	}

	private Integer[] copyChromosome(Chromosome chromosome, NumGen numGen) {
		Integer[] result = new Integer[Chromosome.SIZE];
		for (int i = 0; i < result.length; i++)
			result[i] = copyWithMutations(chromosome.getGene(i), numGen);
		return result;
	}

	private int copyWithMutations(int gene, NumGen numGen) {
		return isMutantGene(numGen) ? mutate(gene, numGen) : gene;
	}

	private boolean isMutantGene(NumGen numGen) {
		return numGen.nextDouble() < Configuration.DNA_MUTATION_RATE;
	}

	private boolean isChromosomeMutation(NumGen numGen) {
		return numGen.nextDouble() < (Configuration.DNA_MUTATION_RATE / (Chromosome.SIZE * 2));
	}

	private int mutate(int gene, NumGen numGen) {
		int randomFactor = (int) ((numGen.nextDouble() * Configuration.DNA_MUTATION_RANGE * 2) - Configuration.DNA_MUTATION_RANGE);
		return gene + randomFactor;
	}

	private Integer[] flattenToGenes(List<Integer[]> chromosomes) {
		List<Integer> result = new LinkedList<>();
		for (Integer[] chromosome : chromosomes)
			Collections.addAll(result, chromosome);
		return result.toArray(Integer[]::new);
	}

	private Integer[] padToSameGenomeLength(Integer[] otherGenes, NumGen numGen) {
		Integer[] result = new Integer[genes.length];
		for (int i = 0; i < result.length; i++)
			result[i] = i < otherGenes.length ? otherGenes[i] : numGen.nextByte();
		return result;
	}

	private static Integer[] clipToByteSize(Integer[] genes) {
		Integer[] result = new Integer[genes.length];
		for (int i = 0; i < result.length; i++)
			result[i] = clipToByteSize(genes[i]);
		return result;
	}

	private static int clipToByteSize(int number) {
		return Math.clamp(number, 0, 255);
	}

	private Integer safeGetGene(int i) {
		if (i >= genes.length)
			return 0;
		return genes[i];
	}

	private static int getDefaultSize() {
		return Chromosome.SIZE * Configuration.DNA_NUMBER_OF_CHROMOSOMES;
	}

	private static Integer[] randomGenes(int size, NumGen numGen) {
		Integer[] genes = new Integer[size];
		for (int i = 0; i < genes.length; i++)
			genes[i] = numGen.nextByte();
		return genes;
	}

	private static Integer[] clipGenes(Integer[] genes) {
		return (genes.length > 0) ? clipToByteSize(genes) : new Integer[]{0};
	}
}
