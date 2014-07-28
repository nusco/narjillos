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

	private static final double INITIAL_ENERGY = 100_000;
	private static final double ENERGY_PER_FOOD_ITEM = 100_000;
	public static final double MAX_ENERGY = 200_000;
	private static final double LIFESPAN_WITHOUT_EATING = 10_000;
	private static final double NATURAL_ENERGY_DECAY = MAX_ENERGY / LIFESPAN_WITHOUT_EATING;
	private static final double AGONY_LEVEL = NATURAL_ENERGY_DECAY * 300;
	private static final double LINEAR_VELOCITY_DECAY = 0.5;
	private static final double ANGULAR_VELOCITY_DECAY = 0.5;

	private final Body body;
	private final DNA genes;

	private Vector target = Vector.ZERO;
	private double energy = INITIAL_ENERGY;

	private Vector linearVelocity = Vector.ZERO;
	private double angularVelocity = 0;

	private int numberOfDescendants = 0;

	private final List<NarjilloEventListener> eventListeners = new LinkedList<>();

	public Narjillo(Body body, DNA genes) {
		this.body = body;
		this.genes = genes;
	}

	public DNA getDNA() {
		return genes;
	}

	@Override
	public synchronized Vector getPosition() {
		return body.getPosition();
	}

	public synchronized void setPosition(Vector position) {
		body.setPosition(position);
	}

	@Override
	public synchronized void tick() {
		updatePosition();
		updateVelocities();

		if (getEnergy() > AGONY_LEVEL)
			sendDeathAnimation();

		Vector targetDirection = getTargetDirection();
		Acceleration acceleration = body.tick(targetDirection);

		linearVelocity = linearVelocity.plus(acceleration.getAccelerationAlong(body.getMainAxis()));
		angularVelocity = angularVelocity + acceleration.angularAcceleration;
		decreaseEnergy(acceleration.energySpent + Narjillo.NATURAL_ENERGY_DECAY);
	}

	private void updatePosition() {
		Vector newPosition = getPosition().plus(linearVelocity);
		double newAngle = getAngle() + angularVelocity;
		updatePosition(newPosition, newAngle);
	}

	private void updatePosition(Vector position, double angle) {
		Vector startingPosition = getPosition();
		setPosition(position);

		// FIXME: don't pivot around the mouth - change position depending
		// on rotation instead
		body.setAngle(angle);

		for (NarjilloEventListener eventListener : eventListeners)
			eventListener.moved(new Segment(startingPosition, getPosition()));
	}

	private void updateVelocities() {
		linearVelocity = linearVelocity.by(LINEAR_VELOCITY_DECAY);
		angularVelocity = angularVelocity * ANGULAR_VELOCITY_DECAY;
	}

	private double getAngle() {
		return body.getAngle();
	}

	private void sendDeathAnimation() {
		// TODO: for some reason only 9 works here - 10 is too much (the
		// creatures spin wildly in agony) and 8 is too little (barely
		// any bending at all). Bending is supposed to be instantaneous,
		// instead it seems to be additive.
		// Why? Find out what is going on here, and possibly rethink the bending
		// mechanics. Maybe it should come from the WaveNerve?
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
		energy += ENERGY_PER_FOOD_ITEM;
		if (energy > MAX_ENERGY)
			energy = MAX_ENERGY;
	}

	public void addEventListener(NarjilloEventListener eventListener) {
		eventListeners.add(eventListener);
	}

	public synchronized int getNumberOfDescendants() {
		return numberOfDescendants;
	}

	public synchronized DNA reproduce() {
		numberOfDescendants++;
		return getDNA().copy();
	}

	@Override
	public String getLabel() {
		return "narjillo";
	}

	void decreaseEnergy(double amount) {
		energy -= amount;
		if (energy <= 0) {
			energy = 0;
			for (NarjilloEventListener eventListener : eventListeners)
				eventListener.died();
		}
	}

	public List<BodyPart> getOrgans() {
		return body.getOrgans();
	}
}
