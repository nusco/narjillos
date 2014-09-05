package org.nusco.narjillos.creature.genetics;

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
		});
	}

	@After
	public void tearDownObserver() {
		DNA.setObserver(DNAObserver.NULL);
	}

	@Test
	public void notifiesCreationgOfNewDNA() {
		DNA parentDna = new DNA("{1}");

		String expected = parentDna.toString() + ",null";

		assertEquals(expected, log[0]);
	}

	@Test
	public void notifiesDNACopy() {
		DNA parentDna = new DNA("{1}");
		DNA childDNA = parentDna.copy(new RanGen(1234));

		String expected = childDNA.toString() + "," + parentDna.toString();

		assertEquals(expected, log[0]);
	}
}
