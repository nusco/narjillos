package org.nusco.narjillos.creature.genetics;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GenePool implements DNAObserver {

	private Map<DNA, DNA> childsToParents = new LinkedHashMap<>();
	private Set<DNA> currentPool = new LinkedHashSet<>();
			
	@Override
	public void created(DNA newDNA, DNA parent) {
		childsToParents.put(newDNA, parent);
		currentPool.add(newDNA);
	}

	@Override
	public void removed(DNA dna) {
		currentPool.remove(dna);
	}

	public List<DNA> getAncestry(DNA dna) {
		LinkedList<DNA> result = new LinkedList<>();
		result.add(dna);
		collectAncestors(result, dna);
		Collections.reverse(result);
		return result;
	}

	private void collectAncestors(LinkedList<DNA> collector, DNA dna) {
		DNA parent = childsToParents.get(dna);
		if (parent == null)
			return;
		collector.add(parent);
		collectAncestors(collector, parent);
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
}
