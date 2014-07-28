package org.nusco.narjillos.creature.body;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.nusco.narjillos.creature.body.pns.WaveNerve;
import org.nusco.narjillos.shared.physics.Vector;

public class Body {

	// TODO: shouldn't the scales follow for the units I pick?
	// If they don't, then maybe I have the wrong units or
	// physical calculations
	private static final double PROPULSION_SCALE = 0.1;
	private static final double ROTATION_SCALE = 0.00008;
	private static final double WAVE_SIGNAL_FREQUENCY = 0.01;
	private static final int FIXED_MAX_AMPLITUDE_THAT_SHOULD_ACTUALLY_BE_GENETICALLY_DETERMINED = 45;

	// 1 means that every movement is divided by the entire mass. This makes
	// high mass a sure-fire loss.
	// 0.5 means half as much penalty. This justifies having a high mass, for
	// the extra push it affords.
	private static final double MASS_PENALTY_DURING_PROPULSION = 0.3;

	private final Head head;
	private final List<Organ> parts;
	private final double mass;
	private final WaveNerve tickerNerve;

	public Body(Head head) {
		this.head = head;
		this.parts = calculateOrgans(head);
		this.mass = calculateTotalMass();
		this.tickerNerve = new WaveNerve(WAVE_SIGNAL_FREQUENCY * getMetabolicRate());
	}

	public Effort tick(Vector targetDirection) {
		double angleToTarget = getMainAxis().getAngleWith(targetDirection);
		double skewing = (angleToTarget % 180) / 180; // from -1 to 1
		// Always a range of two, but shifts -1 to +1
		// so it can go from -2 to +2
		double fromMinusTwoToTwo = tickerNerve.tick(skewing);
		double targetAngle = fromMinusTwoToTwo * FIXED_MAX_AMPLITUDE_THAT_SHOULD_ACTUALLY_BE_GENETICALLY_DETERMINED;
		
		ForceField forceField = new ForceField();
		head.tick(targetAngle, forceField);

		double rotationAngle = calculateRotationAngle(forceField, getCenterOfMass());
		
		Vector movement = calculateMovement(forceField.getTotalForce());
		
		double energySpent = forceField.getTotalEnergySpent() * getMetabolicRate();
		
		return new Effort(movement, rotationAngle, energySpent);
	}

	private Vector getMainAxis() {
		return head.getMainAxis();
	}

	public double getMass() {
		return mass;
	}

	public double getAngle() {
		return head.getAbsoluteAngle();
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

	private double calculateRotationAngle(ForceField forceField, Vector center) {
		// also remember to correct position - right now, the rotating creature
		// is pivoting around its own mouth
		double rotationalForce = forceField.getRotationalForceAround(getCenterOfMass());
		double result = -rotationalForce * ROTATION_SCALE * getMassPenalty();
		
		final double maxRotation = 5;
		if (Math.abs(result) > maxRotation)
			result = Math.signum(result) * maxRotation;
		
		return result;
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
	
	public void forceBend(double bendAngle) {
		forceBendWithChildren(head, bendAngle);
	}
	
	private void forceBendWithChildren(BodyPart bodyPart, double bendAngle) {
		bodyPart.forceBend(bendAngle);
		for (BodyPart child : bodyPart.getChildren())
			forceBendWithChildren(child, bendAngle);
	}
	
	public Vector getCenterOfMass() {
		List<Organ> organs = getOrgans();
		Vector[] weightedCentersOfMass = new Vector[organs.size()];
		Iterator<Organ> iterator = getOrgans().iterator();
		for (int i = 0; i < weightedCentersOfMass.length; i++) {
			Organ organ = iterator.next();
			weightedCentersOfMass[i] = organ.getCenterOfMass().by(organ.getMass());
		}
		
		// do it in one swoop instead of calling Vector#plus() a lot
		// (but check in the end whether this has any effect on performance -
		// probably not, frankly
		int totalX = 0;
		int totalY = 0;
		for (int i = 0; i < weightedCentersOfMass.length; i++) {
			totalX += weightedCentersOfMass[i].x;
			totalY += weightedCentersOfMass[i].y;
		}
		double totalMass = getMass();
		return Vector.cartesian(totalX / totalMass, totalY / totalMass);
	}

	// TODO: the position is in the narjillo, but the angle is in the body
	// This feels wrong. Should the entire body ignore spatial positioning?
	public void setAngle(double angle) {
		head.setAngleToParent(angle);
	}
}
