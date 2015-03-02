package org.nusco.narjillos.shared.things;

import org.nusco.narjillos.shared.physics.Segment;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.utilities.Configuration;

public class FoodPiece implements Thing {

	public Vector position;
	private final Energy energy = new LifeFormEnergy(Configuration.ENERGY_PER_FOOD_ITEM, Double.MAX_VALUE);
	private Thing eater;

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
		return "food_piece";
	}

	@Override
	public Energy getEnergy() {
		return energy;
	}

	@Override
	public double getRadius() {
		return Configuration.FOOD_RADIUS;
	}

	public Thing getEater() {
		return eater;
	}
	
	public void setEater(Thing eater) {
		this.eater = eater;
	}
}
