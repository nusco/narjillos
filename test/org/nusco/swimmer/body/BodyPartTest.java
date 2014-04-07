package org.nusco.swimmer.body;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.swimmer.body.pns.Nerve;
import org.nusco.swimmer.physics.Vector;

public class BodyPartTest extends VisibleOrganTest {
	private VisibleOrgan parent;
	
	@Override
	public VisibleOrgan createVisibleOrgan() {
		parent = new Head(15, THICKNESS, 100);
		return new BodyPart(Vector.cartesian(20, 10), 20, THICKNESS, 10, 100, parent);
	}

	@Override
	public void hasAParent() {
		assertEquals(parent, organ.getParent());
	}

	@Override
	public void hasAVectorRelativeToItsParent() {
		assertEquals(Vector.cartesian(20, 10), organ.getRelativeVector());
	}

	@Test
	public void startsAtItsParentsEndPoint() {
		assertEquals(parent.getEndPoint(), organ.getStartPoint());
	}

	@Test
	public void hasAnAngleRelativeToTheParent() {
		assertEquals(10, organ.getRelativeAngle(), 0);
	}
	
	@Test
	public void hasAnAbsoluteAngle() {
		Head head = new Head(0, 0, 0);
		VisibleOrgan organ1 = new BodyPart(Vector.ONE, 0, 0, 30, 0, head);
		VisibleOrgan organ2 = new BodyPart(Vector.ONE, 0, 0, -10, 0, organ1);
		assertEquals(20, organ2.getAngle(), 0);
	}
	
	@Test
	public void theAngleRelativeToTheParentStaysInTheMinusOrPlus180To180DegreesRange() {
		assertRelativeAngleEquals(-10, 350);
		assertRelativeAngleEquals(10, 10);
		assertRelativeAngleEquals(179, -181);
	}

	private void assertRelativeAngleEquals(int expectedAngle, int relativeAngle) {
		BodyPart part = new BodyPart(Vector.ONE, 0, 0, relativeAngle, 0 , new Head(0, 0, 0));
		assertEquals(expectedAngle, part.getRelativeAngle(), 0);
	}

	@Override
	public void hasAnEndPoint() {
		// TODO
//		Head head = new Head(10, 0, 0);
//		VisibleOrgan organ1 = head.sproutVisibleOrgan(10, 0, 90, 0);
//		VisibleOrgan organ2 = organ1.sproutVisibleOrgan(10, 0, -90, 0);
//		assertEquals(Vector.cartesian(20, 15), organ2.getEndPoint());
	}

	@Test
	public void anglesAreControlledByTheNeurons() {
		//TODO: this test doesn't work. find a smarter way to test this complex chain
		// TODO: also add NullOrgans to the mix
		final Nerve doublerNeuron = new Nerve() {
			@Override
			public Vector process(Vector inputSignal) {
				return Vector.cartesian(inputSignal.getX() * 2, inputSignal.getY() * 2);
			}
			
			@Override
			public Vector readOutputSignal() {
				return Vector.cartesian(2, 0);
			}
		};
		Head head = new Head(0, 0, 0) {
			@Override
			public Nerve getNerve() {
				return doublerNeuron;
			}
		};

		int angleFromParent = 1;
		VisibleOrgan organ1 = head.sproutVisibleOrgan(Vector.ONE, 0, 0, angleFromParent, 0);
		VisibleOrgan organ2 = organ1.sproutVisibleOrgan(Vector.ONE, 0, 0, angleFromParent, 0);
		
		assertAngle(1, organ1);
		assertAngle(2, organ2);

		for (int i = 0; i < 4; i++) {
			head.tick();
			assertAngle(1, organ1);
			assertAngle(2, organ2);

			head.tick();
			assertAngle(1, organ1);
			assertAngle(2, organ2);

			head.tick();
			assertAngle(1, organ1);
			assertAngle(2, organ2);
		}

	}

	private void assertAngle(int expected, VisibleOrgan organ) {
		assertEquals(expected, organ.getAngle(), 0.1);
	}
}
