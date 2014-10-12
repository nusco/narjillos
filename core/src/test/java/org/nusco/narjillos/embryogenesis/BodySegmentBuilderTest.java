package org.nusco.narjillos.embryogenesis;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.narjillos.creature.body.BodySegment;
import org.nusco.narjillos.creature.body.Head;
import org.nusco.narjillos.creature.body.Organ;
import org.nusco.narjillos.embryogenesis.BodySegmentBuilder;
import org.nusco.narjillos.genomics.Chromosome;
import org.nusco.narjillos.shared.utilities.ColorByte;

public class BodySegmentBuilderTest extends ConcreteOrganBuilderTest {

	@Override
	protected BodySegmentBuilder createConcreteOrganBuilder(Chromosome chromosome) {
		return new BodySegmentBuilder(chromosome);
	}
	
	@Test
	public void decodesAnAngleToTheParentBetweenMinus70And70() {
		assertEquals(-70, new BodySegmentBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0)).getAngleToParent(1));
		assertEquals(-69, new BodySegmentBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 1)).getAngleToParent(1));
		assertEquals(-68, new BodySegmentBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 2)).getAngleToParent(1));
		assertEquals(0, new BodySegmentBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 128)).getAngleToParent(1));
		assertEquals(1, new BodySegmentBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 129)).getAngleToParent(1));
		assertEquals(70, new BodySegmentBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 255)).getAngleToParent(1));
	}

	@Test
	public void mirrorsTheAngleToTheParentIfTheMirrorSignIsNegative() {
		assertEquals(70, new BodySegmentBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0)).getAngleToParent(-1));
		assertEquals(69, new BodySegmentBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 1)).getAngleToParent(-1));
		assertEquals(68, new BodySegmentBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 2)).getAngleToParent(-1));
		assertEquals(0, new BodySegmentBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 128)).getAngleToParent(-1));
		assertEquals(-1, new BodySegmentBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 129)).getAngleToParent(-1));
		assertEquals(-70, new BodySegmentBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 255)).getAngleToParent(-1));
	}

	@Test
	public void decodesAnAmplitudeBetween1And100() {
		assertEquals(1, createConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 1)).getAmplitude());
		assertEquals(2, createConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 5)).getAmplitude());
		assertEquals(80, createConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 255)).getAmplitude());
	}

	@Test
	public void decodesADelayBetween0And30() {
		assertEquals(0, createConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0)).getDelay());
		assertEquals(0, createConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 8)).getDelay());
		assertEquals(1, createConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 9)).getDelay());
		assertEquals(30, createConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 255)).getDelay());
	}

	@Test
	public void decodesASkewingBetweenMinus90And90() {
		assertEquals(-90, createConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 0)).getSkewing());
		assertEquals(-45, createConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 63)).getSkewing());
		assertEquals(0, createConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 127)).getSkewing());
		assertEquals(90, createConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 255)).getSkewing());
	}

	@Test
	public void buildsABodySegment() {
		int controlFlowGene = 0b00000000;
		int controlLoopGene = 0b00000000;
		int lengthGene = 40;
		int thicknessGene = 255;
		int delayGene = 90;
		int amplitudeGene = 107;
		int angleToParentGene = 81;
		int skewingGene = 150;
		int hueGene = 50;
		
		Chromosome chromosome = new Chromosome(controlFlowGene, controlLoopGene, lengthGene, thicknessGene, delayGene, amplitudeGene, angleToParentGene, skewingGene, hueGene);
		BodySegmentBuilder builder = createConcreteOrganBuilder(chromosome);
		Organ head = new Head(10, 10, new ColorByte(40), 10, 0.5);
		BodySegment bodySegment = (BodySegment) builder.buildOrgan(head, 1);
		
		assertEquals(40, bodySegment.getLength(), 0);
		assertEquals(50, bodySegment.getThickness(), 0);
		assertEquals(10, bodySegment.getDelay(), 0);
		assertEquals(34, bodySegment.getAmplitude(), 0);
		assertEquals(-25, bodySegment.getAbsoluteAngle(), 0);
		assertEquals(16, bodySegment.getSkewing(), 0);
		assertEquals(new ColorByte(32), bodySegment.getColor());
	}
}
