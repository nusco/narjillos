package org.nusco.swimmers.genetics;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.swimmers.body.Swimmer;

public class DNATest {

	@Test
	public void returnsAPhenotype() {
		DNA dna = DNA.createRandomDNA();
		Swimmer swimmer = dna.toPhenotype();
		
		assertEquals(ExampleParts.asList(), swimmer.getParts());
	}
}
