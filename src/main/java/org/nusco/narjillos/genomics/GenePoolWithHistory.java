package org.nusco.narjillos.genomics;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.nusco.narjillos.core.utilities.RanGen;

/**
 * A GenePool that tracks ancestry history.
 */
public class GenePoolWithHistory extends GenePool {

	private final Map<Long, DNA> dnaById = new LinkedHashMap<>();
	private final List<Long> currentPool = new LinkedList<>();
	private final Map<Long, Long> childrenToParents = new LinkedHashMap<>();
	private final Map<Long, Integer> dnaToGeneration = new LinkedHashMap<>();

	@Override
	public DNA createDNA(String dna) {
		DNA result = super.createDNA(dna);
		add(result, null);
		return result;
	}

	@Override
	public DNA createRandomDNA(RanGen ranGen) {
		DNA result = super.createRandomDNA(ranGen);
		add(result, null);
		return result;
	}

	@Override
	public DNA mutateDNA(DNA parent, RanGen ranGen) {
		DNA result = super.mutateDNA(parent, ranGen);
		add(result, parent);
		return result;
	}

	@Override
	public void remove(DNA dna) {
		currentPool.remove(dna.getId());
	}

	@Override
	public DNA getDna(Long id) {
		return dnaById.get(new Long(id));
	}

	@Override
	public List<DNA> getAncestry(DNA dna) {
		List<DNA> result = new LinkedList<>();

		if (dna == null)
			return result;

		Long currentDnaId = dna.getId();
		while (currentDnaId != 0) {
			result.add(dnaById.get(currentDnaId));
			currentDnaId = childrenToParents.get(currentDnaId);
		}

		Collections.reverse(result);
		return result;
	}

	@Override
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

	@Override
	List<Long> getCurrentPool() {
		return currentPool;
	}

	@Override
	Set<Long> getHistoricalPool() {
		return dnaById.keySet();
	}

	@Override
	Map<Long, Long> getChildrenToParents() {
		return childrenToParents;
	}

	@Override
	public int getGenerationOf(DNA dna) {
		return dnaToGeneration.get(dna.getId());
	}

	private void add(DNA dna, DNA parent) {
		dnaById.put(dna.getId(), dna);
		
		if (parent == null)
			dnaToGeneration.put(dna.getId(), 1);
		else
			dnaToGeneration.put(dna.getId(), getGenerationOf(parent) + 1);
			
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
