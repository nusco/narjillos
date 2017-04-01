package org.nusco.narjillos.experiment.environment;

import org.nusco.narjillos.core.geometry.BoundingBox;
import org.nusco.narjillos.core.geometry.Segment;
import org.nusco.narjillos.core.geometry.Vector;
import org.nusco.narjillos.core.things.Energy;
import org.nusco.narjillos.core.things.LifeFormEnergy;
import org.nusco.narjillos.core.things.Thing;
import org.nusco.narjillos.core.configuration.Configuration;

public class FoodPellet implements Thing {

	private final Vector position;

	private final BoundingBox boundingBox;

	private final Energy energy = new LifeFormEnergy(Configuration.FOOD_ENERGY, Double.MAX_VALUE);

	private Thing interactingThing;

	public FoodPellet(Vector position) {
		this.position = position;
		this.boundingBox = BoundingBox.punctiform(position); // TODO
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

	@Override
	public Thing getLastInteractingThing() {
		return interactingThing;
	}

	@Override
	public BoundingBox getBoundingBox() {
		return boundingBox;
	}

	public void setInteractingThing(Thing interactingThing) {
		this.interactingThing = interactingThing;
	}
}
