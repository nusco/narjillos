package org.nusco.narjillos.ecosystem.chemistry;

public enum Element {
	ZERO,
	OXYGEN,
	HYDROGEN,
	NITROGEN;
	
	/**
	 * Deterministically converts any positive integer to an element, but
	 * never returns element Zero.
	 */
	public static Element fromInteger(int n) {
		int cardinality = Element.values().length - 1;
		return values()[n % cardinality + 1];
	};

	@Override
	public String toString() {
		return super.toString().substring(0, 1);
	}
}
