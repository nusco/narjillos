package org.nusco.narjillos.creature;

import org.nusco.narjillos.embryogenesis.Embryo;
import org.nusco.narjillos.genomics.DNA;
import org.nusco.narjillos.shared.physics.Segment;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.things.Energy;
import org.nusco.narjillos.shared.things.LifeFormEnergy;
import org.nusco.narjillos.shared.things.Thing;
import org.nusco.narjillos.shared.utilities.Configuration;
import org.nusco.narjillos.shared.utilities.RanGen;

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
	// TODO: make transient?
	private Narjillo hatchedNarjillo = null;
	private int hatchAge = NOT_HATCHED_YET;
	
	public Egg(DNA dna, Vector position, Vector velocity, double energy, RanGen ranGen) {
		this.dna = dna;
		this.incubationTime = calculateIncubationTime(ranGen);
		this.position = position;
		this.velocity = velocity;
		this.energy = energy;
	}

	public Segment tick() {
		age++;

		if (velocity.getLength() > Configuration.EGG_MIN_VELOCITY) {
			position = position.plus(velocity);
			velocity = velocity.by(Configuration.EGG_VELOCITY_DECAY);
		} else
			velocity = Vector.ZERO;
		
		return new Segment(position, velocity);
	}

	public boolean hatch() {
		if (hasHatched())
			return false;
		if (!hasStopped())
			return false;
		if (age < incubationTime)
			return false;

		
		hatchAge = age;
		hatchedNarjillo = new Narjillo(dna, new Embryo(dna).develop(), getPosition(), new LifeFormEnergy(energy, Configuration.CREATURE_MAX_LIFESPAN));
		energy = 0;
		return true;
	}

	public Narjillo getHatchedNarjillo() {
		return hatchedNarjillo;
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

	private int calculateIncubationTime(RanGen ranGen) {
		final int MAX_INCUBATION_INTERVAL = Configuration.EGG_MAX_INCUBATION_TIME - Configuration.EGG_MIN_INCUBATION_TIME;
		int extraIncubation = (int) (MAX_INCUBATION_INTERVAL * ranGen.nextDouble());
		return Configuration.EGG_MIN_INCUBATION_TIME + extraIncubation;
	}
}
