package org.nusco.narjillos.genomics;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * A pool of DNA strands.
 */
public class SimpleGenePool extends GenePool {

	public DNA getDNA(long id) {
		return null;
	}

	public List<DNA> getAncestry(DNA dna) {
		return new LinkedList<>();
	}

	public DNA getMostSuccessfulDNA() {
		return null;
	}

	public int getCurrentSize() {
		return 0;
	}

	public int getHistoricalSize() {
		return 0;
	}

	public void remove(DNA dna) {
	}

	Map<Long, Long> getChildrenToParents() {
		return new LinkedHashMap<>();
	}
}
