package org.nusco.narjillos.creature.embryogenesis;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.narjillos.core.chemistry.Element;
import org.nusco.narjillos.creature.body.Fiber;
import org.nusco.narjillos.creature.body.Head;
import org.nusco.narjillos.genomics.Chromosome;

public class HeadBuilderTest extends ConcreteOrganBuilderTest {

	@Override
	protected HeadBuilder getConcreteOrganBuilder(Chromosome chromosome) {
		return new HeadBuilder(chromosome);
	}

	@Test
	public void decodesFiberComponents() {
		Chromosome chromosome = new Chromosome(0, 0, 0, 0, 10, 20, 30);
		assertEquals(10, getConcreteOrganBuilder(chromosome).getRed());
		assertEquals(20, getConcreteOrganBuilder(chromosome).getGreen());
		assertEquals(30, getConcreteOrganBuilder(chromosome).getBlue());
	}

	@Test
	public void decodesAMetabolicRateBetween0And3() {
		assertEquals(0, getConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 0)).getMetabolicRate(), 0.01);
		assertEquals(0.74, getConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 63)).getMetabolicRate(), 0.01);
		assertEquals(1, getConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 85)).getMetabolicRate(), 0.01);
		assertEquals(3, getConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 255)).getMetabolicRate(), 0.01);
	}

	@Test
	public void decodesAWaveBeatRatioBetween0dot2And5() {
		assertEquals(0.2, getConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 0, 0)).getWaveBeatRatio(), 0.01);
		assertEquals(2.59, getConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 0, 127)).getWaveBeatRatio(), 0.01);
		assertEquals(5.0, getConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 0, 255)).getWaveBeatRatio(), 0.01);
	}

	@Test
	public void decodesAByproduct() {
		assertEquals(Element.OXYGEN, getConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 0, 0, 0)).getByproduct());
		assertEquals(Element.HYDROGEN, getConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 0, 0, 1)).getByproduct());
		assertEquals(Element.NITROGEN, getConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 0, 0, 2)).getByproduct());
		assertEquals(Element.OXYGEN, getConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 0, 0, 3)).getByproduct());
	}

	@Test
	public void decodesAnEnergyToChildrenBetween10000And35500() {
		assertEquals(10000, getConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)).getEnergyToChildren(), 0.0);
		assertEquals(22600, getConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 126)).getEnergyToChildren(), 0.01);
		assertEquals(35500, getConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 255)).getEnergyToChildren(), 0.0);
	}

	@Test
	public void decodesAnEggVelocityBetween0and255() {
		assertEquals(0, getConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)).getEggVelocity(), 0.01);
		assertEquals(10, getConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10)).getEggVelocity(), 0.01);
		assertEquals(255, getConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 255)).getEggVelocity(), 0.01);
	}

	@Test
	public void decodesAnEggLayingIntervalBetween0and25500() {
		assertEquals(0, getConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)).getEggInterval(), 0.01);
		assertEquals(1000, getConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10)).getEggInterval(), 0.01);
		assertEquals(25500, getConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 255)).getEggInterval(), 0.01);
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

		Chromosome chromosome = new Chromosome(controlFlowGene, controlLoopGene, lengthGene, thicknessGene, redGene, greenGene, blueGene,
			metabolicRateGene, waveBeatRatioGene, byproductGene, energyToChildrenGene, eggVelocityGene, eggIntervalGene);
		HeadBuilder builder = getConcreteOrganBuilder(chromosome);
		Head head = (Head) builder.buildOrgan(null, 0);

		head.growToAdultFormWithChildren();

		assertEquals(30, head.getLength(), 0);
		assertEquals(25, head.getThickness(), 0.01);
		assertEquals(new Fiber(50, 51, 52), head.getFiber());
		assertEquals(1.764, head.getMetabolicRate(), 0.01);
		assertEquals(2.64, head.getWaveBeatRatio(), 0.01);
		assertEquals(Element.NITROGEN, head.getByproduct());
		assertEquals(22600, head.getEnergyToChildren(), 0.0);
		assertEquals(81, head.getEggVelocity());
		assertEquals(1000, head.getEggInterval());
	}
}
