package org.nusco.swimmers.creature;

import java.util.LinkedList;
import java.util.List;

import org.nusco.swimmers.creature.body.Head;
import org.nusco.swimmers.creature.body.Organ;
import org.nusco.swimmers.creature.genetics.DNA;
import org.nusco.swimmers.creature.physics.ForceField;
import org.nusco.swimmers.shared.physics.Segment;
import org.nusco.swimmers.shared.physics.Vector;
import org.nusco.swimmers.shared.things.Thing;

public class Swimmer implements Thing {

	public static final double INITIAL_ENERGY = 50_000;
	private static final double ENERGY_PER_FOOD_ITEM = 80_000;

	private final Head head;

	private Vector position;
	private Vector target = Vector.ZERO;
	private double energy = INITIAL_ENERGY;

	private final List<SwimmerEventListener> swimmerEventListeners = new LinkedList<>();
	private final DNA genes;

	public Swimmer(Head head, DNA genes) {
		this.head = head;
		this.genes = genes;
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

		// TODO: collision detection and physical movement should probably move
		// outside this class. do we need a Body class?
		Vector tangentialForce = forceField.getTangentialForce();
		Vector newPosition = getPosition().plus(tangentialForce);
		updatePosition(newPosition);

		decreaseEnergy(forceField.getTotalEnergy());
	}

	@Override
	public String getLabel() {
		return "swimmer";
	}
	
	public Organ getHead() {
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
}
