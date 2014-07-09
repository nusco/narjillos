package org.nusco.swimmers.creature.physics;

import java.util.LinkedList;
import java.util.List;

import org.nusco.swimmers.creature.body.MovementListener;
import org.nusco.swimmers.shared.physics.Segment;
import org.nusco.swimmers.shared.physics.Vector;

public class ForceField implements MovementListener {

	private static final double PROPULSION_SCALE = 0.003;
	
	private final List<Vector> forces = new LinkedList<>();
	private final Vector direction;

	public ForceField(Vector direction) {
		this.direction = direction;
	}

	@Override
	public void moveEvent(Segment beforeMovement, Segment afterMovement) {
		Vector beforeVector = beforeMovement.end;
		Vector afterVector = afterMovement.end;
		
		double velocityAngle = afterVector.getAngleWith(beforeVector);
		double force = velocityAngle * beforeVector.getLength() * PROPULSION_SCALE;
		
		Vector normal = beforeVector.getNormal();
		
		addForce(Vector.polar(normal.getAngle(), force));
	}

	void addForce(Vector force) {
		forces.add(force);
	}

	public Vector getTangentialForce() {
		return getTotalForce().getProjectionOn(direction);
	}

	public double getTotalEnergy() {
		double result = 0;
		for (Vector force : forces)
			result += force.getLength();
		return result;
	}

	private Vector getTotalForce() {
		Vector result = Vector.ZERO;
		for (Vector force : forces)
			result = result.plus(force);
		return result;
	}
}
