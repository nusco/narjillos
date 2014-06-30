package org.nusco.swimmers.creature.body;

import java.util.LinkedList;
import java.util.List;

import org.nusco.swimmers.creature.body.pns.Nerve;
import org.nusco.swimmers.physics.Segment;
import org.nusco.swimmers.physics.Vector;

public abstract class Organ {

	private final int length;
	private final int thickness;
	private final int color;

	private final Nerve nerve;
	private final Organ parent;

	private double angleToParent = 0;
	private List<Organ> children = new LinkedList<>();

	private MovementListener movementListener = MovementListener.NULL;
	
	protected Organ(int length, int thickness, int rgb, Nerve nerve, Organ parent) {
		this.length = length;
		this.thickness = thickness;
		this.color = rgb;
		this.nerve = nerve;
		this.parent = parent;
	}
	
	public int getLength() {
		return length;
	}

	public int getThickness() {
		return thickness;
	}

	public int getColor() {
		return color;
	}

	protected final double getAngleToParent() {
		return angleToParent;
	}

	protected final void setAngleToParent(double angleToParent) {
		this.angleToParent = angleToParent;
	}

	public abstract double getAbsoluteAngle();

	public Vector getStartPoint() {
		return getParent().getEndPoint();
	}

	public Vector getEndPoint() {
		return getStartPoint().plus(Vector.polar(getAbsoluteAngle(), length));
	}

	protected final Organ getParent() {
		return parent;
	}

	public List<Organ> getChildren() {
		return children;
	}

	public Vector tick(Vector inputSignal) {
		Segment beforeMovement = new Segment(getStartPoint(), getVector());

		Vector outputSignal = getNerve().tick(inputSignal);
		move(outputSignal);
		
		notifyMovementListener(beforeMovement, new Segment(getStartPoint(), getVector()));

		tickChildren(outputSignal);

		return outputSignal;
	}

	protected abstract void move(Vector signal);

	private void notifyMovementListener(Segment beforeMovement, Segment afterMovement) {
		movementListener.moveEvent(beforeMovement, afterMovement);
	}

	private void tickChildren(Vector signal) {
		for(Organ child : getChildren())
			child.tick(signal);
	}

	public Vector getVector() {
		return Vector.polar(getAbsoluteAngle(), getLength());
	}
	
	public Nerve getNerve() {
		return nerve;
	}

	public double getMass() {
		return getLength() * getThickness();
	}

	public void setMovementListener(MovementListener listener) {
		movementListener = listener;
		for(Organ child : getChildren())
			child.setMovementListener(listener);
	}
	
	@Override
	public int hashCode() {
		return length;
	}

	@Override
	public boolean equals(Object obj) {
		Organ other = (Organ) obj;
		return color == other.color && length == other.length && thickness == other.thickness;
	}

	// for debugging
	public Vector peek = Vector.ZERO;

	public Organ sproutOrgan(int length, int thickness, int angleToParentAtRest, int rgb) {
		return addChild(new BodySegment(length, thickness, angleToParentAtRest, rgb, this));
	}

	Organ sproutOrgan(Nerve nerve) {
		return addChild(new BodySegment(nerve));
	}

	public Organ sproutConnectiveTissue() {
		return addChild(new ConnectiveTissue(this));
	}

	protected Organ addChild(Organ child) {
		children.add(child);
		return child;
	}
}