package org.nusco.swimmers.body;

import static org.junit.Assert.*;

import org.junit.Test;

public class AngleTest {
	@Test
	public void normalizesToPlusOrMinus180Degrees() {
		assertEquals(0.0, Angle.normalize(0), 0);
		assertEquals(90.0, Angle.normalize(90), 0);
		assertEquals(180.0, Angle.normalize(180), 0);
		
		assertEquals(-180.0, Angle.normalize(181), 180);
		assertEquals(-179.0, Angle.normalize(181), 0);
		assertEquals(-90.0, Angle.normalize(270), 0);
		assertEquals(-1.0, Angle.normalize(359), 0);

		assertEquals(1.0, Angle.normalize(361), 0);
		assertEquals(-1.0, Angle.normalize(-361), 0);
		assertEquals(180.0, Angle.normalize(540), 0);
		assertEquals(-179.0, Angle.normalize(-539), 0);
	}
}
