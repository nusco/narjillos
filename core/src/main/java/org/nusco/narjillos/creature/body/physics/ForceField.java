package org.nusco.narjillos.creature.body.physics;

import java.util.LinkedList;
import java.util.List;

import org.nusco.narjillos.shared.physics.Segment;
import org.nusco.narjillos.shared.physics.Vector;

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
 * Angular Momentum (for the body segments, approximated as thin rods):
 * angular_momentum = moment_of_inertia * angular_velocity
 * moment_of_inertia_around_far_end = mass * length^2 * 16 / 48
 * 
 * Also, by the parallel axis theorem:
 * moment_of_inertia_around_center_of_mass = moment_of_inertia_around_far_end +
 *                                           mass * distance_between_far_end_and_center_of_mass^2 
 *                                         = mass * (length^2 * 16 / 48 + distance_between_far_end_and_center_of_mass^2)
 * So the angular_momentum is the same as the above, multiplied by the angular_velocity.
 * 
 * Angular Momentum (for the whole body, approximated as a thin disk):
 * angular_velocity = angular_momentum / moment_of_inertia
 *                  = angular_momentum / (mass * radius^2 / 4)
 */
public class ForceField {

	private static final double PROPULSION_SCALE = 1 / 100_000_000;
	private static final double ROTATION_SCALE = 10000;

	private final double bodyMass;
	private final double bodyRadius;
	private final List<Vector> linearMomenta = new LinkedList<>();
	private final List<Double> angularMomenta = new LinkedList<>();
	private Vector centerOfMass;

	public ForceField(double bodyMass, double bodyRadius, Vector centerOfMass) {
		this.bodyMass = bodyMass;
		this.bodyRadius = bodyRadius;
		this.centerOfMass = centerOfMass;
	}

	public void registerMovement(Segment initialPositionInSpace, Segment finalPositionInSpace, double mass) {
		linearMomenta.add(calculateLinearMomentum(initialPositionInSpace, finalPositionInSpace, mass));
		angularMomenta.add(calculateAngularMomentum(initialPositionInSpace, finalPositionInSpace, mass));
	}

	private Vector calculateLinearMomentum(Segment beforeMovement, Segment afterMovement, double mass) {
		Vector startPoint = beforeMovement.getMidPoint();
		Vector endPoint = afterMovement.getMidPoint();
		Vector velocity = endPoint.minus(startPoint);
		return velocity.by(mass);
	}

	private double calculateAngularMomentum(Segment initialPositionInSpace, Segment finalPositionInSpace, double mass) {
		double length = initialPositionInSpace.vector.getLength();
		double distance = initialPositionInSpace.startPoint.minus(centerOfMass).getLength();
		double angular_velocity = finalPositionInSpace.vector.getAngle() - initialPositionInSpace.vector.getAngle();
		
		return angular_velocity * (length * length * 16 / 48 + distance * distance);
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
		double result = -getTotalAngularMomentum() / (bodyMass * bodyRadius * bodyRadius / 4) * ROTATION_SCALE;
		System.out.println(result);
		return result;
	}

	private double getTotalAngularMomentum() {
		double result = 0;
		for (double angularMomentum : angularMomenta)
			result += angularMomentum;
		return result;
	}

	public double getTotalEnergySpent() {
		// FIXME
		return 0;
	}
}
