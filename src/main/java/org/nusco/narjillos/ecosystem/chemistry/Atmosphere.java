package org.nusco.narjillos.ecosystem.chemistry;

import static org.nusco.narjillos.ecosystem.chemistry.Element.*;

import java.util.LinkedHashMap;
import java.util.Map;

import org.nusco.narjillos.core.utilities.Configuration;

public class Atmosphere {

	private static final double SATURATION_LEVEL = Configuration.ECOSYSTEM_INITIAL_ELEMENT_LEVEL * 3;
	
	public static int getInitialElementLevel() {
		return Configuration.ECOSYSTEM_INITIAL_ELEMENT_LEVEL;
	}

	private final Map<Element, Integer> levels = new LinkedHashMap<>();
	
	public Atmosphere() {
		levels.put(OXYGEN, getInitialElementLevel());
		levels.put(HYDROGEN, getInitialElementLevel());
		levels.put(NITROGEN, getInitialElementLevel());
	}
	
	public int getElementLevel(Element element) {
		return levels.get(element);
	}

	public double convert(Element element) {
		double density = getDensityOf(element);
		
		Integer elementLevel = levels.get(element);
		if (elementLevel > 0) {
			levels.put(element, elementLevel - 1);
			
			Element byproduct = element.getByproduct();
			levels.put(byproduct, levels.get(byproduct) + 1);
		}
		
		return density;
	}

	private double getDensityOf(Element element) {
		return levels.get(element) / SATURATION_LEVEL;
	}
}
