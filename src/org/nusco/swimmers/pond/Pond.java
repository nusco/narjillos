package org.nusco.swimmers.pond;

import java.util.LinkedList;
import java.util.List;

import org.nusco.swimmers.shared.physics.Vector;
import org.nusco.swimmers.shared.things.Thing;

public class Pond {

	private final int size;
	private final List<Thing> things = new LinkedList<>();

	public Pond(int size) {
		this.size = size;
	}
	
	public double getSize() {
		return size;
	}

	public List<Thing> getThings() {
		return things;
	}

	public void add(Thing thing, Vector position) {
		thing.setPosition(position);
		things.add(thing);
	}

	public Vector find(String typeOfThing, Vector near) {
		double minDistance = Double.MAX_VALUE;
		Vector result = Vector.ZERO;
		for (Thing thing : getThings()) {
			if (thing.getLabel().equals(typeOfThing)) {
				double distance = thing.getPosition().minus(near).getLength();
				if (distance < minDistance) {
					minDistance = distance;
					result = thing.getPosition();
				}
			}
		}
		return result;
	}

	public void tick() {
		for (Thing thing : things)
			thing.tick();
	}
}
