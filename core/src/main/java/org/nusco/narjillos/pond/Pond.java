package org.nusco.narjillos.pond;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.creature.SwimmerEventListener;
import org.nusco.narjillos.creature.genetics.DNA;
import org.nusco.narjillos.creature.genetics.Embryo;
import org.nusco.narjillos.shared.physics.Segment;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.things.Thing;
import org.nusco.narjillos.shared.utilities.RanGen;

public class Pond {

	private static final double COLLISION_DISTANCE = 30;
	private static final int NUMBER_OF_WORKERS = 2;

	private final long size;
	private final Set<FoodPiece> foodPieces = Collections.newSetFromMap(new ConcurrentHashMap<FoodPiece, Boolean>());
	private final Set<Narjillo> narjillos = Collections.newSetFromMap(new ConcurrentHashMap<Narjillo, Boolean>());
	private final ParallelTicker scheduler = new ParallelTicker(NUMBER_OF_WORKERS);

	private final List<PondEventListener> pondEvents = new LinkedList<>();

	private int tickCounter = 0;

	public Pond(long size) {
		this.size = size;
	}

	public long getSize() {
		return size;
	}

	public synchronized Set<Thing> getThings() {
		Set<Thing> result = new HashSet<Thing>();
		result.addAll(foodPieces);
		result.addAll(narjillos);
		return result;
	}

	private Vector find(Set<? extends Thing> things, Vector near) {
		double minDistance = Double.MAX_VALUE;
		Vector result = Vector.cartesian(getSize() / 2, getSize() / 2);
		for (Thing thing : things) {
			double distance = thing.getPosition().minus(near).getLength();
			if (distance < minDistance) {
				minDistance = distance;
				result = thing.getPosition();
			}
		}
		return result;
	}

	public synchronized Vector findFoodPiece(Vector near) {
		return find(foodPieces, near);
	}

	public synchronized Vector findNarjillo(Vector near) {
		return find(narjillos, near);
	}

	public synchronized void tick() {
		scheduler.tick(foodPieces);
		scheduler.tick(narjillos);

		if (tickCounter-- < 0) {
			tickCounter = 100000;
			updateTargets();
		}
	}

	public synchronized final FoodPiece spawnFood(Vector position) {
		FoodPiece newFood = new FoodPiece();
		placeInPond(newFood, position);
		foodPieces.add(newFood);
		return newFood;
	}

	public synchronized final Narjillo spawnSwimmer(Vector position, DNA genes) {
		final Narjillo narjillo = new Embryo(genes).develop();
		narjillo.addSwimmerEventListener(new SwimmerEventListener() {

			@Override
			public void moved(Segment movement) {
				checkCollisionsWithFood(narjillo, movement);
			}

			@Override
			public void died() {
				killNarjillo(narjillo);
			}
		});
		placeInPond(narjillo, position);
		narjillos.add(narjillo);
		return narjillo;
	}

	private void updateTarget(Narjillo swimmer) {
		Vector position = swimmer.getPosition();
		Vector locationOfClosestFood = findFoodPiece(position);
		swimmer.setTarget(locationOfClosestFood);
	}

	private synchronized void updateTargets() {
		for (Narjillo narjillo : narjillos)
			updateTarget(narjillo);
	}

	private synchronized void checkCollisionsWithFood(Narjillo narjillo, Segment movement) {
		// TODO: naive algorithm. replace with space partitioning and finding
		// neighbors
		for (FoodPiece foodThing : foodPieces)
			checkCollisionWithFood(narjillo, movement, foodThing);
	}

	private void checkCollisionWithFood(Narjillo narjillo, Segment movement, FoodPiece foodThing) {
		// Ugly optimization to avoid getting into the expensive
		// getMinimumDistanceFromPoint() method.
		// This will hopefully be redundant once we have a good space partition
		// algorithm, but it makes quite a difference until then
		if (!isUnderSafeDistance(narjillo, foodThing))
			return;

		if (movement.getMinimumDistanceFromPoint(foodThing.getPosition()) > COLLISION_DISTANCE)
			return;

		consumeFood(narjillo, foodThing);
	}

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

	private synchronized void consumeFood(Narjillo narjillo, FoodPiece foodPiece) {
		if (!foodPieces.contains(foodPiece))
			return; // race condition: already consumed
		narjillo.feed();
		foodPieces.remove(foodPiece);
		reproduce(narjillo);
		updateTargets();
	}

	private synchronized void killNarjillo(final Narjillo narjillo) {
		narjillos.remove(narjillo);
	}

	private void reproduce(Narjillo narjillo) {
		DNA childDNA = narjillo.getDescendantDNA();
		Vector position = narjillo.getPosition().plus(
				Vector.cartesian(6000 * RanGen.nextDouble() - 3000,
						6000 * RanGen.nextDouble() - 3000));
		spawnSwimmer(position, childDNA);
	}

	private final void placeInPond(Thing thing, Vector position) {
		thing.setPosition(position);
		for (PondEventListener pondEvent : pondEvents)
			pondEvent.thingAdded(thing);
	}

	public void addEventListener(PondEventListener pondEventListener) {
		pondEvents.add(pondEventListener);
	}

	public synchronized int getNumberOfFoodPieces() {
		return foodPieces.size();
	}

	public synchronized int getNumberOfNarjillos() {
		return narjillos.size();
	}

	public synchronized Narjillo getMostProlificNarjillo() {
		Narjillo result = null;
		int maxDescendants = 0;
		for (Narjillo narjillo : narjillos)
			if (narjillo.getNumberOfDescendants() > maxDescendants) {
				result = narjillo;
				maxDescendants = narjillo.getNumberOfDescendants();
			}
		return result;
	}
}
