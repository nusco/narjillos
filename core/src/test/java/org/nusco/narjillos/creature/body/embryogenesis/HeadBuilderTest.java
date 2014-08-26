package org.nusco.narjillos.creature.body.embryogenesis;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.narjillos.creature.body.Head;
import org.nusco.narjillos.creature.body.embryogenesis.HeadBuilder;
import org.nusco.narjillos.creature.genetics.Chromosome;
import org.nusco.narjillos.shared.utilities.ColorByte;

public class HeadBuilderTest extends OrganBuilderTest {

	@Override
	protected HeadBuilder createConcreteOrganBuilder(Chromosome chromosome) {
		return new HeadBuilder(chromosome);
	}

	@Test
	public void decodesAMetabolicRateBetween0And3() {
		assertEquals(0, new HeadBuilder(new Chromosome(0, 0, 0, 0)).getMetabolicRate(), 0.01);
		assertEquals(0.74, new HeadBuilder(new Chromosome(0, 0, 0, 63)).getMetabolicRate(), 0.01);
		assertEquals(1, new HeadBuilder(new Chromosome(0, 0, 0, 85)).getMetabolicRate(), 0.01);
		assertEquals(3, new HeadBuilder(new Chromosome(0, 0, 0, 255)).getMetabolicRate(), 0.01);
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
