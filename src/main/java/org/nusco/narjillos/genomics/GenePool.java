package org.nusco.narjillos.genomics;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.nusco.narjillos.shared.utilities.RanGen;

/**
 * A pool of DNA strands.
 */
public class GenePool {

	private final Map<Long, DNA> dnaById = new LinkedHashMap<>();
	private final List<Long> currentPool = new LinkedList<>();
	private final Map<Long, Long> childrenToParents = new LinkedHashMap<>();

	private long dnaSerial = 0;
	private boolean ancestralMemory = false;

	public void enableAncestralMemory() {
		ancestralMemory = true;
	}

	public boolean hasAncestralMemory() {
		return ancestralMemory;
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

	public int getCurrentPoolSize() {
		return currentPool.size();
	}

	public int getHistoricalPoolSize() {
		return dnaById.size();
	}

	public DNA createDNA(String dna) {
		DNA result = new DNA(nextId(), dna);
		add(result, null);
		return result;
	}

	public DNA createRandomDNA(RanGen ranGen) {
		DNA result = DNA.random(nextId(), ranGen);
		add(result, null);
		return result;
	}

	public DNA mutateDNA(DNA parent, RanGen ranGen) {
		DNA result = parent.copyWithMutations(nextId(), ranGen);
		add(result, parent);
		return result;
	}

	public void remove(DNA dna) {
		if (!hasAncestralMemory())
			return;

		currentPool.remove(dna.getId());
	}

	private long nextId() {
		return ++dnaSerial;
	}
	
	private void add(DNA dna, DNA parent) {
		if (!hasAncestralMemory())
			return;
		
		dnaById.put(dna.getId(), dna);
		currentPool.add(dna.getId());
		if (parent == null)
			childrenToParents.put(dna.getId(), 0l);
		else
			childrenToParents.put(dna.getId(), parent.getId());
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
