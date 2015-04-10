package org.nusco.narjillos.core.utilities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.junit.Test;
import org.nusco.narjillos.core.utilities.RanGen;

public class RanGenTest {

	@Test
	public void generatesADeterministicSequenceOfNumbers() {
		RanGen ranGen1 = new RanGen(123);
		RanGen ranGen2 = new RanGen(123);
		
		assertAreInSynch(ranGen1, ranGen2);
	}

	@Test
	public void returnsItsCurrentSeed() {
		RanGen ranGen1 = new RanGen(123);

		// get to a known state
		for (int i = 0; i < 10; i++)
			ranGen1.nextDouble();
		
		long seedBeforeNumberGeneration = ranGen1.getSeed();

		RanGen ranGen2 = new RanGen(seedBeforeNumberGeneration);
		
		assertAreInSynch(ranGen1, ranGen2);
	}

	@Test
	public void throwsAnExceptionIfCalledFromMultipleThreads() throws InterruptedException {
		final RanGen ranGen = new RanGen(123456);
		ranGen.nextByte();

		final ConcurrentLinkedQueue<String> results = new ConcurrentLinkedQueue<>();

		new Thread() {
			@Override
			public void run() {
				try {
					ranGen.nextByte();
				} catch (RuntimeException e) {
					results.add(e.getMessage());
					return;
				}
				results.add("no exception");
			}
		}.start();

		while (results.isEmpty())
			Thread.sleep(10);

		assertTrue(results.peek().startsWith("RanGen accessed from multiple threads"));
	}

	private void assertAreInSynch(RanGen ranGen1, RanGen ranGen2) {
		for (int i = 0; i < 100; i++) {
			assertEquals(ranGen2.nextDouble(), ranGen1.nextDouble(), 0.0);
			assertEquals(ranGen2.nextInt(), ranGen1.nextInt(), 0.0);
			assertEquals(ranGen2.nextByte(), ranGen1.nextByte(), 0.0);
		}
	}
}
