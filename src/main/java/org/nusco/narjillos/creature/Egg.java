package org.nusco.narjillos.creature;

import org.nusco.narjillos.core.geometry.Segment;
import org.nusco.narjillos.core.geometry.Vector;
import org.nusco.narjillos.core.things.Energy;
import org.nusco.narjillos.core.things.LifeFormEnergy;
import org.nusco.narjillos.core.things.Thing;
import org.nusco.narjillos.core.utilities.Configuration;
import org.nusco.narjillos.core.utilities.NumGen;
import org.nusco.narjillos.genomics.DNA;

import java.util.Optional;

/**
 * A narjillo egg, that eventually hatches to spawn a cute baby narjillo.
 */
public class Egg implements Thing {

	private static final int NOT_HATCHED_YET = -1;

	private final DNA dna;

	private final int incubationTime;

	private int age = 0;

	private Vector position;

	private Vector velocity;

	private double energy;

	private int hatchAge = NOT_HATCHED_YET;

	private transient Narjillo hatchedNarjillo = null;

	public Egg(DNA dna, Vector position, Vector velocity, double energy, NumGen numGen) {
		this.dna = dna;
		this.incubationTime = calculateIncubationTime(numGen);
		this.position = position;
		this.velocity = velocity;
		this.energy = energy;
	}

	@Override
	public Segment tick() {
		age++;

		if (velocity.getLength() > Configuration.EGG_MIN_VELOCITY) {
			position = position.plus(velocity);
			velocity = velocity.by(Configuration.EGG_VELOCITY_DECAY);
		} else
			velocity = Vector.ZERO;

		return new Segment(position, velocity);
	}

	public boolean hatch(NumGen numGen) {
		if (hasHatched())
			return false;
		if (!hasStopped())
			return false;
		if (age < incubationTime)
			return false;

		hatchAge = age;
		double angle = numGen.nextInt() % 360;
		hatchedNarjillo = new Narjillo(dna, getPosition(), angle, new LifeFormEnergy(energy, Configuration.CREATURE_MAX_LIFESPAN));
		energy = 0;
		return true;
	}

	public Thing getLastInteractingThing() {
		if (hatchedNarjillo == null)
			return Thing.NULL;

		return hatchedNarjillo;
	}

	public Optional<Narjillo> getHatchedNarjillo() {
		if (hatchedNarjillo == null)
			return Optional.empty();

		return Optional.of(hatchedNarjillo);
	}

	@Override
	public Vector getPosition() {
		return position;
	}

	@Override
	public Vector getCenter() {
		return getPosition();
	}

	@Override
	public Energy getEnergy() {
		return new LifeFormEnergy(energy, Double.MAX_VALUE);
	}

	@Override
	public String getLabel() {
		return "egg";
	}

	public DNA getDNA() {
		return dna;
	}

	public boolean isDecayed() {
		return getDecay() >= 1;
	}

	public double getDecay() {
		if (!hasHatched())
			return 0;

		return Math.min(1, Math.max(0, age - hatchAge) / 100.0);
	}

	private boolean hasHatched() {
		return hatchAge != NOT_HATCHED_YET;
	}

	public int getAge() {
		return age;
	}

	public Vector getVelocity() {
		return velocity;
	}

	private boolean hasStopped() {
		return getVelocity().equals(Vector.ZERO);
	}

	@Override
	public double getRadius() {
		return Configuration.EGG_RADIUS;
	}

	public int getIncubationTime() {
		return incubationTime;
	}

	private int calculateIncubationTime(NumGen numGen) {
		final int MAX_INCUBATION_INTERVAL = Configuration.EGG_MAX_INCUBATION_TIME - Configuration.EGG_MIN_INCUBATION_TIME;
		int extraIncubation = (int) (MAX_INCUBATION_INTERVAL * numGen.nextDouble());
		return Configuration.EGG_MIN_INCUBATION_TIME + extraIncubation;
	}
}
