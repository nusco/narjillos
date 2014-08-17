package org.nusco.narjillos.creature;

import java.util.LinkedList;
import java.util.List;

import org.nusco.narjillos.creature.body.Body;
import org.nusco.narjillos.creature.body.BodyPart;
import org.nusco.narjillos.creature.body.physics.Acceleration;
import org.nusco.narjillos.creature.genetics.Creature;
import org.nusco.narjillos.creature.genetics.DNA;
import org.nusco.narjillos.shared.physics.Segment;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.things.Thing;

public class Narjillo implements Thing, Creature {

	static final double INITIAL_ENERGY = 100_000;
	public static final double MAX_ENERGY = 200_000;
	static final double ENERGY_PER_FOOD_ITEM = 100_000;
	static final double LIFESPAN = 30_000;
	static final double ENERGY_DECAY = MAX_ENERGY / LIFESPAN;
	static final double AGONY_LEVEL = ENERGY_DECAY * 300;
	static final double LINEAR_VELOCITY_DECAY = 0.5;
	static final double ANGULAR_VELOCITY_DECAY = 0.5;

	public final Body body;
	private final DNA genes;

	private Vector target = Vector.ZERO;
	private Vector linearVelocity = Vector.ZERO;
	private double angularVelocity = 0;

	private double energy = INITIAL_ENERGY;
	private double maxEnergyForAge = MAX_ENERGY;

	private final List<NarjilloEventListener> eventListeners = new LinkedList<>();

	public Narjillo(Body body, DNA genes) {
		this.body = body;
		this.genes = genes;
	}

	public DNA getDNA() {
		return genes;
	}

	@Override
	public synchronized void tick() {
		applyLifecycleAnimations();
		letVelocitiesDecayWithAttrition();

		Acceleration effort = body.tick(getTargetDirection());

		updateVelocitiesBasedOn(effort);
		updatePositionBasedOnVelocities();

		updateEnergyBasedOn(effort);
	}

	private void applyLifecycleAnimations() {
		if (getEnergy() <= AGONY_LEVEL)
			applyDeathAnimation();
	}

	private void updateVelocitiesBasedOn(Acceleration effort) {
		// The lateral movement is ignored. Creatures who
		// have too much of it are wasting their energy.
		Vector axis = body.getMainAxis();
		linearVelocity = linearVelocity.plus(effort.getLinearAccelerationAlong(axis));
		angularVelocity = angularVelocity + effort.angular;
	}

	private void updateEnergyBasedOn(Acceleration effort) {
		maxEnergyForAge -= Narjillo.ENERGY_DECAY;
		updateEnergyBy(-effort.energySpent);
	}

	public synchronized void setPosition(Vector position) {
		body.setPosition(position);
	}

	@Override
	public synchronized Vector getPosition() {
		return body.getPosition();
	}

	private void setAngle(double angle) {
		body.setAngle(angle);
	}

	private void updatePositionBasedOnVelocities() {
		Vector newPosition = getPosition().plus(linearVelocity);
		double newAngle = getAngle() + angularVelocity;
		updatePosition(newPosition, newAngle);
	}

	private void updatePosition(Vector position, double angle) {
		Vector startingPosition = getPosition();

		Vector shiftedPosition = position.plus(getPivotingTranslation(angle));
		setPosition(shiftedPosition);
		setAngle(angle);
		
		for (NarjilloEventListener eventListener : eventListeners)
			eventListener.moved(new Segment(startingPosition, getPosition()));
	}

	private Vector getPivotingTranslation(double angle) {
		double rotation = Math.toRadians(angle - body.getAngle());
		
		// pivot around the center of mass
		Vector centerOfMass = body.getCenterOfMass();
		double shiftX = centerOfMass.x * (1 - Math.cos(rotation));
		double shiftY = centerOfMass.y * Math.sin(rotation);
		
		return Vector.cartesian(-shiftX, -shiftY);
	}

	private void letVelocitiesDecayWithAttrition() {
		// we keep things simple: "attrition" is just a constant
		linearVelocity = linearVelocity.by(LINEAR_VELOCITY_DECAY);
		angularVelocity = angularVelocity * ANGULAR_VELOCITY_DECAY;
	}

	private double getAngle() {
		return body.getAngle();
	}

	private void applyDeathAnimation() {
		// TODO: for some reason only 9 works here - 10 is too much (the
		// creatures spin wildly in agony) and 8 is too little (barely
		// any bending at all).
		// Bending is supposed to be instantaneous, instead it seems to be
		// additive.
		// Why? Find out what is going on here, and possibly rethink the
		// bending mechanics. Maybe it should come from the WaveNerve?
		double bendAngle = ((AGONY_LEVEL - getEnergy()) / (double) AGONY_LEVEL) * 9;
		body.forceBend(bendAngle);
	}

	public synchronized double getEnergy() {
		return energy;
	}

	public synchronized Vector getTargetDirection() {
		return target.minus(getPosition()).normalize(1);
	}

	public synchronized void setTarget(Vector target) {
		this.target = target;
	}

	public synchronized void feed() {
		double energyBoost = ENERGY_PER_FOOD_ITEM;
		energy += energyBoost;
		if (energy > maxEnergyForAge)
			energy = maxEnergyForAge;
	}

	public void addEventListener(NarjilloEventListener eventListener) {
		eventListeners.add(eventListener);
	}

	public synchronized DNA reproduce() {
		return getDNA().copy();
	}

	@Override
	public String getLabel() {
		return "narjillo";
	}

	void updateEnergyBy(double amount) {
		if (isDead())
			return;
		
		energy += amount;
		if (energy > maxEnergyForAge)
			energy = maxEnergyForAge;
		
		if (energy <= 0) {
			energy = 0;
			for (NarjilloEventListener eventListener : eventListeners)
				eventListener.died();
		}
	}

	boolean isDead() {
		return energy <= 0;
	}

	public List<BodyPart> getOrgans() {
		return body.getBodyParts();
	}

	public Vector getCenterOfMass() {
		return body.getCenterOfMass();
	}
}
