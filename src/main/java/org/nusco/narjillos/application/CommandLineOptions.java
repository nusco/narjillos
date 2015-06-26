package org.nusco.narjillos.application;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.nusco.narjillos.experiment.Experiment;
import org.nusco.narjillos.genomics.GenePool;
import org.nusco.narjillos.serializer.Persistence;

@SuppressWarnings("serial")
public class CommandLineOptions extends Options {

	public static final int NO_SEED = -1;
	
	private Experiment experiment = null;
	private GenePool genePool = null;
	private boolean persistent = true;
	private boolean trackingHistory = false;
	private long seed = NO_SEED;
	private String dna = null;

	public static CommandLineOptions parse(String... args) {
		try {
			return new CommandLineOptions(args);
		} catch (RuntimeException e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	CommandLineOptions(String... args) {
		addOption("h", "help", false, "print this message");
		addOption("p", "persistent", false, "periodically save experiment to file");
		addOption("history", false, "track genepool history (needs a lot of memory)");
		addOption("s", "seed", true, "start experiment with given seed");
		addOption("dna", true, "populate experiment with specific dna at start (either the genes, or a file containing them)");

		CommandLineParser parser = new BasicParser();
		
		try {
			CommandLine line;
			try {
				line = parser.parse(this, args);
			} catch (UnrecognizedOptionException e) {
				throw new RuntimeException(e.getMessage() + "\n" + getHelpText());
			}
			
	        if (line.hasOption("h")) {
	        	System.out.println(getHelpText());
	        	System.exit(0);
	        }

	        setPersistent(line.hasOption("p"));
	        setTrackingHistory(line.hasOption("history"));

	        if (line.hasOption("seed")) {
	        	if (line.hasOption("dna"))
	        		throw new RuntimeException("You cannot pick a seed and dna at the same time. It's either one, or the other.");
	        	setSeed(line.getOptionValue("seed"));
	        }
	        
	        if (line.hasOption("dna"))
	        	setDna(line.getOptionValue("dna"));

	        if (line.getArgs().length == 0)
	        	return;
	        
	        if (line.getArgs().length > 1)
	        	throw new RuntimeException(getDontUnderstandText());
	        
	        if (getDna() != null || getSeed() != NO_SEED)
	        	throw new RuntimeException("If you load the experiment from a file, then you cannot pick its seed or DNA.\n" + getHelpText());
	        
        	if (line.hasOption("history"))
        		System.out.println("WARNING: I'm loading an existing experiment, so I'm ignoring the -history option.");

        	setFile(line.getArgs()[0]);
        	
        	if (getExperiment() != null && !isPersistent()) {
				System.err.println("WARNING: you're loading an experiment from file, but you didn't ask for persistence. I will read the experiment file, but I will not update it.");
				System.err.println("If that is not what you want, then maybe use the --persistence option.");
			}
        } catch(ParseException e) {
	        throw new RuntimeException(e);
	    }
	}

	public Experiment getExperiment() {
		return experiment;
	}

	public GenePool getGenePool() {
		return genePool;
	}
	
	public boolean isPersistent() {
		return persistent;
	}

	public boolean isTrackingHistory() {
		return trackingHistory;
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

	private void setFile(String file) {
		this.experiment = Persistence.loadExperiment(file);
		this.genePool = this.experiment.getGenePool();
	}

	private void setPersistent(boolean persistent) {
		this.persistent = persistent;
	}

	private void setTrackingHistory(boolean trackingHistory) {
		this.trackingHistory = trackingHistory;
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
		this.dna = Persistence.loadDNADocument(dna);
	}
}
