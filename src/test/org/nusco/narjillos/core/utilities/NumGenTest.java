package org.nusco.narjillos.core.utilities;

import org.junit.Test;

import java.util.concurrent.ConcurrentLinkedQueue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NumGenTest {
	@Test
	public void generatesADeterministicSequenceOfNumbers() {
		NumGen numGen1 = new NumGen(123);
		NumGen numGen2 = new NumGen(123);

		assertAreInSync(numGen1, numGen2);
	}

	@Test
	public void creatingItWithASingleSeedIsTheSameAsCreatingItWithTwoEqualSeeds() {
		NumGen numGen1 = new NumGen(123);
		NumGen numGen2 = new NumGen(123, 123, 0);

		assertAreInSync(numGen1, numGen2);
	}

	@Test
	public void aNewOneCanBeCreatedFromTheDataOfAnExistingOne() {
		NumGen numGen1 = new NumGen(123, 456, 789);
		for (int i = 0; i < 100; i++) {
			numGen1.nextInt();
			numGen1.nextDouble();
			numGen1.nextByte();
			numGen1.nextSerial();
		}

		NumGen numGen2 = new NumGen(numGen1.getSeed1(), numGen1.getSeed2(), numGen1.getSerial());

		assertAreInSync(numGen1, numGen2);
	}

	@Test
	public void generatesSerialNumbers() {
		NumGen numGen = new NumGen(123);

		assertEquals(1, numGen.nextSerial());
		assertEquals(2, numGen.nextSerial());
	}

	@Test
	public void canBeGivenASerialNumberToStartFrom() {
		NumGen numGen = new NumGen(321, 654, 10);

		assertEquals(11, numGen.nextSerial());
		assertEquals(12, numGen.nextSerial());
	}

	@Test
	public void throwsAnExceptionIfCalledFromMultipleThreads() throws InterruptedException {
		final NumGen numGen = new NumGen(123);
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

	private void assertAreInSync(NumGen numGen1, NumGen numGen2) {
		for (int i = 0; i < 100; i++) {
			assertEquals(numGen2.nextDouble(), numGen1.nextDouble(), 0.0);
			assertEquals(numGen2.nextInt(), numGen1.nextInt(), 0.0);
			assertEquals(numGen2.nextByte(), numGen1.nextByte(), 0.0);
		}
	}
}
