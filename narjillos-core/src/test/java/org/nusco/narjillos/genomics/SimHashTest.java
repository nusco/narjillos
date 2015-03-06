package org.nusco.narjillos.genomics;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.embryogenesis.Embryo;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.things.Energy;
import org.nusco.narjillos.shared.utilities.RanGen;

public class SimHashTest {

	RanGen ranGen = new RanGen(1);
	
	@Test
	public void returnsAByteSizedHash() {
		DNA dna = DNA.random(1, ranGen);
		for (int i = 0; i < 100; i++) {
			DNA newDna = DNA.random(i + 2, ranGen);
			//DNA newDna = dna.copyWithMutations(i + 2, ranGen);

			Narjillo n1 = new Narjillo(dna, new Embryo(dna).develop(), Vector.ZERO, Energy.INFINITE);
			Narjillo n2 = new Narjillo(newDna, new Embryo(newDna).develop(), Vector.ZERO, Energy.INFINITE);

			int diff = SimHash.getDistance(n1, n2);
			System.out.println(diff);
			if (diff <= 1) {
				System.out.println("1> " + dna);
				System.out.println("2> " + newDna);
			}
			dna = newDna;
//			assertTrue(hash >= 0);
//			assertTrue(hash <= 255);
		}
	}
}
