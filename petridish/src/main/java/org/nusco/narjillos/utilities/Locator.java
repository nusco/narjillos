package org.nusco.narjillos.utilities;

import java.util.LinkedList;

import org.nusco.narjillos.creature.Egg;
import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.ecosystem.Ecosystem;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.things.Thing;

public class Locator {

	final int MIN_FIND_RADIUS = 200;

	private final Ecosystem ecosystem;

	public Locator(Ecosystem ecosystem) {
		this.ecosystem = ecosystem;
	}

	/**
	 * Finds eggs or narjillos within a limited radius, which is proportional to
	 * the size of the egg/narjillos involved. (Intuitively, bigger things are
	 * "easier to find").
	 */
	public Thing findLivingThingNear(Vector position) {
		Thing result = findEggNear(position);

		if (result != null)
			return result;
		
		return findNarjilloNear(position);
	}

	public Narjillo findNarjilloNear(Vector position) {
		Narjillo result = null;
		double minDistance = Double.MAX_VALUE;

		for (Narjillo narjillo : new LinkedList<>(ecosystem.getNarjillos())) {
			Vector centerOfMass = narjillo.calculateCenterOfMass();
			double distance = centerOfMass.minus(position).getLength();
			if (distance < minDistance) {
				double maxFindDistance = Math.max(radiusOf(narjillo) * 1.2, MIN_FIND_RADIUS);
				if (distance < maxFindDistance) {
					minDistance = distance;
					result = narjillo;
				}
			}
		}

		return result;
	}

	private Thing findEggNear(Vector position) {
		Thing result = null;
		double minDistance = Double.MAX_VALUE;

		for (Thing egg : new LinkedList<>(ecosystem.getThings("egg"))) {
			double distance = egg.getPosition().minus(position).getLength();
			if (distance < minDistance) {
				double maxFindDistance = Math.max(Egg.RADIUS * 2, MIN_FIND_RADIUS);
				if (distance < maxFindDistance) {
					minDistance = distance;
					result = egg;
				}
			}
		}

		return result;
	}

	private double radiusOf(Narjillo narjillo) {
		return narjillo.getBody().getRadius();
	}
}
