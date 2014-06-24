package org.nusco.swimmers.creature.genetics;

import java.util.HashSet;
import java.util.Set;

import org.nusco.swimmers.creature.Swimmer;

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

	private static int getNumberOfDifferences(Set<Object> set1, Set<Object> set2) {
		Set<Object> result = new HashSet<>();
		result.addAll(set1);
		result.removeAll(set2);
		return result.size();
	}

	private static Set<Object> toSetOfParts(Swimmer swimmer) {
		Set<Object> result = new HashSet<Object>();
		result.addAll(swimmer.getParts());
		return result;
	}

	private static Swimmer nextGeneration(DNA genes) {
		genes.mutate();
		return new Embryo(genes).develop();
	}
}
