package org.nusco.narjillos.genomics;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.embryogenesis.Embryo;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.things.Energy;
import org.nusco.narjillos.shared.utilities.RanGen;

public class SimHashTest {

	RanGen ranGen = new RanGen(99);
	
	@Test
	public void returnsAByteSizedHash() {
		DNA dna = DNA.random(1, ranGen);
		int min = 1000;
		int max = 0;
		for (int i = 0; i < 10_000; i++) {
			DNA newDna = DNA.random(i + 2, ranGen);
			//DNA newDna = dna.copyWithMutations(i + 2, ranGen);

			Narjillo n1 = new Narjillo(dna, new Embryo(dna).develop(), Vector.ZERO, Energy.INFINITE);
			Narjillo n2 = new Narjillo(newDna, new Embryo(newDna).develop(), Vector.ZERO, Energy.INFINITE);

			int diff = SimHash.getDistance(n1, n2);
			if (diff < min)
				min = diff;
			if (diff > max)
				max = diff;
			dna = newDna;
//			assertTrue(hash >= 0);
//			assertTrue(hash <= 255);
		}
		System.out.println("min: " + min +", max: " + max);
	}
}
