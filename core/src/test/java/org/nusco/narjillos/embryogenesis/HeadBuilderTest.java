package org.nusco.narjillos.embryogenesis;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.narjillos.creature.body.Head;
import org.nusco.narjillos.embryogenesis.HeadBuilder;
import org.nusco.narjillos.genomics.Chromosome;
import org.nusco.narjillos.shared.utilities.ColorByte;

public class HeadBuilderTest extends OrganBuilderTest {

	@Override
	protected HeadBuilder createConcreteOrganBuilder(Chromosome chromosome) {
		return new HeadBuilder(chromosome);
	}

	@Test
	public void decodesAMetabolicRateBetween0And3() {
		assertEquals(0, new HeadBuilder(new Chromosome(0, 0, 0, 0, 0)).getMetabolicRate(), 0.01);
		assertEquals(0.74, new HeadBuilder(new Chromosome(0, 0, 0, 63, 0)).getMetabolicRate(), 0.01);
		assertEquals(1, new HeadBuilder(new Chromosome(0, 0, 0, 85, 0)).getMetabolicRate(), 0.01);
		assertEquals(3, new HeadBuilder(new Chromosome(0, 0, 0, 255, 0)).getMetabolicRate(), 0.01);
	}

	@Test
	public void decodesAPercentEnergyToChildrenBetween0and1() {
		assertEquals(0, new HeadBuilder(new Chromosome(0, 0, 0, 0, 0)).getPercentEnergyToChildren(), 0.01);
		assertEquals(0.5, new HeadBuilder(new Chromosome(0, 0, 0, 0, 126)).getPercentEnergyToChildren(), 0.01);
		assertEquals(1, new HeadBuilder(new Chromosome(0, 0, 0, 0, 255)).getPercentEnergyToChildren(), 0.01);
	}

	@Test
	public void buildsAHead() {
		int controlGene = 0b00000000;
		int lengthGene = 40;
		int thicknessGene = 255;
		int metabolicRateGene = 150;
		int colorGene = 40;
		int angleToParentGene = 81;

		Chromosome chromosome = new Chromosome(controlGene, lengthGene, thicknessGene, metabolicRateGene, colorGene, angleToParentGene);
		HeadBuilder builder = createConcreteOrganBuilder(chromosome);
		Head head = builder.build();
		
		assertEquals(40, head.getLength(), 0);
		assertEquals(50, head.getThickness(), 0);
		assertEquals(new ColorByte(40), head.getColor());
		assertEquals(1.764, head.getMetabolicRate(), 0.01);
	}
}
