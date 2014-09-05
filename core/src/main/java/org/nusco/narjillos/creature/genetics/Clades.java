package org.nusco.narjillos.creature.genetics;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Clades implements DNAObserver {

	private Map<DNA, DNA> childsToParents = new LinkedHashMap<>();
	
	@Override
	public void created(DNA newDNA, DNA parent) {
		childsToParents.put(newDNA, parent);
	}

	public List<DNA> getAncestry(DNA dna) {
		LinkedList<DNA> result = new LinkedList<>();
		result.add(dna);
		collectAncestors(result, dna);
		Collections.reverse(result);
		return result;
	}

	private void collectAncestors(LinkedList<DNA> collector, DNA dna) {
		DNA parent = childsToParents.get(dna);
		if (parent == null)
			return;
		collector.add(parent);
		collectAncestors(collector, parent);
	}
}
