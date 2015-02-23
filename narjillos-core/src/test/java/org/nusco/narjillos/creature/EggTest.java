package org.nusco.narjillos.creature;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.nusco.narjillos.genomics.DNA;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.utilities.RanGen;

public class EggTest {

	DNA dna = new DNA(1, "{1_2_3}");
	Egg egg = new Egg(dna, Vector.cartesian(10, 20), 100, new RanGen(1));
	
	@Test
	public void hatchesANarjilloAfterAnIncubationPeriod() {
		assertEquals(100, egg.getEnergy().getValue(), 0);
		assertNull(egg.getHatchedNarjillo());

		waitUntilItHatches(egg);
		
		Narjillo narjillo = egg.getHatchedNarjillo();
		assertEquals(egg.getPosition(), narjillo.getPosition());
		assertEquals(0, egg.getEnergy().getValue(), 0);
		assertEquals(100, narjillo.getEnergy().getValue(), 0);
	}
	
	@Test
	public void onlyHatchesOnce() {
		waitUntilItHatches(egg);
		
		assertFalse(egg.hatch());
	}
	
	@Test
	public void theHatchedNarjilloHasTheOriginalDNA() {
		waitUntilItHatches(egg);
		
		Narjillo narjillo = egg.getHatchedNarjillo();
		assertSame(dna, narjillo.getDNA());
	}
	
	@Test
	public void passesItsEnergyToTheHatchedNarjillo() {
		assertEquals(100, egg.getEnergy().getValue(), 0);
		
		waitUntilItHatches(egg);
		Narjillo narjillo = egg.getHatchedNarjillo();

		assertEquals(0, egg.getEnergy().getValue(), 0);
		assertEquals(100, narjillo.getEnergy().getValue(), 0);
	}
	
	@Test
	public void decaysUpTo100PercentAfterHatching() {
		assertFalse(egg.isDecayed());
		assertEquals(0, egg.getDecay(), 0);
		
		waitUntilItHatches(egg);

		for (int i = 0; i < 100; i++) {
			assertFalse(egg.isDecayed());
			assertEquals(i / 100.0, egg.getDecay(), 0);
			egg.tick();
		}

		assertTrue(egg.isDecayed());
		assertEquals(1, egg.getDecay(), 0);

		egg.tick();
		assertEquals(1, egg.getDecay(), 0);
	}

	private void waitUntilItHatches(Egg egg) {
		while (!egg.hatch())
			egg.tick();
	}
}
