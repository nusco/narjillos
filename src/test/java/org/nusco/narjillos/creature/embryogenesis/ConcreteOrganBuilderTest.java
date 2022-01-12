package org.nusco.narjillos.creature.embryogenesis;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.nusco.narjillos.creature.embryogenesis.bodyplan.BodyPlanInstruction;
import org.nusco.narjillos.genomics.Chromosome;

public abstract class ConcreteOrganBuilderTest {

	protected abstract ConcreteOrganBuilder getConcreteOrganBuilder(Chromosome chromosome);

	@Test
	public void decodesABodyPlanInstruction() {
		assertThat(getConcreteOrganBuilder(new Chromosome(0)).getBodyPlanInstruction()).isEqualTo(BodyPlanInstruction.CONTINUE);
		assertThat(getConcreteOrganBuilder(new Chromosome(1)).getBodyPlanInstruction()).isEqualTo(BodyPlanInstruction.SKIP);
		assertThat(getConcreteOrganBuilder(new Chromosome(2)).getBodyPlanInstruction()).isEqualTo(BodyPlanInstruction.BRANCH);
		assertThat(getConcreteOrganBuilder(new Chromosome(3)).getBodyPlanInstruction()).isEqualTo(BodyPlanInstruction.BRANCH);
		assertThat(getConcreteOrganBuilder(new Chromosome(4)).getBodyPlanInstruction()).isEqualTo(BodyPlanInstruction.MIRROR);
		assertThat(getConcreteOrganBuilder(new Chromosome(5)).getBodyPlanInstruction()).isEqualTo(BodyPlanInstruction.STOP);
		assertThat(getConcreteOrganBuilder(new Chromosome(6)).getBodyPlanInstruction()).isEqualTo(BodyPlanInstruction.STOP);
		// roll over to 0
		assertThat(getConcreteOrganBuilder(new Chromosome(7)).getBodyPlanInstruction()).isEqualTo(BodyPlanInstruction.CONTINUE);
	}

	@Test
	public void decodesALengthBetween1And255() {
		assertThat(getConcreteOrganBuilder(new Chromosome(0, 0, 10)).getLength()).isEqualTo(0);
		assertThat(getConcreteOrganBuilder(new Chromosome(0, 0, 30)).getLength()).isEqualTo(30);
		assertThat(getConcreteOrganBuilder(new Chromosome(0, 0, 42)).getLength()).isEqualTo(42);
		assertThat(getConcreteOrganBuilder(new Chromosome(0, 0, 255)).getLength()).isEqualTo(255);
	}

	@Test
	public void veryShortLengthsAtrophyTo0() {
		assertThat(getConcreteOrganBuilder(new Chromosome(0, 29)).getLength()).isEqualTo(0);
	}

	@Test
	public void decodesAThicknessBetween1And50() {
		assertThat(getConcreteOrganBuilder(new Chromosome(0, 0, 0, 0)).getThickness()).isEqualTo(1);
		assertThat(getConcreteOrganBuilder(new Chromosome(0, 0, 0, 1)).getThickness()).isEqualTo(1);
		assertThat(getConcreteOrganBuilder(new Chromosome(0, 0, 0, 6)).getThickness()).isEqualTo(2);
		assertThat(getConcreteOrganBuilder(new Chromosome(0, 0, 0, 50)).getThickness()).isEqualTo(10);
		assertThat(getConcreteOrganBuilder(new Chromosome(0, 0, 0, 255)).getThickness()).isEqualTo(50);
	}
}
