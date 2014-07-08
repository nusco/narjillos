package org.nusco.swimmers.pond;

import java.util.LinkedList;
import java.util.List;

import org.nusco.swimmers.shared.physics.Vector;
import org.nusco.swimmers.shared.things.Thing;

public class Pond {

	public static final double MAX_THING_SIZE = 1500;

	private final long size;
	private final List<Thing> things = new LinkedList<>();
	private final List<PondEvent> pondEvents = new LinkedList<>();

	public Pond(long size) {
		this.size = size;
	}
	
	public long getSize() {
		return size;
	}

	public List<Thing> getThings() {
		return things;
	}

	protected List<Thing> getThings(String label) {
		List<Thing> result = new LinkedList<>();
		for (Thing thing : things)
			if (thing.getLabel().equals(label))
				result.add(thing);
		return result;
	}

	public final void add(Thing thing, Vector position) {
		thing.setPosition(position);
		things.add(thing);
		for (PondEvent pondEvent : pondEvents)
			pondEvent.thingAdded(thing);
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

	public void addEventListener(PondEvent pondEvent) {
		pondEvents.add(pondEvent);
	}
}
