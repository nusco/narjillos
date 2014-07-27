package org.nusco.narjillos.creature.body;

import java.util.LinkedList;
import java.util.List;

import org.nusco.narjillos.creature.body.pns.DelayNerve;
import org.nusco.narjillos.creature.body.pns.Nerve;
import org.nusco.narjillos.shared.physics.Segment;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.utilities.ColorByte;

/**
 * Connects Organs in a tree.
 */
public abstract class BodyPart extends Organ {

	private final Nerve nerve;
	private final Organ parent;
	private final List<BodyPart> children = new LinkedList<>();

	private double angleToParent = 0;
	public double forcedBend = 0;
	
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

	protected final Organ getParent() {
		return parent;
	}

	public List<BodyPart> getChildren() {
		return children;
	}

	public final void tick(double signal, ForceField forceField) {
		Segment beforeMovement = getSegment();

		double processedSignal = getNerve().tick(signal);

		double newAngleToParent = calculateAngleToParent(processedSignal, forceField);
		setAngleToParent(newAngleToParent);

		resetForcedBend();

		forceField.record(beforeMovement, this);
		tickChildren(processedSignal, forceField);
	}

	protected abstract double calculateAngleToParent(double targetAngle, ForceField forceField);

	protected void tickChildren(double signal, ForceField forceField) {
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

	protected double getForcedBend() {
		return forcedBend;
	}

	void forceBend(double bendAngle) {
		forcedBend = bendAngle;
	}
	
	private void resetForcedBend() {
		forcedBend = 0;
	}
}