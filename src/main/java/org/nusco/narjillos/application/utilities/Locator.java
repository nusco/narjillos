package org.nusco.narjillos.application.utilities;

import java.util.LinkedList;
import java.util.List;

import org.nusco.narjillos.core.geometry.Vector;
import org.nusco.narjillos.core.things.Thing;
import org.nusco.narjillos.creature.Egg;
import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.experiment.environment.Environment;
import org.nusco.narjillos.experiment.environment.FoodPellet;

/**
 * Provides methods to find {@link Thing}s in a {@link Environment}.
 */
public class Locator {

	private static final double DEFAULT_RADIUS = 100D;

	private final Environment environment;

	public Locator(Environment environment) {
		this.environment = environment;
	}

	/**
	 * Finds a {@link Thing} in a {@link Environment} using a custom minimum radius for the Thing.
	 * <p>
	 * The minimum radius is used to artificially <i>enlarge</i> the things being searched
	 * allowing better control on the precision of the search process.
	 *
	 * @param position       where to find a Thing.
	 * @param thingMinRadius while searching, the radius of things is boosted to this value if smaller.
	 * @return the first found Thing or null if nothing is present at the given position.
	 */
	private Thing findThingAt(Vector position, double thingMinRadius) {
		Thing result = findWithLabel(position, thingMinRadius, FoodPellet.LABEL);

		if (result != null)
			return result;

		result = findWithLabel(position, thingMinRadius, Egg.LABEL);

		if (result != null)
			return result;

		return findNarjilloAt(position, thingMinRadius);
	}

	/**
	 * Finds a {@link Thing} in a {@link Environment} using the default minimum radius of 100.
	 *
	 * @param position where to find a Thing.
	 * @return the found Thing or null if nothing is present at the given position.
	 */
	public Thing findThingAt(Vector position) {
		return findThingAt(position, DEFAULT_RADIUS);
	}

	/**
	 * Finds a {@link Narjillo} in a {@link Environment} using a custom minimum radius for the Thing.
	 * <p>
	 * The minimum radius is used to artificially <i>enlarge</i> the things being searched
	 * allowing better control on the precision of the search process.
	 *
	 * @param position       where to find a Thing.
	 * @param thingMinRadius while searching, the radius of things is boosted to this value if smaller.
	 * @return the found Narjillo or null if nothing is present at the given position.
	 */
	public Thing findNarjilloAt(Vector position, double thingMinRadius) {
		return findWithLabel(position, thingMinRadius, Narjillo.LABEL);
	}

	/**
	 * Finds a {@link Narjillo} in a {@link Environment} using the default minimum radius of 100.
	 *
	 * @param position where to find a Thing.
	 * @return the found Narjillo or null if nothing is present at the given position.
	 */
	public Thing findNarjilloAt(Vector position) {
		return findWithLabel(position, DEFAULT_RADIUS, Narjillo.LABEL);
	}

	/**
	 * Returns a random {@link Narjillo} or {@link Egg} in the {@link Environment}.
	 *
	 * @return the found thing.
	 */
	public Thing findRandomLivingThing() {
		List<Thing> allThings = new LinkedList<>();
		allThings.addAll(environment.getThings(Narjillo.LABEL));
		allThings.addAll(environment.getThings(Egg.LABEL));
		return allThings.get((int) (Math.random() * allThings.size()));
	}

	private Thing findWithLabel(Vector position, double thingMinRadius, String label) {
		Thing result = null;
		double minDistance = Double.MAX_VALUE;

		for (Thing thing : environment.getThings(label)) {
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
