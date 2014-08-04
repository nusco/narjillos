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
import org.nusco.narjillos.creature.genetics.Creature;
import org.nusco.narjillos.creature.genetics.DNA;
import org.nusco.narjillos.creature.genetics.Population;
import org.nusco.narjillos.shared.physics.Segment;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.things.Thing;
import org.nusco.narjillos.shared.utilities.RanGen;

public class Ecosystem {

	private static final double COLLISION_DISTANCE = 30;

	private final long size;
	private final Set<FoodPiece> foodPieces = Collections.newSetFromMap(new ConcurrentHashMap<FoodPiece, Boolean>());
	private final Population narjillos = new Population();

	private final List<PondEventListener> pondEvents = new LinkedList<>();

	private int tickCounter = 0;

	public Ecosystem(long size) {
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

	private Thing find(Collection<? extends Thing> things, Vector near) {
		double minDistance = Double.MAX_VALUE;
		Thing result = null;
		for (Thing thing : things) {
			double distance = thing.getPosition().minus(near).getLength();
			if (distance < minDistance) {
				minDistance = distance;
				result = thing;
			}
		}
		return result;
	}

	public FoodPiece findFoodPiece(Vector near) {
		return (FoodPiece)find(foodPieces, near);
	}

	public Creature findNarjillo(Vector near) {
		return (Creature)find(narjillos.toCollection(), near);
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
		final Narjillo narjillo = new Narjillo(new Embryo(genes).develop(), genes);
		narjillo.addEventListener(new NarjilloEventListener() {

			@Override
			public void moved(Segment movement) {
				checkCollisionsWithFood(narjillo, movement);
			}

			@Override
			public void died() {
				remove(narjillo);
			}
		});
		narjillo.setPosition(position);
		notifyThingAdded(narjillo);
		narjillos.add(narjillo);
		return narjillo;
	}

	private void updateTarget(Narjillo narjillo) {
		Vector position = narjillo.getPosition();
		FoodPiece foodPiece = findFoodPiece(position);
		Vector locationOfClosestFood = (foodPiece != null) ? foodPiece.getPosition() : getFarawayPoint();
		narjillo.setTarget(locationOfClosestFood);
	}

	private Vector getFarawayPoint() {
		// this should not be necessary once narjillos can think for themselves
		return Vector.polar(Double.MAX_VALUE, Double.MAX_VALUE);
	}

	protected void updateTargets() {
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
		remove(foodPiece);
		
		reproduce(narjillo);
		updateTargets();
	}

	protected synchronized void remove(FoodPiece foodPiece) {
		if (!foodPieces.contains(foodPiece))
			return;
		foodPieces.remove(foodPiece);
		notifyThingRemoved(foodPiece);
	}

	protected synchronized void remove(final Creature narjillo) {
		if (!narjillos.getCollection().contains(narjillo))
			return;
		narjillos.remove(narjillo);
		notifyThingRemoved(narjillo);
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

	private final void notifyThingRemoved(Thing thing) {
		for (PondEventListener pondEvent : pondEvents)
			pondEvent.thingRemoved(thing);
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

	protected synchronized void clearFood() {
		for (FoodPiece foodPiece : foodPieces)
			remove(foodPiece);
	}

	protected synchronized void clearCreatures() {
		for (Creature creature : narjillos.getCollection())
			remove(creature);
	}
}
