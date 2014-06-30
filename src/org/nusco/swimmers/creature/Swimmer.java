package org.nusco.swimmers.creature;

import java.util.LinkedList;
import java.util.List;

import org.nusco.swimmers.creature.body.Head;
import org.nusco.swimmers.creature.body.Organ;
import org.nusco.swimmers.creature.physics.Propulsion;
import org.nusco.swimmers.shared.physics.Vector;
import org.nusco.swimmers.shared.things.Thing;

public class Swimmer implements Thing {

	private final Head head;

	private Vector position;
	private Vector target = Vector.ZERO;

	public Swimmer(Head head) {
		this.head = head;
	}

	@Override
	public void setPosition(Vector position) {
		this.position = position;
	}

	@Override
	public Vector getPosition() {
		return position;
	}

	public List<Organ> getOrgans() {
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

	public Head getHead() {
		return head;
	}

	public void tick() {
		Propulsion propulsion = new Propulsion(head.getVector());
		head.setMovementListener(propulsion);

		head.tick(getCurrentTarget());

		Vector tangentialForce = propulsion.getTangentialForce();
		Vector newPosition = getPosition().plus(tangentialForce);
		setPosition(newPosition);
	}

	public Vector getCurrentTarget() {
		return target;
	}

	public void setCurrentTarget(Vector target) {
		// TODO: make the signal stronger when farther away?
		this.target = target.normalize(1);
	}

	@Override
	public String getLabel() {
		return "swimmer";
	}
}
