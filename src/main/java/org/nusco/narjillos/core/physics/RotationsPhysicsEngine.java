package org.nusco.narjillos.core.physics;

import java.util.LinkedList;
import java.util.List;

import org.nusco.narjillos.core.geometry.Angle;
import org.nusco.narjillos.core.geometry.Segment;
import org.nusco.narjillos.core.geometry.Vector;
import org.nusco.narjillos.core.configuration.Configuration;

/**
 * The physics of rotations.
 * <p>
 * Here are the formulas it uses. To understand them, consider that we don't use
 * inertia - so we can safely replace velocities with positions in space. (In
 * other words, we assume that after each movement of each body, the body's
 * velocity drops to zero).
 * <p>
 * Angular Momentum (for the body segments, approximated as thin rods):
 * angular_momentum = moment_of_inertia * angular_velocity
 * moment_of_inertia_around_far_end = mass * length^2 * 16 / 48
 * <p>
 * Also, by the parallel axis theorem:
 * moment_of_inertia_around_body_center = moment_of_inertia_around_far_end +
 * mass * distance_between_far_end_and_body_center^2
 * = mass * (length^2 * 16 / 48 + distance_between_far_end_and_body_center^2)
 * <p>
 * So the angular_momentum is the same as the moment_of_inertia_around_body_center,
 * multiplied by the angular_velocity.
 * <p>
 * Angular Momentum (for the whole body, approximated as a thin disk):
 * total_angular_velocity = total_angular_momentum / total_moment_of_inertia
 * = total_angular_momentum / (mass * radius^2 / 4)
 * <p>
 * rotation_energy = moment_of_inertia * angular_velocity^2 / 2;
 */
public class RotationsPhysicsEngine {

	private final double bodyMass;

	private final double bodyRadius;

	private final Vector centerOfMass;

	private final List<Double> angularMomenta = new LinkedList<>();

	private double rotationEnergy = 0;

	public RotationsPhysicsEngine(double bodyMass, double bodyRadius, Vector centerOfMass) {
		this.bodyMass = bodyMass;
		this.bodyRadius = bodyRadius;
		this.centerOfMass = centerOfMass;
	}

	public void registerMovement(double initialAngle, double finalAngle, Segment finalPositionInSpace, double mass) {
		double angularVelocity = calculateAngularVelocity(initialAngle, finalAngle);
		double momentOfInertia = calculateMomentOfInertia(finalPositionInSpace, mass);
		double angularMomentum = momentOfInertia * angularVelocity;
		angularMomenta.add(angularMomentum);
		rotationEnergy += calculateRotationEnergy(momentOfInertia, angularVelocity);
	}

	public double getRotation() {
		return -getTotalAngularMomentum() / (bodyMass * bodyRadius * bodyRadius / 4);
	}

	public double getEnergy() {
		return rotationEnergy * Configuration.PHYSICS_ENERGY_EXPENSE_PER_JOULE / 1_000_000_000L;
	}

	private double calculateAngularVelocity(double initialAngle, double finalAngle) {
		return Angle.normalize(finalAngle - initialAngle);
	}

	private double calculateMomentOfInertia(Segment positionInSpace, double mass) {
		double length = positionInSpace.getVector().getLength();
		double distance = positionInSpace.getStartPoint().minus(centerOfMass).getLength();
		return mass * length * length * 16 / 48 + distance * distance;
	}

	private double calculateRotationEnergy(double momentOfInertia, double angularVelocity) {
		return momentOfInertia * angularVelocity * angularVelocity / 2;
	}

	private double getTotalAngularMomentum() {
		double result = 0;
		for (double angularMomentum : angularMomenta)
			result += angularMomentum;
		return result;
	}
}
