package org.nusco.narjillos.persistence.file;

import static org.junit.Assert.assertEquals;
import static org.nusco.narjillos.core.chemistry.Element.HYDROGEN;
import static org.nusco.narjillos.core.chemistry.Element.NITROGEN;
import static org.nusco.narjillos.core.chemistry.Element.OXYGEN;

import java.util.Iterator;

import org.junit.Test;
import org.nusco.narjillos.core.physics.Vector;
import org.nusco.narjillos.core.things.Energy;
import org.nusco.narjillos.core.things.FoodPellet;
import org.nusco.narjillos.core.things.Thing;
import org.nusco.narjillos.core.utilities.RanGen;
import org.nusco.narjillos.creature.Egg;
import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.experiment.environment.Ecosystem;
import org.nusco.narjillos.genomics.DNA;
import org.nusco.narjillos.genomics.SimpleGenePool;
import org.nusco.narjillos.persistence.file.JSON;

public class JSONEcosystemSerializationTest {

	@Test
	public void serializesAndDeserializesEcosystem() {
		Ecosystem ecosystem = new Ecosystem(123, false);
		FoodPellet food1 = ecosystem.spawnFood(Vector.cartesian(10, 10));
		FoodPellet food2 = ecosystem.spawnFood(Vector.cartesian(20, 20));
		Egg egg = ecosystem.spawnEgg(new DNA(1, "{1_2_3_4_5_6_7_8}"), Vector.cartesian(30, 30), new RanGen(0));

		DNA dna = DNA.random(1, new RanGen(100));
		Narjillo narjillo = new Narjillo(dna, Vector.cartesian(100, 101), 90, Energy.INFINITE);
		ecosystem.insertNarjillo(narjillo);
		
		for (int i = 0; i < 10; i++)
			ecosystem.tick(new SimpleGenePool(), new RanGen(1234));
		
		String json = JSON.toJson(ecosystem, Ecosystem.class);
		Ecosystem deserialized = JSON.fromJson(json, Ecosystem.class);

		assertEquals(123, deserialized.getSize());
		assertEquals(4, deserialized.getThings("").size());
		assertEquals(1, deserialized.getNarjillos().size());

		assertEquals(deserialized.getAtmosphere().getAmountOf(OXYGEN), ecosystem.getAtmosphere().getAmountOf(OXYGEN));
		assertEquals(deserialized.getAtmosphere().getAmountOf(HYDROGEN), ecosystem.getAtmosphere().getAmountOf(HYDROGEN));
		assertEquals(deserialized.getAtmosphere().getAmountOf(NITROGEN), ecosystem.getAtmosphere().getAmountOf(NITROGEN));
		
		Iterator<Thing> thingsIterator = deserialized.getThings("").iterator();
		assertEquals(narjillo.getPosition(), thingsIterator.next().getPosition());
		assertEquals(food1.getPosition(), thingsIterator.next().getPosition());
		assertEquals(food2.getPosition(), thingsIterator.next().getPosition());
		assertEquals(egg.getPosition(), thingsIterator.next().getPosition());
	}
}
