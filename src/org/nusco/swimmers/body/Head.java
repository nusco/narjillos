package org.nusco.swimmers.body;

public class Head extends VisibleOrgan {
	
	private Vector startPoint = Vector.ZERO;

	public Head(int length, int thickness, int rgb) {
		super(length, thickness, 0, rgb);
	}

	@Override
	public Vector getStartPoint() {
		return startPoint;
	}

	@Override
	public double getAngle() {
		return normalize(getRelativeAngle());
	}

	@Override
	public Organ getParent() {
		return null;
	}

	public void placeAt(Vector point) {
		this.startPoint = point;
	}
	
	@Override
	public int hashCode() {
		return 1;
	}

	@Override
	public boolean equals(Object obj) {
		Head other = (Head) obj;
		return startPoint == other.startPoint && super.equals(obj);
	}
}
