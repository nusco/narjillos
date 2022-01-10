package org.nusco.narjillos.persistence.serialization;

import static org.assertj.core.api.Assertions.assertThat;
import static org.nusco.narjillos.core.chemistry.Element.NITROGEN;

import org.junit.jupiter.api.Test;
import org.nusco.narjillos.core.chemistry.Element;
import org.nusco.narjillos.core.geometry.Vector;
import org.nusco.narjillos.creature.body.*;

public class JSONBodyPartSerializationTest {

	@Test
	public void serializesAndDeserializesHeads() {
		var parameters = new HeadParameters();
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

		var head = new Head(parameters);
		head.forcePosition(Vector.cartesian(6, 7), 8);

		for (int i = 0; i < 10; i++)
			head.tick(20, 1, 1);

		String json = JSON.toJson(head, Organ.class);
		Organ deserialized = JSON.fromJson(json, Organ.class);

		assertThat(deserialized.getLength()).isEqualTo(head.getLength());
		assertThat(deserialized.getThickness()).isEqualTo(head.getThickness());
		assertThat(deserialized.getStartPoint()).isEqualTo(Vector.cartesian(6, 7));
		assertThat(deserialized.getAbsoluteAngle()).isEqualTo(8.0);
		assertThat(deserialized.getFiber()).isEqualTo(new Fiber(10, 20, 30));

		Head desHead = (Head) deserialized;
		assertThat(desHead.getMetabolicRate()).isEqualTo(4.0);
		assertThat(desHead.getByproduct()).isEqualTo(NITROGEN);
		assertThat(desHead.getEnergyToChildren()).isEqualTo(0.5);
		assertThat(desHead.getEggVelocity()).isEqualTo(30);
		assertThat(desHead.getEggInterval()).isEqualTo(40);
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

		var bodySegment = new BodyPart(bodyPartParameters);

		for (int i = 0; i < 10; i++)
			bodySegment.tick(10, 1, 2);

		String json = JSON.toJson(bodySegment, Organ.class);
		BodyPart deserialized = (BodyPart) JSON.fromJson(json, Organ.class);

		assertThat(deserialized.getLength()).isEqualTo(bodySegment.getLength());
		assertThat(deserialized.getThickness()).isEqualTo(bodySegment.getThickness());
		assertThat(deserialized.getFiber()).isEqualTo(new Fiber(10, 20, 30));
		assertThat(deserialized.getDelay()).isEqualTo(4);
		assertThat(deserialized.getAngleToParentAtRest()).isEqualTo(-5.0);
		assertThat(deserialized.getOrientation()).isEqualTo(-1);
		assertThat(deserialized.getAmplitude()).isEqualTo(6);
		assertThat(deserialized.getSkewing()).isEqualTo(7);
		assertThat(deserialized.getMass()).isEqualTo(bodySegment.getMass());
		assertThat(deserialized.getStartPoint()).isEqualTo(bodySegment.getStartPoint());
		assertThat(deserialized.getCenterOfMass()).isEqualTo(bodySegment.getCenterOfMass());
		assertThat(deserialized.getAbsoluteAngle()).isEqualTo(bodySegment.getAbsoluteAngle());
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
		MovingOrgan deserializedParent = JSON.fromJson(json, MovingOrgan.class);

		assertThat(deserializedParent.getLength()).isEqualTo(parent.getLength());
		assertThat(deserializedParent.getChildren()).hasSize(1);

		ConnectedOrgan deserializedChild = deserializedParent.getChildren().get(0);
		assertThat(deserializedChild.getLength()).isEqualTo(child.getLength());
		assertThat(deserializedChild.getParent()).isEqualTo(deserializedParent);

		// everything still works after ticking
		for (int i = 0; i < 3; i++) {
			parent.tick(20, 10, 1);
			deserializedParent.tick(20, 10, 1);
		}
		assertThat(deserializedChild.getAbsoluteAngle()).isEqualTo(child.getAbsoluteAngle());
	}
}
