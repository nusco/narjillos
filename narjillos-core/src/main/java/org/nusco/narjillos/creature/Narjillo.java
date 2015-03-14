package org.nusco.narjillos.creature;

import java.util.List;

import org.nusco.narjillos.creature.body.Body;
import org.nusco.narjillos.creature.body.Mouth;
import org.nusco.narjillos.creature.body.Organ;
import org.nusco.narjillos.ecosystem.Culture;
import org.nusco.narjillos.genomics.DNA;
import org.nusco.narjillos.genomics.GenePool;
import org.nusco.narjillos.shared.physics.Segment;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.things.Energy;
import org.nusco.narjillos.shared.things.FoodPiece;
import org.nusco.narjillos.shared.things.Thing;
import org.nusco.narjillos.shared.utilities.Configuration;
import org.nusco.narjillos.shared.utilities.RanGen;

/**
 * A fully-formed, autonomous creature.
 */
public class Narjillo implements Thing {

	private final Body body;
	private final DNA dna;
	private final Energy energy;
	private final Mouth mouth = new Mouth();
	private Vector target = Vector.ZERO;
	private int age = 0;
	
	public Narjillo(DNA genes, Body body, Vector position, Energy energy) {
		this.body = body;
		body.teleportTo(position);
		this.dna = genes;
		this.energy = energy;
	}
	
	@Override
	public Segment tick() {
		growOlder();
		
		Vector startingPosition = body.getStartPoint();

		if (isDead())
			return new Segment(startingPosition, Vector.ZERO);

		mouth.tick(getPosition(), target, getBody().getAngle());
		double energySpent = body.tick(getMouth().getDirection());
		double energyGained = body.getGreenMass() * Configuration.CREATURE_GREEN_FIBERS_EXTRA_ENERGY;
		energy.tick(energySpent, energyGained);

		return new Segment(startingPosition, body.getStartPoint().minus(startingPosition));
	}

	@Override
	public Vector getPosition() {
		return body.getStartPoint();
	}

	@Override
	public Energy getEnergy() {
		return energy;
	}
	
	@Override
	public Vector getCenter() {
		return getCenterOfMass();
	}

	@Override
	public double getRadius() {
		return getBody().getRadius();
	}

	@Override
	public String getLabel() {
		return "narjillo";
	}

	public DNA getDNA() {
		return dna;
	}

	public Vector getTarget() {
		return target;
	}

	public void setTarget(Vector target) {
		this.target = target;
	}

	public Vector getCenterOfMass() {
		return body.getCenterOfMass();
	}

	public double getEnergyLevel() {
		return energy.getLevel();
	}

	public void feedOn(FoodPiece thing) {
		energy.steal(thing.getEnergy());
		thing.setEater(this);
	}

	public boolean isDead() {
		return energy.isZero();
	}

	public Body getBody() {
		return body;
	}

	public List<Organ> getOrgans() {
		return body.getOrgans();
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

	/**
	 * Returns the newly laid egg, or null if the narjillo doesn't want to lay it.
	 */
	public Egg layEgg(Culture ecosystem, GenePool genePool, RanGen ranGen) {
		// TODO: this entire algorithm must be rethought
		
		if (isTooYoungToLayEggs())
			return null;
		
//		if (ranGen.nextDouble() > Configuration.CREATURE_CHANCE_OF_LAYING_EGG)
//			return null;
		
		double percentEnergyToChildren = getBody().getPercentEnergyToChildren();

		// if the energy available is not close to the max, refuse to reproduce
		if (getEnergy().getMaximumValue() - getEnergy().getValue() < 1000)
			return null;
		
		double childEnergy = getEnergy().donate(percentEnergyToChildren);
		if (childEnergy == 0)
			return null; // refuse to lay egg
		
		DNA childDNA = genePool.mutateDNA(getDNA(), ranGen);
	
		Vector position = getNeckLocation();
		Vector velocity = Vector.polar(360 * ranGen.nextDouble(), Configuration.EGG_MAX_VELOCITY * ranGen.nextDouble());
		return new Egg(childDNA, position, velocity, childEnergy, ranGen);
	}

	/**
	 * Forces the laying of an egg, no questions asked (except in a few
	 * extreme cases - see the code).
	 */
	public Egg forceLayEgg(Culture ecosystem, GenePool genePool, RanGen ranGen) {
		// TODO: this will disappear once I have a good algorithm in layEgg()
		
		if (isTooYoungToLayEggs())
			return null;
		
		double percentEnergyToChildren = getBody().getPercentEnergyToChildren();
		double childEnergy = getEnergy().donate(percentEnergyToChildren);
		if (childEnergy == 0)
			return null; // refuse to lay egg
		
		DNA childDNA = genePool.mutateDNA(getDNA(), ranGen);
	
		Vector position = getNeckLocation();
		Vector velocity = Vector.polar(360 * ranGen.nextDouble(), Configuration.EGG_MAX_VELOCITY * ranGen.nextDouble());
		return new Egg(childDNA, position, velocity, childEnergy, ranGen);
	}

	private boolean isTooYoungToLayEggs(){
		return getAge() < Configuration.CREATURE_MATURE_AGE;
	}

	private void growOlder() {
		age++;
	}
}
