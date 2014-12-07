package org.nusco.narjillos.ecosystem;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import org.nusco.narjillos.shared.things.Thing;

/**
 * Partitioned space for fast neighbor searching, collision detection, etc.
 */
public class Space {

	private static int AREAS_PER_EDGE = 100;

	// The assumption here is that no Thing will ever
	// move more than this value in a single tick.
	// So a Thing can only move from one area to one of its neighboring areas,
	// not farther away.
	// If we have faster Things, then we need to make this
	// at least equal to their maximum velocity.
	private final double areaSize;
	
	private final Set<Thing>[][] areas;
	private final Set<Thing> allTheThings = new LinkedHashSet<>();

	// TODO: right now there is no visibility to/from outer space. the first is easy, the second is hard
	private final Set<Thing> outerSpace = new LinkedHashSet<>();
	
	@SuppressWarnings("unchecked")
	public Space(long size) {
		areaSize = ((double) size) / AREAS_PER_EDGE;
		this.areas = new Set[AREAS_PER_EDGE][AREAS_PER_EDGE];
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
		int x = toAreaCoordinates(thing.getPosition().x);
		int y = toAreaCoordinates(thing.getPosition().y);

		Set<Thing> result = new LinkedHashSet<>();

		if (isInOuterSpace(x, y)) {
			result.addAll(copy_without_(outerSpace, thing));
			return result;
		}
		
		result.addAll(getArea(x - 1, y - 1));
		result.addAll(getArea(x - 1, y));
		result.addAll(getArea(x - 1, y + 1));
		result.addAll(getArea(x, y - 1));
		
		// Special case: the central area will include the thing
		// we're searching for. We need to remove it.
		Set<Thing> centralAreaWithoutThing = copy_without_(getArea(x, y), thing);
		result.addAll(centralAreaWithoutThing);

		result.addAll(getArea(x, y + 1));
		result.addAll(getArea(x + 1, y -1));
		result.addAll(getArea(x + 1, y));
		result.addAll(getArea(x + 1, y + 1));
		return result;
	}

	public Thing findClosestTo(Thing thing) {
		if (isEmpty())
			return null;

		// TODO: replace with spiral search
		
		Set<Thing> nearbyNeighbors = getNearbyNeighbors(thing);
		
		if (!nearbyNeighbors.isEmpty())
			return findClosestTo_Amongst(thing, nearbyNeighbors);

		return findClosestTo_Amongst(thing, allTheThings);
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
	
	private Set<Thing> copy_without_(Collection<Thing> area, Thing thing) {
		Set<Thing> centralAreaWithoutThing = new LinkedHashSet<>();
		centralAreaWithoutThing.addAll(area);
		centralAreaWithoutThing.remove(thing);
		return centralAreaWithoutThing;
	}

	private boolean isInOuterSpace(int x, int y) {
		return x < 0 || x >= AREAS_PER_EDGE || y < 0 || y >= AREAS_PER_EDGE;
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
