package org.nusco.narjillos.persistence.serialization;

import org.nusco.narjillos.creature.body.pns.Nerve;

import com.google.gson.JsonParseException;

class NerveAdapter extends HierarchyAdapter<Nerve> {

	private static final String NERVE_PACKAGE = Nerve.class.getPackage().getName();

	@Override
	protected String getTypeTag(Nerve nerve) {
		return nerve.getClass().getSimpleName().split("Nerve")[0];
	}

	@Override
	protected Class<?> getClass(String typeTag) throws JsonParseException {
		return getClassForName(NERVE_PACKAGE + "." + typeTag + "Nerve");
	}
}