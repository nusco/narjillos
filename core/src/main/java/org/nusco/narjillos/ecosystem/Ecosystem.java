package org.nusco.narjillos.ecosystem;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
import org.nusco.narjillos.shared.utilities.VisualDebugger;

//TODO: I should really make everything in here and its subclasses
//more thread-safe. Right now many external interventions from
//another thread have the potential to break it.
/**
 * The place that Narjillos live in.
 * 
 * Can find things and detecting collisions.
 */
public class Ecosystem {

	private static final double COLLISION_DISTANCE = 30;

	private final long size;
	private final Set<FoodPiece> foodPieces = Collections.synchronizedSet(new LinkedHashSet<FoodPiece>());
	private final Population narjillos = new Population();

	private final List<EcosystemEventListener> ecosystemEvents = new LinkedList<>();

	public Ecosystem(long size) {
		this.size = size;
	}

	public long getSize() {
		return size;
	}

	public Set<Thing> getThings() {
		Set<Thing> result = new LinkedHashSet<Thing>();
		synchronized (this) {
			result.addAll(foodPieces);
		}
		result.addAll(narjillos.getCreatures());
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
		return (Creature)find(narjillos.getCreatures(), near);
	}

	public void tick() {
		narjillos.tick();
		
		// TODO: no need to tick food for now
		
		if (VisualDebugger.DEBUG)
			VisualDebugger.clear();
	}

	public final FoodPiece spawnFood(Vector position) {
		FoodPiece newFood = new FoodPiece();
		newFood.setPosition(position);
		notifyThingAdded(newFood);
		synchronized (this) {
			foodPieces.add(newFood);
		}
		return newFood;
	}

	public final Narjillo spawnNarjillo(Vector position, DNA genes) {
		final Narjillo narjillo = new Narjillo(new Embryo(genes).develop(), position, genes);
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
		notifyThingAdded(narjillo);
		narjillos.add(narjillo);
		return narjillo;
	}

	private void updateTarget(Narjillo narjillo) {
		Vector position = narjillo.getPosition();
		FoodPiece foodPiece = findFoodPiece(position);
		narjillo.setTarget(foodPiece);
	}

	protected void updateTargets() {
		for (Thing narjillo : narjillos.getCreatures())
			updateTarget((Narjillo)narjillo);
	}

	protected void updateTargets(Thing food) {
		for (Thing narjillo : narjillos.getCreatures()) {
			if (((Narjillo)narjillo).getTarget() == food)
				updateTarget((Narjillo)narjillo);
		}
	}

	private void checkCollisionsWithFood(Narjillo narjillo, Segment movement) {
		// TODO: naive algorithm. replace with space partitioning and finding
		// neighbors
		if (movement.getVector().isZero())
			return;
		
		Set<FoodPiece> foodPiecesCopy = new LinkedHashSet<>();
		synchronized (this) {
			foodPiecesCopy.addAll(foodPieces);
		}
		for (FoodPiece foodThing : foodPiecesCopy)
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
		// it's unlikely that we need to consider food farther away
		final double safeDistance = 300;
		if (Math.abs(position.x - foodThing.getPosition().x) < safeDistance)
			return true;
		if (Math.abs(position.y - foodThing.getPosition().y) < safeDistance)
			return true;
		return false;
	}

	private void consumeFood(Narjillo narjillo, FoodPiece foodPiece) {
		synchronized (this) {
			if (!foodPieces.contains(foodPiece))
				return; // race condition: already consumed
		}

		narjillo.feedOn(foodPiece);
		remove(foodPiece);
		
		reproduce(narjillo);
		updateTargets();
	}

	private void remove(FoodPiece foodPiece) {
		synchronized (this) {
			if (!foodPieces.contains(foodPiece))
				return;
			foodPieces.remove(foodPiece);
		}
		notifyThingRemoved(foodPiece);
	}

	private void remove(final Creature narjillo) {
		if (!narjillos.getCreatures().contains(narjillo))
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
		for (EcosystemEventListener ecosystemEvent : ecosystemEvents)
			ecosystemEvent.thingAdded(thing);
	}

	private final void notifyThingRemoved(Thing thing) {
		for (EcosystemEventListener ecosystemEvent : ecosystemEvents)
			ecosystemEvent.thingRemoved(thing);
	}

	public void addEventListener(EcosystemEventListener ecosystemEventListener) {
		ecosystemEvents.add(ecosystemEventListener);
	}

	public synchronized int getNumberOfFoodPieces() {
		return foodPieces.size();
	}

	public int getNumberOfNarjillos() {
		return narjillos.size();
	}

	public Population getPopulation() {
		return narjillos;
	}
}
