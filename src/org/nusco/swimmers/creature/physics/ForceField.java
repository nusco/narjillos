package org.nusco.swimmers.creature.physics;

import java.util.LinkedList;
import java.util.List;

import org.nusco.swimmers.creature.body.Organ;
import org.nusco.swimmers.creature.body.MovementListener;
import org.nusco.swimmers.shared.physics.Segment;
import org.nusco.swimmers.shared.physics.Vector;

public class ForceField implements MovementListener {

	private static final double VISCOSITY = 1.7;
	
	private final List<Vector> forces = new LinkedList<>();
	private double energySpent = 0;
	
	@Override
	public void moveEvent(Segment beforeMovement, Organ organ) {
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
		
		return averageMovement.normalize(movementIntensityInViscousFluid).getProjectionOn(afterMovement.vector.getNormal()).invert();
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
