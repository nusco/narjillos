package org.nusco.swimmer.body;

import java.util.LinkedList;
import java.util.List;

import org.nusco.swimmer.body.pns.Nerve;
import org.nusco.swimmer.physics.Vector;

public abstract class Organ {

	protected final Organ parent;
	private final Nerve nerve;
	private List<Organ> children = new LinkedList<>();
	protected final int length;
	protected final int thickness;
	protected final int rgb;
	protected final double relativeAngle;

	protected Organ(int length, int thickness, int relativeAngle, int rgb, Nerve nerve, Organ parent) {
		this.length = length;
		this.thickness = thickness;
		this.relativeAngle = relativeAngle;
		this.rgb = rgb;
		this.nerve = nerve;
		this.parent = parent;
	}

	public Nerve getNerve() {
		return nerve;
	}

	public Organ getParent() {
		return parent;
	}

	public List<Organ> getChildren() {
		return children;
	}

	public Organ sproutOrgan(int length, int thickness, int relativeAngle, int rgb) {
		Organ child = new Segment(length, thickness, relativeAngle, rgb, this);
		children.add(child);
		getNerve().connectTo(child.getNerve());
		return child;
	}

	public NullOrgan sproutNullOrgan() {
		NullOrgan child = new NullOrgan(this);
		children.add(child);
		getNerve().connectTo(child.getNerve());
		return child;
	}
	
	public static Organ createHead(int length, int thickness, int rgb) {
		return new Head(length, thickness, rgb);
	}

	public int getLength() {
		return length;
	}

	public int getThickness() {
		return thickness;
	}

	public abstract Vector getStartPoint();

	public Vector getEndPoint() {
		return getStartPoint().plus(Vector.polar(getAngle(), length));
	}

	public abstract double getAngle();

	public Organ getAsParent() {
		return this;
	}

	public double getRelativeAngle() {
		return relativeAngle;
	}

	public int getRGB() {
		return rgb;
	}

	public void tick() {
	}

	@Override
	public String toString() {
		return "[" + length + "," + thickness + "," + getAngle() + "]";
	}
}