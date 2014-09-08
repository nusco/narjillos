package org.nusco.narjillos.serializer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.nusco.narjillos.creature.body.BodyPart;
import org.nusco.narjillos.creature.body.BodySegment;
import org.nusco.narjillos.creature.body.Head;
import org.nusco.narjillos.creature.body.Organ;
import org.nusco.narjillos.serializer.JSON;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.utilities.ColorByte;

public class JSONBodyPartSerializationTest {

	@Test
	public void serializesAndDeserializesHeads() {
		Head head = new Head(1, 2, new ColorByte(3), 4, 0.5);
		head.moveTo(Vector.cartesian(6, 7), 8);

		String json = JSON.toJson(head, BodyPart.class);
		BodyPart deserialized = (Head)JSON.fromJson(json, BodyPart.class);
		
		assertEquals(1, deserialized.getLength());
		assertEquals(2, deserialized.getThickness());
		assertEquals(3, deserialized.getColor().toByteSizedInt());
		assertEquals(4, ((Head) deserialized).getMetabolicRate(), 0.0);
		assertEquals(0.5, ((Head) deserialized).getPercentEnergyToChildren(), 0.0);
		assertEquals(Vector.cartesian(6, 7), deserialized.getStartPoint());
		assertEquals(8, deserialized.getAbsoluteAngle(), 0.0);
	}

	@Test
	public void serializesAndDeserializesBodySegments() {
		Organ parent = new Head(10, 20, new ColorByte(0), 40, 0.5);
		BodyPart bodyPart = new BodySegment(1, 2, new ColorByte(3), parent, 4, -5, 6, 7);
		bodyPart.updateCaches();
		
		String json = JSON.toJson(bodyPart, BodyPart.class);
		BodySegment deserialized = (BodySegment)JSON.fromJson(json, BodyPart.class);
		
		assertEquals(1, deserialized.getLength());
		assertEquals(2, deserialized.getThickness());
		assertEquals(3, deserialized.getColor().toByteSizedInt());
		assertEquals(4, deserialized.getDelay());
		assertEquals(-5, deserialized.getAngleToParentAtRest(), 0.0);
		assertEquals(-1, deserialized.getOrientation(), 0.0);
		assertEquals(6, deserialized.getAmplitude(), 0.0);
		assertEquals(7, deserialized.getSkewing());
		assertEquals(2, deserialized.getMass(), 0.0);
		assertEquals(bodyPart.getStartPoint(), deserialized.getStartPoint());
		assertEquals(bodyPart.getCenterOfMass(), deserialized.getCenterOfMass());
		assertEquals(bodyPart.getAbsoluteAngle(), deserialized.getAbsoluteAngle(), 0.0);
	}

	@Test
	public void serializesAndDeserializesAnEntireTreeOfOrgans() {
		Organ parent = new Head(100, 0, new ColorByte(0), 0, 0.5);
		Organ child = new BodySegment(200, 0, new ColorByte(0), parent, 0, 0, 0, 0);
		parent.addChild(child);
		
		String json = JSON.toJson(parent, Organ.class);
		Organ deserializedParent = (Organ) JSON.fromJson(json, Organ.class);
		
		assertEquals(100, deserializedParent.getLength());
		assertEquals(1, deserializedParent.getChildren().size());
		Organ deserializedChild = deserializedParent.getChildren().get(0);
		assertEquals(200, deserializedChild.getLength());
		assertSame(deserializedParent, deserializedChild.getParent());

		// everything still works after ticking
		for (int i = 0; i < 3; i++) {
			parent.recursivelyUpdateAngleToParent(10, 20);
			deserializedParent.recursivelyUpdateAngleToParent(10, 20);
		}
		assertEquals(child.getAbsoluteAngle(), deserializedChild.getAbsoluteAngle(), 0.0);
	}
}
