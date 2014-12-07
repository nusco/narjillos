package org.nusco.narjillos.ecosystem;

import java.util.LinkedHashSet;
import java.util.Set;

import org.nusco.narjillos.shared.physics.Segment;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.things.Thing;

/**
 * Partitioned space for fast neighbor searching, collision detection, etc.
 */
public class Space {

	private static final double COLLISION_DISTANCE = 60;

	// The assumption here is that no Thing will ever
	// move more than this value in a single tick.
	// So a Thing can only move from one area to one of its neighboring areas,
	// not farther away.
	// If we have faster Things, then we need to make this
	// at least equal to their maximum velocity.
	private final double areaSize;

	private final int areasPerEdge;
	private final Set<Thing>[][] areas;

	private final Set<Thing> allTheThings = new LinkedHashSet<>();

	// TODO: right now there is no visibility to/from outer space. the first is easy, the second is hard
	private final Set<Thing> outerSpace = new LinkedHashSet<>();
	
	@SuppressWarnings("unchecked")
	public Space(long size, int areasPerEdge) {
		this.areasPerEdge = areasPerEdge;
		areaSize = ((double) size) / areasPerEdge;
		this.areas = new Set[areasPerEdge][areasPerEdge];
		for (int i = 0; i < areas.length; i++)
			for (int j = 0; j < areas[i].length; j++)
				areas[i][j] = new LinkedHashSet<>();
	}

	double getAreaSize() {
		return areaSize;
	}

	public int[] add(Thing thing) {
		int x = toAreaCoordinates(thing.getPosition().x);
		int y = toAreaCoordinates(thing.getPosition().y);
		Set<Thing> area = getArea(x, y);

		area.add(thing);
		allTheThings.add(thing);
		
		return new int[] {x, y};
	}

	public void remove(Thing thing) {
		getArea(thing).remove(thing);
		allTheThings.remove(thing);
	}

	public boolean contains(Thing thing) {
		return getArea(thing).contains(thing);
	}

	public Set<Thing> getNearbyNeighbors(Thing thing) {
		Set<Thing> result = getNearbyNeighbors(thing.getPosition());
		result.remove(thing);
		return result;
	}

	private Set<Thing> getNearbyNeighbors(Vector position) {
		int x = toAreaCoordinates(position.x);
		int y = toAreaCoordinates(position.y);
		Set<Thing> result = new LinkedHashSet<>();
		
		if (isInOuterSpace(x, y)) {
			result.addAll(outerSpace);
			return result;
		}
		
		result.addAll(getArea(x - 1, y - 1));
		result.addAll(getArea(x - 1, y));
		result.addAll(getArea(x - 1, y + 1));
		result.addAll(getArea(x, y - 1));
		result.addAll(getArea(x, y));
		result.addAll(getArea(x, y + 1));
		result.addAll(getArea(x + 1, y -1));
		result.addAll(getArea(x + 1, y));
		result.addAll(getArea(x + 1, y + 1));
		return result;
	}

	public Thing findClosestTo(Thing thing) {
		// TODO: naive three-step approximation. Replace with spiral search?

		if (isEmpty())
			return null;
		
		Set<Thing> nearbyNeighbors = getNearbyNeighbors(thing);
		
		if (!nearbyNeighbors.isEmpty())
			return findClosestTo_Amongst(thing, nearbyNeighbors);

		return findClosestTo_Amongst(thing, allTheThings);
	}

	public Set<Thing> detectCollisions(Segment movement) {
		Set<Thing> collidedFoodPieces = new LinkedHashSet<>();

		for (Thing neighbor : getNearbyNeighbors(movement.getStartPoint()))
			if (movement.getMinimumDistanceFromPoint(neighbor.getPosition()) <= COLLISION_DISTANCE)
				collidedFoodPieces.add(neighbor);

		return collidedFoodPieces;
	}

	private Thing findClosestTo_Amongst(Thing thing, Set<Thing> things) {
		double minDistance = Double.MAX_VALUE;
		Thing result = null;

		for (Thing neighbor : things) {
			double distance = neighbor.getPosition().minus(thing.getPosition()).getLength();
			if (distance < minDistance) {
				minDistance = distance;
				result = neighbor;
			}
		}

		return result;
	}

	private boolean isInOuterSpace(int x, int y) {
		return x < 0 || x >= areasPerEdge || y < 0 || y >= areasPerEdge;
	}

	private Set<Thing> getArea(int x, int y) {
		if (isInOuterSpace(x, y))
			return outerSpace;
		return areas[x][y];
	}

	private Set<Thing> getArea(Thing thing) {
		int x = toAreaCoordinates(thing.getPosition().x);
		int y = toAreaCoordinates(thing.getPosition().y);
		return getArea(x, y);
	}

	private int toAreaCoordinates(double x) {
		return (int) Math.floor(x / areaSize);
	}

	public Set<Thing> getAll() {
		return allTheThings;
	}

	public boolean isEmpty() {
		return allTheThings.isEmpty();
	}
}
