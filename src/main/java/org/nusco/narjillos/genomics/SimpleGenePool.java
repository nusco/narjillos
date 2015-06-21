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

	@Override
	public DNA getDna(Long id) {
		return null;
	}

	@Override
	public List<DNA> getAncestry(DNA dna) {
		return new LinkedList<>();
	}

	@Override
	public DNA getMostSuccessfulDNA() {
		return null;
	}

	@Override
	Set<Long> getHistoricalPool() {
		return new LinkedHashSet<Long>();
	}

	@Override
	Map<Long, Long> getChildrenToParents() {
		return new LinkedHashMap<>();
	}

	@Override
	public int getGenerationOf(DNA dna) {
		return 0;
	}
}
