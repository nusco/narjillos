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
import org.nusco.narjillos.shared.things.Energy;
import org.nusco.narjillos.shared.things.Thing;

public class Narjillo implements Thing, Creature {

	static final double MAX_LIFESPAN = 40_000;
	
	private final Body body;
	private final DNA genes;
	private final List<NarjilloEventListener> eventListeners = new LinkedList<>();

	private Thing target = Thing.NULL;
	private Energy energy;

	public Narjillo(Body body, Vector position, DNA genes) {
		this.body = body;
		body.teleportTo(position);
		this.genes = genes;
		energy = new Energy(body.getMass(), MAX_LIFESPAN);
	}

	public DNA getDNA() {
		return genes;
	}

	public Energy getEnergy() {
		return energy;
	}

	double getEnergyValue() {
		return energy.getValue();
	}
	
	@Override
	public void tick() {
		if (isDead())
			return;

		applyLifecycleAnimations();

		Vector startingPosition = body.getStartPoint();

		double energySpent = body.tick(getTargetDirection());
		energy.tick(-energySpent);

		for (NarjilloEventListener eventListener : this.eventListeners)
			eventListener.moved(new Segment(startingPosition, body.getStartPoint()));

		if (isDead())
			for (NarjilloEventListener eventListener : eventListeners)
				eventListener.died();
	}

	private void applyLifecycleAnimations() {
		if (energy.getValue() <= energy.getAgonyLevel())
			applyDeathAnimation();
	}

	@Override
	public Vector getPosition() {
		return body.getStartPoint();
	}

	private void applyDeathAnimation() {
		// TODO: needs tweaking. the effect became invisible across commits
		double bendAngle = ((energy.getAgonyLevel() - getEnergy().getValue()) / energy.getAgonyLevel()) * 9;
		body.forceBend(bendAngle);
	}

	public Vector getTargetDirection() {
		try {
			return target.getPosition().minus(getPosition()).normalize(1);
		} catch (ZeroVectorException e) {
			return Vector.ZERO;
		}
	}

	public void setTarget(Thing target) {
		this.target = target;
	}

	public void feedOn(Thing thing) {
		energy.consume(thing);
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

	boolean isDead() {
		return energy.isDepleted();
	}

	public List<BodyPart> getBodyParts() {
		return body.getBodyParts();
	}

	double getMass() {
		return body.getMass();
	}

	public double getEnergyPercent() {
		return energy.getValue() / energy.getMax();
	}

	public Thing getTarget() {
		return target;
	}

	public Vector getCenterOfMass() {
		return body.calculateCenterOfMass();
	}
}
