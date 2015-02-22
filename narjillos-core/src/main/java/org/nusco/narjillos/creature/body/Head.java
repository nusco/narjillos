package org.nusco.narjillos.creature.body;

import org.nusco.narjillos.creature.body.pns.WaveNerve;
import org.nusco.narjillos.shared.physics.Vector;

/**
 * The head of a creature, and the root of a tree of Organs.
 */
public class Head extends MovingOrgan {

	private static final double BASE_WAVE_FREQUENCY = 0.01;

	private final double metabolicRate;
	private final double percentEnergyToChildren;

	private Vector startPoint = Vector.ZERO;
	
	public Head(int adultLength, int adultThickness, int red, int green, int blue, double metabolicRate, double percentEnergyToChildren) {
		super(adultLength, adultThickness, new Fiber(red, green, blue), null, new WaveNerve(BASE_WAVE_FREQUENCY * metabolicRate), 0);
		this.percentEnergyToChildren = percentEnergyToChildren;
		this.metabolicRate = metabolicRate;
	}

	public void tick(double angleToTarget) {
		tick(angleToTarget, 0, 1);
	}

	@Override
	public double getMetabolicRate() {
		return metabolicRate;
	}

	public double getPercentEnergyToChildren() {
		return percentEnergyToChildren;
	}

	public void forcePosition(Vector startPoint, double angle) {
		// we already reset the cache in setAngleToParent(), so
		// no need to do it twice here
		this.startPoint  = startPoint;
		setAngleToParent(angle);
		updateTree();
	}

	@Override
	public void translateBy(Vector translation) {
		this.startPoint  = getStartPoint().plus(translation);
		super.translateBy(translation);
	}

	@Override
	protected Vector calculateStartPoint() {
		return startPoint;
	}

	@Override
	protected double calculateAbsoluteAngle() {
		return getAngleToParent();
	}
	
	@Override
	protected double calculateNewAngleToParent(double targetAngle, double angleToTarget) {
		// The head never rotates on its own. It must be
		// explicitly repositioned by its client.
		return getAngleToParent();
	}
}