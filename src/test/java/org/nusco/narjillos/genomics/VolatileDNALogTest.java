package org.nusco.narjillos.genomics;

public class VolatileDNALogTest extends DNALogTest {

	@Override
	protected DNALog createNewInstance() {
		return new VolatileDNALog();
	}
}
