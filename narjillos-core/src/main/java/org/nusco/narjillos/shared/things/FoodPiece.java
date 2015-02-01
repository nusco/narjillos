package org.nusco.narjillos.shared.things;

import org.nusco.narjillos.shared.physics.Segment;
import org.nusco.narjillos.shared.physics.Vector;

public class FoodPiece implements Thing {

	public static final double RADIUS = 7;
	private static final double ENERGY = 30_000;
	
	public Vector position;
	private final Energy energy = new Energy(ENERGY, Double.MAX_VALUE);
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
		return RADIUS;
	}

	public Thing getEater() {
		return eater;
	}
	
	public void setEater(Thing eater) {
		this.eater = eater;
	}
}
