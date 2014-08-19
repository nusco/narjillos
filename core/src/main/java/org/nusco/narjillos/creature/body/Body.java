package org.nusco.narjillos.creature.body;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.nusco.narjillos.creature.body.physics.Impulse;
import org.nusco.narjillos.creature.body.physics.ForceField;
import org.nusco.narjillos.creature.body.pns.WaveNerve;
import org.nusco.narjillos.shared.physics.Segment;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.physics.ZeroVectorException;

/**
 * The physical body of a Narjillo, with all its organs and their position in space.
 * 
 * This class contains the all-important Body.tick() method. Look at its comments
 * for details.
 */
public class Body {

	private static final double WAVE_SIGNAL_FREQUENCY = 0.01;
	private static final double MAX_SKEWING = 70;
	private static final double MAX_SKEWING_VELOCITY = 1;

	private final Head head;
	private final List<BodyPart> bodyParts = new LinkedList<>();
	private final double mass;
	private final WaveNerve tickerNerve;
	private double currentDirectionSkewing = 0;
	
	public Body(Head head) {
		this.head = head;
		addWithChildren(this.bodyParts, head);
		this.mass = calculateTotalMass();
		this.tickerNerve = new WaveNerve(WAVE_SIGNAL_FREQUENCY * getMetabolicRate());
	}

	/**
	 * Take a target direction. Change the body's geometry based on the target
	 * direction. Return the resulting set of translational/linear forces.
	 */
	public Impulse tick(Vector targetDirection) {
		// Before any movement, store away the current body positions
		// and center of mass. These will come useful later.
		Vector centerOfMassBeforeReshaping = calculateCenterOfMass();
		Map<BodyPart, Segment> initialPositions = tick_SnapshotBodyPartPositions();
		
		// The body reactively changes its geometry in response to the
		// target's direction. It doesn't "think" were to go - it just
		// changes its geometry *somehow*. Natural selection will favor
		// movements that result in getting closer to the target.
		// 
		// The key concept here is that the body changes its shape as
		// if it were in a vacuum: the center of mass stays in the same
		// position, and the moment of inertia stays zero. (This last
		// quality is not yet implemented.)
		tick_UpdateBodyShapeInVacuum(targetDirection, centerOfMassBeforeReshaping);

		// Now we move out of the "vacuum" metaphor: the body's movement
		// generates translational and rotation forces.
		ForceField forceField = tick_CalculateForcesGeneratedByMovement(getBodyParts(), initialPositions, centerOfMassBeforeReshaping);
		Vector centerOfMassAfterReshaping = calculateCenterOfMass();
		return tick_CalculateAccelerationForWholeBody(forceField, centerOfMassAfterReshaping);
	}

	private void tick_UpdateBodyShapeInVacuum(Vector targetDirection, Vector centerOfMassBeforeReshaping) {
		// TODO: this is the place where I should change rotation and translation to keep
		// moment of inertia at zero and center of mass constant.
		// The creature changed the shape of its body but it changed neither its
		// center of mass, nor its moment of inertia. Instead, I only keep the center
		// of mass constant, and I let the moment of inertia float. The result is
		// the "tail wiggling dog" effect.
		tick_UpdateBodyAngles(targetDirection);

		Vector centerOfMassAfterReshaping = calculateCenterOfMass();
		Vector centerOfMassOffset = centerOfMassBeforeReshaping.minus(centerOfMassAfterReshaping);
		head.move(centerOfMassOffset, 0);
	}

	private Map<BodyPart, Segment> tick_SnapshotBodyPartPositions() {
		Map<BodyPart, Segment> result = new HashMap<>();
		for (BodyPart bodyPart : getBodyParts())
			result.put(bodyPart, bodyPart.getPositionInSpace());
		return result;
	}

	private void tick_UpdateBodyAngles(Vector targetDirection) {
		double angleToTarget;
		try {
			Vector mainAxis = getMainAxis();
			angleToTarget = mainAxis.getAngleWith(targetDirection);
		} catch (ZeroVectorException e) {
			return;
		}
		
		head.skew(tick_CalculateDirectionSkewing(angleToTarget));
		
		double targetAmplitudePercent = tickerNerve.tick(0);
		head.recursivelyUpdateAngleToParent(targetAmplitudePercent);
		head.resetSkewing();
	}

	private ForceField tick_CalculateForcesGeneratedByMovement(List<BodyPart> bodyParts, Map<BodyPart, Segment> previousPositions, Vector centerOfMass) {
		ForceField forceField = new ForceField(getMass(), getRadius(), centerOfMass);
		for (BodyPart bodyPart : bodyParts)
			forceField.registerMovement(previousPositions.get(bodyPart), bodyPart.getPositionInSpace(), bodyPart.getMass());
		return forceField;
	}

	private double getRadius() {
		// FIXME: simplify for now. fix later.
		return 1500;
	}

	private Impulse tick_CalculateAccelerationForWholeBody(ForceField forceField, Vector centerOfMass) {
		Vector translation = forceField.getTranslation();
		double rotation = forceField.getRotation();

		double energySpent = forceField.getTotalEnergySpent() * getMetabolicRate();
		
		return new Impulse(translation, rotation, energySpent);
	}

	private double tick_CalculateDirectionSkewing(double angleToTarget) {
		double updatedSkewing = (angleToTarget % 180) / 180 * MAX_SKEWING;
		double skewingVelocity = updatedSkewing - currentDirectionSkewing;
		if (Math.abs(skewingVelocity) > MAX_SKEWING_VELOCITY)
			skewingVelocity = Math.signum(skewingVelocity) * MAX_SKEWING_VELOCITY;
		return currentDirectionSkewing += skewingVelocity;
	}

	public Vector getStartPoint() {
		// TODO: consider moving the reference startPoint to the center of mass and
		// having a separate method for mouth position
		return head.getStartPoint();
	}

	public double getMass() {
		return mass;
	}

	public Vector getMainAxis() throws ZeroVectorException {
		return head.getMainAxis();
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
		head.skew(bendAngle);
	}

	// TODO: find a way to calculate this as few times per tick as possible.
	// Right now it's called multiple times, and it's unclear to me
	// when I can cache it and when I can't.
	// Also, the center of mass should be absolute, not relative to the head.
	// This would make some calculations easier in tick(), but it would also
	// require some rewriting in other places.
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
	
	public void move(Vector position, double rotation) {
		Vector shiftedPosition = position.plus(rotateAround(calculateCenterOfMass(), rotation));
		head.setPosition(shiftedPosition, rotation);
	}

	private Vector rotateAround(Vector center, double angle) {
		Vector pivot = center.minus(getStartPoint());
		
		double rotation = angle - getAngle();
		
		double shiftX = pivot.x * (1 - Math.cos(Math.toRadians(rotation)));
		double shiftY = pivot.y * Math.sin(Math.toRadians(rotation));
		
		return Vector.cartesian(-shiftX, -shiftY);
	}
}
