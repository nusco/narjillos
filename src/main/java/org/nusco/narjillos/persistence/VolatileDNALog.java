package org.nusco.narjillos.persistence;

import java.util.LinkedList;
import java.util.List;

import org.nusco.narjillos.genomics.DNA;
import org.nusco.narjillos.genomics.DNALog;

public class VolatileDNALog implements DNALog {
	
	@Override
	public void save(DNA dna) {
	}

	@Override
	public DNA getDNA(long id) {
		return null;
	}

	@Override
	public List<DNA> getAllDNA() {
		return new LinkedList<DNA>();
	}
}
