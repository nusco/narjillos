package org.nusco.narjillos.creature.body.embryology;

import java.util.LinkedList;
import java.util.List;

import org.nusco.narjillos.creature.body.Body;
import org.nusco.narjillos.creature.body.Organ;
import org.nusco.narjillos.creature.body.Head;
import org.nusco.narjillos.creature.genetics.DNA;
import org.nusco.narjillos.creature.genetics.DNAParser;

public class Embryo {

	private DNA genes;

	public Embryo(DNA genes) {
		this.genes = genes;
	}
	
	public Body develop() {
		DNAParser parser = new DNAParser(genes);
		
		Head head = createHeadSystem(parser);

		List<Organ> bodyParts = new LinkedList<>();
		bodyParts.add(head);
		createDescendants(bodyParts, parser);
		
		return new Body(head);
	}

	private Head createHeadSystem(DNAParser parser) {
		return new OrganBuilder(parser.nextChromosome()).buildHead();
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
		return new TwinOrgansBuilder(parser.nextChromosome(), parser.nextChromosome()).buildBodyPart(parent);
	}
}
