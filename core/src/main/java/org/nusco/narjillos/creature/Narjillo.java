package org.nusco.narjillos.creature;

import java.util.LinkedList;
import java.util.List;

import org.nusco.narjillos.creature.body.BodyPart;
import org.nusco.narjillos.creature.body.Head;
import org.nusco.narjillos.creature.body.Organ;
import org.nusco.narjillos.creature.genetics.DNA;
import org.nusco.narjillos.creature.physics.ForceField;
import org.nusco.narjillos.shared.physics.Segment;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.things.Thing;

public class Narjillo implements Thing {

	public static final double MAX_ENERGY = 1_000_000;
	static final double INITIAL_ENERGY = 700_000;
	private static final double ENERGY_PER_FOOD_ITEM = 700_000;
	private static final double MAX_TICKS_TO_DEATH = 50_000;
	private static final double NATURAL_ENERGY_DECAY = MAX_ENERGY / MAX_TICKS_TO_DEATH;

	private static final double PROPULSION_SCALE = 0.1;
	
	// 1 means that every movement is divided by the entire mass. This makes high mass a sure-fire loss.
	// 0.5 means half as much penalty. This justifies having a high mass, for the extra push it affords.
	private static final double MASS_PENALTY_DURING_PROPULSION = 0.3;
	
	private final Head head;
	private final DNA genes;
	private final double mass;

	private Vector position = Vector.ZERO;
	private Vector target = Vector.ZERO;
	private double energy = INITIAL_ENERGY;

	private int numberOfDescendants = 0;
	
	private final List<NarjilloEventListener> eventListeners = new LinkedList<>();

	public Narjillo(Head head, DNA genes) {
		this.head = head;
		this.genes = genes;
		mass = calculateTotalMass();
	}

	@Override
	public synchronized Vector getPosition() {
		return position;
	}

	@Override
	public synchronized void setPosition(Vector position) {
		this.position = position;
	}
	
	@Override
	public synchronized void tick() {
		Vector targetDirection = getTargetDirection();

		ForceField forceField = new ForceField();
		head.tick(targetDirection, forceField);

		// TODO: physical movement should probably move
		// outside this class. create a Body class that encapsulates
		// all body operations (except for construction and visualization).
		// the entire body should be just a machine that mechanically reacts
		// to a simple input target vector
		Vector force = forceField.getTotalForce();
		Vector movement = calculateMovement(force);
		
		Vector newPosition = getPosition().plus(movement);
		updatePosition(newPosition);

		double energySpentForMovement = forceField.getTotalEnergySpent() * getMetabolicRate();
		decreaseEnergy(energySpentForMovement + NATURAL_ENERGY_DECAY);
	}
	
	public Head getHead() {
		return head;
	}

	public DNA getDNA() {
		return genes;
	}

	public synchronized double getEnergy() {
		return energy;
	}

	public double getMass() {
		return mass;
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
		return getDNA().mutate();
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

	private double getMetabolicRate() {
		// FIXME: the metabolic rate shouldn't be stored in the head.
		// the entire narjillo/head/neck thing should be rethought
		return getHead().getMetabolicRate();
	}

	private Vector calculateMovement(Vector force) {
		// zero mass can actually happen
		if (getMass() == 0)
			return force.by(PROPULSION_SCALE);

		return force.by(PROPULSION_SCALE * getMassPenalty());
	}

	private double getMassPenalty() {
		return 1.0 / (getMass() * MASS_PENALTY_DURING_PROPULSION);
	}

	void decreaseEnergy(double amount) {
		energy -= amount;
		if (energy <= 0)
			for (NarjilloEventListener eventListener : eventListeners)
				eventListener.died();
	}
	
	private double calculateTotalMass() {
		double result = 0;
		List<Organ> allOrgans = getAllOrgans();
		for (Organ organ : allOrgans)
			result += organ.getMass();
		return result;
	}

	private List<Organ> getAllOrgans() {
		List<Organ> result = new LinkedList<>();
		addWithChildren(getHead(), result);
		return result;
	}

	private void addWithChildren(BodyPart organ, List<Organ> result) {
		result.add(organ);
		for (BodyPart child : organ.getChildren())
			addWithChildren(child, result);
	}
}
