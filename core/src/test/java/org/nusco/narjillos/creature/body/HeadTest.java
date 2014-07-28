package org.nusco.narjillos.creature.body;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.nusco.narjillos.creature.body.pns.Nerve;
import org.nusco.narjillos.creature.body.pns.PassNerve;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.utilities.ColorByte;

public class HeadTest extends BodyPartTest {

	@Override
	public Head createConcreteOrgan(int length, int thickness) {
		return new Head(length, thickness, new ColorByte(100), 1);
	}

	@Test
	public void startsAtPointZeroByDefault() {
		assertEquals(Vector.ZERO, organ.getStartPoint());
	}

	@Override
	public void hasAnEndPoint() {
		assertEquals(Vector.cartesian(20, 0), getBodyPart().getEndPoint());
	}

	@Override
	public void hasAParent() {
		assertEquals(null, getBodyPart().getParent());
	}

	@Test
	public void hasAPassNerve() {
		Nerve nerve = new Head(0, 0, new ColorByte(0), 1).getNerve();
				
		assertEquals(PassNerve.class, nerve.getClass());
	}
	
	@Test
	public void hasACenterOfMass() {
		Head head = new Head(10, 6, new ColorByte(100), 1);
		head.setAngleToParent(90);
		
		assertTrue(head.getCenterOfMass().almostEquals(Vector.cartesian(0, 5)));
	}
}
