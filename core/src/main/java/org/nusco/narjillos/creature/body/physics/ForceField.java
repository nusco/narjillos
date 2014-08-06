package org.nusco.narjillos.creature.body.physics;

import java.util.LinkedList;
import java.util.List;

import org.nusco.narjillos.creature.body.BodyPart;
import org.nusco.narjillos.shared.physics.Segment;
import org.nusco.narjillos.shared.physics.Vector;

public class ForceField {

	private static final double PROPULSION_SCALE = 0.05;
	private static final double ROTATION_SCALE = 0.0001;

	private static final double VISCOSITY = 1.7;
	
	// 1 means that every movement is divided by the entire mass. This makes
	// high mass a sure-fire loss.
	// 0.5 means half as much penalty. This justifies having a high mass, for
	// the extra push it gives you.
	private static final double MASS_PENALTY_DURING_PROPULSION = 0.3;

	private final List<Segment> forces = new LinkedList<>();
	private double energySpent = 0;
	
	public void record(Segment beforeMovement, BodyPart organ) {
		Vector force = reverseCalculateForceFromMovement(beforeMovement, organ.getSegment(), organ.getMass());
		Segment forceSegment = new Segment(organ.getStartPoint(), force);
		addForce(forceSegment);
	}

	public Vector calculateMovement(double mass) {
		return getTotalForce().invert().by(PROPULSION_SCALE * getMassPenalty(mass));
	}

	public double calculateRotationAngle(double mass, Vector centerOfMass) {
		// also remember to correct position - right now, the rotating creature
		// is pivoting around its own mouth
		double rotationalForce = getRotationalForceAround(centerOfMass);
		return -rotationalForce * ROTATION_SCALE * getMassPenalty(mass);
	}

	public double getTotalEnergySpent() {
		return energySpent / 1000;
	}

	private Vector reverseCalculateForceFromMovement(Segment beforeMovement, Segment afterMovement, double mass) {
		// TODO: this is all made-up stuff. try some real physics
		Vector startPointMovement = afterMovement.startPoint.minus(beforeMovement.startPoint);
		Vector endPointMovement = afterMovement.vector.minus(beforeMovement.vector);
		Vector averageMovement = startPointMovement.plus(endPointMovement).by(0.5);
		
		double normalizedMovementIntensity = averageMovement.getLength() * mass / 10000;
		double movementIntensityInViscousFluid = addViscosity(normalizedMovementIntensity) * 10000;

		energySpent += movementIntensityInViscousFluid / 10000;
		
		return averageMovement.normalize(movementIntensityInViscousFluid).getProjectionOn(afterMovement.vector.getNormal());
	}

	private double addViscosity(double normalizedMovementIntensity) {
		if (normalizedMovementIntensity < 1)
			return normalizedMovementIntensity;

		double result = Math.pow(normalizedMovementIntensity, VISCOSITY);
		result = Math.min(result, 300);
		return result;
	}

	void addForce(Segment force) {
		forces.add(force);
	}

	private Vector getTotalForce() {
		Vector result = Vector.ZERO;
		for (Segment force : forces)
			result = result.plus(force.vector);
		return result;
	}

	private double getRotationalForceAround(Vector center) {
		double result = 0;
		for (Segment force : forces)
			result += getRotationalForceAround(center, force);
		return result;
	}
	
	private double getRotationalForceAround(Vector center, Segment force) {
		Vector distance = force.startPoint.minus(center);
		return distance.getVectorProductWith(force.vector);
	}

	private double getMassPenalty(double mass) {
		if (mass <= 0)
			return 1.0;
		return 1.0 / (mass * MASS_PENALTY_DURING_PROPULSION);
	}
}
