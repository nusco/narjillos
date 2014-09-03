package org.nusco.narjillos.serializer;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.creature.body.embryogenesis.Embryo;
import org.nusco.narjillos.creature.genetics.DNA;
import org.nusco.narjillos.ecosystem.GenePool;
import org.nusco.narjillos.shared.physics.Vector;

public class JSONGenePoolSerializationTest {

	@Test
	public void serializesAndDeserializesGenePool() {
		DNA dna1 = new DNA("{1_2_3_4_5_6_7}");
		Vector position1 = Vector.cartesian(1, 1);
		Narjillo narjillo1 = new Narjillo(dna1, new Embryo(dna1).develop(), position1);
		DNA dna2 = new DNA("{8_9_10_11_12_13_14}");
		Vector position2 = Vector.cartesian(100, 100);
		Narjillo narjillo2 = new Narjillo(dna2, new Embryo(dna2).develop(), position2);

		GenePool genePool = new GenePool();
		genePool.add(narjillo1);
		genePool.add(narjillo2);
		
		String json = JSON.toJson(genePool, GenePool.class);
		GenePool deserialized = JSON.fromJson(json, GenePool.class);

		assertEquals(2, deserialized.getSize());
		assertEquals(position1, deserialized.getNarjillos().iterator().next().getPosition());
	}
}
