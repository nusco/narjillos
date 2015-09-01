package org.nusco.narjillos.persistence;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.nusco.narjillos.genomics.DNA;
import org.nusco.narjillos.genomics.DNALog;

public class VolatileDNALog implements DNALog {

	private final Map<Long, DNA> idToDna= new LinkedHashMap<>();
	private final List<Long> aliveDna = new LinkedList<>();
	
	@Override
	public void save(DNA dna) {
		idToDna.put(dna.getId(), dna);
		aliveDna.add(dna.getId());
	}

	@Override
	public DNA getDna(long id) {
		return idToDna.get(id);
	}

	@Override
	public void markAsDead(long id) {
		aliveDna.remove(id);
	}

	@Override
	public List<DNA> getAllDna() {
		LinkedList<DNA> result = new LinkedList<DNA>(idToDna.values());
		Collections.sort(result, new Comparator<DNA>() {

			@Override
			public int compare(DNA dna1, DNA dna2) {
				return (int) (dna1.getId() - dna2.getId());
			}
		});
		return result;
	}
	
	@Override
	public List<Long> getAliveDna() {
		Collections.sort(aliveDna, new Comparator<Long>() {

			@Override
			public int compare(Long id1, Long id2) {
				return (int) (id1 - id2);
			}
		});
		return aliveDna;
	}

	@Override
	public void close() {
	}

	@Override
	public void delete() {
		idToDna.clear();
		aliveDna .clear();
	}
}
