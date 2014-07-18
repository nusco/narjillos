package org.nusco.swimmers.creature.genetics;

import org.nusco.swimmers.creature.body.BodyPart;

class TwinOrgansBuilder {

	private final int[] organ1Genes;
	private final int[] organ2Genes;
	public static final int MIRROR_ORGAN = 0b00000001;

	public TwinOrgansBuilder(int[] organ1Genes, int[] organ2Genes) {
		this.organ1Genes = organ1Genes;
		this.organ2Genes = organ2Genes;
	}

	private boolean isMirrorSegment(int[] genes) {
		int controlGene = genes[0];
		return (controlGene & TwinOrgansBuilder.MIRROR_ORGAN) == TwinOrgansBuilder.MIRROR_ORGAN;
	}

	public BodyPart[] buildSegments(BodyPart parent) {
		if (organ1Genes == null)
			return new BodyPart[0];

		if (organ2Genes == null)
			return new BodyPart[] { new OrganBuilder(organ1Genes).buildSegment(parent, 1) };
		
		if(isMirrorSegment(organ1Genes))
			return buildMirrorSegments(parent, organ2Genes);
		else if(isMirrorSegment(organ2Genes))
			return buildMirrorSegments(parent, organ1Genes);
		else return new BodyPart[] {
			new OrganBuilder(organ1Genes).buildSegment(parent, 1),
			new OrganBuilder(organ2Genes).buildSegment(parent, -1)
		};
	}

	private BodyPart[] buildMirrorSegments(BodyPart parent, int[] genes) {
		return new BodyPart[] {
			new OrganBuilder(genes).buildSegment(parent, 1),
			new OrganBuilder(genes).buildSegment(parent, -1)
		};
	}
}
