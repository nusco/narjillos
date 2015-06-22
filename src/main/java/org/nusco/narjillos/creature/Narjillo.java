package org.nusco.narjillos.creature;

import java.util.List;

import org.nusco.narjillos.core.physics.Segment;
import org.nusco.narjillos.core.physics.Vector;
import org.nusco.narjillos.core.things.Energy;
import org.nusco.narjillos.core.things.FoodPiece;
import org.nusco.narjillos.core.things.Thing;
import org.nusco.narjillos.core.utilities.Configuration;
import org.nusco.narjillos.core.utilities.RanGen;
import org.nusco.narjillos.creature.body.Body;
import org.nusco.narjillos.creature.body.ConnectedOrgan;
import org.nusco.narjillos.creature.body.Mouth;
import org.nusco.narjillos.creature.embryogenesis.Embryo;
import org.nusco.narjillos.ecosystem.chemistry.Atmosphere;
import org.nusco.narjillos.ecosystem.chemistry.Element;
import org.nusco.narjillos.genomics.DNA;
import org.nusco.narjillos.genomics.GenePool;

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

	public Narjillo(DNA dna, Vector position, double angle, Energy energy) {
		this.body = new Embryo(dna).develop();
		body.forcePosition(position, angle);
		this.dna = dna;
		this.energy = energy;
	}

	@Override
	public Segment tick(Atmosphere atmosphere) {
		growOlder();

		Vector startingPosition = body.getStartPoint();

		if (isDead())
			return new Segment(startingPosition, Vector.ZERO);

		mouth.tick(getPosition(), target, getBody().getAngle());

		double energyRequiredToMove = body.tick(getMouth().getDirection());
		updateEnergy(energyRequiredToMove, atmosphere);

		return new Segment(startingPosition, body.getStartPoint().minus(startingPosition));
	}

	private void updateEnergy(double energyRequiredToMove, Atmosphere atmosphere) {
		// Make energy cheaper depending on atmospheric composition.
		// A creature whose breathable element has a density of 1 can move for free.
		// A creature whose breathable element has a density of 0 has to spend all
		// the energy from its reserves.
		//
		// (Don't mutate the Atmosphere here - this code is called in parallel,
		// and it would become non-deterministic if we mutate shared objects).
		double densityOfBreathableElement = atmosphere.getDensityOf(body.getBreathedElement());
		double energyConsumed = energyRequiredToMove * (1 - densityOfBreathableElement);

		// Account for the gain of energy by green fibers.
		double energyGatheredByGreenFibers = body.getGreenMass() * Configuration.CREATURE_GREEN_FIBERS_EXTRA_ENERGY;

		energy.tick(energyConsumed - energyGatheredByGreenFibers);
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

	public List<ConnectedOrgan> getOrgans() {
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
	 * Returns the newly laid egg, or null if the narjillo doesn't want to lay
	 * it.
	 */
	public Egg layEgg(GenePool genePool, RanGen ranGen) {
		if (getAge() < nextEggAge)
			return null;

		if (isTooYoungToLayEggs()) {
			// skip this chance to reproduce
			decideWhenToLayTheNextEgg();
			return null;
		}

		double energyToChild = getBody().getEnergyToChildren();
		double energyToEgg = Math.pow(getBody().getEggVelocity() * Configuration.EGG_MASS, 2);

		double totalEnergyRequired = energyToChild + energyToEgg;
		if (getEnergy().getValue() < totalEnergyRequired)
			return null;

		getEnergy().decreaseBy(energyToChild);
		DNA childDNA = genePool.mutateDNA(getDNA(), ranGen);

		decideWhenToLayTheNextEgg();
		Vector position = getNeckLocation();
		Vector velocity = Vector.polar(360 * ranGen.nextDouble(), getBody().getEggVelocity());
		return new Egg(childDNA, position, velocity, energyToChild, ranGen);
	}

	public Element getBreathedElement() {
		return body.getBreathedElement();
	}

	public Element getByproduct() {
		return body.getByproduct();
	}

	public double getBrainWaveAngle() {
		return body.getBrainWaveAngle();
	}

	private void decideWhenToLayTheNextEgg() {
		nextEggAge = getAge() + getBody().getEggInterval();
	}

	private boolean isTooYoungToLayEggs() {
		return getAge() < Configuration.CREATURE_MATURE_AGE;
	}

	private void growOlder() {
		age++;
	}
}
