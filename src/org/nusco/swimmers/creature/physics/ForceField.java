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

	@Override
	public void moveEvent(Segment beforeMovement, Organ organ) {
		Vector beforeVector = beforeMovement.end;
		Vector afterVector = organ.getVector();
		
		double velocityAngle = afterVector.getAngleWith(beforeVector);
		double force = velocityAngle * beforeVector.getLength() * organ.getThickness();
		
		Vector normal = beforeVector.getNormal();
		
		addForce(Vector.polar(normal.getAngle(), force));
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
}
