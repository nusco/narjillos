package org.nusco.narjillos.shared.utilities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.junit.Test;

public class RanGenTest {

	static {
		if (RanGen.getSeed() == RanGen.NO_SEED)
			RanGen.seed(123);
	}

	@Test
	public void generatesAPredictableSequenceOfNumbersAfterSeeding() {
		assertEquals(86, RanGen.nextByte());
		assertEquals(0.237, RanGen.nextDouble(), 0.01);
		assertEquals(-1.736, RanGen.nextGaussian(), 0.01);
		assertEquals(1111887674, RanGen.nextInt());
	}

	@Test(expected=RuntimeException.class)
	public void cannotBeSeededTwice() {
		RanGen.seed(245);
	}
	
	@Test
	public void throwsAnExceptionIfAccessedByMultipleThreads() throws InterruptedException {
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
}
