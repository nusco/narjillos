package org.nusco.narjillos.creature.body;

import org.nusco.narjillos.core.geometry.Angle;
import org.nusco.narjillos.core.geometry.Vector;
import org.nusco.narjillos.creature.body.pns.Nerve;

/**
 * Enhances organs with the notion of time, embodied by the tick() method. This
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
		// faster. This gives juvenile narjillos a nice "infant" shape.
		growBy(level);

		double processedPercentOfAmplitude = getNerve().tick(inputSignal);
		setAngleToParent(calculateNewAngleToParent(processedPercentOfAmplitude, angleToTarget));

		update();

		for (ConnectedOrgan child : getChildren())
			((MovingOrgan) child).tick(angleToTarget, processedPercentOfAmplitude, level + 1);
	}

	public void rotateBy(double rotation) {
		setAngleToParent(Angle.normalize(getAngleToParent() + rotation));
		updateTree();
	}

	final double getAngleToParent() {
		return angleToParent;
	}

	final void setAngleToParent(double newAngleToParent) {
		angleToParent = newAngleToParent;
	}

	void updateTree() {
		update();

		for (ConnectedOrgan child : getChildren())
			((MovingOrgan) child).updateTree();
	}

	void translateBy(Vector translation) {
		// TODO: is this early cache update really how it's supposed to work?
		updateAfterTranslation();

		for (ConnectedOrgan child : getChildren())
			((MovingOrgan) child).translateBy(translation);
	}

	protected abstract double calculateNewAngleToParent(double targetAngle, double angleToTarget);
}
