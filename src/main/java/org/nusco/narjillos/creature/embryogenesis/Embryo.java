package org.nusco.narjillos.creature.embryogenesis;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.nusco.narjillos.creature.body.Body;
import org.nusco.narjillos.creature.body.MovingOrgan;
import org.nusco.narjillos.creature.embryogenesis.bodyplan.BodyPlan;
import org.nusco.narjillos.creature.embryogenesis.bodyplan.OrganBuilder;
import org.nusco.narjillos.genomics.Chromosome;
import org.nusco.narjillos.genomics.DNA;

/**
 * Takes DNA, develops into a fully formed Body.
 */
public class Embryo {

	private final DNA dna;

	public Embryo(DNA dna) {
		this.dna = dna;
	}

	public Body develop() {
		List<OrganBuilder> organBuilders = getOrganBuilders();
		BodyPlan bodyPlan = new BodyPlan(organBuilders.toArray(new OrganBuilder[0]));
		MovingOrgan head = bodyPlan.buildBodyTree();
		return new Body(head);
	}

	private List<OrganBuilder> getOrganBuilders() {
		Iterator<Chromosome> iterator = dna.iterator();
		List<OrganBuilder> result = new LinkedList<>();
		result.add(new HeadBuilder(iterator.next()));

		while (iterator.hasNext()) {
			Chromosome chromosome = iterator.next();
			result.add(new BodySegmentBuilder(chromosome));
		}

		return result;
	}
}
