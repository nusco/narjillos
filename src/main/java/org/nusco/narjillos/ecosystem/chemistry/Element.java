package org.nusco.narjillos.ecosystem.chemistry;

public enum Element {
	OXYGEN,
	HYDROGEN,
	NITROGEN;

	public Element getByproduct() {
		switch (this) {
		case OXYGEN:
			return HYDROGEN;
		case HYDROGEN:
			return NITROGEN;
		case NITROGEN:
			return OXYGEN;
		}
		return null;
	}
}
