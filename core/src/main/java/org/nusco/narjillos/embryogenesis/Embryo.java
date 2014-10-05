package org.nusco.narjillos.embryogenesis;

import java.util.LinkedList;
import java.util.List;

import org.nusco.narjillos.creature.body.Body;
import org.nusco.narjillos.creature.body.Organ;
import org.nusco.narjillos.creature.body.Head;
import org.nusco.narjillos.genomics.Chromosome;
import org.nusco.narjillos.genomics.DNA;
import org.nusco.narjillos.genomics.DNAParser;

public class Embryo {

	private final DNA genes;

	public Embryo(DNA genes) {
		this.genes = genes;
	}
	
	public Body develop() {
		DNAParser parser = new DNAParser(genes);
		Head head = new HeadBuilder(parser.nextChromosome()).build();

		List<Organ> organs = new LinkedList<>();
		organs.add(head);
		developRestOfBody(organs, parser);
		
		return new Body(head);
	}

	private void developRestOfBody(List<Organ> organs, DNAParser parser) {
		Chromosome chromosome = parser.nextChromosome();
		if (chromosome == null)
			return;
		List<Organ> children = new LinkedList<>();
		for (Organ organ : organs) {
			Organ child = new BodySegmentBuilder(chromosome).build(organ, +1);
			children.add(child);
		}
		developRestOfBody(children, parser);
	}
}
