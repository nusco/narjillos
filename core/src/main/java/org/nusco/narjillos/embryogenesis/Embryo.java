package org.nusco.narjillos.embryogenesis;

import java.util.LinkedList;
import java.util.List;

import org.nusco.narjillos.creature.body.Body;
import org.nusco.narjillos.creature.body.Organ;
import org.nusco.narjillos.embryogenesis.bodyplan.BodyPlan;
import org.nusco.narjillos.embryogenesis.bodyplan.OrganBuilder;
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
		List<OrganBuilder> organBuilders = getOrganBuilders(parser);
		BodyPlan bodyPlan = new BodyPlan(organBuilders.toArray(new OrganBuilder[0]));
		Organ head = bodyPlan.buildBodyTree();
		return new Body(head);
	}

	private List<OrganBuilder> getOrganBuilders(DNAParser parser) {
		List<OrganBuilder> result = new LinkedList<>();
		result.add(new HeadBuilder(parser.nextChromosome()));

		while (true) {
			Chromosome chromosome = parser.nextChromosome();
			if (chromosome == null)
				return result;
			result.add(new BodySegmentBuilder(chromosome));
		}
	}
}
