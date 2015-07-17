package org.nusco.narjillos.core.physics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.nusco.narjillos.core.physics.Vector;
import org.nusco.narjillos.core.physics.ZeroVectorAngleException;

public class VectorTest {
	
	@Test
	public void canBeCreatedFromCartesianCoordinates() throws ZeroVectorAngleException {
		double x = 10.1;
		double y = 20.1;
		Vector v = Vector.cartesian(x, y);
		
		assertEquals(10.1, v.x, 0);
		assertEquals(20.1, v.y, 0);
		assertEquals(63.32, v.getAngle(), 0.1);
		assertEquals(22.49, v.getLength(), 0.01);
	}
	
	@Test
	public void canBeCreatedFromPolarCoordinates() throws ZeroVectorAngleException {
		double degrees = 30;
		double length = 10;
		Vector v = Vector.polar(degrees, length);
		
		assertEquals(8.66, v.x, 0.01);
		assertEquals(5.00, v.y, 0.01);
		assertEquals(30, v.getAngle(), 0.01);
		assertEquals(10, v.getLength(), 0.01);
	}
	
	@Test
	public void canBeZero() {
		assertTrue(Vector.ZERO.isZero());
		assertTrue(Vector.polar(10, 0).isZero());
		assertTrue(Vector.cartesian(0, 0).isZero());
		assertFalse(Vector.cartesian(0.001, 0).isZero());
	}
	
	@Test
	public void normalizesAnglesWhenUsingPolarCoordinates() throws ZeroVectorAngleException {
		assertEquals(0, Vector.polar(0, 1).getAngle(), 0.001);
		assertEquals(180, Vector.polar(180, 1).getAngle(), 0.001);
		assertEquals(0, Vector.polar(360, 1).getAngle(), 0.001);
		assertEquals(1, Vector.polar(361, 1).getAngle(), 0.001);
		assertEquals(-10, Vector.polar(-10, 1).getAngle(), 0.001);
		assertEquals(180, Vector.polar(-180, 1).getAngle(), 0.001);
		assertEquals(179, Vector.polar(-181, 1).getAngle(), 0.001);
		assertEquals(0, Vector.polar(-360, 1).getAngle(), 0.001);
		assertEquals(-1, Vector.polar(-361, 1).getAngle(), 0.001);
	}
	
	@Test
	public void normalizesAnglesWhenUsingCartesianCoordinates() throws ZeroVectorAngleException {
		double PRECISION_FOR_ANGLES_ALONG_THE_AXES = 0.0;
		double PRECISION_FOR_ANY_OTHER_ANGLE = 0.01;

		assertEquals(0, Vector.cartesian(1, 0).getAngle(), PRECISION_FOR_ANGLES_ALONG_THE_AXES);
		assertEquals(45, Vector.cartesian(1, 1).getAngle(), PRECISION_FOR_ANY_OTHER_ANGLE);
		assertEquals(90, Vector.cartesian(0, 1).getAngle(), PRECISION_FOR_ANGLES_ALONG_THE_AXES);
		assertEquals(-45, Vector.cartesian(1, -1).getAngle(), PRECISION_FOR_ANY_OTHER_ANGLE);
		assertEquals(-90, Vector.cartesian(0, -1).getAngle(), PRECISION_FOR_ANGLES_ALONG_THE_AXES);
		assertEquals(-135, Vector.cartesian(-1, -1).getAngle(), PRECISION_FOR_ANY_OTHER_ANGLE);
		assertEquals(180, Vector.cartesian(-1, 0).getAngle(), PRECISION_FOR_ANGLES_ALONG_THE_AXES);
		assertEquals(135, Vector.cartesian(-1, 1).getAngle(), PRECISION_FOR_ANY_OTHER_ANGLE);
	}

	@Test
	public void hasALength() {
		assertEquals(0, Vector.ZERO.getLength(), 0);
		assertEquals(1, Vector.cartesian(1, 0).getLength(), 0);
		assertEquals(1, Vector.cartesian(0, -1).getLength(), 0);
		assertEquals(5, Vector.cartesian(3, 4).getLength(), 0.001);
	}

	@Test
	public void itsLengthIsAlwaysPositive() {
		assertEquals(1, Vector.polar(0, -1).getLength(), 0.001);
		assertEquals(1, Vector.polar(180, -1).getLength(), 0.001);
		assertEquals(1, Vector.polar(90, -1).getLength(), 0.001);
		assertEquals(1, Vector.polar(45, -1).getLength(), 0.001);
	}
	
	@Test(expected = ZeroVectorAngleException.class)
	public void hasNoAngleIfItsLengthIsZero() throws ZeroVectorAngleException {
		assertEquals(0, Vector.ZERO.getAngle(), 0);
	}

	@Test
	public void canBeCreatedWithANegativeLengths() throws ZeroVectorAngleException {
		assertEquals(180, Vector.polar(0, -1).getAngle(), 0.01);
		assertEquals(0, Vector.polar(180, -1).getAngle(), 0.01);
		assertEquals(-90, Vector.polar(90, -1).getAngle(), 0.01);
		assertEquals(-135, Vector.polar(45, -1).getAngle(), 0.01);
	}
	
	@Test
	public void canBeAddedToAnotherVector() {
		Vector start = Vector.cartesian(10, 20);
	
		Vector end = start.plus(Vector.cartesian(3, -4));
		
		assertEquals(Vector.cartesian(13, 16), end);
	}

	@Test
	public void canBeSubtractedFromAnotherVector() {
		Vector start = Vector.cartesian(10, 20);
	
		Vector difference = start.minus(Vector.cartesian(3, -4));
		
		assertEquals(Vector.cartesian(7, 24), difference);
	}

	@Test
	public void canBeMultipliedByAScalar() {
		Vector original = Vector.cartesian(10, -20);
		
		Vector calculated = original.by(-0.5);

		assertEquals(Vector.cartesian(-5, 10), calculated);
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
	
	@Test(expected = ZeroVectorAngleException.class)
	public void hasNoNormalComponentIfItsLengthIsZero() throws ZeroVectorAngleException {
		Vector.ZERO.getNormalComponentOn(Vector.polar(90, 1));
	}
	
	@Test(expected = ZeroVectorAngleException.class)
	public void hasNoNormalComponentOnVectorZero() throws ZeroVectorAngleException {
		Vector.polar(90, 1).getNormalComponentOn(Vector.ZERO);
	}

	@Test
	public void hasADistanceFromAnotherVector() {
		Vector vector1 = Vector.cartesian(120, 130);
		Vector vector2 = Vector.cartesian(-20, 80);

		assertEquals(148.66, vector1.getDistanceFrom(vector2), 0.001);
		assertEquals(148.66, vector2.getDistanceFrom(vector1), 0.001);
	}
	
	private void assertAlmostEquals(Vector v1, Vector v2) {
		assertTrue("Different vectors: " + v1 + ", " + v2, v1.approximatelyEquals(v2));
	}
}
