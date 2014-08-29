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
		
		assertEquals(RanGen.NO_SEED, RanGen.getSeed());
		
		RanGen.seed(123);
	}

	@Test
	public void generatesADeterministicSequenceOfNumbers() {
		assertEquals(205, RanGen.nextByte());
		assertEquals(0.99, RanGen.nextDouble(), 0.01);
		assertEquals(543942802, RanGen.nextInt());
	}
	
	@Test
	public void canBeFastForwardedToAGivenCounterPosition() {
		reset();
		RanGen.seed(1234);
		for (int i = 0; i < 100; i++) {
			RanGen.nextByte();
			RanGen.nextDouble();
			RanGen.nextInt();
		}
		long position = RanGen.getPosition();
		int expected = RanGen.nextInt();
		
		reset();
		RanGen.seed(1234);
		RanGen.fastForwardTo(position);
		
		assertEquals(expected, RanGen.nextInt());
	}
	
	@Test(expected=RuntimeException.class)
	public void cannotBeSeededTwice() {
		RanGen.seed(245);
	}
	
	@Test
	public void throwsAnExceptionIfYouGenerateNumbersFromMultipleThreads() throws InterruptedException {
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
