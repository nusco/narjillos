package org.nusco.narjillos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.nusco.narjillos.core.utilities.Version;

public class CommandLineOptionsTest {

	private final String EXPERIMENT_ID = "1234-" + Version.read();

	@Test
	public void acceptsEmptyArguments() {
		new CommandLineOptions();
	}

	@Test
	public void givesHelpInCaseOfUnknownCommands() {
		assertThatThrownBy(() -> new CommandLineOptions("-wtf"))
				.isInstanceOf(RuntimeException.class)
				.hasMessageContaining("Unrecognized option: -wtf")
				.hasMessageContaining("usage:");
	}

	@Test
	public void acceptsAFile() {
		assertThatThrownBy(() -> new CommandLineOptions(EXPERIMENT_ID + ".exp"))
				.isInstanceOf(RuntimeException.class)
				.hasMessageContaining("No file named " + EXPERIMENT_ID + ".exp");
	}

	@Test
	public void acceptsAFastOption() {
		assertThat(new CommandLineOptions().isFast()).isFalse();
		assertThat(new CommandLineOptions("-f").isFast()).isTrue();
		assertThat(new CommandLineOptions("--fast").isFast()).isTrue();
	}

	@Test
	public void acceptsASaveOption() {
		assertThat(new CommandLineOptions().isPersistent()).isFalse();
		assertThat(new CommandLineOptions("-s").isPersistent()).isTrue();
		assertThat(new CommandLineOptions("--save").isPersistent()).isTrue();
	}

	@Test
	public void acceptsAnExperimentSeed() {
		var options = new CommandLineOptions("--seed", "1234");

		assertThat(options.getSeed()).isEqualTo(1234);
	}

	@Test
	public void stripsVersionFromExperimentSeed() {
		var options = new CommandLineOptions("-seed", EXPERIMENT_ID);

		assertThat(options.getSeed()).isEqualTo(1234);
	}

	@Test
	public void refusesSeedAndDnaTogether() {
		assertThatThrownBy(() -> new CommandLineOptions("-seed", "1234", "-dna", "{1_2_3}"))
				.isInstanceOf(RuntimeException.class)
				.hasMessageContaining("seed and dna at the same time");
	}

	@Test
	public void refusesSeedAndFileTogether() {
		assertThatThrownBy(() -> new CommandLineOptions("-seed", "1234", EXPERIMENT_ID + ".exp"))
				.isInstanceOf(RuntimeException.class)
				.hasMessageContaining("If you load the experiment from a file, then you cannot");
	}

	@Test
	public void acceptsADNADocument() {
		var options = new CommandLineOptions("-dna", "{1_2_3}");

		assertThat(options.getDna()).isEqualTo("{1_2_3}");
	}

	@Test
	public void acceptsADNAFilename() {
		assertThatThrownBy(() -> new CommandLineOptions("-dna", "my_dna.dna"))
				.isInstanceOf(RuntimeException.class)
				.hasMessageContaining("NoSuchFileException: my_dna.dna");
	}

	@Test
	public void refusesDnaAndFileTogether() {
		assertThatThrownBy(() -> new CommandLineOptions("-dna", "{1_2_3}", EXPERIMENT_ID + ".exp"))
				.isInstanceOf(RuntimeException.class)
				.hasMessageContaining("If you load the experiment from a file, then you cannot");
	}
}
