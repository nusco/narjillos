package org.nusco.narjillos.genomics;

import java.util.List;

public interface DNALog {

	public void save(DNA dna);

	public DNA getDna(long id);

	public void markAsDead(long id);

	public List<DNA> getAllDna();

	public List<DNA> getLiveDna();

	public void close();

	public void delete();
}