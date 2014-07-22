package org.nusco.swimmers.pond;

import java.util.LinkedList;
import java.util.List;

import org.nusco.swimmers.creature.Narjillo;
import org.nusco.swimmers.creature.SwimmerEventListener;
import org.nusco.swimmers.creature.genetics.DNA;
import org.nusco.swimmers.creature.genetics.Embryo;
import org.nusco.swimmers.shared.physics.Segment;
import org.nusco.swimmers.shared.physics.Vector;
import org.nusco.swimmers.shared.things.Thing;
import org.nusco.swimmers.shared.utilities.RanGen;

public class Pond {

	private static final double COLLISION_DISTANCE = 30;

	private final long size;

	private final List<FoodPiece> foodPieces = new LinkedList<>();
	private final List<Narjillo> narjillos = new LinkedList<>();

	private final List<PondEventListener> pondEvents = new LinkedList<>();

	private int tickCounter = 0;

	public Pond(long size) {
		this.size = size;
	}

	public long getSize() {
		return size;
	}

	public List<Thing> getThings() {
		List<Thing> result = new LinkedList<Thing>();
		result.addAll(getFoodPieces());
		result.addAll(getNarjillos());
		return result;
	}

	// Expensive but safe when invoked from another thread.
	// optimize
	private List<FoodPiece> getFoodPieces() {
		synchronized (foodPieces) {
			return new LinkedList<FoodPiece>(foodPieces);
		}
	}

	private synchronized List<Narjillo> getNarjillos() {
		synchronized (narjillos) {
			return new LinkedList<Narjillo>(narjillos);
		}
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
			tickCounter = 100000;
			updateTargets();
		}
	}

	public FoodPiece spawnFood(Vector position) {
		FoodPiece newFood = new FoodPiece();
		synchronized (foodPieces) {
			foodPieces.add(newFood);
		}
		placeInPond(newFood, position);
		return newFood;
	}

	public final Narjillo spawnSwimmer(Vector position, DNA genes) {
		final Narjillo narjillo = new Embryo(genes).develop();
		narjillo.addSwimmerEventListener(new SwimmerEventListener() {

			@Override
			public void moved(Segment movement) {
				checkCollisionsWithFood(narjillo, movement);
			}

			@Override
			public void died() {
				removeFromPond(narjillo);
				synchronized (narjillos) {
					narjillos.remove(narjillo);
				}
			}
		});
		synchronized (narjillos) {
			narjillos.add(narjillo);
		}
		placeInPond(narjillo, position);
		return narjillo;
	}

	private void updateTarget(Narjillo swimmer) {
		Vector position = swimmer.getPosition();
		Vector locationOfClosestFood = find("food_piece", position);
		swimmer.setTarget(locationOfClosestFood);
	}

	private void updateTargets() {
		for (Narjillo narjillo : getNarjillos())
			updateTarget(narjillo);
	}

	private void checkCollisionsWithFood(Narjillo narjillo, Segment movement) {
		// TODO: naive algorithm. replace with space partitioning and finding
		// neighbors
		for (FoodPiece foodThing : getFoodPieces()) {
			if (isUnderSafeDistance(narjillo, foodThing)
					&& movement.getMinimumDistanceFromPoint(foodThing.getPosition()) < COLLISION_DISTANCE)
				consumeFood(narjillo, foodThing);
		}
	}

	// ugly optimization to avoid getting into the expensive
	// getMinimumDistanceFromPoint() method.
	// this will hopefully be redundant once we have a good space partition algorithm,
	// but it makes quite a difference until then
	private boolean isUnderSafeDistance(Narjillo narjillo, Thing foodThing) {
		Vector position = narjillo.getPosition();
		 // it's unlikely that we need to consider food further away
		final double safeDistance = 300;
		if (Math.abs(position.x - foodThing.getPosition().x) < safeDistance)
			return true;
		if (Math.abs(position.y - foodThing.getPosition().y) < safeDistance)
			return true;
		return false;
	}

	private void consumeFood(Narjillo narjillo, FoodPiece foodPiece) {
		narjillo.feed();
		removeFromPond(foodPiece);
		synchronized (foodPieces) {
			foodPieces.remove(foodPiece);
		}
		reproduce(narjillo);
		updateTargets();
	}

	private void reproduce(Narjillo narjillo) {
		DNA childDNA = narjillo.getGenes().mutate();
		Vector position = narjillo.getPosition().plus(
				Vector.cartesian(6000 * RanGen.nextDouble() - 3000, 6000 * RanGen.nextDouble() - 3000));
		spawnSwimmer(position, childDNA);
	}

	private final void placeInPond(Thing thing, Vector position) {
		thing.setPosition(position);
		for (PondEventListener pondEvent : pondEvents)
			pondEvent.thingAdded(thing);
	}

	private void removeFromPond(Thing thing) {
		for (PondEventListener pondEvent : pondEvents)
			pondEvent.thingRemoved(thing);
	}

	public void addEventListener(PondEventListener pondEventListener) {
		pondEvents.add(pondEventListener);
	}

	public int getNumberOfFoodPieces() {
		return foodPieces.size();
	}

	public Object getNumberOfNarjillos() {
		return narjillos.size();
	}
}
