package org.nusco.swimmers.creature;

import java.util.LinkedList;
import java.util.List;

import org.nusco.swimmers.creature.body.Head;
import org.nusco.swimmers.creature.body.Organ;
import org.nusco.swimmers.creature.physics.Propulsion;
import org.nusco.swimmers.physics.Vector;

public class Swimmer {

	private final Head head;

	private Vector target = Vector.ZERO;

	public Swimmer(Head head) {
		this.head = head;
	}

	public void placeAt(Vector position) {
		head.setStartPoint(position);
	}

	public List<Organ> getParts() {
		List<Organ> result = new LinkedList<>();
		result.add(head);
		addChildrenDepthFirst(result, head);
		return result;
	}

	private void addChildrenDepthFirst(List<Organ> result, Organ organ) {
		for (Organ child : organ.getChildren()) {
			result.add(child);
			addChildrenDepthFirst(result, child);
		}
	}

	public Organ getHead() {
		return head;
	}

	public void tick() {
		Propulsion propulsion = new Propulsion(head.getVector());
		head.setMovementListener(propulsion);

		head.tick(getCurrentTarget());

		Vector tangentialForce = propulsion.getTangentialForce();
		Vector newPosition = head.getStartPoint().plus(tangentialForce);
		head.setStartPoint(newPosition);
	}

	public Vector getCurrentTarget() {
		return target;
	}

	public void setCurrentTarget(Vector target) {
		this.target = target;
	}

	public Vector getPosition() {
		return head.getStartPoint();
	}
}
