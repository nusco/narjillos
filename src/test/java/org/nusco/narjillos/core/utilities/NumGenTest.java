package org.nusco.narjillos.core.utilities;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.junit.jupiter.api.Test;

public class NumGenTest {

	@Test
	public void generatesADeterministicSequenceOfNumbers() {
		var numGen1 = new NumGen(123);
		var numGen2 = new NumGen(123);

		assertAreInSynch(numGen1, numGen2);
	}

	@Test
	public void generatesASerialNumber() {
		var numGen = new NumGen(123);

		assertThat(numGen.nextSerial()).isEqualTo(1);
		assertThat(numGen.nextSerial()).isEqualTo(2);
	}

	@Test
	public void throwsAnExceptionIfCalledFromMultipleThreads() throws InterruptedException {
		var numGen = new NumGen(123456);
		numGen.nextByte();

		var results = new ConcurrentLinkedQueue<String>();

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

		assertThat(results.peek()).startsWith("NumGen accessed from multiple threads");
	}

	private void assertAreInSynch(NumGen numGen1, NumGen numGen2) {
		for (int i = 0; i < 100; i++) {
			assertEquals(numGen2.nextDouble(), numGen1.nextDouble(), 0.0);
			assertEquals(numGen2.nextInt(), numGen1.nextInt(), 0.0);
			assertEquals(numGen2.nextByte(), numGen1.nextByte(), 0.0);
		}
	}
}
