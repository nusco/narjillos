package org.nusco.swimmers.body;

public class BodyPart extends Part {
	private final Part parent;

	public BodyPart(int length, int thickness, Part parent, int relativeAngle) {
		super(length, thickness, relativeAngle);
		this.parent = parent;
	}

	public Part getParent() {
		return parent;
	}
	
	public Vector getStartPoint() {
		return parent.getEndPoint();
	}

	@Override
	public int getAngle() {
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
