package org.nusco.swimmers.creature.body;

import java.util.LinkedList;
import java.util.List;

import org.nusco.swimmers.creature.body.pns.Nerve;
import org.nusco.swimmers.shared.physics.Segment;
import org.nusco.swimmers.shared.physics.Vector;

public abstract class Organ {

	private final int length;
	private final int thickness;
	private final int hue;

	private final Nerve nerve;
	private final Organ parent;
	private final List<Organ> children = new LinkedList<>();

	private double angleToParent = 0;

	private MovementListener movementListener = MovementListener.NULL;

	// Cached fields (for performance).
	// This optimization makes the entire class much more complicated - but
	// the performance gains are huge.
	// Most of these fields are calculated with recursive calls that traverse
	// the entire body, and their values are accessed many, many times.
	private Vector cachedStartPoint = null;
	private Vector cachedEndPoint = null;
	private Double cachedAbsoluteAngle = null;
	private Vector cachedMainAxis = null;
	private Vector cachedVector = null;
	private Integer cachedColor = null;

	protected Organ(int length, int thickness, int rgb, Nerve nerve, Organ parent) {
		this.length = length;
		this.thickness = thickness;
		this.hue = rgb;
		this.nerve = nerve;
		this.parent = parent;
	}

	public int getLength() {
		return length;
	}

	public int getThickness() {
		return thickness;
	}

	int getHue() {
		return hue;
	}

	protected final double getAngleToParent() {
		return angleToParent;
	}

	protected final void setAngleToParent(double angleToParent) {
		this.angleToParent = angleToParent;
		resetAllCaches();
	}

	protected void resetAllCaches() {
		cachedAbsoluteAngle = null;
		cachedStartPoint = null;
		cachedEndPoint = null;
		cachedMainAxis = null;
		cachedVector = null;
		cachedColor = null;
	}

	public final double getAbsoluteAngle() {
		if (cachedAbsoluteAngle == null)
			cachedAbsoluteAngle = calculateAbsoluteAngle();
		return cachedAbsoluteAngle;
	}

	protected Vector calculateStartPoint() {
		return getParent().getEndPoint();
	}

	public final Vector getStartPoint() {
		if (cachedStartPoint == null)
			cachedStartPoint = calculateStartPoint();
		return cachedStartPoint;
	}

	public final Vector getEndPoint() {
		if (cachedEndPoint == null)
			cachedEndPoint = getStartPoint().plus(Vector.polar(getAbsoluteAngle(), length));
		return cachedEndPoint;
	}

	public final Vector getVector() {
		if (cachedVector == null)
			cachedVector = Vector.polar(getAbsoluteAngle(), getLength());
		return cachedVector;
	}

	protected final Vector getMainAxis() {
		if (cachedMainAxis == null)
			cachedMainAxis = calculateMainAxis();
		return cachedMainAxis;
	}

	public final int getColor() {
		if (cachedColor == null)
			cachedColor = calculateColor();
		return cachedColor;
	}

	protected abstract double calculateAbsoluteAngle();

	protected Vector calculateMainAxis() {
		return getParent().calculateMainAxis();
	}

	protected abstract int calculateColor();

	protected final Organ getParent() {
		return parent;
	}

	public List<Organ> getChildren() {
		return children;
	}

	public Vector tick(Vector inputSignal) {
		Segment beforeMovement = getSegment();

		Vector outputSignal = getNerve().tick(inputSignal);

		move(outputSignal);
		resetAllCaches();

		notifyMovementListener(beforeMovement, this);

		tickChildren(outputSignal);

		return outputSignal;
	}

	private Segment getSegment() {
		return new Segment(getStartPoint(), getVector());
	}

	protected abstract void move(Vector signal);

	private void notifyMovementListener(Segment beforeMovement, Organ organ) {
		movementListener.moveEvent(beforeMovement, organ);
	}

	protected void tickChildren(Vector signal) {
		for (Organ child : getChildren())
			child.tick(signal);
	}

	public Nerve getNerve() {
		return nerve;
	}

	public double getMass() {
		return getLength() * getThickness();
	}

	protected void setMovementListener(MovementListener listener) {
		movementListener = listener;
		for (Organ child : getChildren())
			child.setMovementListener(listener);
	}

	@Override
	public int hashCode() {
		return hue ^ length ^ thickness;
	}

	@Override
	public boolean equals(Object obj) {
		Organ other = (Organ) obj;
		return hue == other.hue && length == other.length && thickness == other.thickness;
	}

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