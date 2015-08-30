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

	private final Map<Long, DNA> historicalDnaById = new LinkedHashMap<>();
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
	public DNA getDna(Long id) {
		return historicalDnaById.get(new Long(id));
	}

	@Override
	public List<DNA> getAncestryOf(DNA dna) {
		List<DNA> result = new LinkedList<>();

		if (dna == null)
			return result;

		Long currentDnaId = dna.getId();
		while (currentDnaId != 0) {
			DNA currentDna = historicalDnaById.get(currentDnaId);
			result.add(currentDna);
			currentDnaId = currentDna.getParentId();
		}

		Collections.reverse(result);
		return result;
	}

	@Override
	public DNA getMostSuccessfulDNA() {
		DNA result = null;
		int lowestLevenshteinDistance = Integer.MAX_VALUE;
		for (Long dnaId : getCurrentPool()) {
			DNA dna = historicalDnaById.get(dnaId);
			int currentLevenshteinDistance = totalLevenshteinDistanceFromTheRestOfThePool(dna);
			if (currentLevenshteinDistance < lowestLevenshteinDistance) {
				result = dna;
				lowestLevenshteinDistance = currentLevenshteinDistance;
			}
		}
		return result;
	}

	@Override
	public
	Set<Long> getHistoricalPool() {
		return historicalDnaById.keySet();
	}

	@Override
	Map<Long, Long> getChildrenToParents() {
		Map<Long, Long> result = new LinkedHashMap<>();
		for (DNA dna : historicalDnaById.values())
			result.put(dna.getId(), dna.getParentId());
		return result;
	}

	@Override
	Map<Long, List<Long>> getParentsToChildren() {
		Map<Long, List<Long>> result = new LinkedHashMap<>();
		result.put(0L, new LinkedList<Long>());
		for (DNA dna : historicalDnaById.values())
			result.put(dna.getId(), new LinkedList<Long>());
		for (DNA dna : historicalDnaById.values()) 
			result.get(dna.getParentId()).add(dna.getId());
		return result;
	}

	@Override
	public int getGenerationOf(DNA dna) {
		return dnaToGeneration.get(dna.getId());
	}

	private void add(DNA dna, DNA parent) {
		historicalDnaById.put(dna.getId(), dna);
		
		if (parent == null)
			dnaToGeneration.put(dna.getId(), 1);
		else
			dnaToGeneration.put(dna.getId(), getGenerationOf(parent) + 1);
	}

	private int totalLevenshteinDistanceFromTheRestOfThePool(DNA dna) {
		int result = 0;
		for (Long otherDNAId : getCurrentPool()) {
			DNA otherDNA = historicalDnaById.get(otherDNAId);
			if (!otherDNA.equals(dna))
				result += dna.getLevenshteinDistanceFrom(otherDNA);
		}
		return result;
	}
}
