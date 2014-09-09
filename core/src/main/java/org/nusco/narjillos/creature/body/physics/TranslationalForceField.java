package org.nusco.narjillos.creature.body.physics;

import java.util.LinkedList;
import java.util.List;

import org.nusco.narjillos.shared.physics.Segment;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.physics.ZeroVectorException;

/**
 * This class contains the physics of translations.
 * 
 * Here are the formulas it uses. See RotationalForceField for details.
 * 
 * Linear Momentum (for the body segments):
 * linear_momentum = mass * linear_velocity (in [pixelgrams points / tick])
 * 
 * Linear Momentum (for the whole body):
 * total_linear_velocity = total_linear_momentum / mass (in [points / tick])
 * 
 * translation_energy = mass * linear_velocity^2 / 2;
 */
public strictfp class TranslationalForceField implements ForceField {

	private final double bodyMass;
	private final List<Vector> linearMomenta = new LinkedList<>();
	private double translationEnergy = 0;
	
	public TranslationalForceField(double bodyMass) {
		this.bodyMass = bodyMass;
	}

	@Override
	public void registerMovement(Segment initialPositionInSpace, Segment finalPositionInSpace, double mass) {
		Vector linearVelocity = calculateLinearVelocity(initialPositionInSpace, finalPositionInSpace, mass);
		Vector linearMomentum = linearVelocity.by(mass);
		linearMomenta.add(linearMomentum);
		translationEnergy += calculateTranslationEnergy(mass, linearVelocity);
	}

	private Vector calculateLinearVelocity(Segment beforeMovement, Segment afterMovement, double mass) {
		if (beforeMovement.getVector().isZero())
			return Vector.ZERO;

		Vector movement = getMovement(beforeMovement, afterMovement);

		if (movement.isZero())
			return Vector.ZERO;

		try {
			return movement.getNormalComponentOn(afterMovement.getVector());
		} catch (ZeroVectorException e) {
			// should never happen with the previous checks
			return null;
		}
	}

	private Vector getMovement(Segment beforeMovement, Segment afterMovement) {
		Vector startPointMovement = afterMovement.getStartPoint().minus(beforeMovement.getStartPoint());
		Vector endPointMovement = afterMovement.getEndPoint().minus(beforeMovement.getEndPoint());
		Vector movement = startPointMovement.plus(endPointMovement).by(0.5);
		return movement;
	}

	private double calculateTranslationEnergy(double mass, Vector linearVelocity) {
		double linearVelocityLength = linearVelocity.getLength();
		return mass * linearVelocityLength * linearVelocityLength / 2;
	}

	public Vector getTranslation() {
		return getTotalLinearMomentum().by(-1.0 / bodyMass);
	}

	private Vector getTotalLinearMomentum() {
		Vector result = Vector.ZERO;
		for (Vector linearMomentum : linearMomenta)
			result = result.plus(linearMomentum);
		return result;
	}

	@Override
	public double getEnergy() {
		return translationEnergy * ENERGY_SCALE;
	}
}
