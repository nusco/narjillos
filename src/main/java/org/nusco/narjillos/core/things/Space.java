package org.nusco.narjillos.core.things;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.nusco.narjillos.core.geometry.Segment;
import org.nusco.narjillos.core.geometry.Vector;
import org.nusco.narjillos.core.configuration.Configuration;

/**
 * Partitioned space for fast neighbor searches, collision detection, etc.
 *
 * The space is a large square grid. The space around this grid is called "outer space", and it's unusable for our purposes.
 * So the system needs simulation-level constraints that prevent Things from moving into outer space. (For example, destroying
 * things when they venture into outer space).
 */
public class Space {

	private static final int SPACE_AREAS_PER_EDGE = 100;

	private final double areaSize;

	private final Set<Thing>[][] areas;

	private final Set<Thing> things = new LinkedHashSet<>();

	// There is no visibility to/from outer space. The first would be
	// easy, the second would be hard. We just choose to ignore outer space.
	private final Set<Thing> outerSpace = new LinkedHashSet<>();

	private final Map<String, Integer> countsByLabel = new HashMap<>();

	@SuppressWarnings("unchecked")
	public Space(long size) {
		areaSize = ((double) size) / Space.SPACE_AREAS_PER_EDGE;
		this.areas = new Set[Space.SPACE_AREAS_PER_EDGE][Space.SPACE_AREAS_PER_EDGE];
		for (Set<Thing>[] area : areas) {
			for (int j = 0; j < area.length; j++) {
				area[j] = new LinkedHashSet<>();
			}
		}
	}

	public int[] add(Thing thing) {
		int x = toAreaCoordinates(thing.getPosition().x);
		int y = toAreaCoordinates(thing.getPosition().y);
		Set<Thing> area = getArea(x, y);

		area.add(thing);

		synchronized (things) {
			things.add(thing);
		}

		String label = thing.getLabel();

		synchronized (countsByLabel) {
			if (countsByLabel.containsKey(label))
				countsByLabel.put(label, countsByLabel.get(label) + 1);
			else
				countsByLabel.put(label, 1);
		}

		return new int[] { x, y };
	}

	public void remove(Thing thing) {
		getArea(thing).remove(thing);

		synchronized (things) {
			things.remove(thing);
		}

		synchronized (countsByLabel) {
			countsByLabel.put(thing.getLabel(), countsByLabel.get(thing.getLabel()) - 1);
		}
	}

	public boolean contains(Thing thing) {
		return getArea(thing).contains(thing);
	}

	public Thing findClosestTo(Thing thing, String labelRegExp) {
		// Naive three-step approximation. (It can be replaced with spiral search if we ever need more performance).

		if (isEmpty())
			return null;

		Set<Thing> nearbyNeighbors = getNearbyNeighbors(thing, labelRegExp);

		if (!nearbyNeighbors.isEmpty())
			return findClosestTo_Amongst(thing, nearbyNeighbors, labelRegExp);

		synchronized (things) {
			return findClosestTo_Amongst(thing, things, labelRegExp);
		}
	}

	/**
	 * This only searches in the neighboring areas. So, if the movement is able
	 * to span more than one area, it will fail to check all potential
	 * collisions. The assumption here is that movements are smaller than the
	 * areaSize. If this assumption ever becomes limiting, then we'll have to
	 * make this method smarter, and search all the areas that are intersected
	 * by the movement, plus their neighbors (potentially including outerSpace).
	 */
	public Set<Thing> detectCollisions(Segment movement, String label) {
		Set<Thing> collidedFoodPellets = new LinkedHashSet<>();

		getNearbyNeighbors(movement.getStartPoint(), label).stream()
			.filter(
				(neighbor) -> (movement.getMinimumDistanceFromPoint(neighbor.getPosition()) <= Configuration.PHYSICS_COLLISION_DISTANCE))
			.forEach(collidedFoodPellets::add);

		return collidedFoodPellets;
	}

	public Set<Thing> getAll(String label) {
		synchronized (things) {
			return filterByLabel(things, label);
		}
	}

	public boolean isEmpty() {
		synchronized (things) {
			return things.isEmpty();
		}
	}

	public int count(String label) {
		synchronized (countsByLabel) {
			Integer result = countsByLabel.get(label);
			if (result == null)
				return 0;
			return result;
		}
	}

	public double getAreaSize() {
		return areaSize;
	}

	Set<Thing> getNearbyNeighbors(Thing thing, String label) {
		Set<Thing> result = getNearbyNeighbors(thing.getPosition(), label);
		result.remove(thing);
		return result;
	}

	private Set<Thing> getNearbyNeighbors(Vector position, String label) {
		int x = toAreaCoordinates(position.x);
		int y = toAreaCoordinates(position.y);
		Set<Thing> result = new LinkedHashSet<>();

		if (isInOuterSpace(x, y)) {
			populateWithFilteredArea(result, label, outerSpace);
			return result;
		}

		populateWithFilteredArea(result, label, getArea(x - 1, y - 1));
		populateWithFilteredArea(result, label, getArea(x - 1, y));
		populateWithFilteredArea(result, label, getArea(x - 1, y + 1));
		populateWithFilteredArea(result, label, getArea(x, y - 1));
		populateWithFilteredArea(result, label, getArea(x, y));
		populateWithFilteredArea(result, label, getArea(x, y + 1));
		populateWithFilteredArea(result, label, getArea(x + 1, y - 1));
		populateWithFilteredArea(result, label, getArea(x + 1, y));
		populateWithFilteredArea(result, label, getArea(x + 1, y + 1));

		return result;
	}

	private void populateWithFilteredArea(Set<Thing> collector, String label, Set<Thing> area) {
		area.stream()
			.filter((thing) -> (thing.getLabel().contains(label)))
			.forEach(collector::add);
	}

	private Thing findClosestTo_Amongst(Thing thing, Set<Thing> things, String label) {
		double minDistance = Double.MAX_VALUE;
		Thing result = null;

		for (Thing neighbor : things) {
			if (matches(neighbor, label)) {
				double distance = neighbor.getPosition().minus(thing.getPosition()).getLength();
				if (distance < minDistance) {
					minDistance = distance;
					result = neighbor;
				}
			}
		}

		return result;
	}

	private boolean isInOuterSpace(int x, int y) {
		return x < 0 || x >= Space.SPACE_AREAS_PER_EDGE || y < 0 || y >= Space.SPACE_AREAS_PER_EDGE;
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

	private int toAreaCoordinates(double val) {
		return (int) Math.floor(val / areaSize);
	}

	private Set<Thing> filterByLabel(Set<Thing> things, String label) {
		Set<Thing> result = new LinkedHashSet<>();
		things.stream()
			.filter((thing) -> (matches(thing, label)))
			.forEach(result::add);
		return result;
	}

	private boolean matches(Thing thing, String label) {
		return thing.getLabel().contains(label);
	}
}
