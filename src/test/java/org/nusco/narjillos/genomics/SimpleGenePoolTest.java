package org.nusco.narjillos.genomics;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SimpleGenePoolTest {
	
	@Test
	public void doesNotTrackDna() {
		GenePool simpleGenePool = new SimpleGenePool();
		
		simpleGenePool.createDNA("{0}");

		assertEquals(0, simpleGenePool.getCurrentSize());
	}
}
