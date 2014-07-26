package org.nusco.narjillos.creature.genetics;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Population {

	private final Set<Creature> creatures = Collections.newSetFromMap(new ConcurrentHashMap<Creature, Boolean>());

	public void add(Creature creature) {
		creatures.add(creature);
	}

	public Collection<Creature> toCollection() {
		return creatures;
	}

	public Creature getMostTypicalSpecimen() {
		Creature result = null;
		int lowestGeneticDistance = Integer.MAX_VALUE;
		for (Creature creature : creatures) {
			int currentGeneticDistance = totalGeneticDistanceFromOthers(creature);
			if (currentGeneticDistance < lowestGeneticDistance) {
				result = creature;
				lowestGeneticDistance = currentGeneticDistance;
			}
		}
		return result;
	}

	private int totalGeneticDistanceFromOthers(Creature creature) {
		int result = 0;
		for (Creature otherCreature : creatures)
			if (otherCreature != creature)
				result += creature.getDNA().getDistanceFrom(otherCreature.getDNA());
		return result;
	}

	public void tick() {
		for (Creature creature : creatures)
			creature.tick();
	}

	public Set<Creature> getCollection() {
		return creatures;
	}

	public void remove(Creature creature) {
		creatures.remove(creature);
	}

	public int size() {
		return creatures.size();
	}
}
