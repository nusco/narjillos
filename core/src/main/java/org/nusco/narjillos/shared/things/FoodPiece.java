package org.nusco.narjillos.shared.things;

import org.nusco.narjillos.shared.physics.Segment;
import org.nusco.narjillos.shared.physics.Vector;

public class FoodPiece implements Thing {

	private static final double ENERGY = 30_000;
	
	public Vector position;

	public void setPosition(Vector position) {
		this.position = position;
	}

	@Override
	public Vector getPosition() {
		return position;
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
		return new Energy(ENERGY, Double.MAX_VALUE);
	}
}
