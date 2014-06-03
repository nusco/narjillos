package org.nusco.swimmers.creature.genetics;

import org.nusco.swimmers.creature.body.Organ;
import org.nusco.swimmers.creature.body.Side;

class TwinOrgansBuilder {

	private final int[] organ1Genes;
	private final int[] organ2Genes;

	public TwinOrgansBuilder(int[] organ1Genes, int[] organ2Genes) {
		this.organ1Genes = organ1Genes;
		this.organ2Genes = organ2Genes;
	}

	private boolean isMirrorSegment(int[] genes) {
		int controlGene = genes[0];
		return (controlGene & DNA.MIRROR_ORGAN) == DNA.MIRROR_ORGAN;
	}

	public Organ[] buildSegments(Organ parent) {
		if(isMirrorSegment(organ1Genes))
			return buildMirrorSegments(parent, organ2Genes);
		else if(isMirrorSegment(organ2Genes))
			return buildMirrorSegments(parent, organ1Genes);
		else return new Organ[] {
			new OrganBuilder(organ1Genes).buildSegment(parent, Side.RIGHT),
			new OrganBuilder(organ2Genes).buildSegment(parent, Side.LEFT)
		};
	}

	private Organ[] buildMirrorSegments(Organ parent, int[] genes) {
		return new Organ[] {
			new OrganBuilder(genes).buildSegment(parent, Side.RIGHT),
			new OrganBuilder(genes).buildSegment(parent, Side.LEFT)
		};
	}
}
