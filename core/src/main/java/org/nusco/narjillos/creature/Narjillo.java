package org.nusco.narjillos.creature;

import java.util.LinkedList;
import java.util.List;

import org.nusco.narjillos.creature.body.Body;
import org.nusco.narjillos.creature.body.BodyPart;
import org.nusco.narjillos.creature.body.physics.Impulse;
import org.nusco.narjillos.creature.genetics.Creature;
import org.nusco.narjillos.creature.genetics.DNA;
import org.nusco.narjillos.shared.physics.Segment;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.physics.ZeroVectorException;
import org.nusco.narjillos.shared.things.Thing;

public class Narjillo implements Thing, Creature {

	static final double INITIAL_ENERGY = 100_000;
	public static final double MAX_ENERGY = 200_000;
	static final double ENERGY_PER_FOOD_ITEM = 100_000;
	static final double LIFESPAN = 30_000;
	static final double ENERGY_DECAY = MAX_ENERGY / LIFESPAN;
	static final double AGONY_LEVEL = ENERGY_DECAY * 300;

	public final Body body;
	private final DNA genes;

	private Vector target = Vector.ZERO;
	private double energy = INITIAL_ENERGY;
	private double maxEnergyForAge = MAX_ENERGY;

	private final List<NarjilloEventListener> eventListeners = new LinkedList<>();

	public Narjillo(Body body, Vector position, DNA genes) {
		this.body = body;
		this.body.move(position, 0);
		this.genes = genes;
	}

	public DNA getDNA() {
		return genes;
	}

	@Override
	public void tick() {
		applyLifecycleAnimations();

		Impulse impulse = body.tick(getTargetDirection());
		moveBy(impulse);
		updateEnergyBasedOn(impulse);
	}

	private void moveBy(Impulse impulse) {
		Vector newPosition = getPosition().plus(impulse.linearComponent);
		double newAngle = getAngle() + impulse.angularComponent;
		move(newPosition, newAngle);
	}

	private void applyLifecycleAnimations() {
		if (getEnergy() <= AGONY_LEVEL)
			applyDeathAnimation();
	}

	private void updateEnergyBasedOn(Impulse impulse) {
		maxEnergyForAge -= Narjillo.ENERGY_DECAY;
		updateEnergyBy(-impulse.energySpent);
	}

	@Override
	public Vector getPosition() {
		return body.getStartPoint();
	}

	private void move(Vector position, double angle) {
		Vector startingPosition = getPosition();

		body.move(position, angle);
		
		for (NarjilloEventListener eventListener : eventListeners)
			eventListener.moved(new Segment(startingPosition, getPosition()));
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

	public double getEnergy() {
		return energy;
	}

	public Vector getTargetDirection() {
		try {
			return target.minus(getPosition()).normalize(1);
		} catch (ZeroVectorException e) {
			return Vector.ZERO;
		}
	}

	public void setTarget(Vector target) {
		this.target = target;
	}

	public void feed() {
		double energyBoost = ENERGY_PER_FOOD_ITEM;
		energy += energyBoost;
		if (energy > maxEnergyForAge)
			energy = maxEnergyForAge;
	}

	public void addEventListener(NarjilloEventListener eventListener) {
		eventListeners.add(eventListener);
	}

	public DNA reproduce() {
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

	public List<BodyPart> getBodyParts() {
		return body.getBodyParts();
	}
}
