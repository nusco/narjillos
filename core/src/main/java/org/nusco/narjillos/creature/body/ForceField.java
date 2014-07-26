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

	private final List<Vector> forces = new LinkedList<>();
	private double energySpent = 0;
	
	public void record(Segment beforeMovement, Organ organ) {
		Vector force = calculateForceUsedForMovement(beforeMovement, organ.getSegment(), organ.getLength(), organ.getMass());
		addForce(force);
	}

	private Vector calculateForceUsedForMovement(Segment beforeMovement, Segment afterMovement, double length, double mass) {
		Vector startPointMovement = afterMovement.startPoint.minus(beforeMovement.startPoint);
		Vector endPointMovement = afterMovement.vector.minus(beforeMovement.vector);
		Vector averageMovement = startPointMovement.plus(endPointMovement).by(0.5);
		
		double normalizedMovementIntensity = averageMovement.getLength() * mass / 1000;
		double movementIntensityInViscousFluid = addViscosity(normalizedMovementIntensity) * 1000;

		energySpent += movementIntensityInViscousFluid / 1000;
		
		return averageMovement.normalize(movementIntensityInViscousFluid).getProjectionOn(afterMovement.vector.getNormal());
	}

	private double addViscosity(double normalizedMovementIntensity) {
		if (normalizedMovementIntensity < 1)
			return normalizedMovementIntensity;

		double result = Math.pow(normalizedMovementIntensity, VISCOSITY);
		result = Math.min(result, 300);
		return result;
	}

	void addForce(Vector force) {
		forces.add(force);
	}

	public Vector getTotalForce() {
		Vector result = Vector.ZERO;
		for (Vector force : forces)
			result = result.plus(force);
		return result;
	}

	public double getTotalEnergySpent() {
		return energySpent / 1000;
	}
}
