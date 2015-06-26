package org.nusco.narjillos.core.chemistry;

import static org.nusco.narjillos.core.chemistry.Element.*;

import java.util.LinkedHashMap;
import java.util.Map;

import org.nusco.narjillos.core.utilities.Configuration;

public class Atmosphere {

	private final int saturationElementLevels;
	private final Map<Element, Integer> levels = new LinkedHashMap<>();
	
	public Atmosphere() {
		this(Configuration.ECOSYSTEM_INITIAL_ELEMENT_LEVEL);
	}
	
	public Atmosphere(int initialElementLevels) {
		this.saturationElementLevels = initialElementLevels * 3;
		levels.put(OXYGEN, initialElementLevels);
		levels.put(HYDROGEN, initialElementLevels);
		levels.put(NITROGEN, initialElementLevels);
	}

	public synchronized int getAmountOf(Element element) {
		if (element == ZERO)
			return 0;
		
		return levels.get(element);
	}

	public synchronized void convert(Element fromElement, Element toElement) {
		if (fromElement == ZERO || toElement == ZERO)
			return;

		int fromLevel = levels.get(fromElement);
		if (fromLevel > 0) {
			Integer toLevel = levels.get(toElement);
			levels.put(fromElement, fromLevel - 1);
			levels.put(toElement, toLevel + 1);
		}
	}

	public synchronized double getDensityOf(Element element) {
		if (element == ZERO)
			return 0.0;

		return ((double) levels.get(element)) / saturationElementLevels;
	}

	public Atmosphere duplicate() {
		Atmosphere result = new Atmosphere();
		result.levels.put(OXYGEN, levels.get(OXYGEN));
		result.levels.put(HYDROGEN, levels.get(HYDROGEN));
		result.levels.put(NITROGEN, levels.get(NITROGEN));
		return result;
	}
	
	@Override
	public synchronized String toString() {
		return "O: " + levels.get(OXYGEN) + ", H: " + levels.get(HYDROGEN) + ", N: " + levels.get(NITROGEN);
	}
}
