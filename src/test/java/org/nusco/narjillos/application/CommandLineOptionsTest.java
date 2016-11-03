package org.nusco.narjillos.application;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.nusco.narjillos.core.utilities.Version;

public class CommandLineOptionsTest {

	String EXPERIMENT_ID = "1234-" + Version.read();

	@Test
	public void acceptsEmptyArguments() {
		new CommandLineOptions();
	}

	@Test
	public void givesHelpInCaseOfUnknownCommands() {
		try {
			new CommandLineOptions("-wtf");
		} catch (RuntimeException e) {
			assertTrue(e.getMessage().contains("Unrecognized option: -wtf"));
			assertTrue(e.getMessage().contains("usage:"));
		}
	}

	@Test
	public void acceptsAFile() {
		try {
			new CommandLineOptions(EXPERIMENT_ID + ".exp");
		} catch (RuntimeException e) {
			// the file is not there, so this is OK
			assertTrue(e.getMessage().contains("No file named " + EXPERIMENT_ID + ".exp"));
		}
	}

	@Test
	public void acceptsAFastOption() {
		assertFalse(new CommandLineOptions().isFast());
		assertTrue(new CommandLineOptions("-f").isFast());
		assertTrue(new CommandLineOptions("--fast").isFast());
	}

	@Test
	public void acceptsASaveOption() {
		assertFalse(new CommandLineOptions().isPersistent());
		assertTrue(new CommandLineOptions("-s").isPersistent());
		assertTrue(new CommandLineOptions("--save").isPersistent());
	}

	@Test
	public void acceptsAnExperimentSeed() {
		CommandLineOptions options = new CommandLineOptions("--seed", "1234");

		assertEquals(1234, options.getSeed());
	}

	@Test
	public void stripsVersionFromExperimentSeed() {
		CommandLineOptions options = new CommandLineOptions("-seed", EXPERIMENT_ID);

		assertEquals(1234, options.getSeed());
	}

	@Test
	public void refusesSeedAndDnaTogether() {
		try {
			new CommandLineOptions("-seed", "1234", "-dna", "{1_2_3}");
		} catch (RuntimeException e) {
			assertTrue(e.getMessage().contains("seed and dna at the same time"));
		}
	}

	@Test
	public void refusesSeedAndFileTogether() {
		try {
			new CommandLineOptions("-seed", "1234", EXPERIMENT_ID + ".exp");
		} catch (RuntimeException e) {
			assertTrue(e.getMessage().contains("If you load the experiment from a file, then you cannot"));
		}
	}

	@Test
	public void acceptsADNADocument() {
		CommandLineOptions options = new CommandLineOptions("-dna", "{1_2_3}");

		assertEquals("{1_2_3}", options.getDna());
	}

	@Test
	public void acceptsADNAFilename() {
		try {
			new CommandLineOptions("-dna", "my_dna.dna");
		} catch (RuntimeException e) {
			// the file is not there, so this is OK
			assertTrue(e.getMessage().contains("NoSuchFileException: my_dna.dna"));
		}
	}

	@Test
	public void refusesDnaAndFileTogether() {
		try {
			new CommandLineOptions("-dna", "{1_2_3}", EXPERIMENT_ID + ".exp");
		} catch (RuntimeException e) {
			assertTrue(e.getMessage().contains("If you load the experiment from a file, then you cannot"));
		}
	}
}