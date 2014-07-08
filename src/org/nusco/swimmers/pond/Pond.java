package org.nusco.swimmers.pond;

import java.util.LinkedList;
import java.util.List;

import org.nusco.swimmers.creature.LifecycleEventListener;
import org.nusco.swimmers.creature.Swimmer;
import org.nusco.swimmers.creature.genetics.DNA;
import org.nusco.swimmers.creature.genetics.Embryo;
import org.nusco.swimmers.shared.physics.Vector;
import org.nusco.swimmers.shared.things.Thing;

public class Pond {

	public static final double MAX_THING_SIZE = 1500;

	private final long size;
	private final List<Thing> things = new LinkedList<>();
	private final List<PondEventListener> pondEvents = new LinkedList<>();

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
		for (PondEventListener pondEvent : pondEvents)
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

	protected void spawnFood() {
		add(new Food(), randomPosition());
	}

	protected final void spawnSwimmer() {
		final Swimmer swimmer = new Embryo(DNA.random()).develop();
		swimmer.addLifecycleEventListener(new LifecycleEventListener() {
			
			@Override
			public void died() {
				for (PondEventListener pondEvent : pondEvents)
					pondEvent.thingRemoved(swimmer);
			}
		});
		add(swimmer, randomPosition());
	}

	public void addEventListener(PondEventListener pondEventListener) {
		pondEvents.add(pondEventListener);
	}

	private Vector randomPosition() {
		double randomAngle = Math.random() * 360;
		double radius = getSize() / 2;
		double randomDistance = Math.random() * radius;
		return Vector.cartesian(radius, radius).plus(Vector.polar(randomAngle, randomDistance));
	}
}
