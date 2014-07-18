package org.nusco.swimmers.creature.genetics;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DNAParserTest {

	@Test
	public void iteratesOverParts() {
		int[] genes =  new int[]{
				1, 2, 3, 4, 5,
				6, 7, 8, 9, 10
			};

		DNA dna = new DNA(genes);
		DNAParser parser = new DNAParser(dna);
		
		assertArrayEquals(new int[]{1, 2, 3, 4, 5}, parser.nextPart());
		assertArrayEquals(new int[]{6, 7, 8, 9, 10}, parser.nextPart());
		assertNull(parser.nextPart());
	}

	@Test
	public void padsUnterminatedLastPart() {
		int[] genes =  new int[]{
				1, 2, 3, 4, 5,
				6, 7
			};

		DNA dna = new DNA(genes);
		DNAParser parser = new DNAParser(dna);
		
		assertArrayEquals(new int[]{1, 2, 3, 4, 5}, parser.nextPart());
		assertArrayEquals(new int[]{6, 7, 0, 0, 0}, parser.nextPart());
		assertNull(parser.nextPart());
	}

	@Test
	public void padsUnterminatedSinglePart() {
		int[] genes =  new int[]{
				1, 2, 3
			};

		DNA dna = new DNA(genes);
		DNAParser parser = new DNAParser(dna);
		
		assertArrayEquals(new int[]{1, 2, 3, 0, 0}, parser.nextPart());
		assertNull(parser.nextPart());
	}

	@Test
	public void alwaysReturnsAtLeastOnePart() {
		int[] genes =  new int[0];

		DNA dna = new DNA(genes);
		DNAParser parser = new DNAParser(dna);
		
		assertArrayEquals(new int[]{0, 0, 0, 0, 0}, parser.nextPart());
		assertNull(parser.nextPart());
	}
}
