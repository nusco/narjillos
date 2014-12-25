package org.nusco.narjillos;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.nusco.narjillos.experiment.Experiment;
import org.nusco.narjillos.genomics.DNA;
import org.nusco.narjillos.genomics.GenePool;
import org.nusco.narjillos.serializer.Persistence;

@SuppressWarnings("serial")
class CommandLineOptions extends Options {

	static final int NO_SEED = -1;
	
	private Experiment experiment = null;
	private GenePool genePool = null;
	private boolean persistent = true;
	private long seed = NO_SEED;
	private DNA dna = null;

	private CommandLineOptions() {}

	public static CommandLineOptions parse(String... args) {
		CommandLineOptions options = new CommandLineOptions();
		options.addOption("h", "help", false, "print this message");
		options.addOption("p", "persistent", false, "periodically save experiment and ancestry to file");
		options.addOption("seed", true, "start experiment with given seed");
		options.addOption("dna", true, "populate experiment with specific dna at start (either the genes themselves, or a file containing them)");

	    CommandLineParser parser = new BasicParser();
	    try {
	        // parse the command line arguments
	        CommandLine line = parser.parse(options, args);
	        if (line.hasOption("h")) {
	        	printHelp(options);
	        	System.exit(0);
	        	return null;
	        }

	        options.setPersistent(line.hasOption("p"));

	        if (line.hasOption("seed")) {
	        	if (line.hasOption("dna")) {
	        		System.out.println("You cannot pick a seed and dna at the same time. It's either one, or the other.");
	        		System.exit(1);
	        		return null;
	        	}
	        	options.setSeed(line.getOptionValue("seed"));
	        }
	        
	        if (line.hasOption("dna"))
	        	options.setDna(line.getOptionValue("dna"));

	        if (line.getArgs().length == 0)
	        	return options;

	        if (line.getArgs().length > 1) {
	        	System.out.println("I don't understand these arguments.");
	        	printHelp(options);
	        	System.exit(1);
	        }
	        
	        if (options.getDna() != null || options.getSeed() != NO_SEED) {
	        	System.out.println("If you load the experiment from a file, then you cannot pick its seed or DNA.");
	        	printHelp(options);
	        	System.exit(1);
	        }
	        
	        options.setFile(line.getArgs()[0]);
	    }
	    catch(ParseException e) {
	        System.err.println(e.getMessage());
	        System.exit(1);
	        return null;
	    }
	    
	    return options;
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

	public long getSeed() {
		return seed;
	}

	public DNA getDna() {
		return dna;
	}

	private static void printHelp(Options options) {
		new HelpFormatter().printHelp(" ", options);
	}

	private void setFile(String file) {
		this.experiment = Persistence.loadExperimentWithGenePool(file);
		this.genePool = this.experiment.getGenePool();
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
			this.dna = new DNA(dna);
			return;
		}
		// this must be a filename
		this.dna = Persistence.loadDNA(dna);
	}
}
