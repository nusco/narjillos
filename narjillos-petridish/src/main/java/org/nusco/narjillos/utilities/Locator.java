package org.nusco.narjillos.utilities;

import java.util.LinkedList;

import org.nusco.narjillos.ecosystem.Ecosystem;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.things.Thing;

public class Locator {

	private final Ecosystem ecosystem;

	public Locator(Ecosystem ecosystem) {
		this.ecosystem = ecosystem;
	}

	public Thing findThingAt(Vector position) {
		Thing result = findThingAt_WithLabel(position, "food");

		if (result != null)
			return result;

		result = findThingAt_WithLabel(position, "egg");

		if (result != null)
			return result;

		return findNarjilloAt(position);
	}

	public Thing findNarjilloAt(Vector position) {
		return findThingAt_WithLabel(position, "narjillo");
	}

	private Thing findThingAt_WithLabel(Vector position, String label) {
		Thing result = null;
		double minDistance = Double.MAX_VALUE;

		for (Thing thing : new LinkedList<>(ecosystem.getThings(label))) {
			double distance = thing.getCenter().minus(position).getLength();
			if (distance < thing.getRadius() && distance < minDistance) {
				minDistance = distance;
				result = thing;
			}
		}

		return result;
	}
}
