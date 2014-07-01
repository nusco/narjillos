package org.nusco.swimmers.shared.physics;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.swimmers.shared.physics.Vector;

public class VectorTest {
	@Test
	public void canBeCreatedFromItCartesianCoordinates() {
		double x = 10.1;
		double y = 20.1;
		Vector v = Vector.cartesian(x, y);
		
		assertEquals(10.1, v.getX(), 0);
		assertEquals(20.1, v.getY(), 0);
		assertEquals(63.32, v.getAngle(), 0.1);
		assertEquals(22.49, v.getLength(), 0.01);
	}
	
	@Test
	public void canBeCreatedFromItsPolarCoordinates() {
		double degrees = 30;
		double length = 10;
		Vector v = Vector.polar(degrees, length);
		
		assertEquals(8.66, v.getX(), 0.01);
		assertEquals(5.00, v.getY(), 0.01);
		assertEquals(30, v.getAngle(), 0.01);
		assertEquals(10, v.getLength(), 0.01);
	}
	
	@Test
	public void normalizesAnglesWhenUsingPolarCoordinates() {
		assertEquals(0, Vector.polar(0, 1).getAngle(), 0.001);
		assertEquals(180, Vector.polar(180, 1).getAngle(), 0.001);
		assertEquals(0, Vector.polar(360, 1).getAngle(), 0.001);
		assertEquals(1, Vector.polar(361, 1).getAngle(), 0.001);
		assertEquals(-10, Vector.polar(-10, 1).getAngle(), 0.001);
		assertEquals(-180, Vector.polar(-180, 1).getAngle(), 0.001);
		assertEquals(179, Vector.polar(-181, 1).getAngle(), 0.001);
		assertEquals(0, Vector.polar(-360, 1).getAngle(), 0.001);
		assertEquals(-1, Vector.polar(-361, 1).getAngle(), 0.001);
	}
	
	@Test
	public void normalizesAnglesWhenUsingCartesianCoordinates() {
		assertEquals(0, Vector.cartesian(1, 0).getAngle(), 0);
		assertEquals(45, Vector.cartesian(1, 1).getAngle(), 0);
		assertEquals(90, Vector.cartesian(0, 1).getAngle(), 0);
		assertEquals(-45, Vector.cartesian(1, -1).getAngle(), 0);
		assertEquals(-90, Vector.cartesian(0, -1).getAngle(), 0);
		assertEquals(-135, Vector.cartesian(-1, -1).getAngle(), 0);
		assertEquals(180, Vector.cartesian(-1, 0).getAngle(), 0);
		assertEquals(135, Vector.cartesian(-1, 1).getAngle(), 0);
	}
	
	@Test
	public void hasALength() {
		assertEquals(0, Vector.cartesian(0, 0).getLength(), 0);
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

	@Test
	public void itsAngleIsCorrectedForNegativeLengths() {
		assertEquals(-180, Vector.polar(0, -1).getAngle(), 0.001);
		assertEquals(0, Vector.polar(180, -1).getAngle(), 0.001);
		assertEquals(-90, Vector.polar(90, -1).getAngle(), 0.001);
		assertEquals(-135, Vector.polar(45, -1).getAngle(), 0.001);
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
	public void canBeInverted() {
		Vector original = Vector.cartesian(10, -20);
		
		Vector calculated = original.getInverse();

		assertEquals(Vector.cartesian(-10, 20), calculated);
	}

	@Test
	public void canBeNormalizedToAnArbitraryLength() {
		Vector original = Vector.polar(42, 1234);
		
		assertEquals(Vector.polar(42, 10), original.normalize(10));
	}

	@Test
	public void hasANormalVector() {
		assertEqualsVector(Vector.polar(-50, 1), Vector.polar(40, 1234).getNormal());
		assertEqualsVector(Vector.polar(0, 1), Vector.polar(90, 1234).getNormal());
		assertEqualsVector(Vector.polar(90, 1), Vector.polar(180, 1234).getNormal());
		assertEqualsVector(Vector.polar(180, 1), Vector.polar(-90, 1234).getNormal());
	}

	@Test
	public void hasAProjectionOnAnotherVector() {
		assertEqualsVector(Vector.polar(180, 10), Vector.polar(180, 10).getProjectionOn(Vector.polar(180, 1)));
		assertEqualsVector(Vector.polar(180, 10), Vector.polar(180, 10).getProjectionOn(Vector.polar(0, 1)));
		assertEqualsVector(Vector.polar(180, 10), Vector.polar(180, 10).getProjectionOn(Vector.polar(180, 10)));
		assertEqualsVector(Vector.ZERO, Vector.polar(180, 10).getProjectionOn(Vector.polar(90, 1)));
		assertEqualsVector(Vector.polar(45, 7.0710), Vector.polar(90, 10).getProjectionOn(Vector.polar(45, 1)));
		assertEqualsVector(Vector.polar(45, 7.0710), Vector.polar(90, 10).getProjectionOn(Vector.polar(-135, 1)));
	}

	@Test
	public void hasANormalComponentOnAnotherVector() {
		assertEqualsVector(Vector.ZERO, Vector.polar(90, 10).getNormalComponentOn(Vector.polar(90, 1)));
		assertEqualsVector(Vector.polar(90, 10), Vector.polar(90, 10).getNormalComponentOn(Vector.polar(0, 1)));
		assertEqualsVector(Vector.polar(90, 10), Vector.polar(90, 10).getNormalComponentOn(Vector.polar(0, 10)));
		assertEqualsVector(Vector.polar(90, 7.0710), Vector.polar(45, 10).getNormalComponentOn(Vector.polar(0, 10)));
		assertEqualsVector(Vector.polar(90, 7.0710), Vector.polar(45, 10).getNormalComponentOn(Vector.polar(180, 10)));
		assertEqualsVector(Vector.polar(-90, 7.0710), Vector.polar(-45, 10).getNormalComponentOn(Vector.polar(0, 10)));
		assertEqualsVector(Vector.polar(-90, 7.0710), Vector.polar(-45, 10).getNormalComponentOn(Vector.polar(180, 10)));
	}

	@Test
	public void hasAnAngleWithAnotherVector() {
		Vector ninetyDegrees = Vector.polar(90, 10);
		assertEquals(0, ninetyDegrees.getAngleWith(ninetyDegrees), 0.001);
		assertEquals(90, ninetyDegrees.getAngleWith(Vector.ZERO), 0.001);
		assertEquals(180, ninetyDegrees.getAngleWith(Vector.polar(-90, 1)), 0.001);
		assertEquals(-90, ninetyDegrees.getAngleWith(Vector.polar(180, 1)), 0.001);
		assertEquals(-179, ninetyDegrees.getAngleWith(Vector.polar(-91, 1)), 0.001);
		assertEquals(179, Vector.polar(-91, 1).getAngleWith(ninetyDegrees), 0.001);
	}

	@Test
	public void canBeRotated() {
		Vector ninetyDegrees = Vector.polar(90, 10);
		assertEqualsVector(Vector.polar(180, 10), ninetyDegrees.rotateBy(90));
		assertEqualsVector(Vector.polar(-179, 10), ninetyDegrees.rotateBy(91));
		assertEqualsVector(Vector.polar(0, 10), ninetyDegrees.rotateBy(-90));
		assertEqualsVector(Vector.polar(-1, 10), ninetyDegrees.rotateBy(-91));
	}
	
	private void assertEqualsVector(Vector v1, Vector v2) {
		assertEquals(v1.getX(), v2.getX(), 0.001);
		assertEquals(v1.getY(), v2.getY(), 0.001);
	}
}
