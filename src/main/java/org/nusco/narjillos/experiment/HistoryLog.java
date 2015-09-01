package org.nusco.narjillos.experiment;

import java.util.List;

/**
 * A list of stats.
 */
public interface HistoryLog {

	public abstract List<ExperimentHistoryEntry> getEntries();
	public abstract ExperimentHistoryEntry getLatestEntry();
	public abstract void saveEntries(Experiment experiment);
	public abstract void close();
	public abstract void delete();
}