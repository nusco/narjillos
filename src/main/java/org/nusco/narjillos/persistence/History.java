package org.nusco.narjillos.persistence;

import java.util.List;

import org.nusco.narjillos.experiment.Experiment;
import org.nusco.narjillos.experiment.Stat;

public interface History {

	public abstract List<Stat> getStats();
	public abstract Stat getLatestStats();
	public abstract void saveStats(Experiment experiment);
	public abstract void close();
}