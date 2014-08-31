package org.nusco.narjillos.serializer;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;

import org.junit.Test;
import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.creature.genetics.DNA;
import org.nusco.narjillos.ecosystem.Ecosystem;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.things.FoodPiece;
import org.nusco.narjillos.shared.things.Thing;

public class JSONEcosystemSerializationTest {

	@Test
	public void serializesAndDeserializesEcosystem() {
		Ecosystem ecosystem = new Ecosystem(123);
		FoodPiece food1 = ecosystem.spawnFood(Vector.cartesian(10, 10));
		FoodPiece food2 = ecosystem.spawnFood(Vector.cartesian(20, 20));
		Narjillo narjillo = ecosystem.spawnNarjillo(Vector.cartesian(30, 30), new DNA("{1_2_3_4_5_6_7}"));
		
		String json = JSON.toJson(ecosystem, Ecosystem.class);
		System.out.println(json);
		Ecosystem deserialized = JSON.fromJson(json, Ecosystem.class);

		assertEquals(123, deserialized.getSize());
		assertEquals(3, deserialized.getThings().size());
		assertEquals(1, deserialized.getPopulation().getSize());

		Iterator<Thing> thingsIterator = deserialized.getThings().iterator();
		assertEquals(food1.getPosition(), thingsIterator.next().getPosition());
		assertEquals(food2.getPosition(), thingsIterator.next().getPosition());
		assertEquals(narjillo.getPosition(), thingsIterator.next().getPosition());
	}
}
