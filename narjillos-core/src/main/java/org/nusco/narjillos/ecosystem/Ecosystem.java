package org.nusco.narjillos.ecosystem;

import java.util.Collections;
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
public class Ecosystem extends Environment {

	private final Set<Narjillo> narjillos = Collections.synchronizedSet(new LinkedHashSet<Narjillo>());

	private final Space things;
	private final Vector center;

	public Ecosystem(final long size, boolean sizeCheck) {
		super(size);
		this.things = new Space(size);
		this.center = Vector.cartesian(size, size).by(0.5);

		// check that things cannot move faster than a space area in a single
		// tick (which would make collision detection unreliable)
		if (sizeCheck && things.getAreaSize() < Viscosity.getMaxVelocity())
			throw new RuntimeException("Bug: Area size smaller than max velocity");
	}

	@Override
	public Set<Thing> getThings(String label) {
		Set<Thing> result = new LinkedHashSet<Thing>();
		// this ugliness will stay until we have narjillos
		// in the same space as other things
		if (label.equals("narjillo") || label.equals(""))
			result.addAll(narjillos);
		result.addAll(things.getAll(label));
		return result;
	}

	public Vector findClosestFoodPiece(Thing thing) {
		Thing target = things.findClosestTo(thing, "food_piece");

		if (target == null)
			return center;

		return target.getPosition();
	}

	@Override
	protected void tickThings(GenePool genePool, RanGen ranGen) {
		for (Thing thing : new LinkedList<>(things.getAll("egg")))
			tickEgg((Egg) thing);

		for (Narjillo narjillo : new LinkedList<>(narjillos))
			if (narjillo.isDead())
				removeNarjillo(narjillo, genePool);

		tickNarjillos(genePool, ranGen);

		if (shouldSpawnFood(ranGen)) {
			spawnFood(randomPosition(getSize(), ranGen));
			periodicUpdate();
		}

		// TODO: put back
		// for (Narjillo narjillo : new LinkedList<>(narjillos))
		// layEgg(narjillo, genePool, ranGen);
	}

	public final FoodPiece spawnFood(Vector position) {
		FoodPiece newFood = new FoodPiece();
		newFood.setPosition(position);
		insert(newFood);
		return newFood;
	}

	public void insert(Thing thing) {
		things.add(thing);
		notifyThingAdded(thing);
	}

	public void insertNarjillo(Narjillo narjillo) {
		narjillos.add(narjillo);
		notifyThingAdded(narjillo);
	}

	public final Egg spawnEgg(DNA genes, Vector position, RanGen ranGen) {
		Egg egg = new Egg(genes, position, Vector.ZERO, Configuration.ENERGY_OF_SEED_CREATURES, ranGen);
		insert(egg);
		return egg;
	}

	public int getNumberOfFoodPieces() {
		return things.count("food_piece");
	}

	public int getNumberOfEggs() {
		return things.count("egg");
	}

	public int getNumberOfNarjillos() {
		return narjillos.size();
	}

	public Set<Narjillo> getNarjillos() {
		return narjillos;
	}

	public void periodicUpdate() {
		for (Thing creature : narjillos) {
			Narjillo narjillo = (Narjillo) creature;
			Vector closestTarget = findClosestFoodPiece(narjillo);
			narjillo.setTarget(closestTarget);
		}
	}

	public void populate(String dna, GenePool genePool, RanGen ranGen) {
		spawnFood(ranGen);

		for (int i = 0; i < Configuration.ECOSYSTEM_INITIAL_EGGS; i++)
			spawnEgg(genePool.createDNA(dna), randomPosition(getSize(), ranGen), ranGen);
	}

	public void populate(GenePool genePool, RanGen ranGen) {
		spawnFood(ranGen);

		for (int i = 0; i < Configuration.ECOSYSTEM_INITIAL_EGGS; i++)
			spawnEgg(genePool.createRandomDNA(ranGen), randomPosition(getSize(), ranGen), ranGen);
	}

	@Override
	public String getStatistics() {
		return "Narj: " + getNumberOfNarjillos() + " / Eggs: " + getNumberOfEggs() + " / Food: " + getNumberOfFoodPieces();
	}

	@Override
	protected Set<Thing> getCollisions(Segment movement) {
		return things.detectCollisions(movement, "food_piece");
	}

	private void spawnFood(RanGen ranGen) {
		for (int i = 0; i < Configuration.ECOSYSTEM_INITIAL_FOOD_PIECES; i++)
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
		Map<Narjillo, Set<Thing>> narjillosToCollidedFood = calculateCollisions(narjillos);

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
		return getNumberOfFoodPieces() < Configuration.ECOSYSTEM_MAX_FOOD_PIECES
				&& ranGen.nextDouble() < 1.0 / Configuration.ECOSYSTEM_FOOD_RESPAWN_AVERAGE_INTERVAL;
	}

	private Vector randomPosition(long size, RanGen ranGen) {
		return Vector.cartesian(ranGen.nextDouble() * size, ranGen.nextDouble() * size);
	}

	private void updateTargets(Thing food) {
		for (Narjillo narjillo : narjillos) {
			if (narjillo.getTarget().equals(food.getPosition())) {
				Vector closestTarget = findClosestFoodPiece(narjillo);
				narjillo.setTarget(closestTarget);
			}
		}
	}

	private void consume(Narjillo narjillo, Set<Thing> foodPieces, GenePool genePool, RanGen ranGen) {
		for (Thing foodPiece : foodPieces)
			consumeFood(narjillo, (FoodPiece) foodPiece, genePool, ranGen);
	}

	private void consumeFood(Narjillo narjillo, FoodPiece foodPiece, GenePool genePool, RanGen ranGen) {
		if (!things.contains(foodPiece))
			return; // race condition: already consumed

		remove(foodPiece);
		narjillo.feedOn(foodPiece);

		// TODO: remove - instead, give a chance to lay an
		// egg at every tick
		layEgg(narjillo, genePool, ranGen);

		updateTargets(foodPiece);
	}

	private void remove(Thing thing) {
		notifyThingRemoved(thing);
		things.remove(thing);
	}

	private void removeNarjillo(Narjillo narjillo, GenePool genePool) {
		notifyThingRemoved(narjillo);
		narjillos.remove(narjillo);
		genePool.remove(narjillo.getDNA());
	}

	private void layEgg(Narjillo narjillo, GenePool genePool, RanGen ranGen) {
		// TODO: use layEgg() instead
		Egg egg = narjillo.forceLayEgg(this, genePool, ranGen);
		if (egg == null)
			return;
		insert(egg);
	}
}
