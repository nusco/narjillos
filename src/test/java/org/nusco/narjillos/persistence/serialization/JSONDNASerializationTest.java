package org.nusco.narjillos.persistence.serialization;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.nusco.narjillos.genomics.DNA;

public class JSONDNASerializationTest {

	@Test
	public void serializesAndDeserializesDNA() {
		var document = "{001_002_003_004_005_006_007_008_009_010_011_012_013_014}" +
			"{015_016_017_018_019_020_021_022_023_024_025_026_027_028}";
		var dna = new DNA(4, document, 3);

		String json = JSON.toJson(dna, DNA.class);
		DNA deserialized = JSON.fromJson(json, DNA.class);

		assertThat(deserialized.getId()).isEqualTo(dna.getId());
		assertThat(deserialized.getGenes()).containsExactly(dna.getGenes());
		assertThat(deserialized.getParentId()).isEqualTo(dna.getParentId());
	}
}
