package org.nusco.narjillos.pond;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.creature.NarjilloEventListener;
import org.nusco.narjillos.creature.body.embryology.Embryo;
import org.nusco.narjillos.creature.genetics.DNA;
import org.nusco.narjillos.creature.genetics.Population;
import org.nusco.narjillos.shared.physics.Segment;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.things.Thing;
import org.nusco.narjillos.shared.utilities.RanGen;

public class Pond {

	private static final double COLLISION_DISTANCE = 30;

	private final long size;
	private final Set<FoodPiece> foodPieces = Collections.newSetFromMap(new ConcurrentHashMap<FoodPiece, Boolean>());
	private final Population narjillos = new Population();

	private final List<PondEventListener> pondEvents = new LinkedList<>();

	private int tickCounter = 0;

	public Pond(long size) {
		this.size = size;
	}

	public long getSize() {
		return size;
	}

	public Set<Thing> getThings() {
		Set<Thing> result = new HashSet<Thing>();
		result.addAll(foodPieces);
		result.addAll(narjillos.toCollection());
		return result;
	}

	private Vector find(Collection<? extends Thing> things, Vector near) {
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

	public Vector findFoodPiece(Vector near) {
		return find(foodPieces, near);
	}

	public Vector findNarjillo(Vector near) {
		return find(narjillos.toCollection(), near);
	}

	public void tick() {
		narjillos.tick();
		
		// TODO: no need to tick food for now
		
		if (tickCounter-- < 0) {
			tickCounter = 100000;
			updateTargets();
		}
	}

	public final FoodPiece spawnFood(Vector position) {
		FoodPiece newFood = new FoodPiece();
		newFood.setPosition(position);
		notifyThingAdded(newFood);
		foodPieces.add(newFood);
		return newFood;
	}

	public final Narjillo spawnNarjillo(Vector position, DNA genes) {
		final Narjillo narjillo = new Embryo(genes).develop();
		narjillo.addEventListener(new NarjilloEventListener() {

			@Override
			public void moved(Segment movement) {
				checkCollisionsWithFood(narjillo, movement);
			}

			@Override
			public void died() {
				killNarjillo(narjillo);
			}
		});
		narjillo.setPosition(position);
		notifyThingAdded(narjillo);
		narjillos.add(narjillo);
		return narjillo;
	}

	private void updateTarget(Narjillo narjillo) {
		Vector position = narjillo.getPosition();
		Vector locationOfClosestFood = findFoodPiece(position);
		narjillo.setTarget(locationOfClosestFood);
	}

	private void updateTargets() {
		for (Thing narjillo : narjillos.getCollection())
			updateTarget((Narjillo)narjillo);
	}

	private void checkCollisionsWithFood(Narjillo narjillo, Segment movement) {
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

	private void killNarjillo(final Narjillo narjillo) {
		narjillos.remove(narjillo);
	}

	private void reproduce(Narjillo narjillo) {
		DNA childDNA = narjillo.reproduce();
		Vector position = narjillo.getPosition().plus(
				Vector.cartesian(6000 * RanGen.nextDouble() - 3000,
						6000 * RanGen.nextDouble() - 3000));
		spawnNarjillo(position, childDNA);
	}

	private final void notifyThingAdded(Thing thing) {
		for (PondEventListener pondEvent : pondEvents)
			pondEvent.thingAdded(thing);
	}

	public void addEventListener(PondEventListener pondEventListener) {
		pondEvents.add(pondEventListener);
	}

	public int getNumberOfFoodPieces() {
		return foodPieces.size();
	}

	public int getNumberOfNarjillos() {
		return narjillos.size();
	}

	public Population getPopulation() {
		return narjillos;
	}
}
