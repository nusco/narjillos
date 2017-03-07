package org.nusco.narjillos.genomics;

import java.util.List;

public interface DNALog {

	void save(DNA dna);

	DNA getDna(long id);

	void markAsDead(long id);

	List<DNA> getAllDna();

	List<DNA> getLiveDna();

	void close();

	void delete();
}