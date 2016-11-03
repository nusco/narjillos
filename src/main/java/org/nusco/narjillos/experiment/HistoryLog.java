package org.nusco.narjillos.experiment;

import java.util.List;

/**
 * A list of historical statistics.
 */
public interface HistoryLog {

	public List<ExperimentHistoryEntry> getEntries();

	public ExperimentHistoryEntry getLatestEntry();

	public void saveEntry(Experiment experiment);

	public void close();

	public void delete();
}