package org.nusco.narjillos.genomics;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.nusco.narjillos.core.utilities.RanGen;

/**
 * A pool of DNA strands.
 */
public class GenePoolWithHistory extends GenePool {

	private final Map<Long, DNA> dnaById = new LinkedHashMap<>();
	private final List<Long> currentPool = new LinkedList<>();
	private final Map<Long, Long> childrenToParents = new LinkedHashMap<>();

	public DNA getDNA(long id) {
		return dnaById.get(new Long(id));
	}

	public List<DNA> getAncestry(DNA dna) {
		List<DNA> result = new LinkedList<>();

		Long currentDnaId = dna.getId();
		while (currentDnaId != 0) {
			result.add(dnaById.get(currentDnaId));
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

	public int getCurrentSize() {
		return currentPool.size();
	}

	public int getHistoricalSize() {
		return dnaById.size();
	}

	public DNA createDNA(String dna) {
		DNA result = super.createDNA(dna);
		add(result, null);
		return result;
	}

	public DNA createRandomDNA(RanGen ranGen) {
		DNA result = super.createRandomDNA(ranGen);
		add(result, null);
		return result;
	}

	public DNA mutateDNA(DNA parent, RanGen ranGen) {
		DNA result = super.mutateDNA(parent, ranGen);
		add(result, parent);
		return result;
	}

	private void add(DNA dna, DNA parent) {
		dnaById.put(dna.getId(), dna);
		currentPool.add(dna.getId());
		if (parent == null)
			childrenToParents.put(dna.getId(), 0l);
		else
			childrenToParents.put(dna.getId(), parent.getId());
	}

	public void remove(DNA dna) {
		currentPool.remove(dna.getId());
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

	Map<Long, Long> getChildrenToParents() {
		return childrenToParents;
	}
}
