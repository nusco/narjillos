package org.nusco.narjillos.analysis;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.nusco.narjillos.core.utilities.NumGen;
import org.nusco.narjillos.genomics.DNA;
import org.nusco.narjillos.genomics.DNALog;
import org.nusco.narjillos.genomics.VolatileDNALog;

public class DNAAnalyzerTest {

	private final DNALog dnaLog = new VolatileDNALog();

	private final DNAAnalyzer dnaAnalyzer = new DNAAnalyzer(dnaLog);

	@Test
	public void getsMostSuccessfulDNA() {
		dnaLog.save(new DNA(1, "111_111_111_222_111_000_000_000_000_000_000_000_000_000"));
		dnaLog.save(new DNA(2, "111_111_111_111_111_000_000_000_000_000_000_000_000_000"));
		dnaLog.save(new DNA(3, "111_111_111_222_222_000_000_000_000_000_000_000_000_000"));
		dnaLog.save(new DNA(4, "111_111_222_111_222_000_000_000_000_000_000_000_000_000"));
		dnaLog.save(new DNA(5, "111_222_222_222_222_000_000_000_000_000_000_000_000_000"));

		DNA mostSuccessful = dnaAnalyzer.getMostSuccessfulDna();

		assertThat(mostSuccessful.getId()).isEqualTo(1);
	}

	@Test
	public void getsNullAsTheMostSuccesfulInAnEmptyPool() {
		assertThat(dnaAnalyzer.getMostSuccessfulDna()).isNull();
	}

	@Test
	public void getsAGermline() {
		var numGen = new NumGen(123);

		var parent1 = new DNA(1, "{0}");
		dnaLog.save(parent1);
		var child1_1 = parent1.mutate(2, numGen);
		dnaLog.save(child1_1);

		var parent2 = new DNA(3, "{0}");
		dnaLog.save(parent2);
		var child2_1 = parent2.mutate(4, numGen);
		dnaLog.save(child2_1);
		var child2_2 = parent2.mutate(5, numGen);
		dnaLog.save(child2_2);

		var child2_2_1 = child2_2.mutate(6, numGen);
		dnaLog.save(child2_2_1);

		List<DNA> ancestry = dnaAnalyzer.getGermline(child2_2_1);

		assertThat(ancestry).hasSize(3);
		assertThat(ancestry.get(0)).isEqualTo(parent2);
		assertThat(ancestry.get(1)).isEqualTo(child2_2);
		assertThat(ancestry.get(2)).isEqualTo(child2_2_1);
	}

	@Test
	public void getsAnAncestor() {
		var numGen = new NumGen(123);

		var parent = new DNA(1, "{0}");
		dnaLog.save(parent);
		var child1 = parent.mutate(2, numGen);
		dnaLog.save(child1);
		var child2 = child1.mutate(3, numGen);
		dnaLog.save(child2);

		assertThat(dnaAnalyzer.getGermline(child2).get(0)).isEqualTo(parent);
	}

	@Test
	public void countsLivingGermlines() {
		var numGen = new NumGen(123);

		var ancestorOfGermline1 = new DNA(10, "{0}");
		dnaLog.save(ancestorOfGermline1);
		dnaLog.save(ancestorOfGermline1.mutate(11, numGen));
		dnaLog.save(ancestorOfGermline1.mutate(12, numGen));

		var ancestorOfGermline2 = new DNA(20, "{0}");
		dnaLog.save(ancestorOfGermline2);

		var ancestorOfGermline3 = new DNA(30, "{0}");
		dnaLog.save(ancestorOfGermline3);
		dnaLog.save(ancestorOfGermline3.mutate(31, numGen));

		dnaLog.markAsDead(10);
		dnaLog.markAsDead(30);

		assertThat(dnaAnalyzer.getNumberOfLivingGermlines()).isEqualTo(3);

		dnaLog.markAsDead(20);
		assertThat(dnaAnalyzer.getNumberOfLivingGermlines()).isEqualTo(2);

		dnaLog.markAsDead(31);
		assertThat(dnaAnalyzer.getNumberOfLivingGermlines()).isEqualTo(1);
	}
}
