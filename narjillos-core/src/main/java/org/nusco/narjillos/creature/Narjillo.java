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
	private long age = 0;
	private long nextEggAge = 0;
	
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
		double energySpentByMoving = body.tick(getMouth().getDirection());
		double energyGainedByGreenFibers = body.getGreenMass() * Configuration.CREATURE_GREEN_FIBERS_EXTRA_ENERGY;
		energy.tick(energySpentByMoving, energyGainedByGreenFibers);

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

	public long getAge() {
		return age;
	}

	/**
	 * Returns the newly laid egg, or null if the narjillo doesn't want to lay it.
	 */
	public Egg layEgg(Culture ecosystem, GenePool genePool, RanGen ranGen) {
		if (getAge() < nextEggAge)
			return null;
		
		if (isTooYoungToLayEggs()) {
			// too young - skip this chance to reproduce
			decideWhenToLayTheNextEgg();
			return null;
		}
		
		double energyToChild = getEnergy().getValue() * getBody().getPercentEnergyToChildren();
		double energyToEgg = Math.pow(getBody().getEggVelocity() * Configuration.EGG_MASS, 2);
		
		double totalEnergyRequired = energyToChild + energyToEgg;
		if (getEnergy().getValue() < totalEnergyRequired + Configuration.CREATURE_MIN_ENERGY_TO_LAY_EGG)
			return null;
		
		getEnergy().decreaseBy(energyToChild);
		DNA childDNA = genePool.mutateDNA(getDNA(), ranGen);

		decideWhenToLayTheNextEgg();
		Vector position = getNeckLocation();
		Vector velocity = Vector.polar(360 * ranGen.nextDouble(), getBody().getEggVelocity());
		return new Egg(childDNA, position, velocity, energyToChild, ranGen);
	}

	private void decideWhenToLayTheNextEgg() {
		nextEggAge = getAge() + getBody().getEggInterval();
	}

	private boolean isTooYoungToLayEggs(){
		return getAge() < Configuration.CREATURE_MATURE_AGE;
	}

	private void growOlder() {
		age++;
	}
}
