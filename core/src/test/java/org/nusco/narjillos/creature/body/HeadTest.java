package org.nusco.narjillos.creature.body;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.narjillos.creature.body.pns.Nerve;
import org.nusco.narjillos.creature.body.pns.WaveNerve;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.utilities.ColorByte;

public class HeadTest extends OrganTest {

	@Override
	public Head createConcreteBodyPart(int length, int thickness) {
		return new Head(length, thickness, new ColorByte(100), 1);
	}

	@Test
	public void startsAtPointZeroByDefault() {
		assertEquals(Vector.ZERO, part.getStartPoint());
	}

	@Override
	public void hasAnEndPoint() {
		assertEquals(Vector.cartesian(20, 0), part.getEndPoint());
	}

	@Override
	public void hasAParent() {
		assertEquals(null, part.getParent());
	}

	@Test
	public void hasAWaveNerve() {
		Nerve nerve = new Head(0, 0, new ColorByte(0), 1).getNerve();
				
		assertEquals(WaveNerve.class, nerve.getClass());
	}
}
