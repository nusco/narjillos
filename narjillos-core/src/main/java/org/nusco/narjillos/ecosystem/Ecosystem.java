package org.nusco.narjillos.ecosystem;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.nusco.narjillos.creature.Egg;
import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.genomics.DNA;
import org.nusco.narjillos.genomics.GenePool;
import org.nusco.narjillos.shared.physics.Segment;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.things.FoodPiece;
import org.nusco.narjillos.shared.things.Thing;
import org.nusco.narjillos.shared.utilities.Configuration;
import org.nusco.narjillos.shared.utilities.RanGen;
import org.nusco.narjillos.shared.utilities.VisualDebugger;

/**
 * The place that Narjillos call "home".
 */
public class Ecosystem {

	private final long size;
	private final Set<Narjillo> narjillos = Collections.synchronizedSet(new LinkedHashSet<Narjillo>());

	private final Space things;
	private final Vector center;

	private final List<EcosystemEventListener> ecosystemEventListeners = new LinkedList<>();

	private final ExecutorService executorService;

	public Ecosystem(final long size) {
		this.size = size;
		this.things = new Space(size);
		this.center = Vector.cartesian(size, size).by(0.5);
		executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	}

	public long getSize() {
		return size;
	}

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

	public void tick(GenePool genePool, RanGen ranGen) {
		for (Thing thing : new LinkedList<>(things.getAll("egg")))
			tickEgg((Egg) thing);

		for (Narjillo narjillo : new LinkedList<>(narjillos))
			if (narjillo.isDead())
				removeNarjillo(narjillo, genePool);

		tickNarjillos(genePool, ranGen);

		if (shouldSpawnFood(ranGen)) {
			spawnFood(randomPosition(getSize(), ranGen));
			updateAllTargets();
		}

		for (Narjillo narjillo : new LinkedList<>(narjillos))
			layEgg(narjillo, genePool, ranGen);

		if (VisualDebugger.DEBUG)
			VisualDebugger.clear();
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
		Egg egg = new Egg(genes, position, Configuration.ENERGY_OF_SEED_CREATURES, ranGen);
		insert(egg);
		return egg;
	}

	public void addEventListener(EcosystemEventListener ecosystemEventListener) {
		ecosystemEventListeners.add(ecosystemEventListener);
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

	public void updateAllTargets() {
		for (Thing creature : narjillos) {
			Narjillo narjillo = (Narjillo) creature;
			Vector closestTarget = findClosestFoodPiece(narjillo);
			narjillo.setTarget(closestTarget);
		}
	}

	public synchronized void terminate() {
		executorService.shutdown();
		try {
			executorService.awaitTermination(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
		}
	}

	public double getSpaceAreaSize() {
		return things.getAreaSize();
	}

	private void tickEgg(Egg egg) {
		egg.tick();
		if (egg.hatch())
			insertNarjillo(egg.getHatchedNarjillo());
		if (egg.isDecayed())
			remove(egg);
	}

	private synchronized void tickNarjillos(GenePool genePool, RanGen ranGen) {
		if (executorService.isShutdown())
			return; // we're leaving, apparently

		Map<Narjillo, Set<Thing>> narjillosToCollidedFood = calculateCollidedFood(narjillos);

		// Consume food in a predictable order, to avoid non-deterministic
		// behavior or race conditions when multiple narjillos collide with the
		// same piece of food.
		for (Entry<Narjillo, Set<Thing>> entry : narjillosToCollidedFood.entrySet()) {
			Narjillo narjillo = entry.getKey();
			Set<Thing> collidedFood = entry.getValue();
			consume(narjillo, collidedFood, genePool, ranGen);
		}
	}

	private Map<Narjillo, Set<Thing>> calculateCollidedFood(Set<Narjillo> set) {
		Map<Narjillo, Set<Thing>> result = new LinkedHashMap<>();

		// Calculate collided food in parallel...
		Map<Narjillo, Future<Set<Thing>>> collidedFoodFutures = tickAll(set);

		// ...but collect the results in a predictable order
		for (Narjillo narjillo : collidedFoodFutures.keySet()) {
			try {
				result.put(narjillo, collidedFoodFutures.get(narjillo).get());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		return result;
	}

	private Map<Narjillo, Future<Set<Thing>>> tickAll(Set<Narjillo> narjillos) {
		Map<Narjillo, Future<Set<Thing>>> result = new LinkedHashMap<>();
		for (final Narjillo narjillo : narjillos) {
			result.put(narjillo, executorService.submit(new Callable<Set<Thing>>() {
				@Override
				public Set<Thing> call() throws Exception {
					Segment movement = narjillo.tick();
					Set<Thing> collidedFoodPieces = things.detectCollisions(movement, "food_piece");
					return collidedFoodPieces;
				}
			}));
		}
		return result;
	}

	private boolean shouldSpawnFood(RanGen ranGen) {
		return getNumberOfFoodPieces() < Configuration.ECOSYSTEM_MAX_FOOD_PIECES && ranGen.nextDouble() < 1.0 / Configuration.ECOSYSTEM_FOOD_RESPAWN_AVERAGE_INTERVAL;
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
		Egg egg = narjillo.layEgg(this, genePool, ranGen);
		if (egg == null)
			return;
		insert(egg);
	}

	private final void notifyThingAdded(Thing thing) {
		for (EcosystemEventListener ecosystemEvent : ecosystemEventListeners)
			ecosystemEvent.thingAdded(thing);
	}

	private final void notifyThingRemoved(Thing thing) {
		for (EcosystemEventListener ecosystemEvent : ecosystemEventListeners)
			ecosystemEvent.thingRemoved(thing);
	}
}
