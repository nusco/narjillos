package org.nusco.narjillos.creature.body.physics;

import java.util.LinkedList;
import java.util.List;

import org.nusco.narjillos.shared.physics.Segment;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.physics.ZeroVectorException;
import org.nusco.narjillos.shared.utilities.VisualDebugger;

/**
 * This class contains most of the physics engine.
 * 
 * Here are a few formulas. To understand them, consider that we don't use
 * inertia - so we can safely replace velocities with positions in space. (In
 * other words, we assume that after each movement of each body, the body's
 * velocity drops down to zero).
 * 
 * Linear Momentum (for the body segments):
 * linear_momentum = mass * linear_velocity (in [pixelgrams points / tick])
 * 
 * Linear Momentum (for the whole body):
 * total_linear_velocity = total_linear_momentum / mass (in [points / tick])
 * 
 * Angular Momentum (for the body segments, approximated as thin rods):
 * angular_momentum = moment_of_inertia * angular_velocity
 * moment_of_inertia_around_far_end = mass * length^2 * 16 / 48
 * 
 * Also, by the parallel axis theorem:
 * moment_of_inertia_around_body_center = moment_of_inertia_around_far_end +
 *                                        mass * distance_between_far_end_and_body_center^2 
 *                                       = mass * (length^2 * 16 / 48 + distance_between_far_end_and_body_center^2)
 * 
 * So the angular_momentum is the same as the moment_of_inertia_around_body_center,
 * multiplied by the angular_velocity.
 * 
 * Angular Momentum (for the whole body, approximated as a thin disk):
 * total_angular_velocity = total_angular_momentum / total_moment_of_inertia
 *                        = total_angular_momentum / (mass * radius^2 / 4)
 *                  
 * Energies:
 * 
 * translation_energy = mass * linear_velocity^2 / 2;
 * rotation_energy = moment_of_inertia * angular_velocity^2 / 2;
 */
public class ForceField {

	private static final double ENERGY_SCALE = 1.0 / 100_000_000_000L;
	private static final double VISCOSITY = 1; //.01;

	private final double bodyMass;
	private final double bodyRadius;
	private final Vector centerOfMass;
	private final List<Vector> linearMomenta = new LinkedList<>();
	private final List<Double> angularMomenta = new LinkedList<>();
	private double energySpent = 0;
	
	public ForceField(double bodyMass, double bodyRadius, Vector centerOfMass) {
		this.bodyMass = bodyMass;
		this.bodyRadius = bodyRadius;
		this.centerOfMass = centerOfMass;
	}

	public void registerMovement(Segment initialPositionInSpace, Segment finalPositionInSpace, double mass) {
		Vector linearVelocity = calculateLinearVelocity(initialPositionInSpace, finalPositionInSpace, mass);
		Vector linearMomentum = linearVelocity.by(mass);
		linearMomenta.add(linearMomentum);
		energySpent += calculateTranslationEnergy(mass, linearVelocity);

		double angularVelocity = calculateAngularVelocity(initialPositionInSpace, finalPositionInSpace);
		double momentOfInertia = calculateMomentOfInertia(finalPositionInSpace, mass);
		double angularMomentum = momentOfInertia * angularVelocity;
		angularMomenta.add(angularMomentum);
		energySpent += calculateRotationEnergy(momentOfInertia, angularVelocity);
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

	private double calculateAngularVelocity(Segment initialPositionInSpace, Segment finalPositionInSpace) {
		try {
			return finalPositionInSpace.getVector().getAngleWith(initialPositionInSpace.getVector());
		} catch (ZeroVectorException e) {
			return 0;
		}
	}

 	private double calculateMomentOfInertia(Segment positionInSpace, double mass) {
		double length = positionInSpace.getVector().getLength();
		double distance = positionInSpace.getStartPoint().minus(centerOfMass).getLength();
		return mass * length * length * 16 / 48 + distance * distance;
	}

	private double calculateRotationEnergy(double momentOfInertia, double angularVelocity) {
		return momentOfInertia * angularVelocity * angularVelocity / 2;
	}

	public Vector getTranslation() {
		return getTotalLinearMomentum().by(-1.0 / bodyMass);
	}

	private Vector getTotalLinearMomentum() {
		Vector result = Vector.ZERO;
		for (Vector linearMomentum : linearMomenta)
			result = result.plus(linearMomentum);
		try {
			result = result.normalize(Math.pow(result.getLength(), VISCOSITY));
		} catch (ZeroVectorException e) {
			return Vector.ZERO;
		}
		return result;
	}

	public double getRotation() {
		return -getTotalAngularMomentum() / (bodyMass * bodyRadius * bodyRadius / 4);
	}

	private double getTotalAngularMomentum() {
		double result = 0;
		for (double angularMomentum : angularMomenta)
			result += angularMomentum;
		return result;
	}

	public double getTotalEnergySpent() {
		return energySpent * ENERGY_SCALE;
	}
}
