package org.nusco.swimmers.creature.body;

import java.util.LinkedList;
import java.util.List;

import org.nusco.swimmers.creature.pns.Nerve;
import org.nusco.swimmers.physics.Vector;

public abstract class Organ {

	protected final int length;
	protected final int thickness;
	protected final double relativeAngle;
	protected final int rgb;

	private Nerve nerve;

	protected final Organ parent;
	private List<Organ> children = new LinkedList<>();
	
	protected Organ(int length, int thickness, int relativeAngle, int rgb, Nerve nerve, Organ parent) {
		this.length = length;
		this.thickness = thickness;
		this.relativeAngle = relativeAngle;
		this.rgb = rgb;
		this.nerve = nerve;
		this.parent = parent;
	}
	
	public int getLength() {
		return length;
	}

	public int getThickness() {
		return thickness;
	}

	public double getRelativeAngle() {
		return relativeAngle;
	}

	public int getRGB() {
		return rgb;
	}

	public abstract Vector getStartPoint();

	public Vector getEndPoint() {
		return getStartPoint().plus(Vector.polar(getAngle(), length));
	}
	
	public abstract double getAngle();

	public Organ getParent() {
		return parent;
	}

	public List<Organ> getChildren() {
		return children;
	}

	public Organ sproutOrgan(int length, int thickness, int relativeAngle, int rgb) {
		return addChild(new Segment(length, thickness, relativeAngle, rgb, this));
	}

	Organ sproutOrgan(Nerve nerve) {
		return addChild(new Segment(nerve));
	}

	public Organ sproutNullOrgan() {
		return addChild(new NullOrgan(this));
	}

	Organ addChild(Organ child) {
		children.add(child);
		return child;
	}

	public Vector tick(Vector inputSignal) {
		Vector outputSignal = getNerve().send(inputSignal);
		for(Organ child : getChildren())
			child.tick(outputSignal);
		peek = outputSignal;
		return outputSignal;
	}

	Vector getVector() {
		return Vector.polar(getAngle(), getLength());
	}
	
	Nerve getNerve() {
		return nerve;
	}

	public double getMass() {
		return getLength() * getThickness();
	}

	// for debugging
	public Vector peek = Vector.ZERO;
}