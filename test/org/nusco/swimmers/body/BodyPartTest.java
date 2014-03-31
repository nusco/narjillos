package org.nusco.swimmers.body;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.nusco.swimmers.neural.DelayNeuron;
import org.nusco.swimmers.neural.CosWave;

public class BodyPartTest extends VisibleOrganTest {
	private VisibleOrgan parent;
	
	@Override
	public VisibleOrgan createVisibleOrgan() {
		parent = new Head(15, THICKNESS, 100);
		return new BodyPart(20, THICKNESS, 10, 100, parent);
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
		VisibleOrgan organ1 = new BodyPart(0, 0, 30, 0, head);
		VisibleOrgan organ2 = new BodyPart(0, 0, -10, 0, organ1);
		assertEquals(20, organ2.getAngle(), 0);
	}

	@Override
	public void hasAParent() {
		assertEquals(parent, organ.getParent());
	}

	@Test
	public void theAngleRelativeToTheParentStaysInTheMinusOrPlus180To180DegreesRange() {
		assertRelativeAngleEquals(-10, 350);
		assertRelativeAngleEquals(10, 10);
		assertRelativeAngleEquals(179, -181);
	}

	private void assertRelativeAngleEquals(int expectedAngle, int relativeAngle) {
		BodyPart part = new BodyPart(0, 0, relativeAngle, 0 , new Head(0, 0, 0));
		assertEquals(expectedAngle, part.getRelativeAngle(), 0);
	}

	@Override
	public void hasAnEndPoint() {
		// TODO
//		Head head = new Head(10, 0, 0);
//		VisibleOrgan organ1 = head.sproutVisibleOrgan(10, 0, 90, 0);
//		VisibleOrgan organ2 = organ1.sproutVisibleOrgan(10, 0, -90, 0);
//		assertEquals(new Vector(20, 15), organ2.getEndPoint());
	}

	@Test
	public void hasADelayNeuron() {
		assertTrue(organ.getNeuron() instanceof DelayNeuron);
	}

	@Test
	public void neuronsAreConnectedFromParentToChildren() {
		Head head = new Head(0, 0, 0);

		int angleFromParent = 1;
		VisibleOrgan organ1 = head.sproutVisibleOrgan(0, 0, angleFromParent, 0);
		VisibleOrgan organ2 = organ1.sproutVisibleOrgan(0, 0, angleFromParent, 0);
		
		for (int i = 0; i < CosWave.WAVE.length; i++) {
			head.tick();

			if(i < 4)
				assertEquals(1, organ1.getAngle(), 0.0);
			else
				assertEquals(CosWave.WAVE[i - 4], organ1.getAngle(), 0.01);

			if(i < 7)
				assertEquals(2, organ2.getAngle(), 0.0);
			else
				assertEquals(CosWave.WAVE[i - 8], organ2.getRelativeAngle(), 0.0);
		}
	}
}
