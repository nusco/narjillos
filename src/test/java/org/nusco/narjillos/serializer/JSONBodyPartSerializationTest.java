package org.nusco.narjillos.serializer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.nusco.narjillos.core.physics.Vector;
import org.nusco.narjillos.creature.body.BodyPart;
import org.nusco.narjillos.creature.body.ConnectedOrgan;
import org.nusco.narjillos.creature.body.Fiber;
import org.nusco.narjillos.creature.body.Head;
import org.nusco.narjillos.creature.body.MovingOrgan;
import org.nusco.narjillos.creature.body.Organ;

public class JSONBodyPartSerializationTest {

	@Test
	public void serializesAndDeserializesHeads() {
		Head head = new Head(1, 2, 10, 20, 30, 4, 0.5, 30, 40);
		head.forcePosition(Vector.cartesian(6, 7), 8);

		for (int i = 0; i < 10; i++)
			head.tick(20, 1, 1);
		
		String json = JSON.toJson(head, Organ.class);
		Organ deserialized = (Head)JSON.fromJson(json, Organ.class);

		assertEquals(head.getLength(), deserialized.getLength(), 0.0);
		assertEquals(head.getThickness(), deserialized.getThickness(), 0.0);
		assertEquals(Vector.cartesian(6, 7), deserialized.getStartPoint());
		assertEquals(8, deserialized.getAbsoluteAngle(), 0.0);
		assertEquals(new Fiber(10, 20, 30), deserialized.getFiber());
		assertEquals(4, ((Head) deserialized).getMetabolicRate(), 0.0);
		assertEquals(0.5, ((Head) deserialized).getEnergyToChildren(), 0.0);
		assertEquals(30, ((Head) deserialized).getEggVelocity());
		assertEquals(40, ((Head) deserialized).getEggInterval());
	}

	@Test
	public void serializesAndDeserializesBodySegments() {
		ConnectedOrgan parent = new Head(10, 20, 0, 0, 0, 40, 0.5, 30, 40);
		BodyPart bodySegment = new BodyPart(1, 2, 10, 20, 30, parent, 4, -5, 6, 7);

		for (int i = 0; i < 10; i++)
			bodySegment.tick(10, 1, 2);
		
		String json = JSON.toJson(bodySegment, Organ.class);
		BodyPart deserialized = (BodyPart)JSON.fromJson(json, Organ.class);
		
		assertEquals(bodySegment.getLength(), deserialized.getLength(), 0);
		assertEquals(bodySegment.getThickness(), deserialized.getThickness(), 0);
		assertEquals(new Fiber(10, 20, 30), deserialized.getFiber());
		assertEquals(4, deserialized.getDelay());
		assertEquals(-5, deserialized.getAngleToParentAtRest(), 0.0);
		assertEquals(-1, deserialized.getOrientation(), 0.0);
		assertEquals(6, deserialized.getAmplitude(), 0.0);
		assertEquals(7, deserialized.getSkewing());
		assertEquals(bodySegment.getMass(), deserialized.getMass(), 0.0);
		assertEquals(bodySegment.getStartPoint(), deserialized.getStartPoint());
		assertEquals(bodySegment.getCenterOfMass(), deserialized.getCenterOfMass());
		assertEquals(bodySegment.getAbsoluteAngle(), deserialized.getAbsoluteAngle(), 0.0);
	}

	@Test
	public void serializesAndDeserializesAnEntireTreeOfOrgans() {
		MovingOrgan parent = new Head(100, 0, 0, 0, 0, 0, 0.5, 30, 40);
		ConnectedOrgan child = new BodyPart(200, 0, 10, 20, 30, parent, 0, 0, 0, 0);
		parent.addChild(child);
		
		String json = JSON.toJson(parent, MovingOrgan.class);
		MovingOrgan deserializedParent = (MovingOrgan) JSON.fromJson(json, MovingOrgan.class);
		
		assertEquals(parent.getLength(), deserializedParent.getLength(), 0);
		assertEquals(1, deserializedParent.getChildren().size());
		ConnectedOrgan deserializedChild = deserializedParent.getChildren().get(0);
		assertEquals(child.getLength(), deserializedChild.getLength(), 0);
		assertSame(deserializedParent, deserializedChild.getParent());

		// everything still works after ticking
		for (int i = 0; i < 3; i++) {
			parent.tick(20, 10, 1);
			deserializedParent.tick(20, 10, 1);
		}
		assertEquals(child.getAbsoluteAngle(), deserializedChild.getAbsoluteAngle(), 0.0);
	}
}
