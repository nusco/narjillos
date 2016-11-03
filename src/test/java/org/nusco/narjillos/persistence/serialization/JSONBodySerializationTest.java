package org.nusco.narjillos.persistence.serialization;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.nusco.narjillos.core.chemistry.Element;
import org.nusco.narjillos.core.geometry.Vector;
import org.nusco.narjillos.creature.body.Body;
import org.nusco.narjillos.creature.body.BodyPart;
import org.nusco.narjillos.creature.body.BodyPartParameters;
import org.nusco.narjillos.creature.body.ConnectedOrgan;
import org.nusco.narjillos.creature.body.Head;
import org.nusco.narjillos.creature.body.HeadParameters;
import org.nusco.narjillos.persistence.serialization.JSON;

public class JSONBodySerializationTest {

	@Test
	public void serializesAndDeserializesBody() {
		HeadParameters headParameters = new HeadParameters();
		headParameters.setAdultLength(1);
		headParameters.setAdultThickness(2);
		headParameters.setRed(10);
		headParameters.setGreen(20);
		headParameters.setBlue(30);
		headParameters.setMetabolicRate(4);
		headParameters.setWaveBeatRatio(2);
		headParameters.setByproduct(Element.NITROGEN);
		headParameters.setEnergyToChildren(0.5);
		headParameters.setEggVelocity(30);
		headParameters.setEggInterval(40);
		Head head = new Head(headParameters);

		BodyPartParameters bodyPartParameters1 = new BodyPartParameters(11, 12, head, 15);
		bodyPartParameters1.setRedShift(130);
		bodyPartParameters1.setGreenShift(131);
		bodyPartParameters1.setBlueShift(132);
		BodyPart child1 = new BodyPart(bodyPartParameters1);
		head.addChild(child1);

		BodyPartParameters bodyPartParameters2_1 = new BodyPartParameters(21, 22, child1, 25);
		bodyPartParameters2_1.setRedShift(230);
		bodyPartParameters2_1.setGreenShift(231);
		bodyPartParameters2_1.setBlueShift(232);
		BodyPart child2_1 = new BodyPart(bodyPartParameters2_1);
		child1.addChild(child2_1);

		BodyPartParameters bodyPartParameters2_2 = new BodyPartParameters(31, 32, child1, 35);
		bodyPartParameters2_2.setRedShift(330);
		bodyPartParameters2_2.setGreenShift(331);
		bodyPartParameters2_2.setBlueShift(332);
		BodyPart child2_2 = new BodyPart(bodyPartParameters2_2);
		child1.addChild(child2_2);

		Body body = new Body(head);
		body.forcePosition(Vector.cartesian(100, 200), 90);

		String json = JSON.toJson(body, Body.class);
		Body deserialized = JSON.fromJson(json, Body.class);

		// everything still works after ticking
		body.tick(Vector.polar(10, 1));
		deserialized.tick(Vector.polar(10, 1));

		assertEquals(body.getStartPoint(), deserialized.getStartPoint());
		assertEquals(body.getMass(), deserialized.getMass(), 0.0);
		assertEquals(body.getRedMass(), deserialized.getRedMass(), 0.0);
		assertEquals(body.getGreenMass(), deserialized.getGreenMass(), 0.0);
		assertEquals(body.getBlueMass(), deserialized.getBlueMass(), 0.0);

		List<ConnectedOrgan> organs = body.getOrgans();
		List<ConnectedOrgan> deserializedOrgans = deserialized.getOrgans();
		for (int i = 0; i < organs.size(); i++)
			assertEquals(organs.get(i).getLength(), deserializedOrgans.get(i).getLength(), 0);
	}
}
