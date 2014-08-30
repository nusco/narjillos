package org.nusco.narjillos.shared.utilities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.junit.Before;
import org.junit.Test;

public class RanGenTest {

	@Before
	public void resetRanGen() {
		reset();
	}

	@Test(expected = RuntimeException.class)
	public void failsIfNotInitialized() {
		RanGen.nextByte();
	}

	@Test
	public void generatesADeterministicSequenceOfNumbers() {
		RanGen.initializeWith(123);

		assertEquals(194, RanGen.nextByte());
		assertEquals(0.91, RanGen.nextDouble(), 0.01);
		assertEquals(-1537559018, RanGen.nextInt());
	}

	@Test
	public void returnsTheCurrentSeed() {
		RanGen.initializeWith(123);

		for (int i = 0; i < 10; i++)
			RanGen.nextDouble();
		long seedBeforeNumberGeneration = RanGen.getCurrentSeed();
		double expected = RanGen.nextDouble();

		reset();
		RanGen.initializeWith(seedBeforeNumberGeneration);
		double actual = RanGen.nextDouble();

		assertEquals(expected, actual, 0.0);
	}

	@Test(expected = RuntimeException.class)
	public void cannotBeSeededTwice() {
		RanGen.initializeWith(245);
		RanGen.initializeWith(245);
	}

	@Test
	public void throwsAnExceptionIfYouGenerateNumbersFromMultipleThreads() throws InterruptedException {
		RanGen.initializeWith(123456);
		RanGen.nextByte();

		final ConcurrentLinkedQueue<String> results = new ConcurrentLinkedQueue<>();

		new Thread() {
			@Override
			public void run() {
				try {
					RanGen.nextByte();
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

	private void reset() {
		try {
			RanGen.reset();
			throw new RuntimeException("Excepted an exception from RanGen.reset(), got nothing");
		} catch (RuntimeException e) {
			// RanGen.reset() always throws an exception, to
			// protect ourselves from mistakenly using it in
			// production code. In tests, it's OK to ignore it.
		}
	}
}
