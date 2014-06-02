package org.nusco.swimmers.creature.genetics;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;
import org.nusco.swimmers.creature.genetics.DNA;
import org.nusco.swimmers.creature.genetics.OrganParser;

public class OrganParserTest {

	@Test
	public void iteratesOverParts() {
		int[] genes =  new int[]{
				1, 2, 3, 4, 5,
				6, 7, 8, 9, 10
			};

		DNA dna = new DNA(genes);
		OrganParser parser = new OrganParser(dna);
		
		assertArrayEquals(new int[]{1, 2, 3, 4, 5}, parser.nextPart());
		assertArrayEquals(new int[]{6, 7, 8, 9, 10}, parser.nextPart());
	}

	@Test
	public void padsUnterminatedLastPart() {
		int[] genes =  new int[]{
				1, 2, 3, 4, 5,
				6, 7
			};

		DNA dna = new DNA(genes);
		OrganParser parser = new OrganParser(dna);
		
		assertArrayEquals(new int[]{1, 2, 3, 4, 5}, parser.nextPart());
		assertArrayEquals(new int[]{6, 7, 0, 0, 0}, parser.nextPart());
	}
}
