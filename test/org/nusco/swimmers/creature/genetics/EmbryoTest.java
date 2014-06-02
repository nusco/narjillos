package org.nusco.swimmers.creature.genetics;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.nusco.swimmers.creature.Swimmer;
import org.nusco.swimmers.creature.body.Organ;
import org.nusco.swimmers.creature.genetics.DNA;
import org.nusco.swimmers.creature.genetics.Embryo;

public class EmbryoTest {

	@Test
	public void createsASwimmerFromItsDNA() {
		Embryo embryo = new Embryo(DNA.ancestor());

		Swimmer swimmer = embryo.develop();
		
		List<Organ> exampleParts = ExampleParts.asList();
		List<Organ> parts = swimmer.getParts();
		assertEquals(exampleParts.size(), parts.size());
		for (int i = 0; i < exampleParts.size(); i++) {
			assertKindaEquals(exampleParts.get(i), parts.get(i));
		}
	}

	private void assertKindaEquals(Organ organ1, Organ organ2) {
		organ1.toString().equals(organ2.toString());
	}
}
