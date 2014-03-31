package org.nusco.swimmer.genetics;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;
import org.nusco.swimmer.genetics.DNA;

public class OrganParserTest {

	@Test
	public void iteratesOverRightSizedParts() {
		int[] genes =  new int[]{
				1, 2, 3, 4,
				5, 6, 7, 8
			};

		DNA dna = new DNA(genes);
		OrganParser parser = new OrganParser(dna);
		
		assertArrayEquals(new int[]{1, 2, 3, 4}, parser.nextPart());
		assertArrayEquals(new int[]{5, 6, 7, 8}, parser.nextPart());
	}

	@Test
	public void padsShortSizedPartsToTheRightLength() {
		int[] genes =  new int[]{
				1, 2, 3, DNA.TERMINATE_PART,
				5, 6, 7, 8
			};

		DNA dna = new DNA(genes);
		OrganParser parser = new OrganParser(dna);
		
		assertArrayEquals(new int[]{1, 2, 3, 0}, parser.nextPart());
		assertArrayEquals(new int[]{5, 6, 7, 8}, parser.nextPart());
	}

	@Test
	public void padsEmptyPartsToTheRightLength() {
		int[] genes =  new int[]{
				DNA.TERMINATE_PART,
				1, 2, 3, 4
			};

		DNA dna = new DNA(genes);
		OrganParser parser = new OrganParser(dna);
		
		assertArrayEquals(new int[]{0, 0, 0, 0}, parser.nextPart());
		assertArrayEquals(new int[]{1, 2, 3, 4}, parser.nextPart());
	}

	@Test
	public void acceptsAnUnterminatedLastPart() {
		int[] genes =  new int[]{
				1, 2, 3, 4,
				5, 6, 7
			};

		DNA dna = new DNA(genes);
		OrganParser parser = new OrganParser(dna);
		
		assertArrayEquals(new int[]{1, 2, 3, 4}, parser.nextPart());
		assertArrayEquals(new int[]{5, 6, 7, 0}, parser.nextPart());
	}
}
