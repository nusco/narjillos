package org.nusco.narjillos.ecosystem;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.creature.body.embryogenesis.Embryo;
import org.nusco.narjillos.creature.genetics.Creature;
import org.nusco.narjillos.creature.genetics.DNA;
import org.nusco.narjillos.creature.genetics.GenePool;
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
public class Ecosystem {

	private static final double COLLISION_DISTANCE = 30;

	private final long size;
	private final Set<FoodPiece> foodPieces = Collections.synchronizedSet(new LinkedHashSet<FoodPiece>());
	private final GenePool narjillos = new GenePool();

	private final Space foodSpace;
	private final Vector center;

	private final List<EcosystemEventListener> ecosystemEventListeners = new LinkedList<>();
	private volatile boolean shouldBePaused = false;
	private volatile boolean paused = false;
	
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

	public GenePool getGenePool() {
		return narjillos;
	}

	public Set<Thing> getThings() {
		Set<Thing> result = new LinkedHashSet<Thing>();
		synchronized (this) {
			result.addAll(foodPieces);
		}
		result.addAll(narjillos.getCreatures());
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

	public Creature findNarjillo(Vector near) {
		double minDistance = Double.MAX_VALUE;
		Creature result = null;
		for (Creature narjillo : narjillos.getCreatures()) {
			double distance = narjillo.getPosition().minus(near).getLength();
			if (distance < minDistance) {
				minDistance = distance;
				result = narjillo;
			}
		}
		return result;
	}

	public boolean tick(RanGen ranGen) {
		paused = shouldBePaused;
		if (isPaused())
			return false;
		
		Set<Creature> creatures = new LinkedHashSet<>(narjillos.getCreatures());
		for (Creature narjillo : creatures) {
			Segment movement = narjillo.tick();
			consumeCollidedFood(narjillo, movement, ranGen);
			if (narjillo.isDead())
				remove(narjillo);
		}
		
		// no need to tick food
		
		if (VisualDebugger.DEBUG)
			VisualDebugger.clear();
		
		return true;
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

	public final Narjillo spawnNarjillo(Vector position, DNA genes) {
		final Narjillo narjillo = new Narjillo(new Embryo(genes).develop(), position, genes);
		forceAdd(narjillo);
		return narjillo;
	}

	public void forceAdd(final Narjillo narjillo) {
		narjillos.add(narjillo);
		notifyThingAdded(narjillo);
	}

	private void updateTargets(Thing food) {
		for (Thing creature : narjillos.getCreatures()) {
			if (((Narjillo)creature).getTarget() == food) {
				Narjillo narjillo = (Narjillo)creature;
				Vector closestTarget = findClosestTarget(narjillo);
				narjillo.setTarget(closestTarget);
			}
		}
	}

	private void consumeCollidedFood(Creature narjillo, Segment movement, RanGen ranGen) {
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

	private boolean checkCollisionWithFood(Creature narjillo, Segment movement, Thing foodPiece) {
		return movement.getMinimumDistanceFromPoint(foodPiece.getPosition()) <= COLLISION_DISTANCE;
	}

	private void consumeFood(Creature narjillo, Thing foodPiece, RanGen ranGen) {
		// TODO: replace with space.contains()
		if (!foodPieces.contains(foodPiece))
			return; // race condition: already consumed
		foodPieces.remove(foodPiece);
		notifyThingRemoved(foodPiece);
		foodSpace.remove(foodPiece);

		narjillo.feedOn(foodPiece);

		reproduce(narjillo, ranGen);
		updateTargets(foodPiece);
	}

	private void remove(final Creature narjillo) {
		if (!narjillos.getCreatures().contains(narjillo))
			return;
		narjillos.remove(narjillo);
		notifyThingRemoved(narjillo);
	}

	private void reproduce(Creature narjillo, RanGen ranGen) {
		DNA childDNA = narjillo.reproduce(ranGen);
		Vector offset = Vector.cartesian(getRandomInRange(3000, ranGen), getRandomInRange(3000, ranGen));
		Vector position = narjillo.getPosition().plus(offset);
		spawnNarjillo(position, childDNA);
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
		return narjillos.getSize();
	}

	public GenePool getPopulation() {
		return narjillos;
	}
	
	// These method must be called from a separate thread than
	// the one that calls tick();
	public void togglePause() {
		if (!shouldBePaused) {
			shouldBePaused = true;
			while (!isPaused())
				wait100Millis();
		} else {
			shouldBePaused = false;
			while (isPaused())
				wait100Millis();
		}
	}

	public boolean isPaused() {
		return paused;
	}

	public void unpause() {
		if (isPaused())
			togglePause();
	}

	private void wait100Millis() {
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
