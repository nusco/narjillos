package org.nusco.narjillos.serializer;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.nusco.narjillos.core.chemistry.Element;
import org.nusco.narjillos.core.physics.Vector;
import org.nusco.narjillos.creature.body.Body;
import org.nusco.narjillos.creature.body.BodyPart;
import org.nusco.narjillos.creature.body.ConnectedOrgan;
import org.nusco.narjillos.creature.body.Head;
import org.nusco.narjillos.creature.body.HeadParameters;

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
		
		BodyPart child1 = new BodyPart(11, 12, 130, 131, 132, head, 14, 15, 16, 17);
		head.addChild(child1);
		
		BodyPart child2_1 = new BodyPart(21, 22, 230, 231, 232, child1, 24, 25, 26, 27);
		child1.addChild(child2_1);
		
		BodyPart child2_2 = new BodyPart(31, 32, 330, 331, 332, child1, 34, 35, 36, 37);
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
