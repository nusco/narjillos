package org.nusco.narjillos.embryogenesis;

import java.util.LinkedList;
import java.util.List;

import org.nusco.narjillos.creature.body.Body;
import org.nusco.narjillos.creature.body.Organ;
import org.nusco.narjillos.creature.body.Head;
import org.nusco.narjillos.genomics.DNA;
import org.nusco.narjillos.genomics.DNAParser;

strictfp public class Embryo {

	private final DNA genes;

	public Embryo(DNA genes) {
		this.genes = genes;
	}
	
	public Body develop() {
		DNAParser parser = new DNAParser(genes);
		
		Head head = createHead(parser);

		List<Organ> bodyParts = new LinkedList<>();
		bodyParts.add(head);
		
		createDescendants(bodyParts, parser);
		
		return new Body(head);
	}

	private Head createHead(DNAParser parser) {
		return new HeadBuilder(parser.nextChromosome()).build();
	}

	private void createDescendants(List<Organ> bodyParts, DNAParser parser) {
		List<Organ> descendants = new LinkedList<>();
		for (Organ bodyPart : bodyParts)
			descendants.addAll(createDirectDescendants(bodyPart, parser));
		if (descendants.isEmpty())
			return;
		createDescendants(descendants, parser);
	}

	private List<Organ> createDirectDescendants(Organ parent, DNAParser parser) {
		return new TwinOrgansBuilder(parser.nextChromosome(), parser.nextChromosome()).buildChildren(parent);
	}
}
