package org.nusco.narjillos.creature.embryogenesis;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import org.junit.jupiter.api.Test;
import org.nusco.narjillos.core.chemistry.Element;
import org.nusco.narjillos.creature.body.Fiber;
import org.nusco.narjillos.creature.body.Head;
import org.nusco.narjillos.genomics.Chromosome;

public class HeadBuilderTest extends ConcreteOrganBuilderTest {

	private static final double PRECISION = 0.01;

	@Override
	protected HeadBuilder getConcreteOrganBuilder(Chromosome chromosome) {
		return new HeadBuilder(chromosome);
	}

	@Test
	public void decodesFiberComponents() {
		var chromosome = new Chromosome(0, 0, 0, 0, 10, 20, 30);
		assertThat(getConcreteOrganBuilder(chromosome).getRed()).isEqualTo(10);
		assertThat(getConcreteOrganBuilder(chromosome).getGreen()).isEqualTo(20);
		assertThat(getConcreteOrganBuilder(chromosome).getBlue()).isEqualTo(30);
	}

	@Test
	public void decodesAMetabolicRateBetween0And3() {
		assertThat(getConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 0)).getMetabolicRate())
			.isEqualTo(0.0, within(PRECISION));
		assertThat(getConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 63)).getMetabolicRate())
			.isEqualTo(0.74, within(PRECISION));
		assertThat(getConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 85)).getMetabolicRate())
			.isEqualTo(1.0, within(PRECISION));
		assertThat(getConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 255)).getMetabolicRate())
			.isEqualTo(3.0, within(PRECISION));
	}

	@Test
	public void decodesAWaveBeatRatioBetween0dot2And5() {
		assertThat(getConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 0, 0)).getWaveBeatRatio())
			.isEqualTo(0.2, within(PRECISION));
		assertThat(getConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 0, 127)).getWaveBeatRatio())
			.isEqualTo(2.59, within(PRECISION));
		assertThat(getConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 0, 255)).getWaveBeatRatio())
			.isEqualTo(5.0, within(PRECISION));
	}

	@Test
	public void decodesAByproduct() {
		assertThat(getConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 0, 0, 0)).getByproduct()).isEqualTo(Element.OXYGEN);
		assertThat(getConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 0, 0, 1)).getByproduct()).isEqualTo(Element.HYDROGEN);
		assertThat(getConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 0, 0, 2)).getByproduct()).isEqualTo(Element.NITROGEN);
		assertThat(getConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 0, 0, 3)).getByproduct()).isEqualTo(Element.OXYGEN);
	}

	@Test
	public void decodesAnEnergyToChildrenBetween10000And35500() {
		assertThat(getConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)).getEnergyToChildren())
			.isEqualTo(10000.0);
		assertThat(getConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 126)).getEnergyToChildren())
			.isEqualTo(22600.0, within(PRECISION));
		assertThat(getConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 255)).getEnergyToChildren())
			.isEqualTo(35500.0);
	}

	@Test
	public void decodesAnEggVelocityBetween0and255() {
		assertThat(getConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)).getEggVelocity()).isEqualTo(0);
		assertThat(getConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10)).getEggVelocity()).isEqualTo(10);
		assertThat(getConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 255)).getEggVelocity()).isEqualTo(255);
	}

	@Test
	public void decodesAnEggLayingIntervalBetween0and25500() {
		assertThat(getConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)).getEggInterval()).isEqualTo(0);
		assertThat(getConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10)).getEggInterval()).isEqualTo(1000);
		assertThat(getConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 255)).getEggInterval()).isEqualTo(25500);
	}

	@Test
	public void buildsAHead() {
		int controlFlowGene = 0b00000000;
		int controlLoopGene = 0b00000000;
		int lengthGene = 30;
		int thicknessGene = 126;
		int redGene = 50;
		int greenGene = 51;
		int blueGene = 52;
		int metabolicRateGene = 150;
		int waveBeatRatioGene = 130;
		int byproductGene = 2;
		int energyToChildrenGene = 126;
		int eggVelocityGene = 81;
		int eggIntervalGene = 10;

		var chromosome = new Chromosome(controlFlowGene, controlLoopGene, lengthGene, thicknessGene, redGene, greenGene, blueGene,
			metabolicRateGene, waveBeatRatioGene, byproductGene, energyToChildrenGene, eggVelocityGene, eggIntervalGene);
		HeadBuilder builder = getConcreteOrganBuilder(chromosome);
		var head = (Head) builder.buildOrgan(null, 0);

		head.growToAdultFormWithChildren();

		assertThat(head.getLength()).isEqualTo(30.0);
		assertThat(head.getThickness()).isEqualTo(25.0, within(PRECISION));
		assertThat(head.getFiber()).isEqualTo(new Fiber(50, 51, 52));
		assertThat(head.getMetabolicRate()).isEqualTo(1.764, within(PRECISION));
		assertThat(head.getWaveBeatRatio()).isEqualTo(2.64, within(PRECISION));
		assertThat(head.getByproduct()).isEqualTo(Element.NITROGEN);
		assertThat(head.getEnergyToChildren()).isEqualTo(22600.0);
		assertThat(head.getEggVelocity()).isEqualTo(81);
		assertThat(head.getEggInterval()).isEqualTo(1000);
	}
}
