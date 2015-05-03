package org.nusco.narjillos.genomics;

import java.util.Iterator;

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
		Integer[] resultGenes = new Integer[genes.length];
		for (int i = 0; i < resultGenes.length; i++) {
			if (ranGen.nextDouble() < Configuration.DNA_MUTATION_RATE)
				resultGenes[i] = mutate(genes[i], ranGen);
			else
				resultGenes[i] = genes[i];
		}
		return new DNA(id, resultGenes);
	}

	public static DNA random(long id, RanGen ranGen) {
		int size = Chromosome.SIZE * Configuration.DNA_NUMBER_OF_CHROMOSOMES;
		return random(id, ranGen, size);
	}

	public int getSimHashedDistanceFrom(DNA other) {
		int result = 0;
		for (int i = 0; i < Codon.HASH_SIZE; i++)
			if (SimHash.calculateSimHash(this)[i] != SimHash.calculateSimHash(other)[i])
				result++;
		return result;
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
				while (index_in_result < result.length && indexInGenes < getGenes().length) {
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

	Codon[] toCodons() {
		Codon[] result = new Codon[(int) Math.ceil(genes.length / 3.0)];
		for (int i = 0; i < result.length; i++)
			result[i] = new Codon(safeGetGene(i * 3), safeGetGene(i * 3 + 1), safeGetGene(i * 3 + 2));
		return result;
	}

	private Integer[] clipGenes(Integer[] genes) {
		return (genes.length > 0) ? clipToByteSize(genes) : new Integer[] { 0 };
	}

	private int mutate(int gene, RanGen ranGen) {
		int randomFactor = (int) ((ranGen.nextDouble() * Configuration.DNA_MUTATION_RANGE * 2) - Configuration.DNA_MUTATION_RANGE);
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

	private Integer safeGetGene(int i) {
		if (i >= genes.length)
			return 0;
		return genes[i];
	}
}