package org.nusco.narjillos.creature.body;

import java.util.ArrayList;
import java.util.List;

import org.nusco.narjillos.creature.body.pns.Nerve;
import org.nusco.narjillos.shared.physics.Vector;

/**
 * A body part that is connected to other body parts in a tree to form a body.
 */
public abstract class ConnectedOrgan extends Organ {

	private transient ConnectedOrgan parent;
	private final List<ConnectedOrgan> children = new ArrayList<>();
	protected final Nerve nerve;

	public ConnectedOrgan(int adultLength, int adultThickness, Fiber fiber, ConnectedOrgan parent, Nerve nerve) {
		super(adultLength, adultThickness, fiber);
		setParent(parent);
		this.nerve = nerve;
	}

	public final void setParent(ConnectedOrgan parent) {
		this.parent = parent;
	}

	@Override
	protected Vector calculateStartPoint() {
		return getParent().getEndPoint();
	}

	public final ConnectedOrgan getParent() {
		return parent;
	}

	public List<ConnectedOrgan> getChildren() {
		return children;
	}

	public ConnectedOrgan addChild(ConnectedOrgan child) {
		children.add(child);
		return child;
	}

	protected abstract double getMetabolicRate();

	protected Nerve getNerve() {
		return nerve;
	}
}