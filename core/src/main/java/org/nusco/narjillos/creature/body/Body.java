package org.nusco.narjillos.creature.body;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.nusco.narjillos.creature.body.physics.ForceField;
import org.nusco.narjillos.creature.body.physics.Impulse;
import org.nusco.narjillos.creature.body.pns.WaveNerve;
import org.nusco.narjillos.shared.physics.Angle;
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
	private static final double MAX_SKEWING_VELOCITY = 0.1;

	private final Head head;
	private final List<BodyPart> bodyParts = new LinkedList<>();
	private final double mass;
	private final WaveNerve tickerNerve; // TODO: move to head?
	private final double maxRadius;
	private double currentDirectionSkewing = 0;
	
	public Body(Head head) {
		this.head = head;
		addWithChildren(this.bodyParts, head);
		this.mass = calculateTotalMass();
		this.tickerNerve = new WaveNerve(WAVE_SIGNAL_FREQUENCY * getMetabolicRate());
		this.maxRadius = Math.max(1, head.calculateLongestPathToLeaf() / 2);
	}

	/**
	 * Take a target direction. Change the body's geometry based on the target
	 * direction. Move the body. Return the energy spent on the entire operation.
	 */
	public double tick(Vector targetDirection) {
		// Before any movement, store away the current body positions
		// and center of mass. These will come useful later.
		Vector centerOfMassBeforeReshaping = calculateCenterOfMass();
		Map<BodyPart, Segment> initialPositions = tick_SnapshotBodyPartPositions();
		
		// The body reactively changes its geometry in response to the
		// target's direction. It doesn't "think" were to go - it just
		// changes its geometry *somehow*. Natural selection will favor
		// movements that result in getting closer to the target.
		tick_UpdateBodyAngles(targetDirection);
		
		// The key concept here is that the body changed its shape as
		// if it were in a vacuum: the center of mass stays in the same
		// position, and the moment of inertia stays zero. (This last
		// quality is not yet implemented.)
		Vector centerOfMassAfterUpdatingAngles = calculateCenterOfMass();
		Vector centerOfMassOffset = centerOfMassBeforeReshaping.minus(centerOfMassAfterUpdatingAngles);
		head.move(centerOfMassOffset, 0);
		
		// Now we move out of the "vacuum" metaphor: thanks to the fluid's viscosity,
		// the body's movement generates translational and rotation forces.
		ForceField forceField = tick_CalculateForcesGeneratedByMovement(getBodyParts(), initialPositions, centerOfMassBeforeReshaping);
		Vector centerOfMassAfterReshaping = calculateCenterOfMass();
		Impulse impulse = tick_CalculateAccelerationForWholeBody(forceField, centerOfMassAfterReshaping);
		
		moveBy(impulse);
		return impulse.energySpent;
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
			Vector mainAxis = head.getVector().normalize(1).invert();
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
		ForceField forceField = new ForceField(getMass(), getMaxRadius(), centerOfMass);
		for (BodyPart bodyPart : bodyParts)
			forceField.registerMovement(previousPositions.get(bodyPart), bodyPart.getPositionInSpace(), bodyPart.getMass());
		return forceField;
	}

	double getMaxRadius() {
		return maxRadius;
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
	public Vector calculateCenterOfMass() {
		if (getMass() <= 0)
			return getStartPoint();

		// do it in one swoop instead of creating a lot of
		// intermediate vectors

		List<BodyPart> organs = getBodyParts();
		Vector[] weightedCentersOfMass = new Vector[organs.size()];
		Iterator<BodyPart> iterator = getBodyParts().iterator();
		for (int i = 0; i < weightedCentersOfMass.length; i++) {
			BodyPart organ = iterator.next();
			weightedCentersOfMass[i] = organ.getCenterOfMass().by(organ.getMass());
		}
		
		double totalX = 0;
		double totalY = 0;
		for (int i = 0; i < weightedCentersOfMass.length; i++) {
			totalX += weightedCentersOfMass[i].x;
			totalY += weightedCentersOfMass[i].y;
		}
		
		return Vector.cartesian(totalX / getMass(), totalY / getMass());
	}

	public double getAngle() {
		return head.getAbsoluteAngle();
	}
	
	public void moveBy(Impulse impulse) {
		Vector newPosition = getStartPoint().plus(impulse.linearComponent);
		Vector pivotedPosition = newPosition.plus(rotateAround(calculateCenterOfMass(), impulse.angularComponent));
		double newAngle = Angle.normalize(getAngle() + impulse.angularComponent);
		head.setPosition(pivotedPosition, newAngle);
	}

	private Vector rotateAround(Vector center, double rotation) {
		Vector pivot = center.minus(getStartPoint());
		double shiftX = pivot.x * (1 - Math.cos(Math.toRadians(rotation)));
		double shiftY = pivot.y * Math.sin(Math.toRadians(rotation));
		return Vector.cartesian(-shiftX, -shiftY);
	}

	public void teleportTo(Vector position) {
		head.setPosition(position, 0);
	}
}
