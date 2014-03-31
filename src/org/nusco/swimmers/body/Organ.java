package org.nusco.swimmers.body;

import java.util.LinkedList;
import java.util.List;

import org.nusco.swimmers.neural.Neuron;

public abstract class Organ {

	protected final Organ parent;
	private final Neuron neuron;
	private List<Organ> children = new LinkedList<>();

	public Organ(Neuron neuron, Organ parent) {
		this.neuron = neuron;
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

	public Neuron getNeuron() {
		return neuron;
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
		getNeuron().connectTo(child.getNeuron());
		return child;
	}

	public Organ sproutInvisibleOrgan() {
		Organ child = new NullOrgan(this);
		children.add(child);
		getNeuron().connectTo(child.getNeuron());
		return child;
	}
}