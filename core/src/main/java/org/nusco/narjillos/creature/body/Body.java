package org.nusco.narjillos.creature.body;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.nusco.narjillos.creature.body.physics.ForceField;
import org.nusco.narjillos.creature.body.physics.Impulse;
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

	private static final double MAX_SKEWING = 70;
	private static final double MAX_SKEWING_VELOCITY = 0.1;

	private final Head head;
	private final List<BodyPart> bodyParts = new LinkedList<>();
	private final double mass;
	private double currentDirectionSkewing = 0;
	
	public Body(Head head) {
		this.head = head;
		addWithChildren(this.bodyParts, head);
		this.mass = calculateTotalMass();
	}

	public double getMass() {
		return mass;
	}

	public List<BodyPart> getBodyParts() {
		return bodyParts;
	}
	
	public void forceBend(double bendAngle) {
		head.skew(bendAngle);
	}

	public void teleportTo(Vector position) {
		head.setPosition(position, 0);
	}

	public Vector getStartPoint() {
		return head.getStartPoint();
	}

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

	/**
	 * Take a target direction. Change the body's geometry based on the target
	 * direction. Move the body. Return the energy spent on the entire operation.
	 */
	public double tick(Vector targetDirection) {
		// Before any movement, store away the current body positions
		// and center of mass. These will come useful later.
		Vector initialCenterOfMass = calculateCenterOfMass();
		Map<BodyPart, Segment> initialPositions = calculateBodyPartPositions();
		
		// The body reactively changes its geometry in response to the
		// target's direction. It doesn't "think" were to go - it just
		// changes its geometry *somehow*. Natural selection will favor
		// movements that result in getting closer to the target.
		tick_step1_UpdateBodyAngles(targetDirection);
		
		// The key concept here is that the body changed its shape as
		// if it were in a vacuum: the center of mass stays in the same
		// position, and the moment of inertia stays zero. (This last
		// quality is not yet implemented.)
		tick_step2_reposition(initialCenterOfMass);
		
		// Now we move out of the "vacuum" metaphor: thanks to the fluid's viscosity,
		// the body's movement generates translational and rotation forces.
		Impulse impulse = tick_step3_move(initialCenterOfMass, initialPositions);

		return impulse.energySpent;
	}

	private void tick_step1_UpdateBodyAngles(Vector targetDirection) {
		double angleToTarget;
		try {
			Vector mainAxis = head.getVector().normalize(1).invert();
			angleToTarget = mainAxis.getAngleWith(targetDirection);
		} catch (ZeroVectorException e) {
			return;
		}
		
		head.skew(calculateDirectionSkewing(angleToTarget));
		
		head.recursivelyUpdateAngleToParent(0);
		head.resetSkewing();
	}

	private void tick_step2_reposition(Vector centerOfMassBeforeReshaping) {
		Vector centerOfMassAfterUpdatingAngles = calculateCenterOfMass();
		Vector centerOfMassOffset = centerOfMassBeforeReshaping.minus(centerOfMassAfterUpdatingAngles);
		head.moveBy(centerOfMassOffset, 0);
	}

	private Impulse tick_step3_move(Vector centerOfMass, Map<BodyPart, Segment> initialPositions) {
		ForceField forceField = calculateForcesGeneratedByMovement(getBodyParts(), initialPositions, centerOfMass);
		Impulse impulse = calculateAccelerationForWholeBody(forceField, centerOfMass);
		moveBy(impulse, centerOfMass);
		return impulse;
	}

	private Map<BodyPart, Segment> calculateBodyPartPositions() {
		Map<BodyPart, Segment> result = new LinkedHashMap<>();
		for (BodyPart bodyPart : getBodyParts())
			result.put(bodyPart, bodyPart.getPositionInSpace());
		return result;
	}

	private ForceField calculateForcesGeneratedByMovement(List<BodyPart> bodyParts, Map<BodyPart, Segment> previousPositions, Vector centerOfMass) {
		ForceField forceField = new ForceField(getMass(), calculateRadius(centerOfMass), centerOfMass);
		for (BodyPart bodyPart : bodyParts)
			forceField.registerMovement(previousPositions.get(bodyPart), bodyPart.getPositionInSpace(), bodyPart.getMass());
		return forceField;
	}

	double calculateRadius(Vector centerOfMass) {
		final double MIN_RADIUS = 1;
		double result = MIN_RADIUS;
		for (BodyPart bodyPart : getBodyParts()) {
			double startPointDistance = bodyPart.getStartPoint().minus(centerOfMass).getLength();
			double endPointDistance = bodyPart.getEndPoint().minus(centerOfMass).getLength();
			double distance = Math.max(startPointDistance, endPointDistance);
			if (distance > result)
				result = distance;
		}
		return result;
	}

	private Impulse calculateAccelerationForWholeBody(ForceField forceField, Vector centerOfMass) {
		Vector translation = forceField.getTranslation();
		double rotation = forceField.getRotation();

		double energySpent = forceField.getTotalEnergySpent() * getMetabolicRate();
		
		return new Impulse(translation, rotation, energySpent);
	}

	private double calculateDirectionSkewing(double angleToTarget) {
		double updatedSkewing = (angleToTarget % 180) / 180 * MAX_SKEWING;
		double skewingVelocity = updatedSkewing - currentDirectionSkewing;
		if (Math.abs(skewingVelocity) > MAX_SKEWING_VELOCITY)
			skewingVelocity = Math.signum(skewingVelocity) * MAX_SKEWING_VELOCITY;
		return currentDirectionSkewing += skewingVelocity;
	}

	private void addWithChildren(List<BodyPart> result, Organ organ) {
		// children first
		for (Organ child : organ.getChildren())
			addWithChildren(result, child);
		result.add(organ);
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

	private double getAngle() {
		return head.getAbsoluteAngle();
	}
	
	private void moveBy(Impulse impulse, Vector centerOfMass) {
		Vector newPosition = getStartPoint().plus(impulse.linearComponent);
		Vector pivotedPosition = newPosition.plus(rotateAround(centerOfMass, impulse.angularComponent));
		double newAngle = Angle.normalize(getAngle() + impulse.angularComponent);
		head.setPosition(pivotedPosition, newAngle);
	}

	private Vector rotateAround(Vector center, double rotation) {
		Vector pivot = center.minus(getStartPoint());
		double shiftX = pivot.x * (1 - Math.cos(Math.toRadians(rotation)));
		double shiftY = pivot.y * Math.sin(Math.toRadians(rotation));
		return Vector.cartesian(-shiftX, -shiftY);
	}
}
