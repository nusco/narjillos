package org.nusco.narjillos.persistence.file;

import org.nusco.narjillos.genomics.GenePool;
import org.nusco.narjillos.genomics.SimpleGenePool;

import com.google.gson.JsonParseException;

class GenePoolAdapter extends HierarchyAdapter<GenePool> {

	private static final String GENE_POOL_PACKAGE = GenePool.class.getPackage().getName();

	@Override
	protected String getTypeTag(GenePool obj) {
		if (obj instanceof SimpleGenePool)
			return "SimpleGenePool";
		else
			return "GenePoolWithHistory";
	}

	@Override
	protected Class<?> getClass(String typeTag) throws JsonParseException {
		return getClassForName(GENE_POOL_PACKAGE + "." + typeTag);
	}
}
