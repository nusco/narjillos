package org.nusco.swimmers.creature;

import java.util.LinkedList;
import java.util.List;

import org.nusco.swimmers.creature.body.BodyPart;
import org.nusco.swimmers.creature.body.Head;
import org.nusco.swimmers.creature.body.Organ;
import org.nusco.swimmers.creature.genetics.DNA;
import org.nusco.swimmers.creature.physics.ForceField;
import org.nusco.swimmers.shared.physics.Segment;
import org.nusco.swimmers.shared.physics.Vector;
import org.nusco.swimmers.shared.things.Thing;

public class Narjillo implements Thing {

	public static final double INITIAL_ENERGY = 100_000;
	private static final double ENERGY_PER_FOOD_ITEM = 100_000;
	private static final double ENERGY_CONSUMPTION_PER_FORCE_UNIT = 0.001;
	private static final double NATURAL_ENERGY_DECAY = 5;

	private static final double PROPULSION_SCALE = 2;
	
	private final Head head;
	private final int mass;

	private Vector position;
	private Vector target = Vector.ZERO;
	private double energy = INITIAL_ENERGY;

	private final List<SwimmerEventListener> swimmerEventListeners = new LinkedList<>();
	private final DNA genes;

	public Narjillo(Head head, DNA genes) {
		this.head = head;
		this.genes = genes;
		mass = calculateMass();
	}

	@Override
	public Vector getPosition() {
		return position;
	}

	@Override
	public void setPosition(Vector position) {
		this.position = position;
	}
	
	private void updatePosition(Vector position) {
		Vector start = getPosition();
		setPosition(position);
		
		for (SwimmerEventListener swimmerEventListener : swimmerEventListeners)
			swimmerEventListener.moved(new Segment(start, getPosition()));
	}

	@Override
	public void tick() {
		Vector targetDirection = getTargetDirection();

		ForceField forceField = head.createForceField();

		head.tick(targetDirection);

		// TODO: physical movement should probably move
		// outside this class. do we need a Body class?
		Vector force = forceField.getTotalForce().by(PROPULSION_SCALE);
		
		Vector movement = calculateMovement(force);
		
		Vector newPosition = getPosition().plus(movement);
		updatePosition(newPosition);

		double energySpentForMovement = force.getLength() * getMetabolicRate() * ENERGY_CONSUMPTION_PER_FORCE_UNIT;
		decreaseEnergy(energySpentForMovement + NATURAL_ENERGY_DECAY);
	}

	private double getMetabolicRate() {
		// FIXME: the metabolic rate shouldn't be stored in the head.
		// the entire narjillo/head/neck thing should be rethought
		return getHead().getMetabolicRate();
	}

	private Vector calculateMovement(Vector force) {
		// this can actually happen
		if (getMass() == 0)
			return force;

		return force.by(1.0 / getMass());
	}

	@Override
	public String getLabel() {
		return "swimmer";
	}
	
	public Head getHead() {
		return head;
	}

	public DNA getGenes() {
		return genes;
	}

	public double getEnergy() {
		return energy;
	}

	public void feed() {
		energy += ENERGY_PER_FOOD_ITEM;
	}

	void decreaseEnergy(double amount) {
		energy -= amount;
		if (energy <= 0)
			for (SwimmerEventListener swimmerEventListener : swimmerEventListeners)
				swimmerEventListener.died();
	}

	public Vector getTargetDirection() {
		// TODO: make the signal stronger when farther away?
		return target.minus(position).normalize(1);
	}

	public void setTarget(Vector target) {
		this.target = target;
	}

	public void addSwimmerEventListener(SwimmerEventListener lifecycleEventListener) {
		swimmerEventListeners.add(lifecycleEventListener);
	}

	public int getMass() {
		return mass;
	}

	private int calculateMass() {
		int result = 0;
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
