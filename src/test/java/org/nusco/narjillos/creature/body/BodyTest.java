package org.nusco.narjillos.creature.body;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import org.junit.jupiter.api.Test;
import org.nusco.narjillos.core.chemistry.Element;
import org.nusco.narjillos.core.geometry.Vector;

public class BodyTest {

	@Test
	public void isPositionedAtZeroByDefault() {
		var body = new Body(new Head(new HeadParameters()));

		assertThat(body.getStartPoint()).isEqualTo(Vector.ZERO);
	}

	@Test
	public void canBeForcedAtAGivenPosition() {
		var body = new Body(new Head(new HeadParameters()));
		body.forcePosition(Vector.cartesian(10, -10), 15);

		assertThat(body.getStartPoint()).isEqualTo(Vector.cartesian(10, -10));
	}

	@Test
	public void hasAMassProportionalToItsSize() {
		int headLengthInMm = 3;
		int headThicknessInMm = 4;
		var head = new Head(new HeadParameters(headLengthInMm, headThicknessInMm));

		int segmentLengthInMm = 10;
		int segmentThicknessInMm = 20;
		head.addChild(new BodyPart(new BodyPartParameters(segmentLengthInMm, segmentThicknessInMm, head, 0)));
		var body = new Body(head);

		assertThat(body.getAdultMass()).isEqualTo(212, within(0.001));
	}

	@Test
	public void hasACenterOfMassAndARadius() {
		var head = new Head(new HeadParameters(10, 10));
		head.addChild(new BodyPart(new BodyPartParameters(20, 5, head, 0)));
		var body = new Body(head);

		body.growToAdultForm();

		// calculateRadius() needs an explicit center of mass, because of optimizations.
		// So these two are better tested together:
		assertThat(body.getCenterOfMass()).isEqualTo(Vector.cartesian(12.5, 0));
		assertThat(body.getRadius()).isEqualTo(17.5);
	}

	@Test
	public void hasABoundingBox() {
		var head = new Head(new HeadParameters(10, 1));
		var parameters = new BodyPartParameters(20, 1, head, 90);
		head.addChild(new BodyPart(parameters));
		var body = new Body(head);

		body.growToAdultForm();
		body.forcePosition(Vector.cartesian(3, 4), 0);

		var boundingBox = body.getBoundingBox();
		assertThat(boundingBox.left).isEqualTo(3);
		assertThat(boundingBox.right).isEqualTo(13);
		assertThat(boundingBox.bottom).isEqualTo(4);
		assertThat(boundingBox.top).isEqualTo(24);
	}

	@Test
	public void itsMinimumRadiusIsOne() {
		var head = new Head(new HeadParameters(0, 1));
		var body = new Body(head);
		assertThat(body.getRadius()).isEqualTo(1);
	}

	@Test
	public void hasTheSameByproductAsTheHead() {
		var headParameters = new HeadParameters();
		headParameters.setByproduct(Element.OXYGEN);
		var head = new Head(headParameters);
		var body = new Body(head);

		assertThat(head.getByproduct()).isEqualTo(Element.OXYGEN);
		assertThat(body.getByproduct()).isEqualTo(Element.OXYGEN);
	}

	@Test
	public void hasTheSameEnergyToChildrenAsTheHead() {
		var headParameters = new HeadParameters();
		headParameters.setEnergyToChildren(0.42);
		var head = new Head(headParameters);
		var body = new Body(head);

		assertThat(body.getEnergyToChildren()).isEqualTo(0.42);
	}

	@Test
	public void hasTheSameEggVelocityAsTheHead() {
		var headParameters = new HeadParameters();
		headParameters.setEggVelocity(42);
		var head = new Head(headParameters);
		var body = new Body(head);

		assertThat(body.getEggVelocity()).isEqualTo(42);
	}

	@Test
	public void hasTheSameEggIntervalAsTheHead() {
		var headParameters = new HeadParameters();
		headParameters.setEggInterval(10);
		var head = new Head(headParameters);
		var body = new Body(head);

		assertThat(body.getEggInterval()).isEqualTo(10);
	}
}
