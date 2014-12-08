package org.nusco.narjillos.creature.body;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.nusco.narjillos.creature.body.pns.Nerve;
import org.nusco.narjillos.creature.body.pns.WaveNerve;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.utilities.ColorByte;

public class HeadTest extends OrganTest {

	@Override
	public Head createConcreteOrgan(int length, int thickness) {
		return new Head(length, thickness, new ColorByte(100), 1, 0.5);
	}

	@Test
	public void startsAtPointZeroByDefault() {
		assertEquals(Vector.ZERO, bodyPart.getStartPoint());
	}

	@Override
	public void hasAnEndPoint() {
		fullyGrowBodyPart();
		
		assertEquals(Vector.cartesian(20, 0), bodyPart.getEndPoint());
	}

	@Override
	public void hasAParent() {
		assertEquals(null, getBodyPart().getParent());
	}

	@Test
	public void hasAWaveNerve() {
		Nerve nerve = new Head(0, 0, new ColorByte(0), 1, 0.5).getNerve();
				
		assertEquals(WaveNerve.class, nerve.getClass());
	}
	
	@Test
	public void hasACenterOfMass() {
		Head head = new Head(10, 6, new ColorByte(100), 1, 0.5);
		head.updateAngleToParent(90);
		
		assertTrue(head.getCenterOfMass().almostEquals(Vector.cartesian(0, 5)));
	}
	
	@Test
	public void hasAPercentEnergyToChildren() {
		Head head = new Head(10, 6, new ColorByte(100), 1, 0.42);
		
		assertEquals(0.42, head.getPercentEnergyToChildren(), 0.0);
	}
}
