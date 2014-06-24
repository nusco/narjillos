package org.nusco.swimmers.creature.genetics;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.swimmers.creature.body.Organ;

public class OrganBuilderTest {

	@Test
	public void createsAHead() {
		int controlGene = 0b00000000;
		int lengthGene = 50;
		int thicknessGene = 60;
		int ignoredGene = 0;
		int colorGene = 40;
		
		OrganBuilder builder = new OrganBuilder(new int[] {controlGene, lengthGene, thicknessGene, ignoredGene, colorGene});
		Organ head = builder.buildHead();

		assertEquals(lengthGene * OrganBuilder.PART_LENGTH_MULTIPLIER, head.getLength(), 0);
		assertEquals(thicknessGene * OrganBuilder.PART_THICKNESS_MULTIPLIER, head.getThickness(), 0);
		assertEquals(colorGene, head.getColor(), 0);
	}

	@Test
	public void createsAnOrgan() {
		int controlGene = 0b00000000;
		int lengthGene = 50;
		int thicknessGene = 60;
		int relativeAngleGene = 80;
		int colorGene = 40;
		
		OrganBuilder builder = new OrganBuilder(new int[] {controlGene, lengthGene, thicknessGene, relativeAngleGene, colorGene});
		Organ head = builder.buildHead();
		Organ organ = builder.buildSegment(head, 1);
		
		assertEquals(lengthGene * OrganBuilder.PART_LENGTH_MULTIPLIER, organ.getLength(), 0);
		assertEquals(thicknessGene * OrganBuilder.PART_THICKNESS_MULTIPLIER, organ.getThickness(), 0);
		assertEquals(60, organ.getRelativeAngle(), 0);
		assertEquals(colorGene, organ.getColor(), 0);
	}
	
	@Test
	public void generateAMirroredOrgan() {
		int relativeAngleGene = 80;
		
		Organ organ = buildSegment(relativeAngleGene, -1);
		
		assertEquals(-60, organ.getRelativeAngle(), 0);
	}

	private Organ buildSegment(int relativeAngleGene, int angleSign) {
		OrganBuilder builder = new OrganBuilder(new int[] {0, 50, 60, relativeAngleGene, 10});
		Organ head = builder.buildHead();
		return builder.buildSegment(head, angleSign);
	}
}
