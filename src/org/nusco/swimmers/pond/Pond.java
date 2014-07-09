package org.nusco.swimmers.pond;

import java.util.LinkedList;
import java.util.List;

import org.nusco.swimmers.creature.Swimmer;
import org.nusco.swimmers.creature.SwimmerEventListener;
import org.nusco.swimmers.creature.genetics.DNA;
import org.nusco.swimmers.creature.genetics.Embryo;
import org.nusco.swimmers.shared.physics.Segment;
import org.nusco.swimmers.shared.physics.Vector;
import org.nusco.swimmers.shared.things.Thing;
import org.nusco.swimmers.shared.utilities.RanGen;

public class Pond {

	public static final double MAX_THING_SIZE = 1500;

	private static final double COLLISION_DISTANCE = 30;

	private final long size;
	private final List<Thing> things = new LinkedList<>();
	private final List<PondEventListener> pondEvents = new LinkedList<>();

	private int tickCounter = 0;

	public Pond(long size) {
		this.size = size;
	}
	
	public long getSize() {
		return size;
	}

	public synchronized List<Thing> getThings() {
		return new LinkedList<Thing>(things);
	}

	public List<Thing> getThings(String label) {
		List<Thing> result = new LinkedList<>();
		for (Thing thing : getThings())
			if (thing.getLabel().equals(label))
				result.add(thing);
		return result;
	}

	public Vector find(String typeOfThing, Vector near) {
		double minDistance = Double.MAX_VALUE;
		Vector result = Vector.cartesian(getSize() / 2, getSize() / 2);
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
		for (Thing thing : getThings())
			thing.tick();

		if (tickCounter-- < 0) {
			tickCounter = 1000;
			updateTargets();
		}
	}

	public Food spawnFood(Vector position) {
		Food food = new Food();
		addToThings(food, position);
		return food;
	}

	public final Swimmer spawnSwimmer(Vector position, DNA genes) {
		final Swimmer swimmer = new Embryo(genes).develop();
		swimmer.addSwimmerEventListener(new SwimmerEventListener() {
			
			@Override
			public void moved(Segment movement) {
				checkCollisionsWithFood(swimmer, movement);
			}
		
			@Override
			public void died() {
				remove(swimmer);
			}
		});
		addToThings(swimmer, position);
		return swimmer;
	}

	private void updateTarget(Swimmer swimmer) {
		Vector position = swimmer.getPosition();
		Vector locationOfClosestFood = find("food", position);
		swimmer.setTarget(locationOfClosestFood);
	}

	private void updateTargets() {
		for (Thing thing : getThings("swimmer"))
			updateTarget((Swimmer)thing);
	}

	private void checkCollisionsWithFood(Swimmer swimmer, Segment movement) {
		// TODO: naive algorithm. replace with space partitioning and finding neighbors
		List<Thing> foodThings = getThings("food");
		for (Thing foodThing : foodThings) {
			if (movement.getMinimumDistanceFromPoint(foodThing.getPosition()) < COLLISION_DISTANCE)
				consumeFood(swimmer, foodThing);
		}
	}

	private void consumeFood(Swimmer swimmer, Thing foodThing) {
		swimmer.feed();
		remove(foodThing);
		reproduce(swimmer);
		updateTargets();
	}

	private void reproduce(Swimmer swimmer) {
		DNA childDNA = swimmer.getGenes().mutate();
		Vector position = swimmer.getPosition().plus(Vector.cartesian(6000 * RanGen.next() - 3000, 6000 * RanGen.next() - 3000));
		spawnSwimmer(position, childDNA);
	}

	private synchronized final void addToThings(Thing thing, Vector position) {
		thing.setPosition(position);
		things.add(thing);
		for (PondEventListener pondEvent : pondEvents)
			pondEvent.thingAdded(thing);
	}

	private synchronized void remove(Thing thing) {
		things.remove(thing);
		for (PondEventListener pondEvent : pondEvents)
			pondEvent.thingRemoved(thing);
	}

	public void addEventListener(PondEventListener pondEventListener) {
		pondEvents.add(pondEventListener);
	}
}
