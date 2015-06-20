package org.nusco.narjillos.application;

import org.nusco.narjillos.core.utilities.RanGen;
import org.nusco.narjillos.ecosystem.Culture;
import org.nusco.narjillos.ecosystem.IsolationCulture;
import org.nusco.narjillos.genomics.GenePool;
import org.nusco.narjillos.genomics.SimpleGenePool;

/**
 * A dish that isolates a single narjillo in its own dish.
 */
class IsolationDish extends Dish {

	private Culture environment;
	private GenePool genePool = new SimpleGenePool();
	private RanGen ranGen = new RanGen(1234);

	public IsolationDish(CommandLineOptions options) {
		// TODO: I should check this when loading ancestry
		//String applicationVersion = Persistence.readApplicationVersion();
		
		environment = new IsolationCulture(10_000, ranGen);

		// TODO: report loading of ancestry
		System.out.println("ancestry browser (TODO: report loading)");
	}

	public Culture getCulture() {
		return environment;
	}

	public boolean tick() {
		environment.tick(genePool, ranGen );
		return true;
	}

	@Override
	public void terminate() {
		environment.terminate();
	}

	@Override
	public boolean isBusy() {
		return false;
	}

	@Override
	public String getPerformanceStatistics() {
		return "";
	}
}
