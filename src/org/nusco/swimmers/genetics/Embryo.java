package org.nusco.swimmers.genetics;

import java.util.LinkedList;
import java.util.List;

import org.nusco.swimmers.body.Head;
import org.nusco.swimmers.body.Organ;
import org.nusco.swimmers.body.Swimmer;

public class Embryo {

	private DNA genes;

	public Embryo(DNA genes) {
		this.genes = genes;
	}
	
	public Swimmer develop() {
		OrganParser parser = new OrganParser(genes);
		
		Head head = createHead(parser);
		List<Organ> firstLevelChildren = createUpperAndLowerOrgans(parser, head);
		
		LinkedList<Organ> secondLevelChildren = new LinkedList<>();
		for (Organ organ : firstLevelChildren) {
			secondLevelChildren.addAll(createUpperAndLowerOrgans(parser, organ));
		}
		
		createUpperAndLowerOrgans(parser, secondLevelChildren.getFirst());
		createUpperAndLowerOrgans(parser, secondLevelChildren.getLast());

		return new Swimmer(head);
	}

	private List<Organ> createUpperAndLowerOrgans(OrganParser parser, Organ parent) {
		List<Organ> result = new LinkedList<>();
		result.add(createUpperOrganFrom(parser.nextPart(), parent));
		result.add(createLowerOrganFrom(parser.nextPart(), parent));
		return result;
	}

	private Head createHead(OrganParser parser) {
		return new OrganBuilder(parser.nextPart()).createHead();
	}

	private Organ createUpperOrganFrom(int[] genes, Organ parent) {
		return new OrganBuilder(genes).createOrgan(parent, +1);
	}

	private Organ createLowerOrganFrom(int[] genes, Organ parent) {
		return new OrganBuilder(genes).createOrgan(parent, -1);
	}
}
