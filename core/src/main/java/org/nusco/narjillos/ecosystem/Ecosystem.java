package org.nusco.narjillos.ecosystem;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.embryogenesis.Embryo;
import org.nusco.narjillos.genomics.DNA;
import org.nusco.narjillos.shared.physics.Segment;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.things.FoodPiece;
import org.nusco.narjillos.shared.things.Thing;
import org.nusco.narjillos.shared.utilities.RanGen;
import org.nusco.narjillos.shared.utilities.VisualDebugger;

//TODO: check thread-safety (too complicated and fragile right now).
/**
 * The place that Narjillos live in.
 * 
 * Can find things and detect collisions.
 */
public strictfp class Ecosystem {

	private static final double COLLISION_DISTANCE = 30;
	private static final int MAX_NUMBER_OF_FOOD_PIECES = 600;
	private static final int FOOD_RESPAWN_AVERAGE_INTERVAL = 100;

	private final long size;
	private final Set<FoodPiece> foodPieces = Collections.synchronizedSet(new LinkedHashSet<FoodPiece>());
	private final Set<Narjillo> narjillos = Collections.synchronizedSet(new LinkedHashSet<Narjillo>());

	private final Space foodSpace;
	private final Vector center;

	private final List<EcosystemEventListener> ecosystemEventListeners = new LinkedList<>();
	
	public Ecosystem(final long size) {
		this.size = size;
		this.foodSpace = new Space(size);
		this.center = Vector.cartesian(size, size).by(0.5);
	}

	public long getSize() {
		return size;
	}

	public Set<FoodPiece> getFoodPieces() {
		return foodPieces;
	}

	public Set<Narjillo> getNarjillos() {
		return narjillos;
	}

	public Set<Thing> getThings() {
		Set<Thing> result = new LinkedHashSet<Thing>();
		synchronized (this) {
			result.addAll(foodPieces);
		}
		result.addAll(narjillos);
		return result;
	}

	public Vector findClosestTarget(Narjillo narjillo) {
		double minDistance = Double.MAX_VALUE;
		Thing closestFood = null;

		Set<Thing> allFood = new LinkedHashSet<>();
		if (foodPieces.isEmpty())
			return center;

		allFood.addAll(foodPieces);
		
		// TODO: replace with spiral search in partitioned space? (after checking that food exists)
		for (Thing foodPiece : allFood) {
			double distance = foodPiece.getPosition().minus(narjillo.getPosition()).getLength();
			if (distance < minDistance) {
				minDistance = distance;
				closestFood = foodPiece;
			}
		}
		
		return closestFood.getPosition();
	}

	public Narjillo findNarjillo(Vector near) {
		double minDistance = Double.MAX_VALUE;
		Narjillo result = null;
		for (Narjillo narjillo : narjillos) {
			double distance = narjillo.getPosition().minus(near).getLength();
			if (distance < minDistance) {
				minDistance = distance;
				result = narjillo;
			}
		}
		return result;
	}

	public void tick(RanGen ranGen) {
		Set<Narjillo> narjillosCopy = new LinkedHashSet<>(narjillos);
		for (Narjillo narjillo : narjillosCopy) {
			Segment movement = narjillo.tick();
			consumeCollidedFood(narjillo, movement, ranGen);
			if (narjillo.isDead())
				remove(narjillo);
		}
		
		// no need to tick food

		if (shouldSpawnFood(ranGen))
			spawnFood(randomPosition(getSize(), ranGen));
		
		if (VisualDebugger.DEBUG)
			VisualDebugger.clear();
	}

	private boolean shouldSpawnFood(RanGen ranGen) {
		return getNumberOfFoodPieces() < MAX_NUMBER_OF_FOOD_PIECES && ranGen.nextDouble() < 1.0 / FOOD_RESPAWN_AVERAGE_INTERVAL;
	}

	private Vector randomPosition(long size, RanGen ranGen) {
		return Vector.cartesian(ranGen.nextDouble() * size, ranGen.nextDouble() * size);
	}

	public final FoodPiece spawnFood(Vector position) {
		FoodPiece newFood = new FoodPiece();
		newFood.setPosition(position);
		forceAdd(newFood);
		return newFood;
	}

	public void forceAdd(FoodPiece food) {
		foodPieces.add(food);
		foodSpace.add(food);
		notifyThingAdded(food);
	}

	public final Narjillo spawnNarjillo(DNA genes, Vector position) {
		final Narjillo narjillo = new Narjillo(genes, new Embryo(genes).develop(), position);
		forceAdd(narjillo);
		return narjillo;
	}

	public void forceAdd(final Narjillo narjillo) {
		narjillos.add(narjillo);
		notifyThingAdded(narjillo);
	}

	private void updateTargets(Thing food) {
		for (Thing creature : narjillos) {
			if (((Narjillo)creature).getTarget() == food) {
				Narjillo narjillo = (Narjillo)creature;
				Vector closestTarget = findClosestTarget(narjillo);
				narjillo.setTarget(closestTarget);
			}
		}
	}

	public void updateAllTargets() {
		for (Thing creature : narjillos) {
			Narjillo narjillo = (Narjillo)creature;
			Vector closestTarget = findClosestTarget(narjillo);
			narjillo.setTarget(closestTarget);
		}
	}

	private void consumeCollidedFood(Narjillo narjillo, Segment movement, RanGen ranGen) {
		// TODO: naive algorithm. replace with space partitioning
		if (movement.getVector().isZero())
			return;

		Set<Thing> collidedFoodPieces = new LinkedHashSet<>();
		
		for (Set<Thing> nearbyFood: foodSpace.getNeighbors(narjillo))
			for (Thing foodPiece : nearbyFood)
				if (checkCollisionWithFood(narjillo, movement, foodPiece))
					collidedFoodPieces.add(foodPiece);

		for (Thing collidedFoodPiece : collidedFoodPieces)
			consumeFood(narjillo, collidedFoodPiece, ranGen);
	}

	private boolean checkCollisionWithFood(Narjillo narjillo, Segment movement, Thing foodPiece) {
		return movement.getMinimumDistanceFromPoint(foodPiece.getPosition()) <= COLLISION_DISTANCE;
	}

	private void consumeFood(Narjillo narjillo, Thing foodPiece, RanGen ranGen) {
		// TODO: replace with space.contains()
		if (!foodPieces.contains(foodPiece))
			return; // race condition: already consumed
		foodPieces.remove(foodPiece);
		notifyThingRemoved(foodPiece);
		foodSpace.remove(foodPiece);

		narjillo.feedOn(foodPiece);

		Vector offset = Vector.cartesian(getRandomInRange(3000, ranGen), getRandomInRange(3000, ranGen));
		Vector position = narjillo.getPosition().plus(offset);

		reproduce(narjillo, position, ranGen);
		updateTargets(foodPiece);
	}

	private void remove(Narjillo narjillo) {
		if (!narjillos.contains(narjillo))
			return;
		narjillos.remove(narjillo);
		narjillo.getDNA().removeFromPool();
		notifyThingRemoved(narjillo);
	}

	private void reproduce(Narjillo narjillo, Vector position, RanGen ranGen) {
		final Narjillo child = narjillo.reproduce(position, ranGen);
		if (child == null)  // refused to reproduce
			return;
		forceAdd(child );
	}

	private double getRandomInRange(final int range, RanGen ranGen) {
		return (range * 2 * ranGen.nextDouble()) - range;
	}

	private final void notifyThingAdded(Thing thing) {
		for (EcosystemEventListener ecosystemEvent : ecosystemEventListeners)
			ecosystemEvent.thingAdded(thing);
	}

	private final void notifyThingRemoved(Thing thing) {
		for (EcosystemEventListener ecosystemEvent : ecosystemEventListeners)
			ecosystemEvent.thingRemoved(thing);
	}

	public void addEventListener(EcosystemEventListener ecosystemEventListener) {
		ecosystemEventListeners.add(ecosystemEventListener);
	}

	public synchronized int getNumberOfFoodPieces() {
		return foodPieces.size();
	}

	public int getNumberOfNarjillos() {
		return narjillos.size();
	}
}
