package org.nusco.narjillos.persistence.serialization;

import static org.junit.Assert.assertEquals;
import static org.nusco.narjillos.core.chemistry.Element.HYDROGEN;
import static org.nusco.narjillos.core.chemistry.Element.NITROGEN;
import static org.nusco.narjillos.core.chemistry.Element.OXYGEN;

import java.util.Iterator;

import org.junit.Ignore;
import org.junit.Test;
import org.nusco.narjillos.core.geometry.Vector;
import org.nusco.narjillos.core.things.Energy;
import org.nusco.narjillos.experiment.environment.FoodPellet;
import org.nusco.narjillos.core.things.Thing;
import org.nusco.narjillos.core.utilities.NumGen;
import org.nusco.narjillos.creature.Egg;
import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.experiment.environment.Ecosystem;
import org.nusco.narjillos.genomics.DNA;
import org.nusco.narjillos.genomics.VolatileDNALog;

public class JSONEcosystemSerializationTest {

	@Test @Ignore
	public void serializesAndDeserializesEcosystem() {
		Ecosystem ecosystem = new Ecosystem(123, false);
		FoodPellet food1 = ecosystem.spawnFood(Vector.cartesian(10, 10));
		FoodPellet food2 = ecosystem.spawnFood(Vector.cartesian(20, 20));
		Egg egg = ecosystem.spawnEgg(new DNA(1, "{1_2_3_4_5_6_7_8}"), Vector.cartesian(30, 30), new NumGen(0));

		DNA dna = DNA.random(1, new NumGen(100));
		Narjillo narjillo = new Narjillo(dna, Vector.cartesian(100, 101), 90, Energy.INFINITE);
		ecosystem.insert(narjillo);

		for (int i = 0; i < 10; i++)
			ecosystem.tick(new VolatileDNALog(), new NumGen(1234));

		String json = JSON.toJson(ecosystem, Ecosystem.class);
		Ecosystem deserialized = JSON.fromJson(json, Ecosystem.class);

		assertEquals(123, deserialized.getSize());
		assertEquals(4, deserialized.getAll("").size());
		assertEquals(1, deserialized.getAll(Narjillo.LABEL).size());

		assertEquals(deserialized.getAtmosphere().getAmountOf(OXYGEN), ecosystem.getAtmosphere().getAmountOf(OXYGEN), 0.0);
		assertEquals(deserialized.getAtmosphere().getAmountOf(HYDROGEN), ecosystem.getAtmosphere().getAmountOf(HYDROGEN), 0.0);
		assertEquals(deserialized.getAtmosphere().getAmountOf(NITROGEN), ecosystem.getAtmosphere().getAmountOf(NITROGEN), 0.0);

		Iterator<Thing> thingsIterator = deserialized.getAll("").iterator();
		assertEquals(narjillo.getPosition(), thingsIterator.next().getPosition());
		assertEquals(food1.getPosition(), thingsIterator.next().getPosition());
		assertEquals(food2.getPosition(), thingsIterator.next().getPosition());
		assertEquals(egg.getPosition(), thingsIterator.next().getPosition());
	}
}
