package org.nusco.narjillos.creature.body.physics;

import java.util.LinkedList;
import java.util.List;

import org.nusco.narjillos.core.physics.Segment;
import org.nusco.narjillos.core.physics.Vector;
import org.nusco.narjillos.core.physics.ZeroVectorException;
import org.nusco.narjillos.core.utilities.Configuration;

/**
 * The physics of translations.
 * 
 * Here are the formulas it uses. See RotationsPhysicsEngine for details.
 * 
 * Linear Momentum (for the body segments):
 * linear_momentum = mass * linear_velocity (in [pixelgrams points / tick])
 * 
 * Linear Momentum (for the whole body):
 * total_linear_velocity = total_linear_momentum / mass (in [points / tick])
 * 
 * translation_energy = mass * linear_velocity^2 / 2;
 */
public class TranslationsPhysicsEngine {

	private final double bodyMass;
	private final List<Vector> linearMomenta = new LinkedList<>();
	private double translationEnergy = 0;
	
	public TranslationsPhysicsEngine(double bodyMass) {
		this.bodyMass = bodyMass;
	}

	public void registerMovement(Segment initialPositionInSpace, Segment finalPositionInSpace, double mass) {
		Vector linearVelocity = finalPositionInSpace.getDistanceFrom(initialPositionInSpace);
		Vector linearMomentum = linearVelocity.by(mass);
		linearMomenta.add(linearMomentum);
		translationEnergy += calculateTranslationEnergy(mass, linearVelocity);
	}

	public Vector getTranslation() {
		Vector result = getTotalLinearMomentum().by(-1.0 / bodyMass);
		double length = result.getLength();

		if (length == 0)
			return result;
		
		try {
			return Vector.polar(result.getAngle(), Viscosity.limit(length));
		} catch (ZeroVectorException e) {
			throw new RuntimeException(e); // should never happen
		}
	}

	public double getEnergy() {
		return translationEnergy * Configuration.PHYSICS_ENERGY_EXPENSE_PER_JOULE / 1_000_000_000L;
	}

	private double calculateTranslationEnergy(double mass, Vector linearVelocity) {
		double linearVelocityLength = linearVelocity.getLength();
		return mass * linearVelocityLength * linearVelocityLength / 2;
	}

	private Vector getTotalLinearMomentum() {
		Vector result = Vector.ZERO;
		for (Vector linearMomentum : linearMomenta)
			result = result.plus(linearMomentum);
		return result;
	}
}
