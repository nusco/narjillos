package org.nusco.narjillos.core.chemistry;

public enum Element {
	ZERO,
	OXYGEN,
	HYDROGEN,
	NITROGEN;

	public static final String[] CYCLES = new String[] {
		"O2H",
		"O2N",
		"H2O",
		"H2N",
		"N2O",
		"N2H",
		"Z2O",
		"Z2H",
		"Z2N"
	};

	/**
	 * Deterministically convert any positive integer to an element, but
	 * never return element Zero.
	 */
	public static Element fromInteger(int n) {
		int cardinality = Element.values().length - 1;
		return values()[n % cardinality + 1];
	}

	@Override
	public String toString() {
		return super.toString().substring(0, 1);
	}
}
