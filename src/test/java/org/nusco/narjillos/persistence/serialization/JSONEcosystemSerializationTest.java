package org.nusco.narjillos.persistence.serialization;

import static org.assertj.core.api.Assertions.assertThat;
import static org.nusco.narjillos.core.chemistry.Element.*;

import java.util.Iterator;

import org.junit.jupiter.api.Test;
import org.nusco.narjillos.core.geometry.Vector;
import org.nusco.narjillos.core.things.Energy;
import org.nusco.narjillos.core.things.Thing;
import org.nusco.narjillos.core.utilities.NumGen;
import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.experiment.environment.Ecosystem;
import org.nusco.narjillos.genomics.DNA;
import org.nusco.narjillos.genomics.VolatileDNALog;

public class JSONEcosystemSerializationTest {

	@Test
	public void serializesAndDeserializesEcosystem() {
		var ecosystem = new Ecosystem(123, false);

		var food1 = ecosystem.spawnFood(Vector.cartesian(10, 10));
		var food2 = ecosystem.spawnFood(Vector.cartesian(20, 20));
		var egg = ecosystem.spawnEgg(new DNA(1, "{1_2_3_4_5_6_7_8}"), Vector.cartesian(30, 30), new NumGen(0));

		var dna = DNA.random(1, new NumGen(100));

		var narjillo = new Narjillo(dna, Vector.cartesian(100, 101), 90, Energy.INFINITE);
		ecosystem.insert(narjillo);

		for (int i = 0; i < 10; i++)
			ecosystem.tick(new VolatileDNALog(), new NumGen(1234));

		String json = JSON.toJson(ecosystem, Ecosystem.class);
		Ecosystem deserialized = JSON.fromJson(json, Ecosystem.class);

		assertThat(deserialized.getSize()).isEqualTo(123);
		assertThat(deserialized.getAll("")).hasSize(4);
		assertThat(deserialized.getAll(Narjillo.LABEL)).hasSize(1);

		assertThat(deserialized.getAtmosphere().getAmountOf(OXYGEN)).isEqualTo(ecosystem.getAtmosphere().getAmountOf(OXYGEN));
		assertThat(deserialized.getAtmosphere().getAmountOf(HYDROGEN)).isEqualTo(ecosystem.getAtmosphere().getAmountOf(HYDROGEN));
		assertThat(deserialized.getAtmosphere().getAmountOf(NITROGEN)).isEqualTo(ecosystem.getAtmosphere().getAmountOf(NITROGEN));

		Iterator<Thing> thingsIterator = deserialized.getAll("").iterator();

		assertThat(thingsIterator.next().getPosition()).isEqualTo(food1.getPosition());
		assertThat(thingsIterator.next().getPosition()).isEqualTo(food2.getPosition());
		assertThat(thingsIterator.next().getPosition()).isEqualTo(egg.getPosition());
		assertThat(thingsIterator.next().getPosition()).isEqualTo(narjillo.getPosition());
	}
}
