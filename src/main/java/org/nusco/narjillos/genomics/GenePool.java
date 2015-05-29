package org.nusco.narjillos.genomics;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.nusco.narjillos.core.utilities.RanGen;

/**
 * A pool of DNA strands.
 */
public class GenePool {

	private final Map<Long, DNA> dnaById = new LinkedHashMap<>();
	private final List<Long> currentPool = new LinkedList<>();
	private final Map<Long, Long> childrenToParents = new LinkedHashMap<>();

	private long dnaSerial = 0;
	private boolean ancestralMemory = false;

	// TODO: change to a hierarchy with subclasses with/without ancestral
	// memory?
	public void enableAncestralMemory() {
		ancestralMemory = true;
	}

	public boolean hasAncestralMemory() {
		return ancestralMemory;
	}

	public DNA getDNA(long id) {
		return dnaById.get(new Long(id));
	}

	public List<DNA> getAncestry(DNA dna) {
		List<DNA> result = new LinkedList<>();

		Long currentDnaId = dna.getId();
		while (currentDnaId != 0) {
			result.add(dnaById.get(currentDnaId));
			currentDnaId = childrenToParents.get(currentDnaId);
		}

		Collections.reverse(result);
		return result;
	}

	public DNA getMostSuccessfulDNA() {
		DNA result = null;
		int lowestLevenshteinDistance = Integer.MAX_VALUE;
		for (Long dnaId : currentPool) {
			DNA dna = dnaById.get(dnaId);
			int currentLevenshteinDistance = totalLevenshteinDistanceFromTheRestOfThePool(dna);
			if (currentLevenshteinDistance < lowestLevenshteinDistance) {
				result = dna;
				lowestLevenshteinDistance = currentLevenshteinDistance;
			}
		}
		return result;
	}

	public double getMutationSpeedOverLast10Generations() {
		if (currentPool.isEmpty())
			return 0;

		double result = 0;
		for (long dnaId : currentPool) {
			DNA dna = dnaById.get(dnaId);
			DNA tenthGenerationAncestor = getAncestor(dna, 10);
			result += dna.getLevenshteinDistanceFrom(tenthGenerationAncestor);
		}
		return result / currentPool.size();
	}

	DNA getAncestor(DNA dna, int generations) {
		DNA currentAncestor = dna;
		for (int i = 1; i < generations; i++) {
			Long parentId = childrenToParents.get(currentAncestor.getId());
			if (parentId == 0)
				return currentAncestor;
			currentAncestor = dnaById.get(parentId);
		}
		return currentAncestor;
	}

	public int getCurrentPoolSize() {
		return currentPool.size();
	}

	public int getHistoricalPoolSize() {
		return dnaById.size();
	}

	public DNA createDNA(String dna) {
		DNA result = new DNA(nextId(), dna);
		add(result, null);
		return result;
	}

	public DNA createRandomDNA(RanGen ranGen) {
		DNA result = DNA.random(nextId(), ranGen);
		add(result, null);
		return result;
	}

	public DNA mutateDNA(DNA parent, RanGen ranGen) {
		DNA result = parent.mutate(nextId(), ranGen);
		add(result, parent);
		return result;
	}

	public void remove(DNA dna) {
		if (!hasAncestralMemory())
			return;

		currentPool.remove(dna.getId());
	}

	private long nextId() {
		return ++dnaSerial;
	}

	private void add(DNA dna, DNA parent) {
		if (!hasAncestralMemory())
			return;

		dnaById.put(dna.getId(), dna);
		currentPool.add(dna.getId());
		if (parent == null)
			childrenToParents.put(dna.getId(), 0l);
		else
			childrenToParents.put(dna.getId(), parent.getId());
	}

	private int totalLevenshteinDistanceFromTheRestOfThePool(DNA dna) {
		int result = 0;
		for (Long otherDNAId : currentPool) {
			DNA otherDNA = dnaById.get(otherDNAId);
			if (!otherDNA.equals(dna))
				result += dna.getLevenshteinDistanceFrom(otherDNA);
		}
		return result;
	}

	// When converting the gene pool to a tree, add an artifical zero
	// node that acts as a root to the root nodes.
	// This creates a single big tree (with the caveat that the first
	// level actually representes unrelated genotypes). A single tree
	// is more convenient to analyze in most tools that a bunch of
	// separate unrelated trees.

	// TODO: conversions should move to their own class or classes
	public String toCSVFormat() {
		StringBuffer result = new StringBuffer();
		for (Entry<Long, Long> entry : childrenToParents.entrySet())
			result.append(entry.getValue() + ";" + entry.getKey() + "\n");
		return result.toString();
	}

	public String toNEXUSFormat() {
		StringBuffer result = new StringBuffer();
		result.append("begin trees;\n");

		Map<Long, List<Long>> parentsToChildren = calculateParentsToChildren();
		String toNewickTree = toNewickTree(new Long(0), parentsToChildren);
		result.append("tree genotypes = " + toNewickTree + ";\n");
		result.append("end;");
		return result.toString();
	}
	
	// Use a large Java stack when running this - otherwise, it will blow up
	// the stack on very deep phylogenetic trees.
	private String toNewickTree(Long rootId, Map<Long, List<Long>> parentsToChildren) {
		List<Long> children = parentsToChildren.get(rootId);
		if (children.isEmpty())
			return rootId.toString();

		StringBuffer childrenTreeBuffer = new StringBuffer();
		for (Long childId : children)
			childrenTreeBuffer.append(toNewickTree(childId, parentsToChildren) + ",");
		String childrenTree = childrenTreeBuffer.toString();
		String trimmedChildrenTree = childrenTree.substring(0, childrenTree.length() - 1);
		return "(" + trimmedChildrenTree + ")" + rootId;
	}

	private LinkedHashMap<Long, List<Long>> calculateParentsToChildren() {
		LinkedHashMap<Long, List<Long>> parentsToChildren = new LinkedHashMap<>();

		parentsToChildren.put(new Long(0), new LinkedList<Long>());

		for (Long dnaId : childrenToParents.keySet())
			parentsToChildren.put(dnaId, new LinkedList<Long>());

		for (Long childId : childrenToParents.keySet())
			parentsToChildren.get(childrenToParents.get(childId)).add(childId);
		return parentsToChildren;
	}
}
