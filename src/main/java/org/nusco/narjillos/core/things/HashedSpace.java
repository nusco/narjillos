package org.nusco.narjillos.core.things;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HashedSpace {

	private final Map<Thing, List<HashedLocation>> thingsToLocations = new HashMap<>();
	private final Map<HashedLocation, List<Thing>> locationsToThings = new HashMap<>();

	public void add(Thing thing) {
		// TODO: add check that Thing dimension doesn't exceed grid size

		List<HashedLocation> locations = calculateHashedLocationsOf(thing);
		thingsToLocations.put(thing, locations);
		locations.stream().forEach(location -> addThingToLocation(location, thing));
	}

	public Set<Thing> getThings() {
		return thingsToLocations.keySet();
	}

	public List<HashedLocation> getHashedLocationsOf(Thing thing) {
		return thingsToLocations.get(thing);
	}

	public List<Thing> getThingsAtHashedLocation(int lx, int ly) {
		return locationsToThings.get(new HashedLocation(lx, ly));
	}

	private List<HashedLocation> calculateHashedLocationsOf(Thing thing) {
		// TODO: support multiple locations
		List<HashedLocation> result = new LinkedList<>();
		result.add(new HashedLocation(thing));
		return result;
	}

	private void addThingToLocation(HashedLocation location, Thing thing) {
		if (!locationsToThings.containsKey(location))
			locationsToThings.put(location, new LinkedList<Thing>());
		locationsToThings.get(location).add(thing);
	}
}
