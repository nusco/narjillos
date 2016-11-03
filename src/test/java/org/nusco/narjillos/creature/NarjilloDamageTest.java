package org.nusco.narjillos.creature;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.nusco.narjillos.core.geometry.Vector;
import org.nusco.narjillos.core.things.LifeFormEnergy;
import org.nusco.narjillos.genomics.DNA;

public class NarjilloDamageTest {

	Narjillo narjillo = new Narjillo(new DNA(1, "{0_255_10_10_255_255_255_255_0_0_0}{0_255_10_10_255_255_255_255_0_0_0}"), Vector.ZERO, 90,
		new LifeFormEnergy(1000, Double.MAX_VALUE));

	@Test
	public void loosesEnergyWhenDamaged() {
		double initialEnergy = narjillo.getEnergy().getValue();
		narjillo.damage();
		assertTrue(narjillo.getEnergy().getValue() < initialEnergy);
	}

	@Test
	public void isNormallyNotInPain() {
		assertFalse(narjillo.isInPain());
	}

	@Test
	public void isInPainWhenDamaged() {
		narjillo.damage();
		assertTrue(narjillo.isInPain());
	}

	@Test
	public void forgetsPainAfterATick() {
		narjillo.damage();
		narjillo.tick();
		assertFalse(narjillo.isInPain());
	}
}
