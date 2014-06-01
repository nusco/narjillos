package org.nusco.swimmer.body;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.swimmer.body.pns.Nerve;
import org.nusco.swimmer.physics.Vector;

public class SegmentTest extends VisibleOrganTest {
	private Organ parent;
	
	@Override
	public Organ createVisibleOrgan() {
		parent = new Head(15, THICKNESS, 100);
		return new Segment(20, THICKNESS, 10, 100, parent);
	}

	@Override
	public void hasAParent() {
		assertEquals(parent, organ.getParent());
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
		Organ organ1 = new Segment(0, 0, 30, 0, head);
		Organ organ2 = new Segment(0, 0, -10, 0, organ1);
		assertEquals(20, organ2.getAngle(), 0);
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
		Organ organ1 = head.sproutOrgan(0, 0, angleFromParent, 0);
		Organ organ2 = organ1.sproutOrgan(0, 0, angleFromParent, 0);
		
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

	private void assertAngle(int expected, Organ organ) {
		assertEquals(expected, organ.getAngle(), 0.1);
	}
}
