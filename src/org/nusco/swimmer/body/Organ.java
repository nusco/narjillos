package org.nusco.swimmer.body;

import java.util.LinkedList;
import java.util.List;

import org.nusco.swimmer.body.pns.Nerve;
import org.nusco.swimmer.physics.Vector;

public abstract class Organ {

	protected final Organ parent;
	private final Nerve nerve;
	private List<Organ> children = new LinkedList<>();

	public Organ(Nerve nerve, Organ parent) {
		this.nerve = nerve;
		this.parent = parent;
	}

	public abstract double getAngle();

	public abstract double getRelativeAngle();

	public abstract Vector getEndPoint();

	public abstract int getRGB();

	public abstract Organ getAsParent();

	public abstract boolean isVisible();

	public abstract String toString();

	public abstract int getLength();

	public abstract int getThickness();

	public abstract Vector getStartPoint();

	public Nerve getNerve() {
		return nerve;
	}

	public Organ getParent() {
		return parent;
	}

	public List<Organ> getChildren() {
		return children;
	}

	public VisibleOrgan sproutVisibleOrgan(int length, int thickness, int initialRelativeAngle, int rgb) {
		VisibleOrgan child = new BodyPart(length, thickness, initialRelativeAngle, rgb, this);
		children.add(child);
		getNerve().connectTo(child.getNerve());
		return child;
	}

	public Organ sproutInvisibleOrgan() {
		Organ child = new NullOrgan(this);
		children.add(child);
		getNerve().connectTo(child.getNerve());
		return child;
	}
	
	public static Organ createHead(int length, int thickness, int rgb) {
		return new Head(length, thickness, rgb);
	}

	public abstract void tick();
}