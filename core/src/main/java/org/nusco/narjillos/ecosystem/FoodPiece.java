package org.nusco.narjillos.ecosystem;

import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.things.Energy;
import org.nusco.narjillos.shared.things.Thing;

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
	public void tick() {
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
