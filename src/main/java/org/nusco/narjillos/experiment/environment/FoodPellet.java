package org.nusco.narjillos.experiment.environment;

import org.nusco.narjillos.core.geometry.BoundingBox;
import org.nusco.narjillos.core.geometry.Segment;
import org.nusco.narjillos.core.geometry.Vector;
import org.nusco.narjillos.core.things.Energy;
import org.nusco.narjillos.core.things.LifeFormEnergy;
import org.nusco.narjillos.core.things.Thing;
import org.nusco.narjillos.core.configuration.Configuration;
import org.nusco.narjillos.creature.Narjillo;

public class FoodPellet implements Thing {

	public static final String LABEL = "food_pellet";

	private final Vector position;

	private final BoundingBox boundingBox;

	private final Energy energy = new LifeFormEnergy(Configuration.FOOD_ENERGY, Double.MAX_VALUE);

	private Thing interactor;

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
		return FoodPellet.LABEL;
	}

	@Override
	public Energy getEnergy() {
		return energy;
	}

	@Override
	public boolean isDead() {
		return getEnergy().isZero();
	}

	@Override
	public double getRadius() {
		return Configuration.FOOD_RADIUS;
	}

	@Override
	public Thing getInteractor() {
		return interactor;
	}

	@Override
	public BoundingBox getBoundingBox() {
		return boundingBox;
	}

	public void getEaten(Thing feeder) {
		if (getEnergy().isZero())
			return;

		feeder.getEnergy().absorb(getEnergy());
		this.interactor = feeder;
	}
}
