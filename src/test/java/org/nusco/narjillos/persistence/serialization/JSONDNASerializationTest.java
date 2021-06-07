package org.nusco.narjillos.persistence.serialization;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.narjillos.genomics.DNA;

public class JSONDNASerializationTest {

	@Test
	public void serializesAndDeserializesDNA() {
		DNA dna = new DNA(4,
			"{001_002_003_004_005_006_007_008_009_010_011_012_013_014}{015_016_017_018_019_020_021_022_023_024_025_026_027_028}", 3);
		String json = JSON.toJson(dna, DNA.class);
		DNA deserialized = JSON.fromJson(json, DNA.class);

		assertEquals(dna.getId(), deserialized.getId());
		assertArrayEquals(dna.getGenes(), deserialized.getGenes());
		assertEquals(dna.getParentId(), deserialized.getParentId());
	}
}
