package org.nusco.narjillos.persistence.db;

import java.util.List;

import org.nusco.narjillos.experiment.Experiment;
import org.nusco.narjillos.experiment.Stat;
import org.nusco.narjillos.genomics.DNA;

public interface History {

	public abstract List<Stat> getStats();
	public abstract Stat getLatestStats();
	public abstract void saveStats(Experiment experiment);
	public abstract DNA getDNA(long dnaId);
	public abstract void save(DNA dna);
	public abstract void close();
}