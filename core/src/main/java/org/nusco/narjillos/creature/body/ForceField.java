package org.nusco.narjillos.creature.body;

import java.util.LinkedList;
import java.util.List;

import org.nusco.narjillos.shared.physics.Segment;
import org.nusco.narjillos.shared.physics.Vector;

class ForceField {

	private static final double VISCOSITY = 1.7;

	public static ForceField NULL = new ForceField() {
		@Override
		public void record(Segment beforeMovement, Organ organ) {}
	};

	private final List<Segment> forces = new LinkedList<>();
	private double energySpent = 0;
	
	public void record(Segment beforeMovement, Organ organ) {
		Vector force = reverseCalculateForceFromMovement(beforeMovement, organ.getSegment(), organ.getLength(), organ.getMass());
		Segment forceSegment = new Segment(organ.getStartPoint(), force);
		addForce(forceSegment);
	}

	private Vector reverseCalculateForceFromMovement(Segment beforeMovement, Segment afterMovement, double length, double mass) {
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

	public Vector getTotalForce() {
		Vector result = Vector.ZERO;
		for (Segment force : forces)
			result = result.plus(force.vector);
		return result;
	}

	public double getRotationalForceAround(Vector center) {
		return 0;
	}
	
	public double getTotalEnergySpent() {
		return energySpent / 1000;
	}
}
