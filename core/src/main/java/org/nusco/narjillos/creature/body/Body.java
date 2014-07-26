package org.nusco.narjillos.creature.body;

import java.util.LinkedList;
import java.util.List;

import org.nusco.narjillos.shared.physics.Vector;

public class Body {
	
	private static final double PROPULSION_SCALE = 0.1;
	
	// 1 means that every movement is divided by the entire mass. This makes high mass a sure-fire loss.
	// 0.5 means half as much penalty. This justifies having a high mass, for the extra push it affords.
	private static final double MASS_PENALTY_DURING_PROPULSION = 0.3;

	private final Head head;
	private final List<Organ> parts;
	private final double mass;

	public Body(Head head) {
		this.head = head;
		this.parts = calculateOrgans(head);
		this.mass = calculateTotalMass();
	}

	public Effort tick(Vector targetDirection) {
		ForceField forceField = new ForceField();
		head.tick(targetDirection, forceField);
	
		Vector movement = calculateMovement(forceField.getTotalForce());
		double energySpent  = forceField.getTotalEnergySpent() * getMetabolicRate();
		
		return new Effort(movement, energySpent);
	}
	
	public double getMass() {
		return mass;
	}

	public List<Organ> getOrgans() {
		return parts;
	}

	private double getMetabolicRate() {
		return head.getMetabolicRate();
	}

	private double calculateTotalMass() {
		double result = 0;
		List<Organ> allOrgans = getOrgans();
		for (Organ organ : allOrgans)
			result += organ.getMass();
		return result;
	}

	private Vector calculateMovement(Vector force) {
		// zero mass can actually happen
		if (getMass() == 0)
			return force.invert().by(PROPULSION_SCALE);

		return force.invert().by(PROPULSION_SCALE * getMassPenalty());
	}

	private double getMassPenalty() {
		return 1.0 / (getMass() * MASS_PENALTY_DURING_PROPULSION);
	}
	
	private List<Organ> calculateOrgans(Head head) {
		List<Organ> result = new LinkedList<>();
		addWithChildren(head, result);
		return result;
	}

	private void addWithChildren(BodyPart organ, List<Organ> result) {
		result.add(organ);
		for (BodyPart child : organ.getChildren())
			addWithChildren(child, result);
	}
}
