package org.nusco.swimmers.pond;

import org.nusco.swimmers.shared.physics.Vector;
import org.nusco.swimmers.shared.things.Thing;

public class FoodPiece implements Thing {

	public Vector position;

	@Override
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
}
