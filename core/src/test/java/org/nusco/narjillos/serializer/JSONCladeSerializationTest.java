package org.nusco.narjillos.serializer;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;
import org.nusco.narjillos.creature.genetics.Clades;
import org.nusco.narjillos.creature.genetics.DNA;
import org.nusco.narjillos.shared.utilities.RanGen;

public class JSONCladeSerializationTest {

	@Test
	public void serializesAndDeserializesClades() {
		RanGen ranGen = new RanGen(1234);
		
		Clades clades = new Clades();
		
		DNA parent = new DNA("{1}");
		DNA child1 = parent.copy(ranGen);
		parent.copy(ranGen);
		
		child1.copy(ranGen);
		DNA child2 = child1.copy(ranGen);
		
		String json = JSON.toJson(clades, Clades.class);
		Clades deserialized = JSON.fromJson(json, Clades.class);
		
		assertArrayEquals(deserialized.getAncestry(child2).toArray(), clades.getAncestry(child2).toArray());
	}
}
