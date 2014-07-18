package org.nusco.swimmers.creature.genetics;

import org.nusco.swimmers.creature.Swimmer;
import org.nusco.swimmers.creature.body.BodyPart;
import org.nusco.swimmers.creature.body.Head;

public class Embryo {

	private DNA genes;

	public Embryo(DNA genes) {
		this.genes = genes;
	}
	
	public Swimmer develop() {
		DNAParser parser = new DNAParser(genes);
		
		Head head = createHeadSystem(parser);
		BodyPart neck = head.getChildren().get(0);

		BodyPart[] children = new BodyPart[] { neck };
		createDescendants(children, parser);
		
		return new Swimmer(head, genes);
	}

	private Head createHeadSystem(DNAParser parser) {
		return new OrganBuilder(parser.nextPart()).buildHeadSystem();
	}

	private void createDescendants(BodyPart[] organs, DNAParser parser) {
		for (int i = 0; i < organs.length; i++) {
			BodyPart[] nextDescendants = createOrgans(organs[i], parser);
			createDescendants(nextDescendants, parser);
		}
	}

	private BodyPart[] createOrgans(BodyPart parent, DNAParser parser) {
		return new TwinOrgansBuilder(parser.nextPart(), parser.nextPart()).buildSegments(parent);
	}
}
