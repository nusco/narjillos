package org.nusco.narjillos.genomics;

import java.util.List;
import java.util.Map;

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

	public abstract DNA getDNA(long id);

	public abstract int getCurrentSize();

	public abstract int getHistoricalSize();

	public abstract List<DNA> getAncestry(DNA dna);
	
	public abstract DNA getMostSuccessfulDNA();

	public long getCurrentSerialId() {
		return dnaSerialId;
	}

	abstract Map<Long, Long> getChildrenToParents();

	private long nextSerialId() {
		return ++dnaSerialId;
	}
}
