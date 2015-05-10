package org.nusco.narjillos.creature.embryogenesis;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.narjillos.creature.body.Organ;
import org.nusco.narjillos.creature.embryogenesis.bodyplan.BodyPlanInstruction;
import org.nusco.narjillos.genomics.Chromosome;

public abstract class ConcreteOrganBuilderTest {

	protected abstract ConcreteOrganBuilder getConcreteOrganBuilder(Chromosome chromosome);

	@Test
	public void decodesABodyPlanInstruction() {
		assertEquals(BodyPlanInstruction.CONTINUE, getConcreteOrganBuilder(new Chromosome(0)).getBodyPlanInstruction());
		assertEquals(BodyPlanInstruction.SKIP, getConcreteOrganBuilder(new Chromosome(1)).getBodyPlanInstruction());
		assertEquals(BodyPlanInstruction.BRANCH, getConcreteOrganBuilder(new Chromosome(2)).getBodyPlanInstruction());
		assertEquals(BodyPlanInstruction.BRANCH, getConcreteOrganBuilder(new Chromosome(3)).getBodyPlanInstruction());
		assertEquals(BodyPlanInstruction.MIRROR, getConcreteOrganBuilder(new Chromosome(4)).getBodyPlanInstruction());
		assertEquals(BodyPlanInstruction.STOP, getConcreteOrganBuilder(new Chromosome(5)).getBodyPlanInstruction());
		assertEquals(BodyPlanInstruction.STOP, getConcreteOrganBuilder(new Chromosome(6)).getBodyPlanInstruction());
		// roll over to 0
		assertEquals(BodyPlanInstruction.CONTINUE, getConcreteOrganBuilder(new Chromosome(7)).getBodyPlanInstruction());
	}

	@Test
	public void decodesALengthBetween1And255() {
		assertEquals(0, getConcreteOrganBuilder(new Chromosome(0, 0, 10)).getLength());
		assertEquals(30, getConcreteOrganBuilder(new Chromosome(0, 0, 30)).getLength());
		assertEquals(42, getConcreteOrganBuilder(new Chromosome(0, 0, 42)).getLength());
		assertEquals(255, getConcreteOrganBuilder(new Chromosome(0, 0, 255)).getLength());
	}

	@Test
	public void veryShortLengthsAtrophyTo0() {
		assertEquals(0, getConcreteOrganBuilder(new Chromosome(0, 29)).getLength());
	}

	@Test
	public void decodesAThicknessBetween1And50() {
		assertEquals(1, getConcreteOrganBuilder(new Chromosome(0, 0, 0, 0)).getThickness());
		assertEquals(1, getConcreteOrganBuilder(new Chromosome(0, 0, 0, 1)).getThickness());
		assertEquals(2, getConcreteOrganBuilder(new Chromosome(0, 0, 0, 6)).getThickness());
		assertEquals(10, getConcreteOrganBuilder(new Chromosome(0, 0, 0, 50)).getThickness());
		assertEquals(50, getConcreteOrganBuilder(new Chromosome(0, 0, 0, 255)).getThickness());
	}

	protected void fullyGrow(Organ bodyPart) {
		bodyPart.growBy(100_000);
		bodyPart.updateGeometry();
	}
}
