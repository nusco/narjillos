package org.nusco.narjillos.creature.body.embryology;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.narjillos.creature.body.BodyPart;
import org.nusco.narjillos.creature.body.Head;
import org.nusco.narjillos.creature.body.Organ;
import org.nusco.narjillos.creature.genetics.Chromosome;
import org.nusco.narjillos.shared.utilities.ColorByte;

public class BodySegmentBuilderTest extends OrganBuilderTest {

	@Override
	protected BodySegmentBuilder createConcreteOrganBuilder(Chromosome chromosome) {
		return new BodySegmentBuilder(chromosome);
	}
	
	@Test
	public void decodesAnAngleToTheParentBetweenMinus70And70() {
		assertEquals(-70, new BodySegmentBuilder(new Chromosome(0, 0, 0, 0, 0, 0)).getAngleToParent(1));
		assertEquals(-69, new BodySegmentBuilder(new Chromosome(0, 0, 0, 0, 0, 1)).getAngleToParent(1));
		assertEquals(-68, new BodySegmentBuilder(new Chromosome(0, 0, 0, 0, 0, 2)).getAngleToParent(1));
		assertEquals(0, new BodySegmentBuilder(new Chromosome(0, 0, 0, 0, 0, 128)).getAngleToParent(1));
		assertEquals(1, new BodySegmentBuilder(new Chromosome(0, 0, 0, 0, 0, 129)).getAngleToParent(1));
		assertEquals(70, new BodySegmentBuilder(new Chromosome(0, 0, 0, 0, 0, 255)).getAngleToParent(1));
	}

	@Test
	public void mirrorsTheAngleToTheParentIfTheMirrorSignIsNegative() {
		assertEquals(70, new BodySegmentBuilder(new Chromosome(0, 0, 0, 0, 0, 0)).getAngleToParent(-1));
		assertEquals(69, new BodySegmentBuilder(new Chromosome(0, 0, 0, 0, 0, 1)).getAngleToParent(-1));
		assertEquals(68, new BodySegmentBuilder(new Chromosome(0, 0, 0, 0, 0, 2)).getAngleToParent(-1));
		assertEquals(0, new BodySegmentBuilder(new Chromosome(0, 0, 0, 0, 0, 128)).getAngleToParent(-1));
		assertEquals(-1, new BodySegmentBuilder(new Chromosome(0, 0, 0, 0, 0, 129)).getAngleToParent(-1));
		assertEquals(-70, new BodySegmentBuilder(new Chromosome(0, 0, 0, 0, 0, 255)).getAngleToParent(-1));
	}

	@Test
	public void buildsABodySegment() {
		int controlGene = 0b00000000;
		int lengthGene = 40;
		int thicknessGene = 255;
		int delayGene = 90;
		int colorGene = 40;
		int angleToParentGene = 81;

		Chromosome chromosome = new Chromosome(controlGene, lengthGene, thicknessGene, delayGene, colorGene, angleToParentGene);
		BodySegmentBuilder builder = createConcreteOrganBuilder(chromosome);
		Organ head = new Head(10, 10, new ColorByte(40), 10);
		BodyPart bodyPart = builder.build(head, 1);
		
		assertEquals(40, bodyPart.getLength(), 0);
		assertEquals(50, bodyPart.getThickness(), 0);
		assertEquals(new ColorByte(40), bodyPart.getColor());
		assertEquals(-25, bodyPart.getAbsoluteAngle(), 0);
	}
}
