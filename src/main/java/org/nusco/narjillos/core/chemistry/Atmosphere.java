package org.nusco.narjillos.core.chemistry;

import static org.nusco.narjillos.core.chemistry.Element.*;

import java.util.LinkedHashMap;
import java.util.Map;

import org.nusco.narjillos.core.utilities.Configuration;

public class Atmosphere {

	private final double saturationElementLevels;

	private final Map<Element, Double> levels = new LinkedHashMap<>();

	private final int catalystLevel;

	public Atmosphere() {
		this(Configuration.ECOSYSTEM_INITIAL_ELEMENT_LEVEL, Configuration.ECOSYSTEM_CATALYST_LEVEL);
	}

	public Atmosphere(double initialElementLevels, int catalystLevel) {
		this.saturationElementLevels = initialElementLevels * 3;
		levels.put(OXYGEN, initialElementLevels);
		levels.put(HYDROGEN, initialElementLevels);
		levels.put(NITROGEN, initialElementLevels);
		this.catalystLevel = catalystLevel;
	}

	public synchronized double getAmountOf(Element element) {
		if (element == ZERO)
			return 0;

		return levels.get(element);
	}

	public int getCatalystLevel() {
		return catalystLevel;
	}

	public synchronized void convert(Element fromElement, Element toElement) {
		if (fromElement == ZERO || toElement == ZERO)
			return;

		double fromLevel = levels.get(fromElement);
		if (fromLevel > 0) {
			Double toLevel = levels.get(toElement);
			levels.put(fromElement, fromLevel - 1);
			levels.put(toElement, toLevel + 1);
		}
	}

	public synchronized double getDensityOf(Element element) {
		if (element == ZERO)
			return 0.0;

		return levels.get(element) / saturationElementLevels;
	}

	@Override
	public synchronized String toString() {
		return "O: " + levels.get(OXYGEN) + ", H: " + levels.get(HYDROGEN) + ", N: " + levels.get(NITROGEN) + ", X: " + getCatalystLevel();
	}
}
