package org.nusco.narjillos.genomics;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A GenePool that doesn't track history.
 */
public class SimpleGenePool extends GenePool {

	public SimpleGenePool(DNALog journal) {
		super(journal);
	}

	@Override
	public DNA getDna(Long id) {
		return null;
	}

	@Override
	public List<DNA> getAncestryOf(DNA dna) {
		return new LinkedList<>();
	}

	@Override
	public DNA getMostSuccessfulDNA() {
		return null;
	}

	@Override
	public Set<Long> getHistoricalPool() {
		return new LinkedHashSet<Long>();
	}

	@Override
	Map<Long, Long> getChildrenToParents() {
		return new LinkedHashMap<>();
	}

	@Override
	Map<Long, List<Long>> getParentsToChildren() {
		LinkedHashMap<Long, List<Long>> result = new LinkedHashMap<>();
		result.put(0L, new LinkedList<Long>());
		return result;
	}

	@Override
	public int getGenerationOf(DNA dna) {
		return 0;
	}
}
