package org.nusco.narjillos.creature.serializer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.nusco.narjillos.creature.body.pns.DelayNerve;
import org.nusco.narjillos.creature.body.pns.Nerve;
import org.nusco.narjillos.creature.body.pns.PassNerve;
import org.nusco.narjillos.creature.body.pns.WaveNerve;

public class JSONNerveSerializationTest {

	@Test
	public void serializesAndDeserializesPassNerves() {
		Nerve nerve = new PassNerve();

		String json = JSON.toJson(nerve);
		Nerve deserialized = JSON.fromJson(json, PassNerve.class);

		assertTrue(deserialized instanceof PassNerve);
	}

	@Test
	public void serializesAndDeserializesDelayNerves() {
		Nerve nerve = new DelayNerve(4);
		for (int i = 0; i < 3; i++)
			nerve.tick(i);
		
		String json = JSON.toJson(nerve);
		Nerve deserialized = JSON.fromJson(json, DelayNerve.class);

		for (int i = 0; i < 10; i++)
			assertEquals(nerve.tick(0), deserialized.tick(0), 0.0);
	}

	@Test
	public void serializesAndDeserializesWaveNerves() {
		WaveNerve nerve = new WaveNerve(100);
		for (int i = 0; i < 5; i++)
			nerve.tick(0);
		
		String json = JSON.toJson(nerve);
		Nerve deserialized = JSON.fromJson(json, WaveNerve.class);
		
		for (int i = 0; i < 10; i++)
			assertEquals(nerve.tick(0), deserialized.tick(0), 0.0);
	}
}
