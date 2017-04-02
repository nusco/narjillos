package org.nusco.narjillos.experiment.environment;

import org.nusco.narjillos.core.chemistry.Atmosphere;
import org.nusco.narjillos.core.configuration.Configuration;
import org.nusco.narjillos.core.geometry.BoundingBox;
import org.nusco.narjillos.core.geometry.Segment;
import org.nusco.narjillos.core.geometry.Vector;
import org.nusco.narjillos.core.physics.Viscosity;
import org.nusco.narjillos.core.things.Space;
import org.nusco.narjillos.core.things.Thing;
import org.nusco.narjillos.core.utilities.NumGen;
import org.nusco.narjillos.creature.Egg;
import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.genomics.DNA;
import org.nusco.narjillos.genomics.DNALog;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * A complex environment populated with narjillos, eggs and food.
 */
public class Ecosystem extends Environment {

	public static int numberOfBackgroundThreads = Runtime.getRuntime().availableProcessors();

	private final ExecutorService executorService;

	/**
	 * Counter used by the ThreadFactory to name threads.
	 */
	private final AtomicInteger tickWorkerCounter = new AtomicInteger(1);

	private final Space space = new Space();

	private Atmosphere atmosphere = new Atmosphere();

	private final ThingsCounter thingsCounter = new ThingsCounter();

	private final Vector center;

	public Ecosystem(final long size, boolean sizeCheck) {
		super(size);

		ThreadFactory tickWorkerFactory = (Runnable r) -> {
			Thread result = new Thread(r, "tick-worker-" + tickWorkerCounter.getAndIncrement());
			result.setPriority(Thread.currentThread().getPriority());
			return result;
		};
		executorService = Executors.newFixedThreadPool(numberOfBackgroundThreads, tickWorkerFactory);

		this.center = Vector.cartesian(size, size).by(0.5);

		// TODO: fix magic number
		// check that things cannot move faster than a space area in a single
		// tick (which would make collision detection unreliable)
		if (sizeCheck && 400 < Viscosity.getMaxVelocity())
			throw new RuntimeException("Bug: Area size smaller than max velocity");
	}

	public Atmosphere getAtmosphere() {
		return atmosphere;
	}

	public void setAtmosphere(Atmosphere atmosphere) {
		this.atmosphere = atmosphere;
	}

	@Override
	public List<Thing> getAll(String label) {
		return new LinkedList<>(space.getAll(label));
	}

	public Vector findClosestFoodTo(Thing thing) {
		Thing target = space.findClosestTo(thing, FoodPellet.LABEL);

		if (target == null)
			return center;

		return target.getPosition();
	}

	public final FoodPellet spawnFood(Vector position) {
		FoodPellet newFood = new FoodPellet(position);
		insert(newFood);
		return newFood;
	}

	public void insert(Thing thing) {
		space.add(thing);
		thingsCounter.add(thing.getLabel());
		notifyThingAdded(thing);
	}

	public final Egg spawnEgg(DNA genes, Vector position, NumGen numGen) {
		Egg egg = new Egg(genes, position, Vector.ZERO, Configuration.CREATURE_SEED_ENERGY, numGen);
		insert(egg);
		return egg;
	}

	@Override
	public long getCount(String label) {
		return thingsCounter.count(label);
	}

	public void updateTargets() {
		getNarjillos().forEach(this::setFoodTarget);
	}

	public void populate(String dna, DNALog dnaLog, NumGen numGen) {
		spawnFood(numGen);

		for (int i = 0; i < getNumberOf1000SquarePointsBlocks() * Configuration.ECOSYSTEM_EGGS_DENSITY_PER_BLOCK; i++)
			spawnEgg(createDna(dna, dnaLog, numGen), randomPosition(getSize(), numGen), numGen);
	}

	public void populate(DNALog dnaLog, NumGen numGen) {
		spawnFood(numGen);

		for (int i = 0; i < getNumberOf1000SquarePointsBlocks() * Configuration.ECOSYSTEM_EGGS_DENSITY_PER_BLOCK; i++)
			spawnEgg(createRandomDna(dnaLog, numGen), randomPosition(getSize(), numGen), numGen);
	}

	public synchronized void terminate() {
		executorService.shutdown();
		try {
			executorService.awaitTermination(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void tickThings(DNALog dnaLog, NumGen numGen) {
		if (isShuttingDown())
			return; // we're leaving, apparently

		space.getAll(Egg.LABEL).forEach(thing -> tickEgg((Egg) thing, numGen));

		getNarjillos()
			.filter(Narjillo::isDead)
			.forEach(narjillo -> removeNarjillo(narjillo, dnaLog));

		tickNarjillos();

		if (shouldSpawnFood(numGen)) {
			spawnFood(randomPosition(getSize(), numGen));
			updateTargets();
		}

		getNarjillos().forEach(narjillo -> maybeLayEgg((Narjillo) narjillo, dnaLog, numGen));
	}

	private Stream<Narjillo> getNarjillos() {
		return space.getAll(Narjillo.LABEL).stream()
			.map(narjillo -> (Narjillo) narjillo);
	}

	private DNA createDna(String dna, DNALog dnaLog, NumGen numGen) {
		DNA result = new DNA(numGen.nextSerial(), dna, DNA.NO_PARENT);
		dnaLog.save(result);
		return result;
	}

	private Map<Narjillo, Future<Set<Thing>>> tickNarjillos(Stream<Narjillo> narjillos) {
		Map<Narjillo, Future<Set<Thing>>> result = new LinkedHashMap<>();
		narjillos.forEach(narjillo -> {
			result.put(narjillo, executorService.submit(() -> {
				Segment movement = narjillo.tick();
				return getCollisions(movement);
			}));
		});
		return result;
	}

	private boolean isShuttingDown() {
		return executorService.isShutdown();
	}

	private void setFoodTarget(Narjillo narjillo) {
		Vector closestTarget = findClosestFoodTo(narjillo);
		narjillo.setTarget(closestTarget);
	}

	private Set<Thing> getCollisions(Segment movement) {
		return space.detectCollisions(movement, FoodPellet.LABEL);
	}

	private DNA createRandomDna(DNALog dnaLog, NumGen numGen) {
		DNA result = DNA.random(numGen.nextSerial(), numGen);
		dnaLog.save(result);
		return result;
	}

	private void spawnFood(NumGen numGen) {
		for (int i = 0; i < getNumberOf1000SquarePointsBlocks() * Configuration.ECOSYSTEM_FOOD_DENSITY_PER_BLOCK; i++)
			spawnFood(randomPosition(getSize(), numGen));
	}

	private void tickEgg(Egg egg, NumGen numGen) {
		egg.tick();
		if (egg.hatch(numGen))
			insert(egg.getHatchedNarjillo().get());
		if (egg.isDecayed())
			remove(egg);
	}

	private synchronized void tickNarjillos() {
		Map<Narjillo, Set<Thing>> narjillosToCollidedFood = tick(getNarjillos());

		// The next operations happen in a predictable order, to avoid
		// non-deterministic behavior or race conditions - for example, when
		// multiple narjillos collide with the same food pellet.

		breathe();

		// Consume food
		narjillosToCollidedFood.entrySet().forEach(entry -> {
				Narjillo narjillo = entry.getKey();
				Set<Thing> collidedFood = entry.getValue();
				consume(narjillo, collidedFood);
			});
	}

	private void breathe() {
		// The maximum amount of energy that each creature can extract from
		// breathing, depending on the amount of catalyst available to all the
		// creatures. Varies between 0 and 1 included.
		double breathingPowerPerNarjillo = Math.min(1, (double) getAtmosphere().getCatalystLevel() / getCount(Narjillo.LABEL));

		// Increase energies
		getNarjillos().forEach(narjillo -> {
			double densityOfBreathableElement = getAtmosphere().getDensityOf(narjillo.getBreathedElement());
			double energyObtainedByBreathing = densityOfBreathableElement * breathingPowerPerNarjillo;
			narjillo.getEnergy().increaseBy(energyObtainedByBreathing);
		});

		// Consume elements
		getNarjillos().forEach(narjillo -> {
			getAtmosphere().convert(narjillo.getBreathedElement(), narjillo.getByproduct());
		});
	}

	private Map<Narjillo, Set<Thing>> tick(Stream<Narjillo> narjillos) {
		// Calculate collisions in parallel...
		Map<Narjillo, Future<Set<Thing>>> collisionFutures = tickNarjillos(narjillos);

		// ...but collect the results in a predictable sequential order
		Map<Narjillo, Set<Thing>> result = new LinkedHashMap<>();
		for (Narjillo narjillo : collisionFutures.keySet()) {
			try {
				result.put(narjillo, collisionFutures.get(narjillo).get());
			} catch (InterruptedException | ExecutionException e) {
				throw new RuntimeException(e);
			}
		}
		return result;
	}

	private boolean shouldSpawnFood(NumGen numGen) {
		double maxFoodPellets = getNumberOf1000SquarePointsBlocks() * Configuration.ECOSYSTEM_MAX_FOOD_DENSITY_PER_1000_BLOCK;
		if (getCount(FoodPellet.LABEL) >= maxFoodPellets)
			return false;

		double foodRespawnAverageInterval = Configuration.ECOSYSTEM_FOOD_RESPAWN_AVERAGE_INTERVAL_PER_BLOCK
			/ getNumberOf1000SquarePointsBlocks();
		return numGen.nextDouble() < 1.0 / foodRespawnAverageInterval;
	}

	private Vector randomPosition(long size, NumGen numGen) {
		return Vector.cartesian(numGen.nextDouble() * size, numGen.nextDouble() * size);
	}

	private void updateTargets(Thing food) {
		getNarjillos()
			.filter(narjillo -> narjillo.getTarget().equals(food.getPosition()))
			.forEach(this::setFoodTarget);
	}

	private void consume(Narjillo narjillo, Set<Thing> foodPellets) {
		foodPellets.forEach(foodPellet -> consumeFood(narjillo, (FoodPellet) foodPellet));
	}

	private void consumeFood(Narjillo narjillo, FoodPellet foodPellet) {
		if (!space.contains(foodPellet))
			return; // race condition: already consumed

		remove(foodPellet);
		narjillo.feedOn(foodPellet);

		updateTargets(foodPellet);
	}

	private void remove(Thing thing) {
		notifyThingRemoved(thing);
		space.remove(thing);
		thingsCounter.remove(thing.getLabel());
	}

	private void removeNarjillo(Narjillo narjillo, DNALog dnaLog) {
		notifyThingRemoved(narjillo);
		space.remove(narjillo);
		dnaLog.markAsDead(narjillo.getDNA().getId());
	}

	private void maybeLayEgg(Narjillo narjillo, DNALog dnaLog, NumGen numGen) {
		Egg egg = narjillo.layEgg(dnaLog, numGen);
		if (egg == null)
			return;

		insert(egg);
	}

	private double getNumberOf1000SquarePointsBlocks() {
		double blocksPerEdge = getSize() / 1000.0;
		return blocksPerEdge * blocksPerEdge;
	}
}
