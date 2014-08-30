package org.nusco.narjillos.serializer;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.narjillos.serializer.JSON;
import org.nusco.narjillos.shared.utilities.RanGen;

public class JSONRandomSerializationTest {

	@Test
	public void serializesAndDeserializesTheRandomGenerator() {
		bringRanGenToAKnownState();
		String json = JSON.serializeRandomGenerator();
		double expectedNextDouble = RanGen.nextDouble();
		double expectedCurrentSeed = RanGen.getCurrentSeed();

		resetRanGen();
		
		JSON.deserializeRandomGenerator(json);

		assertEquals(expectedNextDouble, RanGen.nextDouble(), 0.0);
		assertEquals(expectedCurrentSeed, RanGen.getCurrentSeed(), 0.0);
	}

	private void bringRanGenToAKnownState() {
		resetRanGen();
		RanGen.initializeWith(42);
		for (int i = 0; i < 1000; i++)
			RanGen.nextDouble();
	}

	private void resetRanGen() {
		try {
			RanGen.reset();
		} catch (RuntimeException e) {
		}
	}
}
