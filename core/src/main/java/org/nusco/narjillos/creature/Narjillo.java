package org.nusco.narjillos.creature;

import java.util.LinkedList;
import java.util.List;

import org.nusco.narjillos.creature.body.Body;
import org.nusco.narjillos.creature.body.Organ;
import org.nusco.narjillos.creature.body.physics.Acceleration;
import org.nusco.narjillos.creature.genetics.Creature;
import org.nusco.narjillos.creature.genetics.DNA;
import org.nusco.narjillos.shared.physics.Segment;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.things.Thing;

public class Narjillo implements Thing, Creature {

	static final double INITIAL_ENERGY = 100_000;
	private static final double ENERGY_PER_FOOD_ITEM = 100_000;
	public static final double MAX_ENERGY = 200_000;
	private static final double MAX_TICKS_TO_DEATH = 10_000;
	static final double NATURAL_ENERGY_DECAY = MAX_ENERGY / MAX_TICKS_TO_DEATH;
	static final double AGONY_LEVEL = NATURAL_ENERGY_DECAY * 300;

	public final Body body;
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
		Vector newPosition = getPosition().plus(linearVelocity);
		double newAngle = getAngle() + angularVelocity;
		updatePosition(newPosition, newAngle);

		sendDeathAnimation();
		updateVelocities();

		Vector targetDirection = getTargetDirection();
		Acceleration effort = body.tick(targetDirection);

		// The lateral movement is just ignored. Creatures who
		// have too much of it are wasting their energy.
		Vector axis = body.getMainAxis();
		linearVelocity = linearVelocity.plus(effort.getLinearAccelerationAlong(axis));
		angularVelocity = angularVelocity + effort.angular;

		decreaseEnergy(effort.energySpent + Narjillo.NATURAL_ENERGY_DECAY);
	}

	private void updateVelocities() {
		double linearVelocityDecay = 0.5;
		linearVelocity = linearVelocity.by(linearVelocityDecay);
		double angularVelocityDecay = 0.5;
		angularVelocity = angularVelocity * angularVelocityDecay;
	}

	private double getAngle() {
		return body.getAngle();
	}

	private void sendDeathAnimation() {
		if (getEnergy() > AGONY_LEVEL)
			return;
		// TODO: for some reason only 9 works here - 10 is too much (the
		// creatures
		// spin wildly in agony) and 8 is too little (barely any bending at
		// all).
		// bending is supposed to be instantaneous, instead it seems to be
		// additive.
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

	private void updatePosition(Vector position, double angle) {
		Vector startingPosition = getPosition();
		setPosition(position);

		// FIXME: don't pivot around the mouth - update position instead
		body.setAngle(angle);

		for (NarjilloEventListener eventListener : eventListeners)
			eventListener.moved(new Segment(startingPosition, getPosition()));
	}

	void decreaseEnergy(double amount) {
		energy -= amount;
		if (energy <= 0) {
			energy = 0;
			for (NarjilloEventListener eventListener : eventListeners)
				eventListener.died();
		}
	}

	public List<Organ> getOrgans() {
		return body.getOrgans();
	}
}
