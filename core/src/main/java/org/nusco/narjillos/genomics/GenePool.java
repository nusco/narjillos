package org.nusco.narjillos.genomics;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GenePool implements DNAObserver {

	private Map<DNA, DNA> childrenToParents = new LinkedHashMap<>();
	private List<DNA> currentPool = new LinkedList<>();

	@Override
	public void created(DNA newDNA, DNA parent) {
		childrenToParents.put(newDNA, parent);
		currentPool.add(newDNA);
	}

	@Override
	public void removed(DNA dna) {
		currentPool.remove(dna);
	}

	public List<DNA> getAncestry(DNA dna) {
		LinkedList<DNA> result = new LinkedList<>();

		DNA current = dna;
		while (current != null) {
			result.add(current);

			// this can happen in very unlucky cases of file contention
			if (!childrenToParents.containsKey(current))
				throw new RuntimeException("Inconsistent ancestry - cannot find parent of " + current.getId());

			current = childrenToParents.get(current);
		}

		Collections.reverse(result);
		return result;
	}

	public DNA getMostSuccessfulDNA() {
		DNA result = null;
		int lowestLevenshteinDistance = Integer.MAX_VALUE;
		for (DNA dna : currentPool) {
			int currentLevenshteinDistance = totalLevenshteinDistanceFromOthers(dna);
			if (currentLevenshteinDistance < lowestLevenshteinDistance) {
				result = dna;
				lowestLevenshteinDistance = currentLevenshteinDistance;
			}
		}
		return result;
	}

	private int totalLevenshteinDistanceFromOthers(DNA dna) {
		int result = 0;
		for (DNA otherDNA : currentPool)
			if (!otherDNA.equals(dna))
				result += dna.getLevenshteinDistanceFrom(otherDNA);
		return result;
	}

	public Map<DNA, DNA> getChildrenToParents() {
		return childrenToParents;
	}
	
	public List<DNA> getCurrentPool() {
		return currentPool;
	}
}
