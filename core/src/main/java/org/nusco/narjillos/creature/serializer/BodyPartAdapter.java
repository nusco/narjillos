package org.nusco.narjillos.creature.serializer;

import org.nusco.narjillos.creature.body.BodyPart;
import org.nusco.narjillos.creature.body.Head;
import org.nusco.narjillos.creature.body.Organ;

import com.google.gson.JsonParseException;

class BodyPartAdapter extends HierarchyAdapter<Organ> {

	private static final String BODY_PACKAGE = BodyPart.class.getPackage().getName();

	@Override
	protected String getTypeTag(Organ obj) {
		if (obj instanceof Head)
			return "Head";
		else
			return "BodySegment";
	}

	@Override
	protected Class<?> getClass(String typeTag) throws JsonParseException {
		return getClassForName(BODY_PACKAGE + "." + typeTag);
	}

	@Override
	protected void preSerialize(Organ obj) {
		obj.updateCaches();
	}

	@Override
	protected void postDeserialize(Organ obj) {
		for (Organ child : obj.getChildren())
			child.setParent(obj);
	}
}
