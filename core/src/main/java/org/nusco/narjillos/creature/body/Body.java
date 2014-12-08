package org.nusco.narjillos.creature.body;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.nusco.narjillos.creature.body.physics.RotationalForceField;
import org.nusco.narjillos.creature.body.physics.TranslationalForceField;
import org.nusco.narjillos.shared.physics.Angle;
import org.nusco.narjillos.shared.physics.Segment;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.physics.ZeroVectorException;

/**
 * The physical body of a Narjillo, with all its organs and their position in
 * space.
 * 
 * This class contains the all-important Body.tick() method. Look at its
 * comments for details.
 */
public class Body {

	private final Organ head;
	private final double metabolicConsumption;
	private final double adultMass;
	private double mass;
	private transient List<BodyPart> bodyParts;

	public Body(Organ head) {
		this.head = head;
		adultMass = calculateAdultMass();
		this.metabolicConsumption = Math.pow(getMetabolicRate(), 1.5);
	}

	public Head getHead() {
		return (Head) head;
	}

	public List<BodyPart> getBodyParts() {
		if (bodyParts == null) {
			bodyParts = new ArrayList<>();
			addWithChildren(bodyParts, head);
		}
		return bodyParts;
	}

	private void addWithChildren(List<BodyPart> result, Organ organ) {
		// children first
		for (Organ child : organ.getChildren())
			addWithChildren(result, child);
		result.add(organ);
	}

	public void teleportTo(Vector position) {
		final int northDirection = 90;
		getHead().moveTo(position, northDirection);
	}

	public Vector getStartPoint() {
		return getHead().getStartPoint();
	}

	public Vector calculateCenterOfMass() {
		// TODO: any way to avoid recalculating this here?
		// When I try to cache it, I get a weird bug with
		// the mass staying too low and narjillos zipping
		// around like crazy.
		double mass = calculateMass();

		if (mass <= 0)
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

		return Vector.cartesian(totalX / mass, totalY / mass);
	}

	/**
	 * Take a target direction. Change the body's geometry based on the target
	 * direction. Move the body. Return the energy consumed on the entire
	 * operation.
	 */
	public double tick(Vector targetDirection) {
		// First, update the mass of a still-developing body.
		if (isStillGrowing())
			mass = calculateMass();

		// Before any movement, store away the current body positions
		// and center of mass. These will come useful later.
		Vector initialCenterOfMass = calculateCenterOfMass();
		Map<BodyPart, Segment> initialBodyPartPositions = calculateBodyPartPositions();

		// This first step happens as if the body where in a vacuum.
		// The organs in the body remodel their own geometry based on the
		// target's direction. They don't "think" were to go - they just
		// changes their positions *somehow*. Natural selection will favor
		// movements that result in getting closer to the target.
		tick_step1_updateAngles(targetDirection);

		// Changing the angles in the body results in a rotational force.
		// Rotate the body to match the force. In other words, keep its moment
		// of inertia equal to zero.
		double rotationEnergy = tick_step2_rotate(initialBodyPartPositions, initialCenterOfMass, mass);

		// The previous updates moved the center of mass. Remember, we're
		// in a vacuum - so the center of mass shouldn't move. Let's put it
		// back to its original position.
		tick_step3_recenter(initialCenterOfMass);

		// Now we can finally move out of the "vacuum" reference system.
		// All the movements from the previous steps result in a different
		// body position in space, and this different position generates
		// translational forces. We can update the body position based on
		// these translations.
		double translationEnergy = tick_step4_translate(initialBodyPartPositions, initialCenterOfMass, mass);

		return getEnergyConsumed(rotationEnergy, translationEnergy);
	}

	private boolean isStillGrowing() {
		return mass < getAdultMass();
	}

	private double getEnergyConsumed(double rotationEnergy, double translationEnergy) {
		return (rotationEnergy + translationEnergy) * metabolicConsumption;
	}

	private void tick_step1_updateAngles(Vector targetDirection) {
		double angleToTarget = getAngleTo(targetDirection);
		getHead().tick(0, angleToTarget);
	}

	private double getAngleTo(Vector direction) {
		if (direction.equals(Vector.ZERO))
			return 0;
		try {
			return Angle.normalize(getAngle() - direction.getAngle());
		} catch (ZeroVectorException e) {
			throw new RuntimeException(e); // should never happen
		}
	}

	private double tick_step2_rotate(Map<BodyPart, Segment> initialPositions, Vector centerOfMass, double mass) {
		RotationalForceField forceField = new RotationalForceField(mass, calculateRadius(centerOfMass), centerOfMass);
		for (BodyPart bodyPart : bodyParts)
			forceField.registerMovement(initialPositions.get(bodyPart), bodyPart.getPositionInSpace(), bodyPart.getMass());
		getHead().moveBy(Vector.ZERO, forceField.getRotation());
		return forceField.getEnergy();
	}

	private void tick_step3_recenter(Vector centerOfMassBeforeReshaping) {
		Vector centerOfMassAfterUpdatingAngles = calculateCenterOfMass();
		Vector centerOfMassOffset = centerOfMassBeforeReshaping.minus(centerOfMassAfterUpdatingAngles);
		getHead().moveBy(centerOfMassOffset, 0);
	}

	private double tick_step4_translate(Map<BodyPart, Segment> initialPositions, Vector centerOfMass, double mass) {
		TranslationalForceField forceField = new TranslationalForceField(mass);
		for (BodyPart bodyPart : bodyParts)
			forceField.registerMovement(initialPositions.get(bodyPart), bodyPart.getPositionInSpace(), bodyPart.getMass());
		getHead().moveBy(forceField.getTranslation(), 0);
		return forceField.getEnergy();
	}

	private Map<BodyPart, Segment> calculateBodyPartPositions() {
		Map<BodyPart, Segment> result = new LinkedHashMap<>();
		for (BodyPart bodyPart : getBodyParts())
			result.put(bodyPart, bodyPart.getPositionInSpace());
		return result;
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

	private double getMetabolicRate() {
		return getHead().getMetabolicRate();
	}

	public double getPercentEnergyToChildren() {
		return getHead().getPercentEnergyToChildren();
	}

	public double calculateMass() {
		double result = 0;
		for (BodyPart organ : getBodyParts())
			result += organ.getMass();
		return result;
	}

	private double calculateAdultMass() {
		double result = 0;
		for (BodyPart organ : getBodyParts())
			result += organ.getAdultMass();
		return result;
	}

	@Override
	public String toString() {
		return head.toString();
	}

	public double getAngle() {
		return Angle.normalize(getHead().getAbsoluteAngle() + 180);
	}

	public double getAdultMass() {
		return adultMass;
	}
}
