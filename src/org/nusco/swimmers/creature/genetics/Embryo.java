package org.nusco.swimmers.creature.genetics;

import java.util.LinkedList;
import java.util.List;

import org.nusco.swimmers.creature.Narjillo;
import org.nusco.swimmers.creature.body.BodyPart;
import org.nusco.swimmers.creature.body.Head;

public class Embryo {

	private DNA genes;

	public Embryo(DNA genes) {
		this.genes = genes;
	}
	
	public Narjillo develop() {
		DNAParser parser = new DNAParser(genes);
		
		Head head = createHeadSystem(parser);
		
		BodyPart neck = head.getChildren().get(0);
		List<BodyPart> bodyParts = new LinkedList<>();
		bodyParts.add(neck);
		createDescendants(bodyParts, parser);
		
		return new Narjillo(head, genes);
	}

	private Head createHeadSystem(DNAParser parser) {
		return new OrganBuilder(parser.nextChromosome()).buildHeadSystem();
	}

	private void createDescendants(List<BodyPart> bodyParts, DNAParser parser) {
		List<BodyPart> descendants = new LinkedList<>();
		for (BodyPart bodyPart : bodyParts)
			descendants.addAll(createDirectDescendants(bodyPart, parser));
		if (descendants.isEmpty())
			return;
		createDescendants(descendants, parser);
	}

	private List<BodyPart> createDirectDescendants(BodyPart parent, DNAParser parser) {
		return new TwinOrgansBuilder(parser.nextChromosome(), parser.nextChromosome()).buildBodyPart(parent);
	}
}
