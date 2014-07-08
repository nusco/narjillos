package org.nusco.swimmers.creature.genetics;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.nusco.swimmers.creature.Swimmer;
import org.nusco.swimmers.creature.body.Organ;

public class MutationManualTest {

	public static int[] visibleMutationsPerGeneration = new int[1000];
	
	public static void main(String[] args) {
		DNA genes = DNA.ancestor();
		Swimmer swimmer = new Embryo(genes).develop();

		for (int i = 0; i < visibleMutationsPerGeneration.length; i++) {
			Set<Object> fatherParts = toSetOfParts(swimmer);
			swimmer = nextGeneration(genes);
			Set<Object> sonParts = toSetOfParts(swimmer);
			visibleMutationsPerGeneration[i] = getNumberOfDifferences(fatherParts, sonParts);
		}
		
		double averageVisibleMutationsPerGeneration = getAverage(visibleMutationsPerGeneration);

		System.out.println("Generations: " + visibleMutationsPerGeneration.length);
		System.out.println("Mutation rate: " + DNA.MUTATION_RATE);
		System.out.println("Average visible mutations per generation: " + averageVisibleMutationsPerGeneration);
	}

	private static double getAverage(int[] values) {
		int sum = 0;
		for (int i = 0; i < values.length; i++)
			sum += values[i];
		return ((double)sum) / values.length;
	}

	// TODO: currently doesn't work - I removed the verbose equals() code from Organs
	private static int getNumberOfDifferences(Set<Object> set1, Set<Object> set2) {
		Set<Object> result = new HashSet<>();
		result.addAll(set1);
		result.removeAll(set2);
		return result.size();
	}

	private static Set<Object> toSetOfParts(Swimmer swimmer) {
		Set<Object> result = new HashSet<Object>();
		result.addAll(getOrgans(swimmer));
		return result;
	}

	public static List<Organ> getOrgans(Swimmer swimmer) {
		List<Organ> result = new LinkedList<>();
		result.add(swimmer.getHead());
		addChildrenDepthFirst(result, swimmer.getHead());
		return result;
	}

	private static void addChildrenDepthFirst(List<Organ> result, Organ organ) {
		for (Organ child : organ.getChildren()) {
			result.add(child);
			addChildrenDepthFirst(result, child);
		}
	}

	private static Swimmer nextGeneration(DNA genes) {
		genes.mutate();
		return new Embryo(genes).develop();
	}
}
