package org.nusco.narjillos.creature.body;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.nusco.narjillos.core.geometry.Vector;

public class BodyPartTest extends ConnectedOrganTest {

	private MovingOrgan parent;

	@Override
	public ConnectedOrgan createConcreteOrgan(int length, int thickness) {
		parent = new Head(new HeadParameters());
		return new BodyPart(new BodyPartParameters(length, thickness, parent, 0));
	}

	@Override
	public void hasAParent() {
		assertThat(getOrgan().getParent()).isEqualTo(parent);
	}

	@Override
	public void hasAnEndPoint() {
		var head = new Head(new HeadParameters(10, 0));
		ConnectedOrgan organ1 = head.addChild(new BodyPart(new BodyPartParameters(10, 0, head, 90)));
		ConnectedOrgan organ2 = organ1.addChild(new BodyPart(new BodyPartParameters(10, 0, organ1, -90)));

		head.growToAdultFormWithChildren();
		organ1.growToAdultFormWithChildren();
		organ2.growToAdultFormWithChildren();

		assertThat(organ2.getEndPoint()).isEqualTo(Vector.cartesian(20, 10));
	}

	@Test
	public void startsAtItsParentsEndPoint() {
		parent.update();
		getOrgan().update();

		assertThat(getOrgan().getStartPoint()).isEqualTo(parent.getEndPoint());
	}

	@Test
	public void hasAnAbsoluteAngle() {
		var head = new Head(new HeadParameters());
		ConnectedOrgan organ1 = new BodyPart(new BodyPartParameters(0, 0, head, 30));
		Organ organ2 = new BodyPart(new BodyPartParameters(0, 0, organ1, -10));

		head.update();
		organ1.update();
		organ2.update();

		assertThat(organ2.getAbsoluteAngle()).isEqualTo(20);
	}

	@Test
	public void hasAnAmplitude() {
		var head = new Head(new HeadParameters());
		var bodyPartParameters = new BodyPartParameters(0, 0, head, -10);
		bodyPartParameters.setAmplitude(42);
		var organ = new BodyPart(bodyPartParameters);

		assertThat(organ.getAmplitude()).isEqualTo(42);
	}

	@Test
	public void hasAFiberShiftedFromItsParent() {
		var parameters = new HeadParameters();
		parameters.setRed(100);
		parameters.setGreen(101);
		parameters.setBlue(102);
		var head = new Head(parameters);

		var bodyPartParameters = new BodyPartParameters(0, 0, head, -10);
		bodyPartParameters.setRedShift(10);
		bodyPartParameters.setGreenShift(20);
		bodyPartParameters.setBlueShift(30);
		var organ = new BodyPart(bodyPartParameters);

		assertThat(organ.getFiber()).isEqualTo(new Fiber(110, 121, 132));
	}

	@Test
	public void hasACenterOfMass() {
		var head = new Head(new HeadParameters(10, 0));
		var organ = (MovingOrgan) head.addChild(new BodyPart(new BodyPartParameters(10, 0, head, 20)));

		// Uses the current angle, not the angle at rest
		organ.setAngleToParent(45);
		head.updateTree();

		organ.growToAdultFormWithChildren();

		final double LENGTH_AT_45_DEGREES = 7.07106;
		double expectedX = head.getEndPoint().x + LENGTH_AT_45_DEGREES / 2;
		double expectedY = head.getEndPoint().y + LENGTH_AT_45_DEGREES / 2;
		var expected = Vector.cartesian(expectedX, expectedY);

		assertThat(organ.getCenterOfMass()
						.approximatelyEquals(expected)).isTrue();
	}
}
