package org.nusco.narjillos.core.geometry;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class VectorTest {

	private static final double PRECISION_01 = 0.1;
	private static final double PRECISION_001 = 0.01;
	private static final double PRECISION_0001 = 0.001;

	@Test
	public void canBeCreatedFromCartesianCoordinates() throws ZeroVectorAngleException {
		double x = 10.1;
		double y = 20.1;
		var v = Vector.cartesian(x, y);

		assertThat(v.x).isEqualTo(10.1);
		assertThat(v.y).isEqualTo(20.1);
		assertThat(v.getAngle()).isEqualTo(63.32, within(PRECISION_01));
		assertThat(v.getLength()).isEqualTo(22.49, within(PRECISION_001));
	}

	@Test
	public void canBeCreatedFromPolarCoordinates() throws ZeroVectorAngleException {
		double degrees = 30;
		double length = 10;
		var v = Vector.polar(degrees, length);

		assertThat(v.x).isEqualTo(8.66, within(PRECISION_001));
		assertThat(v.y).isEqualTo(5.0, within(PRECISION_001));
		assertThat(v.getAngle()).isEqualTo(30, within(PRECISION_001));
		assertThat(v.getLength()).isEqualTo(10, within(PRECISION_001));
	}

	@Test
	public void canBeZero() {
		assertThat(Vector.ZERO.isZero()).isTrue();
		assertThat(Vector.polar(10, 0).isZero()).isTrue();
		assertThat(Vector.cartesian(0, 0).isZero()).isTrue();
		assertThat(Vector.cartesian(0.001, 0).isZero()).isFalse();
	}

	@Test
	public void normalizesAnglesWhenUsingPolarCoordinates() throws ZeroVectorAngleException {
		assertThat(Vector.polar(0, 1).getAngle()).isEqualTo(0, within(PRECISION_0001));
		assertThat(Vector.polar(180, 1).getAngle()).isEqualTo(180, within(PRECISION_0001));
		assertThat(Vector.polar(360, 1).getAngle()).isEqualTo(0, within(PRECISION_0001));
		assertThat(Vector.polar(361, 1).getAngle()).isEqualTo(1, within(PRECISION_0001));
		assertThat(Vector.polar(-10, 1).getAngle()).isEqualTo(-10, within(PRECISION_0001));
		assertThat(Vector.polar(-180, 1).getAngle()).isEqualTo(180, within(PRECISION_0001));
		assertThat(Vector.polar(-181, 1).getAngle()).isEqualTo(179, within(PRECISION_0001));
		assertThat(Vector.polar(-360, 1).getAngle()).isEqualTo(0, within(PRECISION_0001));
		assertThat(Vector.polar(-361, 1).getAngle()).isEqualTo(-1, within(PRECISION_0001));
	}

	@Test
	public void normalizesAnglesWhenUsingCartesianCoordinates() throws ZeroVectorAngleException {
		final double PRECISION_FOR_ANGLES_ALONG_THE_AXES = 0.0;
		final double PRECISION_FOR_ANY_OTHER_ANGLE = 0.01;

		assertThat(Vector.cartesian(1, 0).getAngle()).isEqualTo(0, within(PRECISION_FOR_ANGLES_ALONG_THE_AXES));
		assertThat(Vector.cartesian(1, 1).getAngle()).isEqualTo(45, within(PRECISION_FOR_ANY_OTHER_ANGLE));
		assertThat(Vector.cartesian(0, 1).getAngle()).isEqualTo(90, within(PRECISION_FOR_ANGLES_ALONG_THE_AXES));
		assertThat(Vector.cartesian(1, -1).getAngle()).isEqualTo(-45, within(PRECISION_FOR_ANY_OTHER_ANGLE));
		assertThat(Vector.cartesian(0, -1).getAngle()).isEqualTo(-90, within(PRECISION_FOR_ANGLES_ALONG_THE_AXES));
		assertThat(Vector.cartesian(-1, -1).getAngle()).isEqualTo(-135, within(PRECISION_FOR_ANY_OTHER_ANGLE));
		assertThat(Vector.cartesian(-1, 0).getAngle()).isEqualTo(180, within(PRECISION_FOR_ANGLES_ALONG_THE_AXES));
		assertThat(Vector.cartesian(-1, 1).getAngle()).isEqualTo(135, within(PRECISION_FOR_ANY_OTHER_ANGLE));
	}

	@Test
	public void hasALength() {
		assertThat(Vector.ZERO.getLength()).isEqualTo(0);
		assertThat(Vector.cartesian(1, 0).getLength()).isEqualTo(1);
		assertThat(Vector.cartesian(0, -1).getLength()).isEqualTo(1);
		assertThat(Vector.cartesian(3, 4).getLength()).isEqualTo(5, within(PRECISION_0001));
	}

	@Test
	public void itsLengthIsAlwaysPositive() {
		assertThat(Vector.polar(0, -1).getLength()).isEqualTo(1, within(PRECISION_0001));
		assertThat(Vector.polar(180, -1).getLength()).isEqualTo(1, within(PRECISION_0001));
		assertThat(Vector.polar(90, -1).getLength()).isEqualTo(1, within(PRECISION_0001));
		assertThat(Vector.polar(45, -1).getLength()).isEqualTo(1, within(PRECISION_0001));
	}

	@Test
	public void hasNoAngleIfItsLengthIsZero() {
		assertThatThrownBy(() -> Vector.ZERO.getAngle())
			.isInstanceOf(ZeroVectorAngleException.class);
	}

	@Test
	public void canBeCreatedWithANegativeLengths() throws ZeroVectorAngleException {
		assertThat(Vector.polar(0, -1).getAngle()).isEqualTo(180, within(PRECISION_001));
		assertThat(Vector.polar(180, -1).getAngle()).isEqualTo(0, within(PRECISION_001));
		assertThat(Vector.polar(90, -1).getAngle()).isEqualTo(-90, within(PRECISION_001));
		assertThat(Vector.polar(45, -1).getAngle()).isEqualTo(-135, within(PRECISION_001));
	}

	@Test
	public void canBeAddedToAnotherVector() {
		var start = Vector.cartesian(10, 20);
		var end = start.plus(Vector.cartesian(3, -4));

		assertThat(end).isEqualTo(Vector.cartesian(13, 16));
	}

	@Test
	public void canBeSubtractedFromAnotherVector() {
		var start = Vector.cartesian(10, 20);
		var difference = start.minus(Vector.cartesian(3, -4));

		assertThat(difference).isEqualTo(Vector.cartesian(7, 24));
	}

	@Test
	public void canBeMultipliedByAScalar() {
		var original = Vector.cartesian(10, -20);
		var calculated = original.by(-0.5);

		assertThat(calculated).isEqualTo(Vector.cartesian(-5, 10));
	}

	@Test
	public void hasANormalComponentOnAnotherVector() throws ZeroVectorAngleException {
		assertAlmostEquals(Vector.ZERO, Vector.polar(90, 10).getNormalComponentOn(Vector.polar(90, 1)));
		assertAlmostEquals(Vector.polar(90, 10), Vector.polar(90, 10).getNormalComponentOn(Vector.polar(0, 1)));
		assertAlmostEquals(Vector.polar(90, 10), Vector.polar(90, 10).getNormalComponentOn(Vector.polar(0, 10)));
		assertAlmostEquals(Vector.polar(90, 7.0710), Vector.polar(45, 10).getNormalComponentOn(Vector.polar(0, 10)));
		assertAlmostEquals(Vector.polar(90, 7.0710), Vector.polar(45, 10).getNormalComponentOn(Vector.polar(180, 10)));
		assertAlmostEquals(Vector.polar(-90, 7.0710), Vector.polar(-45, 10).getNormalComponentOn(Vector.polar(0, 10)));
		assertAlmostEquals(Vector.polar(-90, 7.0710), Vector.polar(-45, 10).getNormalComponentOn(Vector.polar(180, 10)));
	}

	@Test
	public void hasNoNormalComponentIfItsLengthIsZero() throws ZeroVectorAngleException {
		assertThatThrownBy(() -> Vector.ZERO.getNormalComponentOn(Vector.polar(90, 1)))
			.isInstanceOf(ZeroVectorAngleException.class);
	}

	@Test
	public void hasNoNormalComponentOnVectorZero() throws ZeroVectorAngleException {
		assertThatThrownBy(() -> Vector.polar(90, 1).getNormalComponentOn(Vector.ZERO))
			.isInstanceOf(ZeroVectorAngleException.class);
	}

	@Test
	public void hasADistanceFromAnotherVector() {
		var vector1 = Vector.cartesian(120, 130);
		var vector2 = Vector.cartesian(-20, 80);

		assertThat(vector1.getDistanceFrom(vector2)).isEqualTo(148.66, within(PRECISION_0001));
		assertThat(vector2.getDistanceFrom(vector1)).isEqualTo(148.66, within(PRECISION_0001));
	}

	private void assertAlmostEquals(Vector v1, Vector v2) {
		assertThat(v1.approximatelyEquals(v2))
			.withFailMessage("Different vectors: %s, %s", v1, v2)
			.isTrue();
	}
}
