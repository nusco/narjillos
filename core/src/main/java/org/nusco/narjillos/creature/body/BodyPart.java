package org.nusco.narjillos.creature.body;

import java.util.LinkedList;
import java.util.List;

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

	protected final double getAngleToParent() {
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

	public Vector tick(Vector inputSignal, MovementRecorder movementRecorder) {
		if (isAtrophic()) {
			// optimization
			resetAllCaches();
			Vector outputSignal = getNerve().tick(inputSignal);
			tickChildren(outputSignal, movementRecorder);
			return outputSignal;
		}
		
		Segment beforeMovement = getSegment();
		Vector outputSignal = getNerve().tick(inputSignal);
		move(outputSignal);
		resetAllCaches();
		movementRecorder.record(beforeMovement, this);
		tickChildren(outputSignal, movementRecorder);
		return outputSignal;
	}

	protected abstract void move(Vector signal);

	protected void tickChildren(Vector signal, MovementRecorder movementListener) {
		for (BodyPart child : getChildren())
			child.tick(signal, movementListener);
	}

	Nerve getNerve() {
		return nerve;
	}

	public BodyPart sproutOrgan(int length, int thickness, int angleToParentAtRest, ColorByte hue, int delay) {
		return addChild(new BodySegment(length, thickness, angleToParentAtRest, hue, this, delay));
	}

	BodyPart sproutOrgan(Nerve nerve) {
		return addChild(new BodySegment(nerve));
	}

	protected BodyPart addChild(BodyPart child) {
		children.add(child);
		return child;
	}
}