package org.nusco.narjillos.genomics;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * A pool of DNA strands.
 */
public class GenePool implements DNAObserver {

	private Map<Long, DNA> dnaById = new LinkedHashMap<>();
	private List<Long> currentPool = new LinkedList<>();
	private Map<Long, Long> childrenToParents = new LinkedHashMap<>();

	@Override
	public void created(DNA newDNA, DNA parent) {
		dnaById.put(newDNA.getId(), newDNA);
		currentPool.add(newDNA.getId());
		if (parent == null)
			childrenToParents.put(newDNA.getId(), 0l);
		else
			childrenToParents.put(newDNA.getId(), parent.getId());
	}

	@Override
	public void removed(DNA dna) {
		currentPool.remove(dna.getId());
	}

	public List<DNA> getAncestry(DNA dna) {
		LinkedList<DNA> result = new LinkedList<>();

		Long currentDnaId = dna.getId();
		while (currentDnaId != 0) {
			result.add(dnaById.get(currentDnaId));

			// this can happen in very unlucky cases when the
			// genepool file and the experiment file go out
			// of synch
			if (!childrenToParents.containsKey(currentDnaId))
				throw new RuntimeException("Inconsistent ancestry - cannot find parent of DNA " + currentDnaId);

			currentDnaId = childrenToParents.get(currentDnaId);
		}

		Collections.reverse(result);
		return result;
	}

	public DNA getMostSuccessfulDNA() {
		DNA result = null;
		int lowestLevenshteinDistance = Integer.MAX_VALUE;
		for (Long dnaId : currentPool) {
			DNA dna = dnaById.get(dnaId);
			int currentLevenshteinDistance = totalLevenshteinDistanceFromTheRestOfThePool(dna);
			if (currentLevenshteinDistance < lowestLevenshteinDistance) {
				result = dna;
				lowestLevenshteinDistance = currentLevenshteinDistance;
			}
		}
		return result;
	}

	public Map<Long, Long> getChildrenToParents() {
		return childrenToParents;
	}

	public int getCurrentPoolSize() {
		return currentPool.size();
	}

	public int getHistoricalPoolSize() {
		return dnaById.size();
	}

	private int totalLevenshteinDistanceFromTheRestOfThePool(DNA dna) {
		int result = 0;
		for (Long otherDNAId : currentPool) {
			DNA otherDNA = dnaById.get(otherDNAId);
			if (!otherDNA.equals(dna))
				result += dna.getLevenshteinDistanceFrom(otherDNA);
		}
		return result;
	}
}
