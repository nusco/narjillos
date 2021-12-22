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

import static org.nusco.narjillos.core.things.HashedLocation.inc;
import static org.nusco.narjillos.core.things.HashedLocation.dec;

/**
 * Partitioned space for fast neighbor searches, collision detection, etc.
 */
public class Space {

	private final Map<String, Map<Thing, Set<HashedLocation>>> labelsToThingsToLocations = new LinkedHashMap<>();
	private final Map<HashedLocation, List<Thing>> locationsToThings = new LinkedHashMap<>();
	private final List<Thing> allThings = new LinkedList<>();

	public synchronized void add(Thing thing) {
		validateMaximumSize(thing);

		Set<HashedLocation> locations = calculateHashedLocationsOf(thing);

		getThingsToLocations(thing.getLabel()).put(thing, locations);
		locations.stream().forEach(location -> addThingToLocation(location, thing));
		allThings.add(thing);
	}

	public synchronized void remove(Thing thing) {
		final Set<HashedLocation> locations = getThingsToLocations(thing.getLabel()).get(thing);

		getThingsToLocations(thing.getLabel()).remove(thing);
		locations.stream().forEach(location -> locationsToThings.get(location).remove(thing));
		allThings.remove(thing);
	}

	public synchronized void update(Thing thing) {
		// TODO
	}

	public synchronized boolean contains(Thing thing) {
		return getThingsToLocations(thing.getLabel()).containsKey(thing);
	}

	public synchronized Thing findClosestTo(Thing thing, String label) {
		// Naive three-step approximation. (It can be replaced with spiral search if we ever need more performance).

		if (allThings.isEmpty())
			return null;

		Set<Thing> nearbyNeighbors = getNearbyNeighbors(thing, label);

		if (!nearbyNeighbors.isEmpty())
			return findClosestTo_Amongst(thing.getPosition(), nearbyNeighbors);

		return findClosestTo_Amongst(thing.getPosition(), getAll(label));
	}

	/**
	 * This only searches in the neighboring areas. So, if the movement is able
	 * to span more than one area, it will fail to check all potential
	 * collisions. The assumption here is that movements are smaller than the
	 * areaSize. If this assumption ever becomes limiting, then we'll have to
	 * make this method smarter, and search all the areas that are intersected
	 * by the movement, plus their neighbors.
	 */
	public synchronized Set<Thing> detectCollisions(Segment movement, String label) {
		Set<Thing> collidedFoodPellets = new LinkedHashSet<>();

		final double COLLISION_DISTANCE_SQUARED = Configuration.PHYSICS_COLLISION_DISTANCE * Configuration.PHYSICS_COLLISION_DISTANCE;

		getNearbyNeighbors(movement.getStartPoint(), label).stream()
			.filter(
				(neighbor) -> (movement.getMinimumDistanceFromPointSquared(neighbor.getPosition()) <= COLLISION_DISTANCE_SQUARED))
			.forEach(collidedFoodPellets::add);

		return collidedFoodPellets;
	}

	public synchronized Set<Thing> getAll(String label) {
		if (label.equals(""))
			return new LinkedHashSet<>(allThings);

		return new LinkedHashSet<>(getThingsToLocations(label).keySet());
	}

	private Map<Thing, Set<HashedLocation>> getThingsToLocations(String label) {
		return labelsToThingsToLocations.computeIfAbsent(label, k -> new LinkedHashMap<>());
	}

	Optional<Set<HashedLocation>> getHashedLocationsOf(Thing thing) {
		Set<HashedLocation> result = getThingsToLocations(thing.getLabel()).get(thing);
		return Optional.ofNullable(result);
	}

	List<Thing> getThingsAtHashedLocation(int lx, int ly) {
		return locationsToThings.getOrDefault(HashedLocation.at(lx, ly), Collections.emptyList());
	}

	Set<Thing> getNearbyNeighbors(Thing thing, String label) {
		Set<Thing> result = getNearbyNeighbors(thing.getPosition(), label);
		result.remove(thing);
		return result;
	}

	private void validateMaximumSize(Thing thing) {
		if (thing.getRadius() <= HashedLocation.GRID_SIZE)
			return;

		// FIXME: temporarily disabled until we have the concept of composite things
		// (right now, a narjillo can easily grow past grid size, although its individual
		// organs can't)

//		String message = String.format("Things with a radius over %s can cause failures in collision detection", HashedLocation.GRID_SIZE);
//		throw new RuntimeException(message);
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

	private Set<Thing> getNearbyNeighbors(Vector position, String label) {
		HashedLocation location = HashedLocation.ofCoordinates(position.x, position.y);
		long x = location.lx;
		long y = location.ly;

		Set<Thing> result = new LinkedHashSet<>();

		populateWithFilteredArea(result, label, HashedLocation.at(dec(x), dec(y)));
		populateWithFilteredArea(result, label, HashedLocation.at(dec(x), y));
		populateWithFilteredArea(result, label, HashedLocation.at(dec(x), inc(y)));
		populateWithFilteredArea(result, label, HashedLocation.at(x, dec(y)));
		populateWithFilteredArea(result, label, HashedLocation.at(x, y));
		populateWithFilteredArea(result, label, HashedLocation.at(x, inc(y)));
		populateWithFilteredArea(result, label, HashedLocation.at(inc(x), dec(y)));
		populateWithFilteredArea(result, label, HashedLocation.at(inc(x), y));
		populateWithFilteredArea(result, label, HashedLocation.at(inc(x), inc(y)));

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

	private Thing findClosestTo_Amongst(Vector position, Set<Thing> things) {
		final double[] minDistance = { Double.MAX_VALUE };
		final Thing[] result = { null };

		things.forEach(thing -> {
			double distance = thing.getPosition().minus(position).getLength();
			if (distance < minDistance[0]) {
				minDistance[0] = distance;
				result[0] = thing;
			}
		});

		return result[0];
	}
}
