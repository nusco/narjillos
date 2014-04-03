package org.nusco.swimmer.genetics;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.swimmer.Swimmer;
import org.nusco.swimmer.genetics.DNA;
import org.nusco.swimmer.genetics.Embryo;

public class EmbryoTest {

	@Test
	public void createsASwimmerFromItsDNA() {
		Embryo embryo = new Embryo(DNA.ancestor());

		Swimmer swimmer = embryo.develop();
		
		assertEquals(ExampleParts.asList(), swimmer.getParts());
	}
}
