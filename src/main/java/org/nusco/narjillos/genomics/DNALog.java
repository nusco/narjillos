package org.nusco.narjillos.genomics;

import java.util.List;

public interface DNALog {

	public abstract void save(DNA dna);
	public abstract DNA getDNA(long id);
	public abstract List<DNA> getAllDNA();
}