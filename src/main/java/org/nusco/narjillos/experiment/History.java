package org.nusco.narjillos.experiment;

import java.util.List;

public interface History {

	public abstract List<Stat> getStats();
	public abstract Stat getLatestStats();
	public abstract void saveStats(Experiment experiment);
	public abstract void close();
}