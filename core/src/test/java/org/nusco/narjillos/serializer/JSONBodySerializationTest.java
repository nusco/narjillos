package org.nusco.narjillos.serializer;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.nusco.narjillos.creature.body.Body;
import org.nusco.narjillos.creature.body.BodyPart;
import org.nusco.narjillos.creature.body.BodySegment;
import org.nusco.narjillos.creature.body.Head;
import org.nusco.narjillos.serializer.JSON;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.utilities.ColorByte;

public class JSONBodySerializationTest {

	@Test
	public void serializesAndDeserializesBody() {
		Head head = new Head(1, 2, new ColorByte(3), 4, 0.5);
		
		BodySegment child1 = new BodySegment(11, 12, new ColorByte(13), head, 14, 15, 16, 17);
		head.addChild(child1);
		
		BodySegment child2_1 = new BodySegment(21, 22, new ColorByte(23), child1, 24, 25, 26, 27);
		child1.addChild(child2_1);
		
		BodySegment child2_2 = new BodySegment(31, 32, new ColorByte(33), child1, 34, 35, 36, 37);
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

		List<BodyPart> bodyParts = body.getBodyParts();
		List<BodyPart> deserializedBodyParts = deserialized.getBodyParts();
		for (int i = 0; i < bodyParts.size(); i++)
			assertEquals(bodyParts.get(i).getLength(), deserializedBodyParts.get(i).getLength());
	}
}
