package org.nusco.narjillos.ecosystem.chemistry;

import static org.nusco.narjillos.ecosystem.chemistry.Element.*;

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

	public int getElementLevel(Element element) {
		return levels.get(element);
	}

	public void convert(Element fromElement, Element toElement) {
		Integer fromLevel = levels.get(fromElement);
		if (fromLevel > 0) {
			levels.put(fromElement, Math.max(0, fromLevel - 1));
			
			levels.put(toElement, Math.min(saturationElementLevels, levels.get(toElement) + 1));
		}
	}

	public double getDensityOf(Element element) {
		return ((double) levels.get(element)) / saturationElementLevels;
	}

	public Atmosphere duplicate() {
		Atmosphere result = new Atmosphere();
		result.levels.put(OXYGEN, levels.get(OXYGEN));
		result.levels.put(HYDROGEN, levels.get(HYDROGEN));
		result.levels.put(NITROGEN, levels.get(NITROGEN));
		return result;
	}
}
