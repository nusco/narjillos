package org.nusco.narjillos.genomics;

import java.util.List;

public interface DNALog {

	public abstract void save(DNA dna);
	public abstract DNA getDna(long id);
	public abstract void markAsDead(long id);
	public abstract List<DNA> getAllDna();
	public abstract List<Long> getAliveDna();
	public abstract void close();
	public abstract void delete();
}