package org.nusco.narjillos.creature;

import org.nusco.narjillos.embryogenesis.Embryo;
import org.nusco.narjillos.genomics.DNA;
import org.nusco.narjillos.shared.physics.Segment;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.things.Energy;
import org.nusco.narjillos.shared.things.Thing;
import org.nusco.narjillos.shared.utilities.Configuration;

/**
 * A narjillo egg, that eventually hatches to spawn a cute baby narjillo.
 */
public class Egg implements Thing {

	private final DNA dna;
	private int age = 0;
	private Vector position;
	private Narjillo hatchedNarjillo = null;
	private double energy;
	private final int incubationTime;
	
	public Egg(DNA dna, Vector position, double energy, int incubationTime) {
		this.dna = dna;
		this.position = position;
		this.energy = energy;
		this.incubationTime = incubationTime;
	}

	public Segment tick() {
		age++;
		return null;
	}

	public boolean hatch() {
		if (age < incubationTime)
			return false;
		if (hatchedNarjillo != null)
			return false;

		hatchedNarjillo = new Narjillo(dna, new Embryo(dna).develop(), getPosition(), energy);
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
		return new Energy(energy, Double.MAX_VALUE);
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
		return Math.min(1, Math.max(0, age - incubationTime) / 100.0);
	}

	public int getAge() {
		return age;
	}

	@Override
	public double getRadius() {
		return Configuration.EGG_RADIUS;
	}
}
