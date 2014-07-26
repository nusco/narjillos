package org.nusco.narjillos.creature.body;

import java.util.LinkedList;
import java.util.List;

import org.nusco.narjillos.creature.body.pns.DelayNerve;
import org.nusco.narjillos.creature.body.pns.Nerve;
import org.nusco.narjillos.shared.physics.Segment;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.utilities.ColorByte;

/**
 * Connects Organs in a tree that ultimately becomes a body
 */
public abstract class BodyPart extends Organ {

	private final Nerve nerve;
	private final Organ parent;
	private final List<BodyPart> children = new LinkedList<>();

	private double angleToParent = 0;

	protected BodyPart(int length, int thickness, ColorByte color, Organ parent, Nerve nerve) {
		super(length, thickness, color);
		this.nerve = nerve;
		this.parent = parent;
	}

	protected double getAngleToParent() {
		return angleToParent;
	}

	protected final void setAngleToParent(double angleToParent) {
		this.angleToParent = angleToParent;
		resetAllCaches();
	}

	protected Vector calculateStartPoint() {
		return getParent().getEndPoint();
	}

	protected abstract double calculateAbsoluteAngle();

	protected Vector calculateMainAxis() {
		return getParent().calculateMainAxis();
	}

	protected final Organ getParent() {
		return parent;
	}

	public List<BodyPart> getChildren() {
		return children;
	}

	// TODO: break down and push down the stuff that doesn't send the signal to children?
	public void tick(Vector inputSignal, ForceField forceField) {
		if (isAtrophic()) {
			// FIXME: what happens when the head is atrophic?
			// optimization
			resetAllCaches();
			Vector outputSignal = getNerve().tick(inputSignal);
			tickChildren(outputSignal, forceField);
		}
		
		Segment beforeMovement = getSegment();
		Vector outputSignal = getNerve().tick(inputSignal);
		
		double updatedAngle = calculateUpdatedAngle(outputSignal);
		setAngleToParent(updatedAngle);
		
		// TODO: should happen in setAngleToParent already
		resetAllCaches();
		
		forceField.record(beforeMovement, this);
		tickChildren(outputSignal, forceField);
	}


	protected abstract double calculateUpdatedAngle(Vector signal);

	protected void tickChildren(Vector signal, ForceField forceField) {
		for (BodyPart child : getChildren())
			child.tick(signal, forceField);
	}

	Nerve getNerve() {
		return nerve;
	}

	public BodyPart sproutOrgan(int length, int thickness, ColorByte hue, int delay, int angleToParentAtRest) {
		return addChild(new BodySegment(length, thickness, hue, new DelayNerve(delay), angleToParentAtRest, this));
	}

	// FIXME: remove and consider pushing down the sproutOrgan group of methods
	BodyPart sproutOrgan(Nerve nerve) {
		return addChild(new BodySegment(nerve));
	}

	private BodyPart addChild(BodyPart child) {
		children.add(child);
		return child;
	}
}