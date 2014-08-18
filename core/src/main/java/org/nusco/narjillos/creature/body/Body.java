package org.nusco.narjillos.creature.body;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.nusco.narjillos.creature.body.physics.Acceleration;
import org.nusco.narjillos.creature.body.physics.ForceField;
import org.nusco.narjillos.creature.body.pns.WaveNerve;
import org.nusco.narjillos.shared.physics.Segment;
import org.nusco.narjillos.shared.physics.Vector;

public class Body {

	private static final double WAVE_SIGNAL_FREQUENCY = 0.01;
	private static final double MAX_SKEWING = 45;
	private static final double MAX_SKEWING_VELOCITY = 1;

	private final Head head;
	private final List<BodyPart> bodyParts = new LinkedList<>();
	private final double mass;
	private final WaveNerve tickerNerve;
	private double skewing = 0;
	
	private volatile Vector centerOfMass;
	
	public Body(Head head) {
		this.head = head;
		addWithChildren(this.bodyParts, head);

		this.mass = calculateTotalMass();
		this.tickerNerve = new WaveNerve(WAVE_SIGNAL_FREQUENCY * getMetabolicRate());
		this.centerOfMass = calculateCenterOfMass();
	}

	public Acceleration tick(Vector targetDirection) {
		// TODO: find a way to calculate this only once per tick.
		// It should be local to this method, not a field.
		centerOfMass = calculateCenterOfMass();

		Map<BodyPart, Segment> previousPositions = tick_SnapshotBodyPartPositions();
		tick_UpdateAnglesForWholeBody(targetDirection);
		ForceField forceField = tick_CalculateForcesGeneratedByMovement(getBodyParts(), previousPositions);
		return tick_CalculateAccelerationForWholeBody(forceField, centerOfMass);
	}

	private Map<BodyPart, Segment> tick_SnapshotBodyPartPositions() {
		Map<BodyPart, Segment> result = new HashMap<>();
		for (BodyPart bodyPart : getBodyParts())
			result.put(bodyPart, bodyPart.getPositionInSpace());
		return result;
	}

	private void tick_UpdateAnglesForWholeBody(Vector targetDirection) {
		double angleToTarget = getMainAxis().getAngleWith(targetDirection);
		double currentSkewing = updateSkewing(angleToTarget);
		
		double targetAmplitudePercent = tickerNerve.tick(0);
		head.recursivelyUpdateAngleToParent(targetAmplitudePercent, currentSkewing);
	}

	private ForceField tick_CalculateForcesGeneratedByMovement(List<BodyPart> bodyParts, Map<BodyPart, Segment> previousPositions) {
		ForceField forceField = new ForceField();
		for (BodyPart bodyPart : bodyParts)
			forceField.calculateForce(previousPositions.get(bodyPart), bodyPart.getPositionInSpace(), bodyPart.getMass());
		return forceField;
	}

	private Acceleration tick_CalculateAccelerationForWholeBody(ForceField forceField, Vector centerOfMass) {
		double rotation = forceField.calculateRotation(getMass(), centerOfMass);
		Vector translation = forceField.calculateTranslation(getMass());

		double energySpent = forceField.getTotalEnergySpent() * getMetabolicRate();
		
		return new Acceleration(translation, rotation, energySpent);
	}

	private double updateSkewing(double angleToTarget) {
		double updatedSkewing = (angleToTarget % 180) / 180 * MAX_SKEWING;
		double skewingVelocity = updatedSkewing - skewing;
		if (Math.abs(skewingVelocity) > MAX_SKEWING_VELOCITY)
			skewingVelocity = Math.signum(skewingVelocity) * MAX_SKEWING_VELOCITY;
		return skewing += skewingVelocity;
	}

	public Vector getMainAxis() {
		return head.getMainAxis();
	}

	public double getMass() {
		return mass;
	}

	public List<BodyPart> getBodyParts() {
		return bodyParts;
	}

	private void addWithChildren(List<BodyPart> result, Organ organ) {
		result.add(organ);
		for (Organ child : organ.getChildren())
			addWithChildren(result, child);
	}

	private double getMetabolicRate() {
		return head.getMetabolicRate();
	}

	private double calculateTotalMass() {
		double result = 0;
		List<BodyPart> allOrgans = getBodyParts();
		for (BodyPart organ : allOrgans)
			result += organ.getMass();
		return result;
	}
	
	public void forceBend(double bendAngle) {
		forceBendWithChildren(head, bendAngle);
	}
	
	private void forceBendWithChildren(Organ bodyPart, double bendAngle) {
		bodyPart.forceBend(bendAngle);
		for (Organ child : bodyPart.getChildren())
			forceBendWithChildren(child, bendAngle);
	}

	private Vector calculateCenterOfMass() {
		List<BodyPart> organs = getBodyParts();
		Vector[] weightedCentersOfMass = new Vector[organs.size()];
		Iterator<BodyPart> iterator = getBodyParts().iterator();
		for (int i = 0; i < weightedCentersOfMass.length; i++) {
			BodyPart organ = iterator.next();
			weightedCentersOfMass[i] = organ.getCenterOfMass().by(organ.getMass());
		}
		
		// do it in one swoop instead of calling Vector#plus() a lot
		// TODO: profile. does this have any visible effect on performance?
		int totalX = 0;
		int totalY = 0;
		for (int i = 0; i < weightedCentersOfMass.length; i++) {
			totalX += weightedCentersOfMass[i].x;
			totalY += weightedCentersOfMass[i].y;
		}
		double totalMass = getMass();
		
		if (totalMass <= 0)
			return Vector.ZERO;
		
		return Vector.cartesian(totalX / totalMass, totalY / totalMass);
	}

	public double getAngle() {
		return head.getAbsoluteAngle();
	}

	public Vector getPosition() {
		return head.getStartPoint();
	}
	
	public void move(Vector position, double rotation) {
		Vector shiftedPosition = position.plus(rotateAround(centerOfMass, rotation));
		head.setPosition(shiftedPosition,rotation);
	}

	private Vector rotateAround(Vector pivot, double angle) {
		double rotation = angle - getAngle();
		
		double shiftX = pivot.x * (1 - Math.cos(Math.toRadians(rotation)));
		double shiftY = pivot.y * Math.sin(Math.toRadians(rotation));
		
		return Vector.cartesian(-shiftX, -shiftY);
	}
}
