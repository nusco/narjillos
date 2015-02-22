package org.nusco.narjillos.serializer;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;

import org.junit.Test;
import org.nusco.narjillos.creature.Egg;
import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.ecosystem.Ecosystem;
import org.nusco.narjillos.embryogenesis.Embryo;
import org.nusco.narjillos.genomics.DNA;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.things.FoodPiece;
import org.nusco.narjillos.shared.things.Thing;
import org.nusco.narjillos.shared.utilities.RanGen;

public class JSONEcosystemSerializationTest {

	@Test
	public void serializesAndDeserializesEcosystem() {
		Ecosystem ecosystem = new Ecosystem(123);
		FoodPiece food1 = ecosystem.spawnFood(Vector.cartesian(10, 10));
		FoodPiece food2 = ecosystem.spawnFood(Vector.cartesian(20, 20));
		Egg egg = ecosystem.spawnEgg(new DNA(1, "{1_2_3_4_5_6_7_8}"), Vector.cartesian(30, 30), new RanGen(0));

		DNA dna = DNA.random(1, new RanGen(100));
		Narjillo narjillo = new Narjillo(dna, new Embryo(dna).develop(), Vector.cartesian(100, 101), 10000);
		ecosystem.insertNarjillo(narjillo);
		
		String json = JSON.toJson(ecosystem, Ecosystem.class);
		Ecosystem deserialized = JSON.fromJson(json, Ecosystem.class);

		assertEquals(123, deserialized.getSize());
		assertEquals(4, deserialized.getThings("").size());
		assertEquals(1, deserialized.getNarjillos().size());

		Iterator<Thing> thingsIterator = deserialized.getThings("").iterator();
		assertEquals(narjillo.getPosition(), thingsIterator.next().getPosition());
		assertEquals(food1.getPosition(), thingsIterator.next().getPosition());
		assertEquals(food2.getPosition(), thingsIterator.next().getPosition());
		assertEquals(egg.getPosition(), thingsIterator.next().getPosition());
	}
}
