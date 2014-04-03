package org.nusco.swimmer.genetics;

import org.nusco.swimmer.body.Organ;

class TwinOrgansBuilder {

	private final int[] organ1Genes;
	private final int[] organ2Genes;

	public TwinOrgansBuilder(int[] organ1Genes, int[] organ2Genes) {
		this.organ1Genes = organ1Genes;
		this.organ2Genes = organ2Genes;
	}

	private boolean isMirrorPart(int[] genes) {
		int controlGene = genes[0];
		return (controlGene & DNA.MIRROR_ORGAN) == DNA.MIRROR_ORGAN;
	}

	public Organ[] buildBodyParts(Organ parent) {
		if(isMirrorPart(organ1Genes))
			return buildMirrorBodyParts(parent, organ2Genes);
		else if(isMirrorPart(organ2Genes))
			return buildMirrorBodyParts(parent, organ1Genes);
		else return new Organ[] {
			new OrganBuilder(organ1Genes).buildBodyPart(parent, +1),
			new OrganBuilder(organ2Genes).buildBodyPart(parent, -1)
		};
	}

	private Organ[] buildMirrorBodyParts(Organ parent, int[] genes) {
		return new Organ[] {
			new OrganBuilder(genes).buildBodyPart(parent, +1),
			new OrganBuilder(genes).buildBodyPart(parent, -1)
		};
	}
}
