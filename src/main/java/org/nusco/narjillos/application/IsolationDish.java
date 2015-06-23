package org.nusco.narjillos.application;

import java.util.List;

import org.nusco.narjillos.core.physics.Vector;
import org.nusco.narjillos.core.things.Energy;
import org.nusco.narjillos.core.utilities.RanGen;
import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.ecosystem.IsolationCulture;
import org.nusco.narjillos.genomics.DNA;
import org.nusco.narjillos.genomics.GenePool;
import org.nusco.narjillos.genomics.SimpleGenePool;

/**
 * A dish that isolates a single narjillo in its own dish.
 */
class IsolationDish implements Dish {

	private final List<DNA> dnas;
	private int currentDnaIndex = 0;
	private IsolationCulture culture;
	private GenePool genePool = new SimpleGenePool();
	private RanGen ranGen = new RanGen(1234);

	public IsolationDish(List<DNA> dnas) {
		this.dnas = dnas;
		if (dnas.isEmpty()) {
			System.out.println("Empty germline");
			System.exit(1);
		}
		culture = new IsolationCulture(10_000, ranGen);
	}

	@Override
	public synchronized boolean tick() {
		culture.tick(genePool, ranGen);
		return true;
	}

	@Override
	public void terminate() {
		culture.terminate();
	}

	@Override
	public boolean isBusy() {
		return false;
	}

	public void moveToFirst() {
		currentDnaIndex = 0;
		resetSpecimen();
	}

	public void moveToLast() {
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

	private void resetSpecimen() {
		culture.updateSpecimen(createNarjillo(dnas.get(currentDnaIndex)));
	}
	
	@Override
	public IsolationCulture getCulture() {
		return culture;
	}

	@Override
	public String getDishStatistics() {
		return "" + (currentDnaIndex + 1) + " of " + dnas.size();
	}

	public Narjillo getNarjillo() {
		return getCulture().getNarjillo();
	}

	private Narjillo createNarjillo(DNA dna) {
		Narjillo narjillo = new Narjillo(dna, Vector.ZERO, 180, Energy.INFINITE);
		narjillo.growToAdultForm();
		return narjillo;
	}
}
