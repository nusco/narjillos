package org.nusco.narjillos.genomics;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.nusco.narjillos.core.utilities.NumGen;

/**
 * A pool of DNA strands.
 */
public class GenePool {

	// TODO: remove and make the whole thing transient in experiment
	private final Set<DNA> currentPool = new LinkedHashSet<>();

	private transient DNALog dnaLog;

	public GenePool(DNALog dnaLog) {
		this.dnaLog = dnaLog;
	}

	public DNA createDna(String dna, NumGen numGen) {
		DNA result = new DNA(numGen.nextSerial(), dna, DNA.NO_PARENT);
		addToPool(result);
		return result;
	}

	public DNA createRandomDna(NumGen numGen) {
		DNA result = DNA.random(numGen.nextSerial(), numGen);
		addToPool(result);
		return result;
	}

	public DNA mutateDna(DNA parent, NumGen numGen) {
		DNA result = parent.mutate(numGen.nextSerial(), numGen);
		addToPool(result);
		return result;
	}

	public DNA getDna(long id) {
		return dnaLog.getDna(id);
	}
	
	public List<DNA> getAncestryOf(DNA dna) {
		List<DNA> result = new LinkedList<>();

		if (dna == null)
			return result;

		Long currentDnaId = dna.getId();
		Map<Long, DNA> dnaById = getDnaById();
		while (currentDnaId != 0) {
			DNA currentDna = dnaById.get(currentDnaId);
			result.add(currentDna);
			currentDnaId = currentDna.getParentId();
		}

		Collections.reverse(result);
		return result;
	}

	public DNA getMostSuccessfulDna() {
		DNA result = null;
		int lowestLevenshteinDistance = Integer.MAX_VALUE;
		for (DNA dna : getCurrentPool()) {
			int currentLevenshteinDistance = totalLevenshteinDistanceFromTheRestOfThePool(dna);
			if (currentLevenshteinDistance < lowestLevenshteinDistance) {
				result = dna;
				lowestLevenshteinDistance = currentLevenshteinDistance;
			}
		}
		return result;
	}

	Map<Long, Long> getChildrenToParents() {
		Map<Long, Long> result = new LinkedHashMap<>();
		for (DNA dna : getDnaById().values())
			result.put(dna.getId(), dna.getParentId());
		return result;
	}

	Map<Long, List<Long>> getParentsToChildren() {
		List<DNA> allDNA = dnaLog.getAllDna();
		Map<Long, List<Long>> result = new LinkedHashMap<>();
		result.put(0L, new LinkedList<Long>());
		for (DNA dna : allDNA)
			result.put(dna.getId(), new LinkedList<Long>());
		for (DNA dna : allDNA) 
			result.get(dna.getParentId()).add(dna.getId());
		return result;
	}

	private Map<Long, DNA> getDnaById() {
		Map<Long, DNA> result = new LinkedHashMap<>();
		for (DNA dna : dnaLog.getAllDna())
			result.put(dna.getId(), dna);
		return result;
	}

	private int totalLevenshteinDistanceFromTheRestOfThePool(DNA dna) {
		int result = 0;
		for (DNA otherDNA: getCurrentPool()) {
			if (!otherDNA.equals(dna))
				result += dna.getLevenshteinDistanceFrom(otherDNA);
		}
		return result;
	}

	public void remove(DNA dna) {
		currentPool.remove(dna.getId());
	}

	public List<DNA> getHistoricalPool() {
		return dnaLog.getAllDna();
	}

	private Set<DNA> getCurrentPool() {
		return currentPool;
	}

	private void addToPool(DNA dna) {
		currentPool.add(dna);
		dnaLog.save(dna);
	}
}
