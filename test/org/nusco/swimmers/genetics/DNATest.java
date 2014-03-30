package org.nusco.swimmers.genetics;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.swimmers.body.Swimmer;

public class DNATest {

	@Test
	public void returnsAPhenotype() {
		int[] genes =  new int[]{
				25, 10, 123, DNA.TERMINATE_PART,
				60, 12, 45, 123,
				60, 12, 45, 123,
				35, 15, 30, 123,
				35, 15, 30, 123,
				15, 15, 30, 123,
				15, 15, 30, 123
			};

		DNA dna = new DNA(genes);
		Swimmer swimmer = dna.toPhenotype();
		
		assertEquals(ExampleParts.asList(), swimmer.getParts());
	}
}
