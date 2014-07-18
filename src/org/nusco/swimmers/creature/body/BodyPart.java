package org.nusco.swimmers.creature.body;

import org.nusco.swimmers.creature.body.pns.Nerve;

public class BodyPart {

	protected final int length;
	protected final int thickness;
	protected final int hue;

	public BodyPart(int length, int thickness, int hue) {
		this.length = length;
		this.thickness = thickness;
		this.hue = hue;
	}

	public int getLength() {
		return length;
	}

	public int getThickness() {
		return thickness;
	}

	protected int getHue() {
		return hue;
	}

	public double getMass() {
		return getLength() * getThickness();
	}

}
