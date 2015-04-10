package org.nusco.narjillos.serializer;

import org.nusco.narjillos.creature.body.ConnectedOrgan;
import org.nusco.narjillos.creature.body.Head;
import org.nusco.narjillos.creature.body.MovingOrgan;
import org.nusco.narjillos.creature.body.Organ;

import com.google.gson.JsonParseException;

class OrganAdapter extends HierarchyAdapter<MovingOrgan> {

	private static final String BODY_PACKAGE = Organ.class.getPackage().getName();

	@Override
	protected String getTypeTag(MovingOrgan obj) {
		if (obj instanceof Head)
			return "Head";
		else
			return "BodyPart";
	}

	@Override
	protected Class<?> getClass(String typeTag) throws JsonParseException {
		return getClassForName(BODY_PACKAGE + "." + typeTag);
	}

	@Override
	protected void preSerialize(MovingOrgan obj) {
		obj.updateGeometry();
	}

	@Override
	protected void postDeserialize(MovingOrgan obj) {
		for (ConnectedOrgan child : obj.getChildren())
			child.setParent(obj);
	}
}
