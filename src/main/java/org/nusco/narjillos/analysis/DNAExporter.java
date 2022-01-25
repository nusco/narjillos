package org.nusco.narjillos.analysis;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Export historical DNA as a tree, in various formats.
 * <p>
 * When converting the gene pool to a tree, it adds an artificial zero node that
 * acts as a root to the root nodes. This creates a single big tree (with the
 * caveat that the first level actually represents unrelated genotypes). The
 * reason for this trickery is that a singly-rooted tree is easier to analyze in
 * most tools that a bunch of separate unrelated trees.
 */
public class DNAExporter {

	private final DNAAnalyzer dnaAnalyzer;

	public DNAExporter(DNAAnalyzer dnaAnalyzer) {
		this.dnaAnalyzer = dnaAnalyzer;
	}

	public String toCSVFormat() {
		StringBuilder result = new StringBuilder();
		for (Entry<Long, Long> entry : dnaAnalyzer.getChildrenToParents().entrySet())
			result.append(String.format("%d;%d\n", entry.getValue(), entry.getKey()));
		return result.toString();
	}

	public String toNEXUSFormat() {
		StringBuilder result = new StringBuilder();
		result.append("begin trees;\n");

		Map<Long, List<Long>> parentsToChildren = dnaAnalyzer.getParentsToChildren();
		String toNewickTree = toNewickTree(0L, parentsToChildren);
		result.append(String.format("tree genotypes = %s;\n", toNewickTree));
		result.append("end;");
		return result.toString();
	}

	// You need a large Java stack when running this - otherwise, it will blow
	// up the stack on very deep phylogenetic trees.
	private String toNewickTree(Long rootId, Map<Long, List<Long>> parentsToChildren) {
		List<Long> children = parentsToChildren.get(rootId);
		if (children.isEmpty())
			return rootId.toString();

		StringBuilder childrenTreeBuilder = new StringBuilder();
		for (Long childId : children)
			childrenTreeBuilder.append(toNewickTree(childId, parentsToChildren)).append(",");
		String childrenTree = childrenTreeBuilder.toString();
		String trimmedChildrenTree = childrenTree.substring(0, childrenTree.length() - 1);
		return "(" + trimmedChildrenTree + ")" + rootId;
	}
}
