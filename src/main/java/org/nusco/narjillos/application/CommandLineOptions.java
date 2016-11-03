package org.nusco.narjillos.application;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.nusco.narjillos.experiment.Experiment;
import org.nusco.narjillos.persistence.ExperimentLoader;

@SuppressWarnings("serial")
public class CommandLineOptions extends Options {

	public static final int NO_SEED = -1;

	private Experiment experiment = null;

	private boolean persistent = true;

	private boolean fast = false;

	private long seed = NO_SEED;

	private String dna = null;

	public static CommandLineOptions parse(boolean printWarnings, String... args) {
		try {
			return new CommandLineOptions(printWarnings, args);
		} catch (RuntimeException e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	CommandLineOptions(String... args) {
		this(true, args);
	}

	private CommandLineOptions(boolean printWarnings, String... args) {
		addOption("?", "help", false, "print this message");
		addOption("f", "fast", false, "fast mode (no graphics)");
		addOption("s", "save", false, "save experiment to file");
		addOption("e", "seed", true, "start experiment with given seed");
		addOption("d", "dna", true, "populate experiment with specific DNA (takes genes, or a file containing genes)");

		CommandLineParser parser = new BasicParser();

		try {
			CommandLine line;
			try {
				line = parser.parse(this, args);
			} catch (UnrecognizedOptionException e) {
				throw new RuntimeException(e.getMessage() + "\n" + getHelpText());
			}

			if (line.hasOption("help")) {
				System.out.println(getHelpText());
				System.exit(0);
			}

			setFast(line.hasOption("fast"));
			setPersistent(line.hasOption("save"));

			if (line.hasOption("seed")) {
				if (line.hasOption("dna"))
					throw new RuntimeException("You cannot pick seed and dna at the same time. It's either one, or the other.");
				setSeed(line.getOptionValue("seed"));
			}

			if (line.hasOption("dna"))
				setDna(line.getOptionValue("dna"));

			if (line.getArgs().length == 0)
				return;

			if (line.getArgs().length > 1)
				throw new RuntimeException(getDontUnderstandText());

			if (getDna() != null || getSeed() != NO_SEED)
				throw new RuntimeException(
					"If you load the experiment from a file, then you cannot pick its seed or DNA.\n" + getHelpText());

			setFile(line.getArgs()[0], printWarnings);

			if (printWarnings && getExperiment() != null && !isPersistent()) {
				System.err.print("WARNING: you're continuing an existing experiment, but I won't update it on disk. ");
				System.err.println("If you want to update the experiment file, then use the --save option.");
			}
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	public Experiment getExperiment() {
		return experiment;
	}

	public boolean isFast() {
		return fast;
	}

	public boolean isPersistent() {
		return persistent;
	}

	public long getSeed() {
		return seed;
	}

	public String getDna() {
		return dna;
	}

	private String getHelpText() {
		StringWriter stringWriter = new StringWriter();
		new HelpFormatter().printHelp(new PrintWriter(stringWriter), 1000, " ", "", this, 2, 2, "");
		return stringWriter.getBuffer().toString();
	}

	private String getDontUnderstandText() {
		return "I don't understand these arguments.\n" + getHelpText();
	}

	private void setFile(String file, boolean printWarnings) {
		this.experiment = ExperimentLoader.load(file, printWarnings);
	}

	private void setFast(boolean fast) {
		this.fast = fast;
	}

	private void setPersistent(boolean persistent) {
		this.persistent = persistent;
	}

	private void setSeed(String seed) {
		String seedWithoutVersion = seed.split("-")[0];
		this.seed = Long.parseLong(seedWithoutVersion);
	}

	private void setDna(String dna) {
		if (dna.startsWith("{")) {
			// inline DNA
			this.dna = dna;
			return;
		}
		// not inline DNA, so it must be a filename
		this.dna = loadDnaDocument(dna);
	}

	private String loadDnaDocument(String fileName) {
		try {
			byte[] encoded = Files.readAllBytes(Paths.get(fileName));
			String dnaDocument = new String(encoded, Charset.defaultCharset());
			return dnaDocument;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
