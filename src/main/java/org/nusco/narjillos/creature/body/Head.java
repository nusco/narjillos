package org.nusco.narjillos.creature.body;

import org.nusco.narjillos.core.physics.Vector;
import org.nusco.narjillos.core.utilities.Configuration;
import org.nusco.narjillos.creature.body.pns.WaveNerve;
import org.nusco.narjillos.ecosystem.chemistry.Element;

/**
 * The head of a creature, and the root of a tree of Organs.
 */
public class Head extends MovingOrgan {

	private final double metabolicRate;
	private final Element consumedElement;
	private final double energyToChildren;
	private final int eggVelocity;
	private final int eggInterval;

	private Vector startPoint = Vector.ZERO;
	
	public Head(int adultLength, int adultThickness, int red, int green, int blue, double metabolicRate, Element consumedElement, double energyToChildren, int eggVelocity, int eggInterval) {
		super(adultLength, adultThickness, new Fiber(red, green, blue), null, new WaveNerve(Configuration.CREATURE_BASE_WAVE_FREQUENCY * metabolicRate), 0);
		this.consumedElement = consumedElement;
		this.metabolicRate = metabolicRate;
		this.energyToChildren = energyToChildren;
		this.eggVelocity = eggVelocity;
		this.eggInterval = eggInterval;
	}

	public void tick(double angleToTarget) {
		tick(angleToTarget, 0, 1);
	}

	@Override
	public double getMetabolicRate() {
		return metabolicRate;
	}

	public Element getConsumedElement() {
		return consumedElement;
	}

	public double getEnergyToChildren() {
		return energyToChildren;
	}

	public int getEggVelocity() {
		return eggVelocity;
	}

	public int getEggInterval() {
		return eggInterval;
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

	double getBrainWaveAngle() {
		return ((WaveNerve) getNerve()).getAngle();
	}
}