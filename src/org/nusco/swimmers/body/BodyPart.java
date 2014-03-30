package org.nusco.swimmers.body;

public class BodyPart extends VisibleOrgan {
	private final Organ parent;

	public BodyPart(int length, int thickness, Organ parent, int relativeAngle, int rgb) {
		super(length, thickness, relativeAngle, rgb);
		this.parent = parent;
	}

	public Organ getParent() {
		return parent.getAsParent();
	}
	
	public Vector getStartPoint() {
		return parent.getEndPoint();
	}

	@Override
	public double getAngle() {
		return normalize(getRelativeAngle() + getParent().getRelativeAngle());
	}

	@Override
	public int hashCode() {
		return 1;
	}

	@Override
	public boolean equals(Object obj) {
		BodyPart other = (BodyPart) obj;
		return parent.equals(other.parent) && super.equals(obj);
	}
}
