package org.nusco.narjillos.application;

import java.util.List;

import org.nusco.narjillos.core.physics.Vector;
import org.nusco.narjillos.core.things.Energy;
import org.nusco.narjillos.core.utilities.NumGen;
import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.experiment.environment.IsolationEnvironment;
import org.nusco.narjillos.genomics.DNA;
import org.nusco.narjillos.genomics.GenePool;
import org.nusco.narjillos.genomics.SimpleGenePool;

/**
 * A dish that isolates a single narjillo in its own environment.
 */
class IsolationDish implements Dish {

	private final List<DNA> dnas;
	private int currentDnaIndex = 0;
	private IsolationEnvironment environment;
	private GenePool genePool = new SimpleGenePool();
	private NumGen numGen = new NumGen(1234);
	
	public IsolationDish(List<DNA> dnas) {
		this.dnas = dnas;
		if (dnas.isEmpty()) {
			System.out.println("Empty genomes list");
			System.exit(1);
		}
		environment = new IsolationEnvironment(10_000, numGen);
	}

	@Override
	public synchronized boolean tick() {
		environment.setTarget();
		environment.tick(genePool, numGen);
		return true;
	}

	@Override
	public void terminate() {
	}

	@Override
	public boolean isBusy() {
		return false;
	}

	public synchronized void moveToFirst() {
		currentDnaIndex = 0;
		resetSpecimen();
	}

	public synchronized void moveToLast() {
		currentDnaIndex = dnas.size() - 1;
		resetSpecimen();
	}

	public synchronized void moveBack(int by) {
		if (currentDnaIndex == 0)
			return;

		currentDnaIndex -= by;
		currentDnaIndex = Math.max(currentDnaIndex, 0);
		resetSpecimen();
	}

	public synchronized void moveForward(int by) {
		if (currentDnaIndex == dnas.size() - 1)
			return;

		currentDnaIndex += by;
		currentDnaIndex = Math.min(currentDnaIndex, dnas.size() - 1);
		resetSpecimen();
	}

	synchronized void resetSpecimen() {
		environment.updateSpecimen(createNarjillo(dnas.get(currentDnaIndex)));
	}
	
	@Override
	public IsolationEnvironment getEnvironment() {
		return environment;
	}

	@Override
	public String getStatistics() {
		return "" + (currentDnaIndex + 1) + " of " + dnas.size();
	}

	public synchronized Narjillo getNarjillo() {
		return getEnvironment().getNarjillo();
	}

	public synchronized void rotateTarget() {
		getEnvironment().rotateTarget();
	}

	private Narjillo createNarjillo(DNA dna) {
		Narjillo narjillo = new Narjillo(dna, Vector.ZERO, 180, Energy.INFINITE);
		narjillo.growToAdultForm();
		return narjillo;
	}
}
