package org.nusco.narjillos.creature.genetics;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.nusco.narjillos.creature.Narjillo;

public class Population {

	private final Set<Narjillo> narjillos = Collections.newSetFromMap(new ConcurrentHashMap<Narjillo, Boolean>());

	public void add(Narjillo narjillo) {
		narjillos.add(narjillo);
	}

	public Collection<Narjillo> toCollection() {
		return narjillos;
	}

	public Narjillo getMostTypicalSpecimen() {
		Narjillo result = null;
		int lowestGeneticDistance = Integer.MAX_VALUE;
		for (Narjillo narjillo : narjillos) {
			int currentGeneticDistance = totalGeneticDistanceFromOthers(narjillo);
			if (currentGeneticDistance < lowestGeneticDistance) {
				result = narjillo;
				lowestGeneticDistance = currentGeneticDistance;
			}
		}
		return result;
	}

	private int totalGeneticDistanceFromOthers(Narjillo narjillo) {
		int result = 0;
		for (Narjillo otherNarjillo : narjillos)
			if (otherNarjillo != narjillo)
				result += narjillo.getDNA().getDistanceWith(otherNarjillo.getDNA());
		return result;
	}

	public void tick() {
		for (Narjillo narjillo : narjillos)
			narjillo.tick();
	}

	public Set<Narjillo> getCollection() {
		return narjillos;
	}

	public void remove(Narjillo narjillo) {
		narjillos.remove(narjillo);
	}

	public int size() {
		return narjillos.size();
	}
}
