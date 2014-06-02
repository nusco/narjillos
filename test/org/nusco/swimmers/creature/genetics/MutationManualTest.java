package org.nusco.swimmers.creature.genetics;

import java.util.HashSet;
import java.util.Set;

import org.nusco.swimmers.creature.Swimmer;
import org.nusco.swimmers.creature.body.Organ;
import org.nusco.swimmers.creature.genetics.DNA;
import org.nusco.swimmers.creature.genetics.Embryo;

public class MutationManualTest {

	public static int[] visibleMutationsPerGeneration = new int[1000];
	
	public static void main(String[] args) {
		DNA genes = DNA.ancestor();
		Swimmer swimmer = new Embryo(genes).develop();

		for (int i = 0; i < visibleMutationsPerGeneration.length; i++) {
			Set<Organ> fatherParts = toSetOfParts(swimmer);
			swimmer = nextGeneration(genes);
			Set<Organ> sonParts = toSetOfParts(swimmer);
			visibleMutationsPerGeneration[i] = getNumberOfDifferences(fatherParts, sonParts);
		}
		
		double averageVisibleMutationsPerGeneration = getAverage(visibleMutationsPerGeneration);

		System.out.println("Generations: " + visibleMutationsPerGeneration.length);
		System.out.println("Mutation rate: " + DNA.MUTATION_RATE);
		System.out.println("Average visible mutations per generation: " + averageVisibleMutationsPerGeneration);

		for (int i = 0; i < visibleMutationsPerGeneration.length; i++) {
			System.out.print(" " +visibleMutationsPerGeneration[i]);
			
		}
	}

	private static double getAverage(int[] values) {
		int sum = 0;
		for (int i = 0; i < values.length; i++)
			sum += values[i];
		return ((double)sum) / values.length;
	}

	private static int getNumberOfDifferences(Set<Organ> set1, Set<Organ> set2) {
		int result = 0;
		for (Organ organ : set1)
			if(!set2.contains(organ))
				result++;
		return result;
	}

	private static Set<Organ> toSetOfParts(Swimmer swimmer) {
		Set<Organ> result = new HashSet<Organ>();
		result.addAll(swimmer.getParts());
		return result;
	}

	private static Swimmer nextGeneration(DNA genes) {
		genes.mutate();
		return new Embryo(genes).develop();
	}
}
