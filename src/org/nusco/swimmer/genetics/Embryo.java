package org.nusco.swimmer.genetics;

import java.util.LinkedList;

import org.nusco.swimmer.Swimmer;
import org.nusco.swimmer.body.Head;
import org.nusco.swimmer.body.Organ;

public class Embryo {

	private DNA genes;

	public Embryo(DNA genes) {
		this.genes = genes;
	}
	
	public Swimmer develop() {
		OrganParser parser = new OrganParser(genes);
		
		Head head = createHead(parser);
		Organ[] firstLevelChildren = createTwinOrgans(parser, head);
		
		LinkedList<Organ> secondLevelChildren = new LinkedList<>();
		for (Organ organ : firstLevelChildren) {
			Organ[] twinSecondLevelChildren = createTwinOrgans(parser, organ);
			secondLevelChildren.add(twinSecondLevelChildren[0]);
			secondLevelChildren.add(twinSecondLevelChildren[1]);
		}
		
		createTwinOrgans(parser, secondLevelChildren.getFirst());
		createTwinOrgans(parser, secondLevelChildren.getLast());

		return new Swimmer(head);
	}

	private Head createHead(OrganParser parser) {
		return new OrganBuilder(parser.nextPart()).buildHead();
	}

	private Organ[] createTwinOrgans(OrganParser parser, Organ parent) {
		return new TwinOrgansBuilder(parser.nextPart(), parser.nextPart()).buildSegments(parent);
	}
}
