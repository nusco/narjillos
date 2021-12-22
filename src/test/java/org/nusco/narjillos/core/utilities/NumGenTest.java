package org.nusco.narjillos.core.utilities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.junit.Test;

public class NumGenTest {

	@Test
	public void generatesADeterministicSequenceOfNumbers() {
		NumGen numGen1 = new NumGen(123);
		NumGen numGen2 = new NumGen(123);

		assertAreInSynch(numGen1, numGen2);
	}

	@Test
	public void generatesASerialNumber() {
		NumGen numGen = new NumGen(123);

		assertEquals(1, numGen.nextSerial());
		assertEquals(2, numGen.nextSerial());
	}

	@Test
	public void throwsAnExceptionIfCalledFromMultipleThreads() throws InterruptedException {
		final NumGen numGen = new NumGen(123456);
		numGen.nextByte();

		final ConcurrentLinkedQueue<String> results = new ConcurrentLinkedQueue<>();

		new Thread(() -> {
			try {
				numGen.nextByte();
			} catch (RuntimeException e) {
				results.add(e.getMessage());
				return;
			}
			results.add("no exception");
		}).start();

		while (results.isEmpty())
			Thread.sleep(10);

		assertTrue(results.peek().startsWith("NumGen accessed from multiple threads"));
	}

	private void assertAreInSynch(NumGen numGen1, NumGen numGen2) {
		for (int i = 0; i < 100; i++) {
			assertEquals(numGen2.nextDouble(), numGen1.nextDouble(), 0.0);
			assertEquals(numGen2.nextInt(), numGen1.nextInt(), 0.0);
			assertEquals(numGen2.nextByte(), numGen1.nextByte(), 0.0);
		}
	}
}
