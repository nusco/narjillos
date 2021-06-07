package org.nusco.narjillos.persistence.serialization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.nusco.narjillos.creature.body.pns.DelayNerve;
import org.nusco.narjillos.creature.body.pns.Nerve;
import org.nusco.narjillos.creature.body.pns.WaveNerve;

public class JSONNerveSerializationTest {

	@Test
	public void serializesAndDeserializesDelayNerves() {
		Nerve nerve = new DelayNerve(4);
		for (int i = 0; i < 3; i++)
			nerve.tick(i);

		String json = JSON.toJson(nerve, Nerve.class);
		Nerve deserialized = JSON.fromJson(json, Nerve.class);

		for (int i = 0; i < 10; i++)
			assertEquals(nerve.tick(0), deserialized.tick(0), 0.0);
	}

	@Test
	public void serializesAndDeserializesWaveNerves() {
		WaveNerve nerve = new WaveNerve(100);
		for (int i = 0; i < 5; i++)
			nerve.tick(0);

		String json = JSON.toJson(nerve, Nerve.class);
		Nerve deserialized = JSON.fromJson(json, Nerve.class);

		for (int i = 0; i < 10; i++)
			assertEquals(nerve.tick(0), deserialized.tick(0), 0.0);
	}

	@Test
	public void serializesAndDeserializesGenericNerves() {
		Nerve nerve = new DelayNerve(10);

		String json = JSON.toJson(nerve, Nerve.class);
		Nerve deserialized = JSON.fromJson(json, Nerve.class);

		assertTrue(deserialized instanceof DelayNerve);
	}
}
