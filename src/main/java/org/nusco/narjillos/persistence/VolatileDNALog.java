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
	public int getDnaCount() {
		return idToDna.size();
	}

	@Override
	public List<DNA> getLiveDna() {
		Collections.sort(liveDna, new Comparator<DNA>() {

			@Override
			public int compare(DNA dna1, DNA dna2) {
				return (int) (dna1.getId() - dna2.getId());
			}
		});
		return liveDna;
	}

	@Override
	public void close() {
	}

	@Override
	public void clear() {
		idToDna.clear();
		liveDna.clear();
	}
}
