package org.nusco.narjillos.core.chemistry;

public enum Element {
	ZERO,
	OXYGEN,
	HYDROGEN,
	NITROGEN;

	public static final String[] CYCLES = new String[] {
		"O->H",
		"O->N",
		"H->O",
		"H->N",
		"N->O",
		"N->H",
		"Z->O",
		"Z->H",
		"Z->N"
	};

	/**
	 * Deterministically converts any positive integer to an element, but
	 * never returns element Zero.
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
