package org.nusco.narjillos.creature.body;

import org.nusco.narjillos.core.chemistry.Element;
import org.nusco.narjillos.core.geometry.Vector;
import org.nusco.narjillos.core.utilities.Configuration;
import org.nusco.narjillos.creature.body.pns.WaveNerve;

/**
 * The head of a creature, and the root of a tree of Organs.
 */
public class Head extends MovingOrgan {

	private final double metabolicRate;
	private double waveBeatRatio;
	private final Element byproduct;
	private final double energyToChildren;
	private final int eggVelocity;
	private final int eggInterval;

	private Vector startPoint = Vector.ZERO;
	
	public Head(HeadParameters parameters) {
		super(parameters.getAdultLength(), parameters.getAdultThickness(), new Fiber(parameters.getRed(), parameters.getGreen(), parameters.getBlue()), null, new WaveNerve(Configuration.CREATURE_BASE_WAVE_FREQUENCY * parameters.getMetabolicRate()), 0);
		this.metabolicRate = parameters.getMetabolicRate();
		this.waveBeatRatio = parameters.getWaveBeatRatio();
		this.byproduct = parameters.getByproduct();
		this.energyToChildren = parameters.getEnergyToChildren();
		this.eggVelocity = parameters.getEggVelocity();
		this.eggInterval = parameters.getEggInterval();
	}

	public void tick(double angleToTarget) {
		tick(angleToTarget, getWaveBeatRatio(), 1);
	}

	@Override
	public double getMetabolicRate() {
		return metabolicRate;
	}

	public double getWaveBeatRatio() {
		return waveBeatRatio;
	}

	public Element getByproduct() {
		return byproduct;
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