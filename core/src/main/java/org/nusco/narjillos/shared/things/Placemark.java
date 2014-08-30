package org.nusco.narjillos.shared.things;

import org.nusco.narjillos.shared.physics.Segment;
import org.nusco.narjillos.shared.physics.Vector;

public class Placemark implements Thing {

	private final Vector position;
	
	public Placemark(Vector position) {
		this.position = position;
	}
	
	@Override
	public Segment tick() {
		return new Segment(position, Vector.ZERO);
	}

	@Override
	public Vector getPosition() {
		return position;
	}

	@Override
	public Energy getEnergy() {
		return null;
	}

	@Override
	public String getLabel() {
		return "placemark";
	}
}
