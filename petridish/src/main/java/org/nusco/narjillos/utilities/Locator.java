package org.nusco.narjillos.utilities;

import java.util.LinkedList;

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
	public Thing findThingNear(Vector position) {
		Thing result = findNear(position, "food");

		if (result != null)
			return result;

		result = findNear(position, "egg");

		if (result != null)
			return result;
		
		return findNarjilloNear(position);
	}

	public Narjillo findNarjilloNear(Vector position) {
		Narjillo result = null;
		double minDistance = Double.MAX_VALUE;

		for (Narjillo narjillo : new LinkedList<>(ecosystem.getNarjillos())) {
			// calculate from the center of mass, not the eye position
			Vector centerOfMass = narjillo.calculateCenterOfMass();
			double distance = centerOfMass.minus(position).getLength();
			if (distance < minDistance) {
				double maxFindDistance = Math.max(radiusOf(narjillo), MIN_FIND_RADIUS);
				if (distance < maxFindDistance) {
					minDistance = distance;
					result = narjillo;
				}
			}
		}

		return result;
	}

	private Thing findNear(Vector position, String label) {
		Thing result = null;
		double minDistance = Double.MAX_VALUE;

		for (Thing thing : new LinkedList<>(ecosystem.getThings(label))) {
			double distance = thing.getPosition().minus(position).getLength();
			if (distance < minDistance) {
				double maxFindDistance = Math.max(thing.getRadius(), MIN_FIND_RADIUS);
				if (distance < maxFindDistance) {
					minDistance = distance;
					result = thing;
				}
			}
		}

		return result;
	}

	private double radiusOf(Narjillo narjillo) {
		return narjillo.getBody().getRadius();
	}
}
