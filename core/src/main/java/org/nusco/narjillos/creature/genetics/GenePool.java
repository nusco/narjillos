package org.nusco.narjillos.creature.genetics;

import java.util.LinkedHashSet;
import java.util.Set;

public class GenePool {

	private final Set<Creature> creatures = new LinkedHashSet<Creature>();

	public void add(Creature creature) {
		creatures.add(creature);
	}

	public void remove(Creature creature) {
		creatures.remove(creature);
	}

	public Set<Creature> getCreatures() {
		return creatures;
	}

	public int size() {
		return creatures.size();
	}

	public Creature getMostTypicalSpecimen() {
		Creature result = null;
		int lowestLevenshteinDistance = Integer.MAX_VALUE;
		for (Creature creature : getCreatures()) {
			int currentLevenshteinDistance = totalLevenshteinDistanceFromOthers(creature, getCreatures());
			if (currentLevenshteinDistance < lowestLevenshteinDistance) {
				result = creature;
				lowestLevenshteinDistance = currentLevenshteinDistance;
			}
		}
		return result;
	}

	private int totalLevenshteinDistanceFromOthers(Creature creature, Set<Creature> allCreatures) {
		int result = 0;
		for (Creature otherCreature : getCreatures())
			if (otherCreature != creature)
				result += creature.getDNA().getLevenshteinDistanceFrom(otherCreature.getDNA());
		return result;
	}
}
