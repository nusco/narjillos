package org.nusco.narjillos.application.utilities;

import java.util.LinkedList;
import java.util.List;

import org.nusco.narjillos.core.physics.Vector;
import org.nusco.narjillos.core.things.Thing;
import org.nusco.narjillos.ecosystem.Culture;

public class Locator {

	private final Culture culture;

	public Locator(Culture culture) {
		this.culture = culture;
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

	public Thing findRandomLivingThing() {
		List<Thing> allThings = new LinkedList<>();
		allThings.addAll(culture.getThings("narjillo"));
		allThings.addAll(culture.getThings("egg"));
		return allThings.get((int)(Math.random() * allThings.size()));
	}

	private Thing findThingAt_WithLabel(Vector position, String label) {
		Thing result = null;
		double minDistance = Double.MAX_VALUE;

		for (Thing thing : culture.getThings(label)) {
			double distance = thing.getCenter().minus(position).getLength();
			if (distance < thing.getRadius() && distance < minDistance) {
				minDistance = distance;
				result = thing;
			}
		}

		return result;
	}
}
