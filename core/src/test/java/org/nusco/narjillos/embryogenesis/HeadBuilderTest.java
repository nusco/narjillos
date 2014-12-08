package org.nusco.narjillos.embryogenesis;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.narjillos.creature.body.Head;
import org.nusco.narjillos.embryogenesis.HeadBuilder;
import org.nusco.narjillos.genomics.Chromosome;
import org.nusco.narjillos.shared.utilities.ColorByte;

public class HeadBuilderTest extends ConcreteOrganBuilderTest {

	@Override
	protected HeadBuilder createConcreteOrganBuilder(Chromosome chromosome) {
		return new HeadBuilder(chromosome);
	}

	@Test
	public void decodesAMetabolicRateBetween0And3() {
		assertEquals(0, new HeadBuilder(new Chromosome(0, 0, 0, 0, 0)).getMetabolicRate(), 0.01);
		assertEquals(0.74, new HeadBuilder(new Chromosome(0, 0, 0, 0, 63)).getMetabolicRate(), 0.01);
		assertEquals(1, new HeadBuilder(new Chromosome(0, 0, 0, 0, 85)).getMetabolicRate(), 0.01);
		assertEquals(3, new HeadBuilder(new Chromosome(0, 0, 0, 0, 255)).getMetabolicRate(), 0.01);
	}

	@Test
	public void decodesAPercentEnergyToChildrenBetween0and1() {
		assertEquals(0, new HeadBuilder(new Chromosome(0, 0, 0, 0, 0, 0)).getPercentEnergyToChildren(), 0.01);
		assertEquals(0.5, new HeadBuilder(new Chromosome(0, 0, 0, 0, 0, 126)).getPercentEnergyToChildren(), 0.01);
		assertEquals(1, new HeadBuilder(new Chromosome(0, 0, 0, 0, 0, 255)).getPercentEnergyToChildren(), 0.01);
	}

	@Test
	public void buildsAHead() {
		int controlFlowGene = 0b00000000;
		int controlLoopGene = 0b00000000;
		int lengthGene = 30;
		int thicknessGene = 126;
		int metabolicRateGene = 150;
		int amplitudeGene = 70;
		int angleToParentGene = 81;
		int skewingGene = 90;
		int hueGene = 50;

		Chromosome chromosome = new Chromosome(controlFlowGene, controlLoopGene, lengthGene, thicknessGene, metabolicRateGene, amplitudeGene, angleToParentGene, skewingGene, hueGene);
		HeadBuilder builder = createConcreteOrganBuilder(chromosome);
		Head head = (Head)builder.buildOrgan(null, 0);
		
		grow(head);
		
		assertEquals(30, head.getLength(), 0);
		assertEquals(21, head.getThickness(), 0.01);
		assertEquals(1.764, head.getMetabolicRate(), 0.01);
		assertEquals(new ColorByte(50), head.getColor());
	}
}
