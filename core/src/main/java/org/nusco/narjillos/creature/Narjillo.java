package org.nusco.narjillos.creature;

import java.util.LinkedList;
import java.util.List;

import org.nusco.narjillos.creature.body.Body;
import org.nusco.narjillos.creature.body.Effort;
import org.nusco.narjillos.creature.body.Organ;
import org.nusco.narjillos.creature.genetics.Creature;
import org.nusco.narjillos.creature.genetics.DNA;
import org.nusco.narjillos.shared.physics.Segment;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.things.Thing;

public class Narjillo implements Thing, Creature {

	static final double INITIAL_ENERGY = 700_000;
	private static final double ENERGY_PER_FOOD_ITEM = 700_000;
	public static final double MAX_ENERGY = 1_000_000;
	private static final double MAX_TICKS_TO_DEATH = 50_000;
	static final double NATURAL_ENERGY_DECAY = MAX_ENERGY / MAX_TICKS_TO_DEATH;

	public final Body body;
	private final DNA genes;

	private Vector position = Vector.ZERO;
	private Vector target = Vector.ZERO;
	private double energy = INITIAL_ENERGY;

	private int numberOfDescendants = 0;
	
	private final List<NarjilloEventListener> eventListeners = new LinkedList<>();

	public Narjillo(Body body, DNA genes) {
		this.body = body;
		this.genes = genes;
	}

	@Override
	public synchronized Vector getPosition() {
		return position;
	}

	public synchronized void setPosition(Vector position) {
		this.position = position;
	}
	
	@Override
	public synchronized void tick() {
		Vector targetDirection = getTargetDirection();
		
		Effort effort = body.tick(targetDirection);
		Vector movement = effort.movement;
		
		Vector newPosition = getPosition().plus(movement);
		updatePosition(newPosition);
	
		decreaseEnergy(effort.energySpent + Narjillo.NATURAL_ENERGY_DECAY);
	}

	public DNA getDNA() {
		return genes;
	}

	public synchronized double getEnergy() {
		return energy;
	}

	public synchronized Vector getTargetDirection() {
		return target.minus(position).normalize(1);
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

	private void updatePosition(Vector position) {
		Vector start = getPosition();
		setPosition(position);
		
		for (NarjilloEventListener eventListener : eventListeners)
			eventListener.moved(new Segment(start, getPosition()));
	}

	void decreaseEnergy(double amount) {
		energy -= amount;
		if (energy <= 0)
			for (NarjilloEventListener eventListener : eventListeners)
				eventListener.died();
	}

	public List<Organ> getOrgans() {
		return body.getOrgans();
	}
}
