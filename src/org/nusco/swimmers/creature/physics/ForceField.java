package org.nusco.swimmers.creature.physics;

import java.util.LinkedList;
import java.util.List;

import org.nusco.swimmers.creature.body.Organ;
import org.nusco.swimmers.creature.body.MovementListener;
import org.nusco.swimmers.shared.physics.Segment;
import org.nusco.swimmers.shared.physics.Vector;

// TODO: replace Organ here with a new superclass or interface describing an organ's geometry
public class ForceField implements MovementListener {

	private final List<Vector> forces = new LinkedList<>();
	private double energySpent = 0;
	
	@Override
	public void moveEvent(Segment beforeMovement, Organ organ) {
		Vector force = calculateForceOfMovement(beforeMovement, organ.getSegment(), organ.getLength(), organ.getMass());
		addForce(force);
	}

	private Vector calculateForceOfMovement(Segment beforeMovement, Segment afterMovement, double length, double mass) {
		Vector startPointMovement = afterMovement.startPoint.minus(beforeMovement.startPoint);
		Vector endPointMovement = afterMovement.vector.minus(beforeMovement.vector);
		Vector averageMovement = startPointMovement.plus(endPointMovement).by(0.5);
		
		double normalizedMovementLength = averageMovement.getLength();
		double viscousMovement = addViscosity(normalizedMovementLength);

		energySpent += averageMovement.getLength() * mass;
		
		return averageMovement.normalize(viscousMovement).getProjectionOn(afterMovement.vector.getNormal()).invert();
	}

	private double addViscosity(double normalizedMovementLength) {
		double viscousMovement;
		if (normalizedMovementLength < 1)
			viscousMovement = normalizedMovementLength;
		else
			viscousMovement = Math.pow(normalizedMovementLength, 1.7);
		viscousMovement = Math.min(viscousMovement, 300);
		return viscousMovement;
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
		return energySpent;
	}
}
