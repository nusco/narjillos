package org.nusco.narjillos.persistence;

import java.util.LinkedList;
import java.util.List;

import org.nusco.narjillos.experiment.Experiment;
import org.nusco.narjillos.experiment.History;
import org.nusco.narjillos.experiment.Stat;

public class NullHistory implements History {

	@Override
	public List<Stat> getStats() {
		return new LinkedList<>();
	}

	@Override
	public Stat getLatestStats() {
		return null;
	}

	@Override
	public void saveStats(Experiment experiment) {
	}

	@Override
	public void close() {
	}
}
