package org.nusco.narjillos.ecosystem;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Future;

import org.nusco.narjillos.creature.Egg;
import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.creature.body.physics.Viscosity;
import org.nusco.narjillos.genomics.DNA;
import org.nusco.narjillos.genomics.GenePool;
import org.nusco.narjillos.shared.physics.Segment;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.things.FoodPiece;
import org.nusco.narjillos.shared.things.Thing;
import org.nusco.narjillos.shared.utilities.Configuration;
import org.nusco.narjillos.shared.utilities.RanGen;

/**
 * The place that Narjillos call "home".
 */
public class Ecosystem extends Culture {

	private final Set<Narjillo> narjillos = new LinkedHashSet<Narjillo>();

	private final Space space;
	private final Vector center;

	public Ecosystem(final long size, boolean sizeCheck) {
		super(size);
		this.space = new Space(size);
		this.center = Vector.cartesian(size, size).by(0.5);

		// check that things cannot move faster than a space area in a single
		// tick (which would make collision detection unreliable)
		if (sizeCheck && space.getAreaSize() < Viscosity.getMaxVelocity())
			throw new RuntimeException("Bug: Area size smaller than max velocity");
	}

	@Override
	public Set<Thing> getThings(String label) {
		Set<Thing> result = new LinkedHashSet<Thing>();
		// this ugliness will stay until we have narjillos
		// in the same space as other things
		if (label.equals("narjillo") || label.equals("")) {
			synchronized (narjillos) {
				result.addAll(narjillos);
			}
		}
		result.addAll(space.getAll(label));
		return result;
	}

	public Vector findClosestFoodPiece(Thing thing) {
		Thing target = space.findClosestTo(thing, "food_piece");

		if (target == null)
			return center;

		return target.getPosition();
	}

	@Override
	protected void tickThings(GenePool genePool, RanGen ranGen) {
		for (Thing thing : new LinkedList<>(space.getAll("egg")))
			tickEgg((Egg) thing);

		synchronized (narjillos) {
			for (Narjillo narjillo : new LinkedList<>(narjillos))
				if (narjillo.isDead())
					removeNarjillo(narjillo, genePool);
		}

		tickNarjillos(genePool, ranGen);

		if (shouldSpawnFood(ranGen)) {
			spawnFood(randomPosition(getSize(), ranGen));
			periodicUpdate();
		}

		synchronized (narjillos) {
			for (Narjillo narjillo : narjillos)
				maybeLayEgg(narjillo, genePool, ranGen);
		}
	}

	public final FoodPiece spawnFood(Vector position) {
		FoodPiece newFood = new FoodPiece();
		newFood.setPosition(position);
		insert(newFood);
		return newFood;
	}

	public void insert(Thing thing) {
		space.add(thing);
		notifyThingAdded(thing);
	}

	public void insertNarjillo(Narjillo narjillo) {
		synchronized (narjillos) {
			narjillos.add(narjillo);
			notifyThingAdded(narjillo);
		}
	}

	public final Egg spawnEgg(DNA genes, Vector position, RanGen ranGen) {
		Egg egg = new Egg(genes, position, Vector.ZERO, Configuration.ENERGY_OF_SEED_CREATURES, ranGen);
		insert(egg);
		return egg;
	}

	public int getNumberOfFoodPieces() {
		return space.count("food_piece");
	}

	public int getNumberOfEggs() {
		return space.count("egg");
	}

	public int getNumberOfNarjillos() {
		synchronized (narjillos) {
			return narjillos.size();
		}
	}

	public Set<Narjillo> getNarjillos() {
		synchronized (narjillos) {
			return new LinkedHashSet<Narjillo>(narjillos);
		}
	}

	public void periodicUpdate() {
		synchronized (narjillos) {
			for (Thing creature : narjillos) {
				Narjillo narjillo = (Narjillo) creature;
				Vector closestTarget = findClosestFoodPiece(narjillo);
				narjillo.setTarget(closestTarget);
			}
		}
	}

	public void populate(String dna, GenePool genePool, RanGen ranGen) {
		spawnFood(ranGen);

		for (int i = 0; i < getNumberOf1000SquarePointsBlocks() * Configuration.ECOSYSTEM_INITIAL_EGGS_DENSITY_PER_1000_SQUARE_POINTS; i++)
			spawnEgg(genePool.createDNA(dna), randomPosition(getSize(), ranGen), ranGen);
	}

	public void populate(GenePool genePool, RanGen ranGen) {
		spawnFood(ranGen);

		for (int i = 0; i < getNumberOf1000SquarePointsBlocks() * Configuration.ECOSYSTEM_INITIAL_EGGS_DENSITY_PER_1000_SQUARE_POINTS; i++)
			spawnEgg(genePool.createRandomDNA(ranGen), randomPosition(getSize(), ranGen), ranGen);
	}

	@Override
	public String getStatistics() {
		return "Narj: " + getNumberOfNarjillos() + " / Eggs: " + getNumberOfEggs() + " / Food: " + getNumberOfFoodPieces();
	}

	@Override
	protected Set<Thing> getCollisions(Segment movement) {
		return space.detectCollisions(movement, "food_piece");
	}

	private void spawnFood(RanGen ranGen) {
		for (int i = 0; i < getNumberOf1000SquarePointsBlocks() * Configuration.ECOSYSTEM_INITIAL_FOOD_DENSITY_PER_1000_SQUARE_POINTS; i++)
			spawnFood(randomPosition(getSize(), ranGen));
	}

	private void tickEgg(Egg egg) {
		egg.tick();
		if (egg.hatch())
			insertNarjillo(egg.getHatchedNarjillo());
		if (egg.isDecayed())
			remove(egg);
	}

	private synchronized void tickNarjillos(GenePool genePool, RanGen ranGen) {
		Map<Narjillo, Set<Thing>> narjillosToCollidedFood;
		synchronized (narjillos) {
			narjillosToCollidedFood = calculateCollisions(narjillos);
		}
		
		// Consume food in a predictable order, to avoid non-deterministic
		// behavior or race conditions when multiple narjillos collide with the
		// same piece of food.
		for (Entry<Narjillo, Set<Thing>> entry : narjillosToCollidedFood.entrySet()) {
			Narjillo narjillo = entry.getKey();
			Set<Thing> collidedFood = entry.getValue();
			consume(narjillo, collidedFood, genePool, ranGen);
		}
	}

	private Map<Narjillo, Set<Thing>> calculateCollisions(Set<Narjillo> set) {
		Map<Narjillo, Set<Thing>> result = new LinkedHashMap<>();

		// Calculate collisions in parallel...
		Map<Narjillo, Future<Set<Thing>>> collisionFutures = tickNarjillos(set);

		// ...but collect the results in a predictable order
		for (Narjillo narjillo : collisionFutures.keySet()) {
			try {
				result.put(narjillo, collisionFutures.get(narjillo).get());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		return result;
	}

	private boolean shouldSpawnFood(RanGen ranGen) {
		double maxFoodPieces = getNumberOf1000SquarePointsBlocks() * Configuration.ECOSYSTEM_MAX_FOOD_DENSITY_PER_1000_SQUARE_POINTS;
		if (getNumberOfFoodPieces() >= maxFoodPieces)
			return false;
		
		double foodRespawnAverageInterval = Configuration.ECOSYSTEM_FOOD_RESPAWN_AVERAGE_INTERVAL_PER_1000_SQUARE_POINTS / getNumberOf1000SquarePointsBlocks();
		return ranGen.nextDouble() < 1.0 / foodRespawnAverageInterval;
	}

	private Vector randomPosition(long size, RanGen ranGen) {
		return Vector.cartesian(ranGen.nextDouble() * size, ranGen.nextDouble() * size);
	}

	private void updateTargets(Thing food) {
		synchronized (narjillos) {
			for (Narjillo narjillo : narjillos) {
				if (narjillo.getTarget().equals(food.getPosition())) {
					Vector closestTarget = findClosestFoodPiece(narjillo);
					narjillo.setTarget(closestTarget);
				}
			}
		}
	}

	private void consume(Narjillo narjillo, Set<Thing> foodPieces, GenePool genePool, RanGen ranGen) {
		for (Thing foodPiece : foodPieces)
			consumeFood(narjillo, (FoodPiece) foodPiece, genePool, ranGen);
	}

	private void consumeFood(Narjillo narjillo, FoodPiece foodPiece, GenePool genePool, RanGen ranGen) {
		if (!space.contains(foodPiece))
			return; // race condition: already consumed

		remove(foodPiece);
		narjillo.feedOn(foodPiece);

		updateTargets(foodPiece);
	}

	private void remove(Thing thing) {
		notifyThingRemoved(thing);
		space.remove(thing);
	}

	private void removeNarjillo(Narjillo narjillo, GenePool genePool) {
		notifyThingRemoved(narjillo);
		synchronized (narjillos) {
			narjillos.remove(narjillo);
		}
		genePool.remove(narjillo.getDNA());
	}

	private void maybeLayEgg(Narjillo narjillo, GenePool genePool, RanGen ranGen) {
		Egg egg = narjillo.layEgg(this, genePool, ranGen);
		if (egg == null)
			return;
		insert(egg);
	}
	
	private double getNumberOf1000SquarePointsBlocks() {
		double blocksPerEdge = getSize() / 1000.0;
		return blocksPerEdge * blocksPerEdge;
	}
}
