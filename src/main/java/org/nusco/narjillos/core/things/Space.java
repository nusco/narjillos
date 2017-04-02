package org.nusco.narjillos.core.things;

import org.nusco.narjillos.core.configuration.Configuration;
import org.nusco.narjillos.core.geometry.BoundingBox;
import org.nusco.narjillos.core.geometry.Segment;
import org.nusco.narjillos.core.geometry.Vector;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Partitioned space for fast neighbor searches, collision detection, etc.
 */
public class Space {

	private final Map<Thing, Set<HashedLocation>> thingsToLocations = new LinkedHashMap<>();
	private final Map<HashedLocation, List<Thing>> locationsToThings = new LinkedHashMap<>();

	public void add(Thing thing) {
		validateMaximumSize(thing);

		Set<HashedLocation> locations = calculateHashedLocationsOf(thing);

		synchronized (thingsToLocations) {
			thingsToLocations.put(thing, locations);
		}

		locations.stream().forEach(location -> addThingToLocation(location, thing));
	}

	public void remove(Thing thing) {
		final Set<HashedLocation> locations = thingsToLocations.get(thing);

		synchronized (thingsToLocations) {
			thingsToLocations.remove(thing);
		}

		locations.stream().forEach(location -> locationsToThings.get(location).remove(thing));
	}

	public Set<Thing> getThings() {
		return thingsToLocations.keySet();
	}

	public Optional<Set<HashedLocation>> getHashedLocationsOf(Thing thing) {
		Set<HashedLocation> result = thingsToLocations.get(thing);
		return result != null ? Optional.of(result) : Optional.empty();
	}

	public Set<Thing> getAll() {
		synchronized (thingsToLocations) {
			return thingsToLocations.keySet();
		}
	}

	List<Thing> getThingsAtHashedLocation(int lx, int ly) {
		List<Thing> result = locationsToThings.get(HashedLocation.at(lx, ly));
		return result != null ? result : Collections.emptyList();
	}

	private void validateMaximumSize(Thing thing) {
		if (thing.getRadius() <= HashedLocation.GRID_SIZE)
			return;

		String message = String.format("Things with a radius over %s can cause failures in collision detection", HashedLocation.GRID_SIZE);
		throw new RuntimeException(message);
	}

	private Set<HashedLocation> calculateHashedLocationsOf(Thing thing) {
		Set<HashedLocation> result = new LinkedHashSet<>();
		BoundingBox boundingBox = thing.getBoundingBox();
		result.add(HashedLocation.ofCoordinates(boundingBox.left, boundingBox.bottom));
		result.add(HashedLocation.ofCoordinates(boundingBox.left, boundingBox.top));
		result.add(HashedLocation.ofCoordinates(boundingBox.right, boundingBox.top));
		result.add(HashedLocation.ofCoordinates(boundingBox.right, boundingBox.bottom));
		return result;
	}

	private void addThingToLocation(HashedLocation location, Thing thing) {
		if (!locationsToThings.containsKey(location))
			locationsToThings.put(location, new LinkedList<>());
		locationsToThings.get(location).add(thing);
	}

	private boolean isEmpty() {
		synchronized (thingsToLocations) {
			return thingsToLocations.isEmpty();
		}
	}

	Set<Thing> getNearbyNeighbors(Thing thing, String label) {
		Set<Thing> result = getNearbyNeighbors(thing.getPosition(), label);
		result.remove(thing);
		return result;
	}

	private Set<Thing> getNearbyNeighbors(Vector position, String label) {
		HashedLocation location = HashedLocation.ofCoordinates(position.x, position.y);
		long x = location.lx;
		long y = location.ly;

		Set<Thing> result = new LinkedHashSet<>();

		populateWithFilteredArea(result, label, HashedLocation.at(x - 1, y - 1));
		populateWithFilteredArea(result, label, HashedLocation.at(x - 1, y));
		populateWithFilteredArea(result, label, HashedLocation.at(x - 1, y + 1));
		populateWithFilteredArea(result, label, HashedLocation.at(x, y - 1));
		populateWithFilteredArea(result, label, HashedLocation.at(x, y));
		populateWithFilteredArea(result, label, HashedLocation.at(x, y + 1));
		populateWithFilteredArea(result, label, HashedLocation.at(x + 1, y - 1));
		populateWithFilteredArea(result, label, HashedLocation.at(x + 1, y));
		populateWithFilteredArea(result, label, HashedLocation.at(x + 1, y + 1));

		return result;
	}

	private void populateWithFilteredArea(Set<Thing> collector, String label, HashedLocation location) {
		List<Thing> things = locationsToThings.get(location);

		if (things == null)
			return;

		things.stream()
			.filter((thing) -> (thing.getLabel().contains(label)))
			.forEach(collector::add);
	}

	public boolean contains(Thing thing) {
		return thingsToLocations.containsKey(thing);
	}

	public Thing findClosestTo(Thing thing, String labelRegExp) {
		// Naive three-step approximation. (It can be replaced with spiral search if we ever need more performance).

		if (isEmpty())
			return null;

		Set<Thing> nearbyNeighbors = getNearbyNeighbors(thing, labelRegExp);

		if (!nearbyNeighbors.isEmpty())
			return findClosestTo_Amongst(thing, nearbyNeighbors, labelRegExp);

		Set<Thing> things = getAll();
		return findClosestTo_Amongst(thing, things, labelRegExp);
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

	private boolean matches(Thing thing, String label) {
		return thing.getLabel().contains(label);
	}

	public Set<Thing> getAll(String label) {
		Set<Thing> result = new LinkedHashSet<>();
		getAll().stream()
			.filter((thing) -> (matches(thing, label)))
			.forEach(result::add);
		return result;
	}
}
