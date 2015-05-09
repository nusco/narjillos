package org.nusco.narjillos.application.utilities;

import java.util.LinkedList;
import java.util.List;

import org.nusco.narjillos.core.physics.Vector;
import org.nusco.narjillos.core.things.Thing;
import org.nusco.narjillos.creature.Egg;
import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.ecosystem.Culture;

/**
 * Provides methods to find {@link Thing}s in a {@link Culture}.
 */
public class Locator {

	private static final double DEFAULT_RADIUS = 100D;

	private final Culture culture;

	public Locator(Culture culture) {
		this.culture = culture;
	}

	/**
	 * Finds a {@link Thing} in a {@link Culture} using a custom minimum radius for the Thing.
	 * <p>
	 * The minimum radius is used to artificially <i>enlarge</i> the things being searched
	 * allowing better control on the precision of the search process.
	 *
	 * @param position where to find a Thing.
	 * @param thingMinRadius while searching, the radius of things is boosted to this value if smaller.
	 * @return the first found Thing or null if nothing is present at the given position.
	 */
	public Thing findThingAt(Vector position, double thingMinRadius) {
		Thing result = findWithLabel(position, thingMinRadius, "food");

		if (result != null)
			return result;

		result = findWithLabel(position, thingMinRadius, "egg");

		if (result != null)
			return result;

		return findNarjilloAt(position, thingMinRadius);
	}

	/**
	 * Finds a {@link Thing} in a {@link Culture} using the default minimum radius of 100.
	 *
	 * @param position where to find a Thing.
	 * @return the found Thing or null if nothing is present at the given position.
	 */
	public Thing findThingAt(Vector position) {
		return findThingAt(position, DEFAULT_RADIUS);
	}

	/**
	 * Finds a {@link Narjillo} in a {@link Culture} using a custom minimum radius for the Thing.
	 * <p>
	 * The minimum radius is used to artificially <i>enlarge</i> the things being searched
	 * allowing better control on the precision of the search process.
	 *
	 * @param position where to find a Thing.
	 * @param thingMinRadius while searching, the radius of things is boosted to this value if smaller.
	 * @return the found Narjillo or null if nothing is present at the given position.
	 */
	public Thing findNarjilloAt(Vector position, double thingMinRadius) {
		return findWithLabel(position, thingMinRadius, "narjillo");
	}

	/**
	 * Finds a {@link Narjillo} in a {@link Culture} using the default minimum radius of 100.
	 *
	 * @param position where to find a Thing.
	 * @return the found Narjillo or null if nothing is present at the given position.
	 */
	public Thing findNarjilloAt(Vector position) {
		return findWithLabel(position, DEFAULT_RADIUS, "narjillo");
	}

	/**
	 * Returns a random {@link Narjillo} or {@link Egg} in the {@link Culture}.
	 * @return the found thing.
	 */
	public Thing findRandomLivingThing() {
		List<Thing> allThings = new LinkedList<>();
		allThings.addAll(culture.getThings("narjillo"));
		allThings.addAll(culture.getThings("egg"));
		return allThings.get((int)(Math.random() * allThings.size()));
	}

	private Thing findWithLabel(Vector position, double thingMinRadius, String label) {
		Thing result = null;
		double minDistance = Double.MAX_VALUE;

		for (Thing thing : culture.getThings(label)) {
			double distance = thing.getCenter().minus(position).getLength();
			double radius = Math.max(thing.getRadius(), thingMinRadius);

			if (distance < radius && distance < minDistance) {
				minDistance = distance;
				result = thing;
			}
		}

		return result;
	}
}
