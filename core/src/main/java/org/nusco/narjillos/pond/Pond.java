package org.nusco.narjillos.pond;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

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

	private final long size;
	private final List<FoodPiece> foodPieces = new LinkedList<>();
	private final List<Narjillo> narjillos = new LinkedList<>();
	private final ParallelTicker scheduler = new ParallelTicker ();
	
	// optimize and conquer
	private List<FoodPiece> foodPiecesAtFrameStart;
	private List<Narjillo> narjillosAtFrameStart;
	private List<Thing> thingsRemovedBeforeFrameEnd;
	
	private final List<PondEventListener> pondEvents = new LinkedList<>();

	private int tickCounter = 0;

	public Pond(long size) {
		this.size = size;
		resetAllCaches();
	}

	public long getSize() {
		return size;
	}

	private synchronized void resetAllCaches() {
		foodPiecesAtFrameStart = Collections.unmodifiableList(foodPieces);
		narjillosAtFrameStart = Collections.unmodifiableList(narjillos);
		thingsRemovedBeforeFrameEnd = new LinkedList<>();
	}

	public List<Thing> getThings() {
		List<Thing> result = new LinkedList<Thing>();
		result.addAll(foodPiecesAtFrameStart);
		result.addAll(narjillosAtFrameStart);
		return result;
	}

	private Vector find(List<? extends Thing> things, Vector near) {
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
		return find(foodPiecesAtFrameStart, near);
	}

	public Vector findNarjillo(Vector near) {
		return find(narjillosAtFrameStart, near);
	}

	public void tick() {
		resetAllCaches();

		scheduler.runJobs(getThings());

		removeThingsAtFrameEnd();
		
		if (tickCounter-- < 0) {
			tickCounter = 100000;
			updateTargets();
		}
	}

	private void removeThingsAtFrameEnd() {
		for (Thing thing : thingsRemovedBeforeFrameEnd) {
			for (PondEventListener pondEvent : pondEvents)
				pondEvent.thingRemoved(thing);
			narjillos.remove(thing);
			foodPieces.remove(thing);
		}
	}

	public FoodPiece spawnFood(Vector position) {
		FoodPiece newFood = new FoodPiece();
		foodPieces.add(newFood);
		placeInPond(newFood, position);
		return newFood;
	}

	public final Narjillo spawnSwimmer(Vector position, DNA genes) {
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
		narjillos.add(narjillo);
		placeInPond(narjillo, position);
		return narjillo;
	}

	private void updateTarget(Narjillo swimmer) {
		Vector position = swimmer.getPosition();
		Vector locationOfClosestFood = findFoodPiece(position);
		swimmer.setTarget(locationOfClosestFood);
	}

	private void updateTargets() {
		for (Narjillo narjillo : narjillosAtFrameStart)
			updateTarget(narjillo);
	}

	private void checkCollisionsWithFood(Narjillo narjillo, Segment movement) {
		// TODO: naive algorithm. replace with space partitioning and finding
		// neighbors
		for (FoodPiece foodThing : foodPiecesAtFrameStart)
			checkCollisionWithFood(narjillo, movement, foodThing);
	}

	private void checkCollisionWithFood(Narjillo narjillo, Segment movement, FoodPiece foodThing) {
		if (!isUnderSafeDistance(narjillo, foodThing))
			return;  // optimization

		if (movement.getMinimumDistanceFromPoint(foodThing.getPosition()) > COLLISION_DISTANCE)
			return;
		
		consumeFood(narjillo, foodThing);
	}

	// ugly optimization to avoid getting into the expensive
	// getMinimumDistanceFromPoint() method.
	// this will hopefully be redundant once we have a good space partition
	// algorithm,
	// but it makes quite a difference until then
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
		if (thingsRemovedBeforeFrameEnd.contains(foodPiece))
			return; // already consumed
		narjillo.feed();
		thingsRemovedBeforeFrameEnd.add(foodPiece);
		reproduce(narjillo);
		updateTargets();
	}

	private synchronized void killNarjillo(final Narjillo narjillo) {
		thingsRemovedBeforeFrameEnd.add(narjillo);
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

	public int getNumberOfFoodPieces() {
		return foodPiecesAtFrameStart.size();
	}

	public int getNumberOfNarjillos() {
		return narjillosAtFrameStart.size();
	}

	public Narjillo getMostProlificNarjillo() {
		List<Narjillo> narjillos = narjillosAtFrameStart;
		Narjillo result = narjillos.get(0);
		for (Narjillo narjillo : narjillos)
			if (narjillo.getNumberOfDescendants() > result.getNumberOfDescendants())
				result = narjillo;
		return result;
	}
}
