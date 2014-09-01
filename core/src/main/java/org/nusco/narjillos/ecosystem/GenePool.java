package org.nusco.narjillos.ecosystem;

import java.util.LinkedHashSet;
import java.util.Set;

import org.nusco.narjillos.creature.Narjillo;

public class GenePool {

	private final Set<Narjillo> narjillos = new LinkedHashSet<Narjillo>();

	public void add(Narjillo narjillo) {
		narjillos.add(narjillo);
	}

	public void remove(Narjillo narjillo) {
		narjillos.remove(narjillo);
	}

	public Set<Narjillo> getNarjillos() {
		return narjillos;
	}

	public int getSize() {
		return narjillos.size();
	}

	public Narjillo getMostTypicalSpecimen() {
		Narjillo result = null;
		int lowestLevenshteinDistance = Integer.MAX_VALUE;
		for (Narjillo narjillo : getNarjillos()) {
			int currentLevenshteinDistance = totalLevenshteinDistanceFromOthers(narjillo, getNarjillos());
			if (currentLevenshteinDistance < lowestLevenshteinDistance) {
				result = narjillo;
				lowestLevenshteinDistance = currentLevenshteinDistance;
			}
		}
		return result;
	}

	private int totalLevenshteinDistanceFromOthers(Narjillo narjillos, Set<Narjillo> allNarjillos) {
		int result = 0;
		for (Narjillo otherNarjillo : getNarjillos())
			if (otherNarjillo != narjillos)
				result += narjillos.getDNA().getLevenshteinDistanceFrom(otherNarjillo.getDNA());
		return result;
	}
}
