package org.nusco.narjillos.experiment;

import java.util.List;

/**
 * A list of historical statistics.
 */
public interface HistoryLog {

	List<ExperimentHistoryEntry> getEntries();

	ExperimentHistoryEntry getLatestEntry();

	void saveEntry(Experiment experiment);
}