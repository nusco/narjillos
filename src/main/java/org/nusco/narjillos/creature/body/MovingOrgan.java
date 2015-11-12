package org.nusco.narjillos.creature.body;

import org.nusco.narjillos.core.geometry.Angle;
import org.nusco.narjillos.core.geometry.Vector;
import org.nusco.narjillos.creature.body.pns.Nerve;

/**
 * Enahnces organs with the notion of time , embodied by the tick() method. This
 * means that the organ moves (see calculateNewAngleToParent()).
 */
public abstract class MovingOrgan extends ConnectedOrgan {

	private double angleToParent = 0;

	protected MovingOrgan(int adultLength, int adultThickness, Fiber fiber, ConnectedOrgan parent, Nerve nerve, int angleToParentAtRest) {
		super(adultLength, adultThickness, fiber, parent, nerve);
		setAngleToParent(angleToParentAtRest);
	}

	public void tick(double angleToTarget, double inputSignal, int level) {
		// Organs towards the head grow slower, organs towards the tail grow
		// faster. This gives juveline narjillos a nice "infant" shape.
		growBy(level);

		double processedPercentOfAmplitude = getNerve().tick(inputSignal);
		setAngleToParent(calculateNewAngleToParent(processedPercentOfAmplitude, angleToTarget));

		updateGeometry();

		for (ConnectedOrgan child : getChildren())
			((MovingOrgan) child).tick(angleToTarget, processedPercentOfAmplitude, level + 1);
	}

	protected final double getAngleToParent() {
		return angleToParent;
	}

	protected final void setAngleToParent(double newAngleToParent) {
		angleToParent = newAngleToParent;
	}

	protected abstract double calculateNewAngleToParent(double targetAngle, double angleToTarget);

	protected void updateTree() {
		updateGeometry();

		for (ConnectedOrgan child : getChildren())
			((MovingOrgan) child).updateTree();
	}

	public void rotateBy(double rotation) {
		setAngleToParent(Angle.normalize(getAngleToParent() + rotation));
		updateTree();
	}

	protected void translateBy(Vector translation) {
		updatePosition();

		for (ConnectedOrgan child : getChildren())
			((MovingOrgan) child).translateBy(translation);
	}
}