package org.nusco.narjillos.creature;

import java.util.List;

import org.nusco.narjillos.creature.body.Body;
import org.nusco.narjillos.creature.body.BodyPart;
import org.nusco.narjillos.embryogenesis.Embryo;
import org.nusco.narjillos.genomics.DNA;
import org.nusco.narjillos.shared.physics.Segment;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.things.Energy;
import org.nusco.narjillos.shared.things.Thing;
import org.nusco.narjillos.shared.utilities.RanGen;

public class Narjillo implements Thing {

	static final double MAX_LIFESPAN = 30_000;
	
	private final Body body;
	private final DNA dna;
	private Vector target = Vector.ZERO;
	private Energy energy;
	private Mouth mouth = new Mouth();
	
	public Narjillo(DNA genes, Body body, Vector position) {
		this(genes, body, position, body.getMass() / 2);
	}

	private Narjillo(DNA genes, Body body, Vector position, double initialEnergy) {
		this.body = body;
		body.teleportTo(position);
		this.dna = genes;
		energy = new Energy(initialEnergy, MAX_LIFESPAN);
	}

	public DNA getDNA() {
		return dna;
	}

	public Energy getEnergy() {
		return energy;
	}
	
	@Override
	public Segment tick() {
		applyLifecycleAnimations();

		Vector startingPosition = body.getStartPoint();

		if (isDead())
			return new Segment(startingPosition, Vector.ZERO);

		mouth.tick(getPosition(), target, getBody().getAngle());
		double energySpent = body.tick(getMouth().getDirection());
		energy.tick(energySpent);

		return new Segment(startingPosition, body.getStartPoint().minus(startingPosition));
	}

	private void applyLifecycleAnimations() {
		// TODO: find a new way to do this
//		if (energy.getValue() <= energy.getAgonyLevel())
//			applyDeathAnimation();
	}

	@Override
	public Vector getPosition() {
		return body.getStartPoint();
	}

	public void setTarget(Vector target) {
		this.target = target;
	}

	public void feedOn(Thing thing) {
		energy.consume(thing);
	}

	public Narjillo reproduce(Vector position, RanGen ranGen) {
		double percentEnergyToChildren = getPercentEnergyToChildren();
		double childEnergy = energy.donatePercent(percentEnergyToChildren);
		if (childEnergy == 0)
			return null;

		DNA childDNA = getDNA().copy(ranGen);
		return new Narjillo(childDNA, new Embryo(childDNA).develop(), position, childEnergy);
	}

	@Override
	public String getLabel() {
		return "narjillo";
	}

	public boolean isDead() {
		return energy.isDepleted();
	}

	public List<BodyPart> getBodyParts() {
		return body.getBodyParts();
	}

	double getMass() {
		return body.getMass();
	}

	public Body getBody() {
		return body;
	}

	public Vector getTarget() {
		return target;
	}

	public Vector getCenterOfMass() {
		return body.calculateCenterOfMass();
	}

	public double getPercentEnergyToChildren() {
		return body.getPercentEnergyToChildren();
	}

	public double getEnergyPercent() {
		return energy.getCurrentPercentOfInitialValue();
	}

	public Mouth getMouth() {
		return mouth;
	}
}
