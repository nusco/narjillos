package org.nusco.swimmers.pond;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.nusco.swimmers.shared.physics.Vector;
import org.nusco.swimmers.shared.things.Thing;

public class Pond {

	public final static int USEFUL_AREA_SIZE = 50000;

	private Map<Thing, Vector> thingsToPositions = new HashMap<>();

	public Set<Thing> getThings() {
		return thingsToPositions.keySet();
	}

	public void add(Thing thing, Vector position) {
		thing.setPosition(position);
		thingsToPositions.put(thing, position);
	}

	public Vector find(String typeOfThing, Vector near) {
		double minDistance = Double.MAX_VALUE;
		Vector result = Vector.ZERO;
		for (Thing thing : getThings()) {
			if (thing.getLabel().equals(typeOfThing)) {
				Vector target = thingsToPositions.get(thing);
				double distance = target.minus(near).getLength();
				if (distance < minDistance) {
					minDistance = distance;
					result = target;
				}
			}
		}
		return result;
	}

	public void tick() {
		for (Thing thing : thingsToPositions.keySet())
			thing.tick();
	}
}
