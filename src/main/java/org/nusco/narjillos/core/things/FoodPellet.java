package org.nusco.narjillos.core.things;

import org.nusco.narjillos.core.geometry.Segment;
import org.nusco.narjillos.core.geometry.Vector;
import org.nusco.narjillos.core.utilities.Configuration;

public class FoodPellet implements Thing {

	private Vector position;

	private final Energy energy = new LifeFormEnergy(Configuration.FOOD_ENERGY, Double.MAX_VALUE);

	private Thing interactingThing;

	public void setPosition(Vector position) {
		this.position = position;
	}

	@Override
	public Vector getPosition() {
		return position;
	}

	@Override
	public Vector getCenter() {
		return getPosition();
	}

	@Override
	public Segment tick() {
		return new Segment(position, Vector.ZERO);
	}

	@Override
	public String getLabel() {
		return "food_pellet";
	}

	@Override
	public Energy getEnergy() {
		return energy;
	}

	@Override
	public double getRadius() {
		return Configuration.FOOD_RADIUS;
	}

	public Thing getLastInteractingThing() {
		return interactingThing;
	}

	public void setInteractingThing(Thing interactingThing) {
		this.interactingThing = interactingThing;
	}
}
