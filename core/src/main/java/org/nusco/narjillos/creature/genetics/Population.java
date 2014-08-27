package org.nusco.narjillos.creature.genetics;

import java.util.LinkedHashSet;
import java.util.Set;

public class Population {

	private final Set<Creature> creatures = new LinkedHashSet<Creature>();
	private Set<Creature> unmodifiableCreatures = new LinkedHashSet<Creature>();

	public synchronized void add(Creature creature) {
		creatures.add(creature);
		unmodifiableCreatures = null;
	}

	public synchronized void remove(Creature creature) {
		creatures.remove(creature);
		unmodifiableCreatures = null;
	}

	public synchronized Set<Creature> getCreatures() {
		if (unmodifiableCreatures == null) {
			unmodifiableCreatures = new LinkedHashSet<Creature>();
			unmodifiableCreatures.addAll(creatures);
		}
		return unmodifiableCreatures;
	}

	public Creature getMostTypicalSpecimen() {
		Creature result = null;
		int lowestGeneticDistance = Integer.MAX_VALUE;
		Set<Creature> allCreatures = getCreatures();
		for (Creature creature : allCreatures) {
			int currentGeneticDistance = totalGeneticDistanceFromOthers(creature, allCreatures);
			if (currentGeneticDistance < lowestGeneticDistance) {
				result = creature;
				lowestGeneticDistance = currentGeneticDistance;
			}
		}
		return result;
	}

	private int totalGeneticDistanceFromOthers(Creature creature, Set<Creature> allCreatures) {
		int result = 0;
		for (Creature otherCreature : getCreatures())
			if (otherCreature != creature)
				result += creature.getDNA().getLevenshteinDistanceFrom(otherCreature.getDNA());
		return result;
	}

	public void tick() {
		Set<Creature> copyOfCreatures = new LinkedHashSet<Creature>();
		synchronized (this) {
			copyOfCreatures.addAll(creatures);
		}

		for (Creature creature : copyOfCreatures)
			creature.tick();
	}

	public synchronized int size() {
		return creatures.size();
	}
}
