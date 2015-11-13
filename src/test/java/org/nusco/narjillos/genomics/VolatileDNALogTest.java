package org.nusco.narjillos.genomics;

import org.nusco.narjillos.genomics.DNALog;

public class VolatileDNALogTest extends DNALogTest {

	@Override
	protected DNALog createNewInstance() {
		return new VolatileDNALog();
	}
}
