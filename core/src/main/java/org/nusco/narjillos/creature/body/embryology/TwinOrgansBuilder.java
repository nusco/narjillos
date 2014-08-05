package org.nusco.narjillos.creature.body.embryology;

import java.util.LinkedList;
import java.util.List;

import org.nusco.narjillos.creature.body.Organ;
import org.nusco.narjillos.creature.genetics.Chromosome;

class TwinOrgansBuilder {

	private final Chromosome chromosome1;
	private final Chromosome chromosome2;

	public TwinOrgansBuilder(Chromosome chromosome1, Chromosome chromosome2) {
		this.chromosome1 = chromosome1;
		this.chromosome2 = chromosome2;
	}

	// Once in twice, organs are mirrored.
	private boolean isMirrorSegment(Chromosome chromosome) {
		int controlGene = chromosome.getGene(0);
		final int MIRROR_ORGAN_BIT = 0b00000001;
		return (controlGene & MIRROR_ORGAN_BIT) == MIRROR_ORGAN_BIT;
	}

	public List<Organ> buildChildren(Organ parent) {
		List<Organ> result = new LinkedList<>();
		
		if (chromosome1 == null)
			return result;

		if (chromosome2 == null) {
			result.add(new BodySegmentBuilder(chromosome1).build(parent, 1));
			return result;
		}
		
		if(isMirrorSegment(chromosome1))
			return buildMirrorSegments(parent, chromosome2);
		
		if(isMirrorSegment(chromosome2))
			return buildMirrorSegments(parent, chromosome1);
		
		result.add(new BodySegmentBuilder(chromosome1).build(parent, 1));
		result.add(new BodySegmentBuilder(chromosome2).build(parent, -1));
		return result;
	}

	private List<Organ> buildMirrorSegments(Organ parent, Chromosome chromosome) {
		List<Organ> result = new LinkedList<>();
		result.add(new BodySegmentBuilder(chromosome).build(parent, 1));
		result.add(new BodySegmentBuilder(chromosome).build(parent, -1));
		return result;
	}
}
