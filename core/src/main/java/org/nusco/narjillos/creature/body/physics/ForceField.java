package org.nusco.narjillos.creature.body.physics;

import java.util.LinkedList;
import java.util.List;

import org.nusco.narjillos.shared.physics.Segment;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.physics.ZeroVectorException;

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
 * linear_velocity = linear_momentum / mass (in [points / tick])
 * 
 * translation_energy = mass * linear_velocity^2 / 2;
 * 
 * Angular Momentum (for the body segments, approximated as thin rods):
 * angular_momentum = moment_of_inertia * angular_velocity
 * moment_of_inertia_around_far_end = mass * length^2 * 16 / 48
 * 
 * Also, by the parallel axis theorem:
 * moment_of_inertia_around_center_of_mass = moment_of_inertia_around_far_end +
 *                                           mass * distance_between_far_end_and_center_of_mass^2 
 *                                         = mass * (length^2 * 16 / 48 + distance_between_far_end_and_center_of_mass^2)
 * 
 * So the angular_momentum is the same as the moment_of_inertia_around_center_of_mass,
 * multiplied by the angular_velocity.
 *
 * rotation_energy = moment_of_inertia * angular_velocity^2 / 2;
 * 
 * Angular Momentum (for the whole body, approximated as a thin disk):
 * angular_velocity = angular_momentum / moment_of_inertia
 *                  = angular_momentum / (mass * radius^2 / 4)
 */
public class ForceField {

	private static final double PROPULSION_SCALE = 1.0 / 1_000_000_000;
	private static final double ROTATION_SCALE = 500;
	private static final double ENERGY_SCALE = 1.0 / 100_000_000;

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
		double momentOfInertia = calculateMomentOfInertia(initialPositionInSpace);
		double angularMomentum = momentOfInertia * angularVelocity;
		angularMomenta.add(angularMomentum);
		energySpent += calculateRotationEnergy(momentOfInertia, angularVelocity);
	}

	private double calculateTranslationEnergy(double mass, Vector linearVelocity) {
		double linearVelocityLength = linearVelocity.getLength();
		return mass * linearVelocityLength * linearVelocityLength / 2;
	}

	private Vector calculateLinearVelocity(Segment beforeMovement, Segment afterMovement, double mass) {
		Vector startPoint = beforeMovement.getMidPoint();
		Vector endPoint = afterMovement.getMidPoint();
		return endPoint.minus(startPoint);
	}

	private double calculateAngularVelocity(Segment initialPositionInSpace, Segment finalPositionInSpace) {
		try {
			return finalPositionInSpace.vector.getAngleWith(initialPositionInSpace.vector);
		} catch (ZeroVectorException e) {
			return 0;
		}
	}

	private double calculateMomentOfInertia(Segment initialPositionInSpace) {
		double length = initialPositionInSpace.vector.getLength();
		double distance = initialPositionInSpace.startPoint.minus(centerOfMass).getLength();
		return length * length * 16 / 48 + distance * distance;
	}

	private double calculateRotationEnergy(double momentOfInertia, double angularVelocity) {
		return momentOfInertia * angularVelocity * angularVelocity / 2;
	}

	public Vector getTranslation() {
		return getTotalLinearMomentum().invert().by(bodyMass * PROPULSION_SCALE);
	}

	private Vector getTotalLinearMomentum() {
		Vector result = Vector.ZERO;
		for (Vector linearMomentum : linearMomenta)
			result = result.plus(linearMomentum);
		return result;
	}

	public double getRotation() {
		double unscaledResult = -getTotalAngularMomentum() / (bodyMass * bodyRadius * bodyRadius / 4);
		return unscaledResult * ROTATION_SCALE;
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
