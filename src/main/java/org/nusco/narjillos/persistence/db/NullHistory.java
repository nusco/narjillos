package org.nusco.narjillos.persistence.db;

import java.util.LinkedList;
import java.util.List;

import org.nusco.narjillos.experiment.Experiment;
import org.nusco.narjillos.experiment.Stat;
import org.nusco.narjillos.genomics.DNA;

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
	public DNA getDNA(long id) {
		return null;
	}

	@Override
	public void save(DNA dna) {
	}

	@Override
	public void close() {
	}
}
