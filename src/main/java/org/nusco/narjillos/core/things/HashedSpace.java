package org.nusco.narjillos.core.things;

import org.nusco.narjillos.core.geometry.BoundingBox;
import org.nusco.narjillos.core.geometry.Vector;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class HashedSpace {

	private final Map<Thing, Set<HashedLocation>> thingsToLocations = new HashMap<>();
	private final Map<HashedLocation, List<Thing>> locationsToThings = new HashMap<>();

	public void add(Thing thing) {
		validateMaximumSize(thing);

		Set<HashedLocation> locations = calculateHashedLocationsOf(thing);
		thingsToLocations.put(thing, locations);
		locations.stream().forEach(location -> addThingToLocation(location, thing));
	}

	public void remove(Thing thing) {
		final Set<HashedLocation> locations = thingsToLocations.get(thing);
		thingsToLocations.remove(thing);
		locations.stream().forEach(location -> locationsToThings.get(location).remove(thing));
	}

	public Set<Thing> getThings() {
		return thingsToLocations.keySet();
	}

	public Optional<Set<HashedLocation>> getHashedLocationsOf(Thing thing) {
		Set<HashedLocation> result = thingsToLocations.get(thing);
		return result != null ? Optional.of(result) : Optional.empty();
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
		Set<HashedLocation> result = new HashSet<>();
		BoundingBox boundingBox = thing.getBoundingBox();
		result.add(HashedLocation.ofCoordinates(boundingBox.right, boundingBox.bottom));
		result.add(HashedLocation.ofCoordinates(boundingBox.right, boundingBox.top));
		result.add(HashedLocation.ofCoordinates(boundingBox.left, boundingBox.bottom));
		result.add(HashedLocation.ofCoordinates(boundingBox.left, boundingBox.top));
		return result;
	}

	private void addThingToLocation(HashedLocation location, Thing thing) {
		if (!locationsToThings.containsKey(location))
			locationsToThings.put(location, new LinkedList<>());
		locationsToThings.get(location).add(thing);
	}
}
