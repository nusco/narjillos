package org.nusco.narjillos.genomics;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.nusco.narjillos.core.utilities.RanGen;

/**
 * A pool of DNA strands.
 */
public abstract class GenePool {

	private long dnaSerialId = 0;

	public DNA createDNA(String dna) {
		return new DNA(nextSerialId(), dna);
	}

	public DNA createRandomDNA(RanGen ranGen) {
		return DNA.random(nextSerialId(), ranGen);
	}

	public DNA mutateDNA(DNA parent, RanGen ranGen) {
		return parent.mutate(nextSerialId(), ranGen);
	}

	public abstract void remove(DNA dna);

	public abstract DNA getDna(Long id);

	public abstract List<DNA> getAncestry(DNA dna);

	abstract List<Long> getCurrentPool();

	abstract Set<Long> getHistoricalPool();
	
	public abstract DNA getMostSuccessfulDNA();

	public abstract int getGenerationOf(DNA dna);

	abstract Map<Long, Long> getChildrenToParents();

	public long getCurrentSerialId() {
		return dnaSerialId;
	}

	private long nextSerialId() {
		return ++dnaSerialId;
	}
}
