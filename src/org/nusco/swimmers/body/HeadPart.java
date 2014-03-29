package org.nusco.swimmers.body;

public class HeadPart extends Part {
	private Vector startPoint = Vector.ZERO;

	public HeadPart(int length, int thickness) {
		super(length, thickness, 0);
	}

	@Override
	public Vector getStartPoint() {
		return startPoint;
	}

	@Override
	public int getAngle() {
		return normalize(getRelativeAngle());
	}

	public void placeAt(Vector point) {
		this.startPoint = point;
	}
}
