package org.nusco.narjillos.genomics;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.nusco.narjillos.shared.utilities.RanGen;

public class DNAObserverTest {

	final String[] log = new String[1];

	@Before
	public void setUpObserver() {
		DNA.setObserver(new DNAObserver() {

			@Override
			public void created(DNA newDNA, DNA parent) {
				log[0] = newDNA + "," + parent;
			}

			@Override
			public void removed(DNA dna) {
				log[0] = "Removed: " + dna;
			}
		});
	}

	@After
	public void tearDownObserver() {
		DNA.setObserver(DNAObserver.NULL);
	}

	@Test
	public void observesCreationgOfNewDNA() {
		DNA parentDna = new DNA("{1}");

		String expected = parentDna.toString() + ",null";
		assertEquals(expected, log[0]);
	}

	@Test
	public void observesDNACopy() {
		DNA parentDna = new DNA("{1}");
		DNA childDNA = parentDna.copy(new RanGen(1234));

		String expected = childDNA.toString() + "," + parentDna.toString();
		assertEquals(expected, log[0]);
	}

	@Test
	public void observesDNARemoval() {
		DNA dna = new DNA("{1}");
		dna.removeFromPool();

		String expected = "Removed: " + dna.toString();
		assertEquals(expected, log[0]);
	}
}
