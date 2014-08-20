package org.nusco.narjillos.creature;

import java.util.LinkedList;
import java.util.List;

import org.nusco.narjillos.creature.body.Body;
import org.nusco.narjillos.creature.body.BodyPart;
import org.nusco.narjillos.creature.genetics.Creature;
import org.nusco.narjillos.creature.genetics.DNA;
import org.nusco.narjillos.shared.physics.Segment;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.physics.ZeroVectorException;
import org.nusco.narjillos.shared.things.Thing;

public class Narjillo implements Thing, Creature {

	private static final double MASS_TO_ENERGY_RATIO = 0.1;
	private static final double MAX_ENERGY_RATIO = 2;
	static final double ENERGY_PER_FOOD_ITEM_RATIO = 1;
	
	private static final double LIFESPAN = 40_000;

	public final Body body;
	private final DNA genes;

	private Vector target = Vector.ZERO;
	private double energy;
	private double maxEnergyForAge;
	private final double energyDecay;
	private final double energyPerFoodItem;
	private final double agonyLevel;
	
	public final List<NarjilloEventListener> eventListeners = new LinkedList<>();

	public Narjillo(Body body, Vector position, DNA genes) {
		this.body = body;
		body.teleportTo(position);
		this.genes = genes;
		
		energy = body.getMass() * MASS_TO_ENERGY_RATIO;
		maxEnergyForAge = energy * MAX_ENERGY_RATIO;
		energyDecay = maxEnergyForAge / LIFESPAN;
		energyPerFoodItem = maxEnergyForAge * ENERGY_PER_FOOD_ITEM_RATIO;
		agonyLevel = energyDecay * 300; // TODO: tweak
	}

	public DNA getDNA() {
		return genes;
	}

	@Override
	public void tick() {
		applyLifecycleAnimations();

		Vector startingPosition = body.getStartPoint();

		double energySpent = body.tick(getTargetDirection());
		decreareEnergyBy(energySpent);
		
		for (NarjilloEventListener eventListener : this.eventListeners)
			eventListener.moved(new Segment(startingPosition, body.getStartPoint()));
	}

	private void applyLifecycleAnimations() {
		if (getEnergy() <= agonyLevel)
			applyDeathAnimation();
	}

	private void decreareEnergyBy(double energySpent) {
		maxEnergyForAge -= energyDecay;
		updateEnergyBy(-energySpent);
	}

	@Override
	public Vector getPosition() {
		return body.getStartPoint();
	}

	private void applyDeathAnimation() {
		// TODO: for some reason only 9 works here - 10 is too much (the
		// creatures spin wildly in agony) and 8 is too little (barely
		// any bending at all).
		// Bending is supposed to be instantaneous, instead it seems to be
		// additive.
		// Why? Find out what is going on here, and possibly rethink the
		// bending mechanics. Maybe it should come from the WaveNerve?
		double bendAngle = ((agonyLevel - getEnergy()) / agonyLevel) * 9;
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
		energy += energyPerFoodItem;
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

	public Vector getCenterOfMass() {
		return body.calculateCenterOfMass();
	}

	public double getMaxEnergy() {
		return maxEnergyForAge;
	}

	double getMass() {
		return body.getMass();
	}
}
