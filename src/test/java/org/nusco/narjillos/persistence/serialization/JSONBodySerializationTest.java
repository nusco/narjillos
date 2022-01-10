package org.nusco.narjillos.persistence.serialization;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.nusco.narjillos.core.chemistry.Element;
import org.nusco.narjillos.core.geometry.Vector;
import org.nusco.narjillos.creature.body.*;

public class JSONBodySerializationTest {

	@Test
	public void serializesAndDeserializesBody() {
		var headParameters = new HeadParameters();
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
		var head = new Head(headParameters);

		var bodyPartParameters1 = new BodyPartParameters(11, 12, head, 15);
		bodyPartParameters1.setRedShift(130);
		bodyPartParameters1.setGreenShift(131);
		bodyPartParameters1.setBlueShift(132);
		var child1 = new BodyPart(bodyPartParameters1);
		head.addChild(child1);

		var bodyPartParameters2_1 = new BodyPartParameters(21, 22, child1, 25);
		bodyPartParameters2_1.setRedShift(230);
		bodyPartParameters2_1.setGreenShift(231);
		bodyPartParameters2_1.setBlueShift(232);
		var child2_1 = new BodyPart(bodyPartParameters2_1);
		child1.addChild(child2_1);

		var bodyPartParameters2_2 = new BodyPartParameters(31, 32, child1, 35);
		bodyPartParameters2_2.setRedShift(330);
		bodyPartParameters2_2.setGreenShift(331);
		bodyPartParameters2_2.setBlueShift(332);
		var child2_2 = new BodyPart(bodyPartParameters2_2);
		child1.addChild(child2_2);

		var body = new Body(head);
		body.forcePosition(Vector.cartesian(100, 200), 90);

		String json = JSON.toJson(body, Body.class);
		Body deserialized = JSON.fromJson(json, Body.class);

		// everything still works after ticking
		body.tick(Vector.polar(10, 1));
		deserialized.tick(Vector.polar(10, 1));

		assertThat(deserialized.getStartPoint()).isEqualTo(body.getStartPoint());
		assertThat(deserialized.getMass()).isEqualTo(body.getMass());
		assertThat(deserialized.getRedMass()).isEqualTo(body.getRedMass());
		assertThat(deserialized.getGreenMass()).isEqualTo(body.getGreenMass());
		assertThat(deserialized.getBlueMass()).isEqualTo(body.getBlueMass());

		List<ConnectedOrgan> organs = body.getOrgans();
		List<ConnectedOrgan> deserializedOrgans = deserialized.getOrgans();
		for (int i = 0; i < organs.size(); i++)
			assertThat(deserializedOrgans.get(i).getLength()).isEqualTo(organs.get(i).getLength());
	}
}
