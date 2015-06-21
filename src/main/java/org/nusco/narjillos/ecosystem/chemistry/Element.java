package org.nusco.narjillos.ecosystem.chemistry;

public enum Element {
	OXYGEN,
	HYDROGEN,
	NITROGEN;
	
	@Override
	public String toString() {
		switch (this) {
		case OXYGEN:
			return "O";
		case HYDROGEN:
			return "H";
		case NITROGEN:
			return "N";
		}
		return null;
	};
}
