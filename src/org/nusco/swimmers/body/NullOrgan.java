package org.nusco.swimmers.body;

import java.util.LinkedList;
import java.util.List;

public class NullOrgan implements Organ {

	private final Organ parent;
	private List<Organ> children = new LinkedList<>();

	public NullOrgan(Organ parent) {
		this.parent = parent;
	}

	@Override
	public double getRelativeAngle() {
		return getParent().getRelativeAngle();
	}

	@Override
	public Vector getStartPoint() {
		return getParent().getEndPoint();
	}

	@Override
	public Vector getEndPoint() {
		return getParent().getEndPoint();
	}

	@Override
	public int getRGB() {
		return 0;
	}

	@Override
	public Organ getAsParent() {
		return getParent();
	}

	@Override
	public Organ getParent() {
		return parent;
	}

	@Override
	public List<Organ> getChildren() {
		return children;
	}

	@Override
	public VisibleOrgan sproutVisibleOrgan(int length, int thickness, int initialRelativeAngle, int rgb) {
		VisibleOrgan child = new BodyPart(length, thickness, this, initialRelativeAngle, rgb);
		children.add(child);
		return child;
	}

	@Override
	public Organ sproutInvisibleOrgan() {
		Organ child = new NullOrgan(this);
		children.add(child);
		return child;
	}

	public boolean isVisible() {
		return false;
	}

	@Override
	public int getLength() {
		return 0;
	}

	@Override
	public int getThickness() {
		return 0;
	}

	@Override
	public double getAngle() {
		return 0;
	}
	
	@Override
	public String toString() {
		return "<null organ>";
	}
}
