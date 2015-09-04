package org.nusco.narjillos.genomics;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.nusco.narjillos.core.utilities.NumGen;

/**
 * A pool of DNA strands.
 */
public class GenePool implements Cloneable {

	private DNALog dnaLog;
	private int allDnaCountCache;

	public GenePool(DNALog dnaLog) {
		this.dnaLog = dnaLog;
		allDnaCountCache = dnaLog.getDnaCount();
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

	public void remove(DNA dna) {
		dnaLog.markAsDead(dna.getId());
	}

	public List<DNA> getAncestryOf(long dnaId) {
		List<DNA> result = new LinkedList<>();

		Map<Long, DNA> dnaById = getDnaById();
		while (dnaId != 0) {
			DNA currentDna = dnaById.get(dnaId);
			result.add(currentDna);
			dnaId = currentDna.getParentId();
		}

		Collections.reverse(result);
		return result;
	}

	// TODO: move these slow operations to Lab. First, it's where they belong,
	// and second, it avoids usage by mistake.
	
	/*
	 * Slow and memory-intensive! Only call for lab analysis.
	 */
	public DNA getMostSuccessfulDna() {
		List<DNA> aliveDna = getAliveDna();
		DNA result = null;
		int lowestLevenshteinDistance = Integer.MAX_VALUE;
		for (DNA dna: aliveDna) {
			int currentLevenshteinDistance = totalLevenshteinDistanceFromTheRestOfThePool(dna, aliveDna);
			if (currentLevenshteinDistance < lowestLevenshteinDistance) {
				result = dna;
				lowestLevenshteinDistance = currentLevenshteinDistance;
			}
		}
		return result;
	}

	/*
	 * Slow and memory-intensive! Only call for lab analysis.
	 */
	public DNA getDna(long id) {
		return getAllDna().get(id);
	}

	public int getAllDnaCount() {
		return allDnaCountCache;
	}

	public void terminate() {
		dnaLog.close();
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

	private int totalLevenshteinDistanceFromTheRestOfThePool(DNA dna, List<DNA> aliveDna) {
		int result = 0;
		for (DNA otherDna: aliveDna) {
			if (!otherDna.equals(dna))
				result += dna.getLevenshteinDistanceFrom(otherDna);
		}
		return result;
	}

	private Map<Long, DNA> getAllDna() {
		Map<Long, DNA> result = new LinkedHashMap<>();
		for (DNA dna : dnaLog.getAllDna())
			result.put(dna.getId(), dna);
		return result;
	}

	private List<DNA> getAliveDna() {
		return dnaLog.getLiveDna();
	}

	private void addToPool(DNA dna) {
		dnaLog.save(dna);
		allDnaCountCache++;
	}
}
