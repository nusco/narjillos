package org.nusco.narjillos.creature;

import java.util.List;

import org.nusco.narjillos.creature.body.Body;
import org.nusco.narjillos.creature.body.Mouth;
import org.nusco.narjillos.creature.body.Organ;
import org.nusco.narjillos.genomics.DNA;
import org.nusco.narjillos.shared.physics.Segment;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.things.Energy;
import org.nusco.narjillos.shared.things.FoodPiece;
import org.nusco.narjillos.shared.things.Thing;
import org.nusco.narjillos.shared.utilities.RanGen;

/**
 * A fully-formed, autonomous creature.
 */
public class Narjillo implements Thing {

	static final double MAX_LIFESPAN = 30_000;
	private static final int INCUBATION_TIME = 500;
	
	private final Body body;
	private final DNA dna;
	private Vector target = Vector.ZERO;
	private Energy energy;
	private Mouth mouth = new Mouth();
	private int age = 0;
	
	public Narjillo(DNA genes, Body body, Vector position, double energyAtBirth) {
		this.body = body;
		body.teleportTo(position);
		this.dna = genes;
		energy = new Energy(energyAtBirth, MAX_LIFESPAN);
	}

	public DNA getDNA() {
		return dna;
	}

	public Energy getEnergy() {
		return energy;
	}
	
	@Override
	public Segment tick() {
		growOlder();
		
		Vector startingPosition = body.getStartPoint();

		if (isDead())
			return new Segment(startingPosition, Vector.ZERO);

		mouth.tick(getPosition(), target, getBody().getAngle());
		double energySpent = body.tick(getMouth().getDirection());
		energy.tick(energySpent);

		return new Segment(startingPosition, body.getStartPoint().minus(startingPosition));
	}

	@Override
	public Vector getPosition() {
		return body.getStartPoint();
	}

	public void setTarget(Vector target) {
		this.target = target;
	}

	public void feedOn(FoodPiece thing) {
		energy.consume(thing);
		thing.setEater(this);
	}

	public Egg layEgg(Vector position, RanGen ranGen) {
		double percentEnergyToChildren = body.getPercentEnergyToChildren();
		double childEnergy = energy.chunkOff(percentEnergyToChildren);
		if (childEnergy == 0)
			return null;

		DNA childDNA = getDNA().copyWithMutations(ranGen);
		return new Egg(childDNA, getPosition(), childEnergy, INCUBATION_TIME);
	}

	@Override
	public String getLabel() {
		return "narjillo";
	}

	public boolean isDead() {
		return energy.isDepleted();
	}

	public List<Organ> getOrgans() {
		return body.getOrgans();
	}

	public Body getBody() {
		return body;
	}

	public Vector getTarget() {
		return target;
	}

	public Vector calculateCenterOfMass() {
		return body.calculateCenterOfMass();
	}

	public double getEnergyPercent() {
		return energy.getPercentOfInitialValue();
	}

	public Mouth getMouth() {
		return mouth;
	}

	public Vector getNeckLocation() {
		return getBody().getHead().getEndPoint();
	}

	public int getAge() {
		return age;
	}
	
	@Override
	public Vector getCenter() {
		return getBody().calculateCenterOfMass();
	}

	@Override
	public double getRadius() {
		return getBody().calculateRadius();
	}

	private void growOlder() {
		age++;
	}
}
