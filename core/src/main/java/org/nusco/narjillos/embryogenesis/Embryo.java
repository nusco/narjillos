package org.nusco.narjillos.embryogenesis;

import java.util.LinkedList;
import java.util.List;

import org.nusco.narjillos.creature.body.Body;
import org.nusco.narjillos.creature.body.MovingOrgan;
import org.nusco.narjillos.embryogenesis.bodyplan.BodyPlan;
import org.nusco.narjillos.embryogenesis.bodyplan.OrganBuilder;
import org.nusco.narjillos.genomics.Chromosome;
import org.nusco.narjillos.genomics.DNA;
import org.nusco.narjillos.genomics.DNAIterator;

/**
 * Takes DNA, develops into a fully formed Body.
 */
public class Embryo {

	private final DNA genes;

	public Embryo(DNA genes) {
		this.genes = genes;
	}
	
	public Body develop() {
		DNAIterator iterator = new DNAIterator(genes);
		List<OrganBuilder> organBuilders = getOrganBuilders(iterator);
		BodyPlan bodyPlan = new BodyPlan(organBuilders.toArray(new OrganBuilder[0]));
		MovingOrgan head = bodyPlan.buildBodyTree();
		return new Body(head);
	}

	private List<OrganBuilder> getOrganBuilders(DNAIterator dnaIterator) {
		List<OrganBuilder> result = new LinkedList<>();
		result.add(new HeadBuilder(dnaIterator.nextChromosome()));

		while (true) {
			Chromosome chromosome = dnaIterator.nextChromosome();
			if (chromosome == null)
				return result;
			result.add(new BodySegmentBuilder(chromosome));
		}
	}
}
