package org.nusco.narjillos.genomics;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public abstract class DNALogTest {

	private DNALog dnaLog;

	public DNALogTest() {
		super();
	}

	@BeforeEach
	public void createDababaseHistory() {
		dnaLog = createNewInstance();
	}

	@AfterEach
	public void deleteTestDatabase() {
		dnaLog.delete();
	}

	@Test
	public void savesAndLoadsDna() {
		var dna = new DNA(42, "{1_2_3}", 41);
		dnaLog.save(dna);

		var retrieved = dnaLog.getDna(42);

		assertThat(retrieved).isNotNull();
		assertThat(dna.getId()).isEqualTo(retrieved.getId());
		assertThat(dna.toString()).isEqualTo(retrieved.toString());
		assertThat(dna.getParentId()).isEqualTo(retrieved.getParentId());
	}

	@Test
	public void returnsNullIfTheDnaIsNotInTheLog() {
		assertThat(dnaLog.getDna(42)).isNull();
	}

	@Test
	public void silentlySkipsWritingIfADnaIsAlreadyInTheLog() {
		var dna = new DNA(42, "{1_2_3}", 41);
		dnaLog.save(dna);
		dnaLog.save(dna);

		assertThat(dnaLog.getAllDna()).hasSize(1);
	}

	@Test
	public void returnsAllDnaSortedById() {
		dnaLog.save(new DNA(43, "{1_2_3}", 0));
		dnaLog.save(new DNA(42, "{1_2_3}", 42));

		List<DNA> genePool = dnaLog.getAllDna();

		assertThat(genePool).hasSize(2);
		assertThat(genePool.get(0)).isEqualTo(dnaLog.getDna(42));
		assertThat(genePool.get(1)).isEqualTo(dnaLog.getDna(43));
	}

	@Test
	public void returnsLiveDnaSortedById() {
		var dna1 = new DNA(42, "{1}", 41);
		var dna2 = new DNA(43, "{2}", 41);
		var dna3 = new DNA(41, "{3}", 41);
		dnaLog.save(dna1);
		dnaLog.save(dna2);
		dnaLog.save(dna3);

		dnaLog.markAsDead(42);

		assertThat(dnaLog.getLiveDna().get(0)).isEqualTo(dna3);
		assertThat(dnaLog.getLiveDna().contains(dna1)).isFalse();
		assertThat(dnaLog.getLiveDna().get(1)).isEqualTo(dna2);
	}

	protected abstract DNALog createNewInstance();
}
