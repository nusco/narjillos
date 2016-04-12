package org.nusco.narjillos.analysis;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.nusco.narjillos.core.geometry.Vector;
import org.nusco.narjillos.core.things.Energy;
import org.nusco.narjillos.core.utilities.NumberFormatter;
import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.genomics.DNA;
import org.nusco.narjillos.genomics.DNALog;

public class DNAAnalyzer {

	private final DNALog dnaLog;

	public DNAAnalyzer(DNALog dnaLog) {
		this.dnaLog = dnaLog;
	}

	public DNA getDna(Long id) {
		return dnaLog.getDna(id);
	}

	public List<DNA> getGermline(DNA dna) {
		List<DNA> result = new LinkedList<>();

		while (dna != null) {
			result.add(dna);
			dna = dnaLog.getDna(dna.getParentId());
		}

		Collections.reverse(result);
		return result;
	}

	public DNA getMostSuccessfulDna() {
		List<DNA> liveDna = dnaLog.getLiveDna();
		DNA result = null;
		int lowestLevenshteinDistance = Integer.MAX_VALUE;
		for (DNA dna: liveDna) {
			int currentLevenshteinDistance = totalLevenshteinDistanceFromTheRestOfThePool(dna, liveDna);
			if (currentLevenshteinDistance < lowestLevenshteinDistance) {
				result = dna;
				lowestLevenshteinDistance = currentLevenshteinDistance;
			}
		}
		return result;
	}

	public String getDNAStatistics(DNA dna) {
		Narjillo specimen = new Narjillo(dna, Vector.ZERO, 90, Energy.INFINITE);
		StringBuilder result = new StringBuilder();
		result.append("Number of organs   => ").append(specimen.getOrgans().size()).append("\n");
		result.append("Adult mass         => ").append(NumberFormatter.format(specimen.getBody().getAdultMass())).append("\n");
		result.append("Wave beat ratio    => ").append(NumberFormatter.format(specimen.getBody().getWaveBeatRatio())).append("\n");
		result.append("Energy to children => ").append(NumberFormatter.format(specimen.getBody().getEnergyToChildren())).append("\n");
		result.append("Egg interval       => ").append(specimen.getBody().getEggInterval()).append("\n");
		result.append("Egg velocity       => ").append(specimen.getBody().getEggVelocity()).append("\n");
		return result.toString();
	}

	public int getNumberOfLivingGermlines() {
		Set<DNA> originalAncestors = new LinkedHashSet<>();

		// optimization: don't walk the same ancestry twice
		// (this can have a big impact on performance)
		Set<DNA> alreadyExplored = new LinkedHashSet<>();

		for (DNA dna : dnaLog.getLiveDna()) {
			DNA originalAncestor = findOriginalAncestor(dna, alreadyExplored);
			if (originalAncestor != null)
				originalAncestors.add(originalAncestor);
		}

		return originalAncestors.size();
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

	private int totalLevenshteinDistanceFromTheRestOfThePool(DNA dna, List<DNA> aliveDna) {
		int result = 0;
		for (DNA otherDna: aliveDna) {
			if (!otherDna.equals(dna))
				result += dna.getLevenshteinDistanceFrom(otherDna);
		}
		return result;
	}

	private Map<Long, DNA> getDnaById() {
		Map<Long, DNA> result = new LinkedHashMap<>();
		for (DNA dna : dnaLog.getAllDna())
			result.put(dna.getId(), dna);
		return result;
	}

	/**
	 * Returns the original ancestor, or null if the ancestry chain converges into
	 * a chain that has already been explored.
	 */
	private DNA findOriginalAncestor(DNA dna, Set<DNA> alreadyExplored) {
		DNA ancestor = dna;
		while (true) {
			if (alreadyExplored.contains(ancestor))
				return null;

			alreadyExplored.add(ancestor);
			
			if (!ancestor.hasParent())
				return ancestor;
			
			ancestor = dnaLog.getDna(ancestor.getParentId());
		}
	}
}
