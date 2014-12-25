package org.nusco.narjillos;

import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CommandLineOptionsTest {

//	@Test
//	public void narjillosAcceptsEmptyArguments() {
//		CommandLineOptions.parse("");
//	}
//
//	@Test
//	public void narjillosAcceptsAnExperimentFile() {
//		CommandLineOptions.parse("1234-0.0.0.exp");
//	}

	@Test
	public void narjillosAcceptsAPersistenceOption() {
//		assertFalse(CommandLineOptions.parse("").isPersistent());
		assertTrue(CommandLineOptions.parse("-p").isPersistent());
		assertTrue(CommandLineOptions.parse("--persistent").isPersistent());
	}

	@Test
	public void narjillosAcceptsAnExperimentSeed() {
		CommandLineOptions options = CommandLineOptions.parse("-seed", "1234");
		
		assertEquals(1234, options.getSeed());
	}

	@Test
	public void narjillosStripsVersionFromExperimentSeed() {
		CommandLineOptions options = CommandLineOptions.parse("-seed", "1234-9.9.9");
		
		assertEquals(1234, options.getSeed());
	}
}