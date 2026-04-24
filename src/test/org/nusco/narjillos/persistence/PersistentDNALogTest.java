package org.nusco.narjillos.persistence;

import org.junit.Test;
import org.nusco.narjillos.genomics.DNALog;
import org.nusco.narjillos.genomics.DNALogTest;

public class PersistentDNALogTest extends DNALogTest {

	protected DNALog createNewInstance() {
		return new PersistentDNALog("123-TESTING");
	}

	@Test
	public void doesNotRaiseAnErrorIfConnectingToTheSameLogFromMultiplePlaces() {
		PersistentDNALog anotherConnectionToTheSameDb = new PersistentDNALog("123-TESTING");
		anotherConnectionToTheSameDb.close();
	}
}
