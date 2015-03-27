package org.nusco.narjillos.serializer;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.narjillos.genomics.DNA;
import org.nusco.narjillos.serializer.JSON;

public class JSONDNASerializationTest {

	@Test
	public void serializesAndDeserializesDNA() {
		DNA dna = new DNA(1, "{001_002_003_004_005_006_007_008_009_10_11}{012_013_014_15_16_17_18_19_20_21_22}");
		String json = JSON.toJson(dna, DNA.class);
		DNA deserialized = JSON.fromJson(json, DNA.class);

		assertEquals(dna.getId(), deserialized.getId());
		assertArrayEquals(dna.getGenes(), deserialized.getGenes());
	}
}
