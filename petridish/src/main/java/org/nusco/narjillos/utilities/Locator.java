package org.nusco.narjillos.utilities;

import java.util.LinkedList;

import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.ecosystem.Ecosystem;
import org.nusco.narjillos.shared.physics.Vector;

public class Locator {

	private final Ecosystem ecosystem;

	public Locator(Ecosystem ecosystem) {
		this.ecosystem = ecosystem;
	}

	/**
	 * Finds narjillos within a limited radius, which is proportional to 
	 * the size of the narjillos involved. (Intuitively, bigger narjillos
	 * are "easier to find").
	 */
	public Narjillo findNarjilloNear(Vector position) {
		final int MIN_FIND_RADIUS = 200;
		
		double minDistance = Double.MAX_VALUE;
		Narjillo result = null;
		
		for (Narjillo narjillo : new LinkedList<>(ecosystem.getNarjillos())) {
			Vector centerOfMass = narjillo.calculateCenterOfMass();
			double distance = centerOfMass.minus(position).getLength();
			if (distance < minDistance) {
				double maxFindDistance = Math.max(radius(narjillo) * 1.2, MIN_FIND_RADIUS);
				if (centerOfMass.minus(position).getLength() < maxFindDistance) {
					minDistance = distance;
					result = narjillo;
				}
			}
		}
		
		return result;
	}

	private double radius(Narjillo narjillo) {
		return narjillo.getBody().getRadius();
	}
}
