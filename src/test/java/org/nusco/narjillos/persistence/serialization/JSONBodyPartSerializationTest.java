package org.nusco.narjillos.persistence.serialization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.nusco.narjillos.core.chemistry.Element;
import org.nusco.narjillos.core.geometry.Vector;
import org.nusco.narjillos.creature.body.BodyPart;
import org.nusco.narjillos.creature.body.BodyPartParameters;
import org.nusco.narjillos.creature.body.ConnectedOrgan;
import org.nusco.narjillos.creature.body.Fiber;
import org.nusco.narjillos.creature.body.Head;
import org.nusco.narjillos.creature.body.HeadParameters;
import org.nusco.narjillos.creature.body.MovingOrgan;
import org.nusco.narjillos.creature.body.Organ;
import org.nusco.narjillos.persistence.serialization.JSON;

public class JSONBodyPartSerializationTest {

	@Test
	public void serializesAndDeserializesHeads() {
		HeadParameters parameters = new HeadParameters();
		parameters.setAdultLength(1);
		parameters.setAdultThickness(2);
		parameters.setRed(10);
		parameters.setGreen(20);
		parameters.setBlue(30);
		parameters.setMetabolicRate(4);
		parameters.setWaveBeatRatio(2);
		parameters.setByproduct(Element.NITROGEN);
		parameters.setEnergyToChildren(0.5);
		parameters.setEggVelocity(30);
		parameters.setEggInterval(40);
		Head head = new Head(parameters);
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
		assertEquals(Element.NITROGEN, ((Head) deserialized).getByproduct());
		assertEquals(0.5, ((Head) deserialized).getEnergyToChildren(), 0.0);
		assertEquals(30, ((Head) deserialized).getEggVelocity());
		assertEquals(40, ((Head) deserialized).getEggInterval());
	}

	@Test
	public void serializesAndDeserializesBodySegments() {
		ConnectedOrgan parent = new Head(new HeadParameters(10, 20));
		BodyPartParameters bodyPartParameters = new BodyPartParameters(1, 2, parent, -5);
		bodyPartParameters.setDelay(4);
		bodyPartParameters.setAmplitude(6);
		bodyPartParameters.setSkewing(7);
		bodyPartParameters.setRedShift(10);
		bodyPartParameters.setGreenShift(20);
		bodyPartParameters.setBlueShift(30);
		BodyPart bodySegment = new BodyPart(bodyPartParameters);

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
		MovingOrgan parent = new Head(new HeadParameters(100, 0));
		BodyPartParameters bodyPartParameters = new BodyPartParameters(200, 0, parent, 0);
		bodyPartParameters.setRedShift(10);
		bodyPartParameters.setGreenShift(20);
		bodyPartParameters.setBlueShift(30);
		ConnectedOrgan child = new BodyPart(bodyPartParameters);
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
