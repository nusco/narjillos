package org.nusco.narjillos;

import org.nusco.narjillos.ecosystem.Environment;
import org.nusco.narjillos.ecosystem.IsolationChamber;
import org.nusco.narjillos.genomics.GenePool;
import org.nusco.narjillos.shared.utilities.RanGen;

public class IsolationLab extends Lab {

	private Environment environment;
	private GenePool genePool = new GenePool();
	private RanGen ranGen = new RanGen(1234);

	public IsolationLab(CommandLineOptions options) {
		// TODO: I should check this when loading ancestry
		//String applicationVersion = Persistence.readApplicationVersion();
		
		environment = new IsolationChamber(10_000, ranGen);

		// TODO: report loading of ancestry
		System.out.println("ancestry browser (TODO: report loading)");
	}

	public Environment getEnvironment() {
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
