package org.nusco.narjillos.experiment.environment;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.nusco.narjillos.core.chemistry.Atmosphere;
import org.nusco.narjillos.core.geometry.BoundingBox;
import org.nusco.narjillos.core.geometry.Segment;
import org.nusco.narjillos.core.geometry.Vector;
import org.nusco.narjillos.core.things.FoodPellet;
import org.nusco.narjillos.core.things.Thing;
import org.nusco.narjillos.core.utilities.Configuration;
import org.nusco.narjillos.core.utilities.NumGen;
import org.nusco.narjillos.creature.Egg;
import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.creature.body.physics.Viscosity;
import org.nusco.narjillos.genomics.DNA;
import org.nusco.narjillos.genomics.DNALog;

/**
 * A complex environment populate with narjillos, eggs and food.
 */
public class Ecosystem extends Environment {

	public static int numberOfBackgroundThreads = Runtime.getRuntime().availableProcessors();

	private final ExecutorService executorService;

	/** Counter used by the ThreadFactory to name threads. */
	private final AtomicInteger tickWorkerCounter = new AtomicInteger(1);

	private final Set<Narjillo> narjillos = new LinkedHashSet<>();

	private final Space space;
	private final Vector center;
	private final BoundingBox boundingBox;
	private Atmosphere atmosphere;

	public Ecosystem(final long size, boolean sizeCheck) {
		super(size);

		ThreadFactory tickWorkerFactory = (Runnable r) -> {
			Thread result = new Thread(r, "tick-worker-" + tickWorkerCounter.getAndIncrement());
			result.setPriority(Thread.currentThread().getPriority());
			return result;
		};
		executorService = Executors.newFixedThreadPool(numberOfBackgroundThreads, tickWorkerFactory);

		this.space = new Space(size);
		this.center = Vector.cartesian(size, size).by(0.5);
		this.boundingBox = new BoundingBox(0, size, 0, size);
		this.atmosphere = new Atmosphere();

		// check that things cannot move faster than a space area in a single
		// tick (which would make collision detection unreliable)
		if (sizeCheck && space.getAreaSize() < Viscosity.getMaxVelocity())
			throw new RuntimeException("Bug: Area size smaller than max velocity");
	}

	public Atmosphere getAtmosphere() {
		return atmosphere;
	}

	public void setAtmosphere(Atmosphere atmosphere) {
		this.atmosphere = atmosphere;
	}

	@Override
	public void tick(DNALog dnaLog, NumGen numGen) {
		if (isShuttingDown())
			return; // we're leaving, apparently

		super.tick(dnaLog, numGen);
	}

	@Override
	public Set<Thing> getThings(String label) {
		Set<Thing> result = new LinkedHashSet<>();
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

	public Vector findClosestFood(Thing thing) {
		Thing target = space.findClosestTo(thing, "food_pellet");

		if (target == null)
			return center;

		return target.getPosition();
	}

	public final FoodPellet spawnFood(Vector position) {
		FoodPellet newFood = new FoodPellet();
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

	public final Egg spawnEgg(DNA genes, Vector position, NumGen numGen) {
		Egg egg = new Egg(genes, position, Vector.ZERO, Configuration.CREATURE_SEED_ENERGY, numGen);
		insert(egg);
		return egg;
	}

	@Override
	public int getNumberOfFoodPellets() {
		return space.count("food_pellet");
	}

	@Override
	public int getNumberOfEggs() {
		return space.count("egg");
	}

	@Override
	public int getNumberOfNarjillos() {
		synchronized (narjillos) {
			return narjillos.size();
		}
	}

	public Set<Narjillo> getNarjillos() {
		synchronized (narjillos) {
			return new LinkedHashSet<>(narjillos);
		}
	}

	public void updateTargets() {
		synchronized (narjillos) {
			narjillos.stream().forEach((narjillo) -> {
				Vector closestTarget = findClosestFood(narjillo);
				narjillo.setTarget(closestTarget);
			});
		}
	}

	public void populate(String dna, DNALog dnaLog, NumGen numGen) {
		spawnFood(numGen);

		for (int i = 0; i < getNumberOf1000SquarePointsBlocks() * Configuration.ECOSYSTEM_EGGS_DENSITY_PER_BLOCK; i++)
			spawnEgg(createDna(dna, dnaLog, numGen), randomPosition(getSize(), numGen), numGen);
	}

	private DNA createDna(String dna, DNALog dnaLog, NumGen numGen) {
		DNA result = new DNA(numGen.nextSerial(), dna, DNA.NO_PARENT);
		dnaLog.save(result);
		return result;
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

	protected boolean isShuttingDown() {
		return executorService.isShutdown();
	}

	protected Map<Narjillo, Future<Set<Thing>>> tickNarjillos(Set<Narjillo> narjillos) {
		Map<Narjillo, Future<Set<Thing>>> result = new LinkedHashMap<>();
		for (final Narjillo narjillo : narjillos) {
			result.put(narjillo, executorService.submit(() -> {
				Segment movement = narjillo.tick();
				damageIfTouchingEdges(narjillo);
				return getCollisions(movement);
			}));
		}
		return result;
	}

	@Override
	protected void tickThings(DNALog dnaLog, NumGen numGen) {
		new LinkedList<>(space.getAll("egg")).stream().forEach((thing) -> tickEgg((Egg) thing, numGen));

		synchronized (narjillos) {
			new LinkedList<>(narjillos).stream().filter((narjillo) -> (narjillo.isDead()))
					.forEach((narjillo) -> removeNarjillo(narjillo, dnaLog));
		}

		tickNarjillos(numGen);

		if (shouldSpawnFood(numGen)) {
			spawnFood(randomPosition(getSize(), numGen));
			updateTargets();
		}

		synchronized (narjillos) {
			narjillos.stream().forEach((narjillo) -> {
				maybeLayEgg(narjillo, dnaLog, numGen);
			});
		}
	}

	private Set<Thing> getCollisions(Segment movement) {
		return space.detectCollisions(movement, "food_pellet");
	}

	private void damageIfTouchingEdges(final Narjillo narjillo) {
		BoundingBox boundingBox2 = narjillo.getBoundingBox();
		if (!boundingBox2.isContainedIn(boundingBox))
			narjillo.damage();
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
			insertNarjillo(egg.getHatchedNarjillo().get());
		if (egg.isDecayed())
			remove(egg);
	}

	private synchronized void tickNarjillos(NumGen numGen) {
		Map<Narjillo, Set<Thing>> narjillosToCollidedFood;
		synchronized (narjillos) {
			narjillosToCollidedFood = tick(narjillos);
		}

		// The next operations happen in a predictable order, to avoid
		// non-deterministic behavior or race conditions - for example, when
		// multiple narjillos collide with the same food pellet.

		breathe();
		
		// Consume food
		narjillosToCollidedFood.entrySet().stream().forEach((entry) -> {
			Narjillo narjillo = entry.getKey();
			Set<Thing> collidedFood = entry.getValue();
			consume(narjillo, collidedFood, numGen);
		});
	}

	private void breathe() {
		// The maximum amount of energy that each creature can extract from
		// breathing, depending on the amount of catalyst available to all the
		// creatures. Varies between 0 and 1 included.
		double breathingPowerPerNarjillo = Math.min(1, (double) getAtmosphere().getCatalystLevel() / getNumberOfNarjillos());

		// Increase energies
		for (Narjillo narjillo : narjillos) {
			double densityOfBreathableElement = getAtmosphere().getDensityOf(narjillo.getBreathedElement());
			double energyObtainedByBreathing = densityOfBreathableElement * breathingPowerPerNarjillo;
			narjillo.getEnergy().increaseBy(energyObtainedByBreathing);
		}

		// Consume elements
		for (Narjillo narjillo : narjillos) {
			getAtmosphere().convert(narjillo.getBreathedElement(), narjillo.getByproduct());
		}
	}

	private Map<Narjillo, Set<Thing>> tick(Set<Narjillo> narjillos) {
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
		if (getNumberOfFoodPellets() >= maxFoodPellets)
			return false;

		double foodRespawnAverageInterval = Configuration.ECOSYSTEM_FOOD_RESPAWN_AVERAGE_INTERVAL_PER_BLOCK
				/ getNumberOf1000SquarePointsBlocks();
		return numGen.nextDouble() < 1.0 / foodRespawnAverageInterval;
	}

	private Vector randomPosition(long size, NumGen numGen) {
		return Vector.cartesian(numGen.nextDouble() * size, numGen.nextDouble() * size);
	}

	private void updateTargets(Thing food) {
		synchronized (narjillos) {
			narjillos.stream().filter((narjillo) -> (narjillo.getTarget().equals(food.getPosition()))).forEach((narjillo) -> {
				Vector closestTarget = findClosestFood(narjillo);
				narjillo.setTarget(closestTarget);
			});
		}
	}

	private void consume(Narjillo narjillo, Set<Thing> foodPellets, NumGen numGen) {
		foodPellets.stream().forEach((foodPellet) -> {
			consumeFood(narjillo, (FoodPellet) foodPellet, numGen);
		});
	}

	private void consumeFood(Narjillo narjillo, FoodPellet foodPellet, NumGen numGen) {
		if (!space.contains(foodPellet))
			return; // race condition: already consumed

		remove(foodPellet);
		narjillo.feedOn(foodPellet);

		updateTargets(foodPellet);
	}

	private void remove(Thing thing) {
		notifyThingRemoved(thing);
		space.remove(thing);
	}

	private void removeNarjillo(Narjillo narjillo, DNALog dnaLog) {
		notifyThingRemoved(narjillo);
		synchronized (narjillos) {
			narjillos.remove(narjillo);
		}
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
