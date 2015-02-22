package org.nusco.narjillos.serializer;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.nusco.narjillos.creature.body.Body;
import org.nusco.narjillos.creature.body.BodyPart;
import org.nusco.narjillos.creature.body.Head;
import org.nusco.narjillos.creature.body.Organ;
import org.nusco.narjillos.shared.physics.Vector;

public class JSONBodySerializationTest {

	@Test
	public void serializesAndDeserializesBody() {
		Head head = new Head(1, 2, 30, 31, 32, 4, 0.5);
		
		BodyPart child1 = new BodyPart(11, 12, 130, 131, 132, head, 14, 15, 16, 17);
		head.addChild(child1);
		
		BodyPart child2_1 = new BodyPart(21, 22, 230, 231, 232, child1, 24, 25, 26, 27);
		child1.addChild(child2_1);
		
		BodyPart child2_2 = new BodyPart(31, 32, 330, 331, 332, child1, 34, 35, 36, 37);
		child1.addChild(child2_2);
		
		Body body = new Body(head);
		body.teleportTo(Vector.cartesian(100, 200));

		String json = JSON.toJson(body, Body.class);
		Body deserialized = JSON.fromJson(json, Body.class);

		// everything still works after ticking
		body.tick(Vector.polar(10, 1));
		deserialized.tick(Vector.polar(10, 1));
		
		assertEquals(body.getStartPoint(), deserialized.getStartPoint());
		assertEquals(body.getMass(), deserialized.getMass(), 0.0);
		
		List<Organ> organs = body.getOrgans();
		List<Organ> deserializedOrgans = deserialized.getOrgans();
		for (int i = 0; i < organs.size(); i++)
			assertEquals(organs.get(i).getLength(), deserializedOrgans.get(i).getLength(), 0);
	}
}
