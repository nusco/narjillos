package org.nusco.narjillos.genomics;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class VolatileDNALog implements DNALog {

	private final Map<Long, DNA> idToDna = new LinkedHashMap<>();

	private final List<DNA> liveDna = new LinkedList<>();

	@Override
	public void save(DNA dna) {
		idToDna.put(dna.getId(), dna);
		liveDna.add(dna);
	}

	@Override
	public DNA getDna(long id) {
		return idToDna.get(id);
	}

	@Override
	public void markAsDead(long id) {
		liveDna.remove(getDna(id));
	}

	@Override
	public List<DNA> getAllDna() {
		LinkedList<DNA> result = new LinkedList<>(idToDna.values());
		result.sort((dna1, dna2) -> (int) (dna1.getId() - dna2.getId()));
		return result;
	}

	@Override
	public List<DNA> getLiveDna() {
		liveDna.sort((dna1, dna2) -> (int) (dna1.getId() - dna2.getId()));
		return liveDna;
	}

	@Override
	public void delete() {
		idToDna.clear();
		liveDna.clear();
	}
}
