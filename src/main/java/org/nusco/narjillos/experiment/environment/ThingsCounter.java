package org.nusco.narjillos.experiment.environment;

import java.util.HashMap;
import java.util.Map;

class ThingsCounter {

	private final Map<String, Integer> countsByLabel = new HashMap<>();

	public synchronized void add(String label) {
		if (countsByLabel.containsKey(label))
			countsByLabel.put(label, countsByLabel.get(label) + 1);
		else
			countsByLabel.put(label, 1);
	}

	public synchronized void remove(String label) {
		countsByLabel.put(label, countsByLabel.get(label) - 1);
	}

	public synchronized int count(String label) {
		Integer result = countsByLabel.get(label);
		if (result == null)
			return 0;
		return result;
	}
}
