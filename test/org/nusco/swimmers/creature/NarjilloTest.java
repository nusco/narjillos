package org.nusco.swimmers.creature;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.swimmers.creature.body.Head;
import org.nusco.swimmers.creature.genetics.DNA;

public class NarjilloTest {

	@Test
	public void hasAMass() {
		Head head = new Head(3, 4, 0, 1);
		head.sproutOrgan(10, 20, 0, 0, 0);
		Narjillo narjillo = new Narjillo(head, DNA.random());
		
		assertEquals(212, narjillo.getMass());
	}
}
