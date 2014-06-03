package org.nusco.swimmers.creature.genetics;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.swimmers.creature.body.Organ;
import org.nusco.swimmers.creature.body.Side;
import org.nusco.swimmers.creature.genetics.OrganBuilder;

public class OrganBuilderTest {

	@Test
	public void createsAHead() {
		int controlGene = 0b00000000;
		int lengthGene = 50;
		int thicknessGene = 60;
		int ignoredGene = 0;
		int rgbGene = 40;
		
		OrganBuilder builder = new OrganBuilder(new int[] {controlGene, lengthGene, thicknessGene, ignoredGene, rgbGene});
		Organ head = builder.buildHead();

		assertEquals(lengthGene * OrganBuilder.PART_LENGTH_MULTIPLIER, head.getLength(), 0);
		assertEquals(thicknessGene * OrganBuilder.PART_THICKNESS_MULTIPLIER, head.getThickness(), 0);
		assertEquals(rgbGene, head.getRGB(), 0);
	}

	@Test
	public void createsAnOrgan() {
		int controlGene = 0b00000000;
		int lengthGene = 50;
		int thicknessGene = 60;
		int relativeAngleGene = 65;
		int rgbGene = 40;
		
		OrganBuilder builder = new OrganBuilder(new int[] {controlGene, lengthGene, thicknessGene, relativeAngleGene, rgbGene});
		Organ head = builder.buildHead();
		Organ organ = builder.buildSegment(head, Side.RIGHT);
		
		assertEquals(lengthGene * OrganBuilder.PART_LENGTH_MULTIPLIER, organ.getLength(), 0);
		assertEquals(thicknessGene * OrganBuilder.PART_THICKNESS_MULTIPLIER, organ.getThickness(), 0);
		assertEquals(relativeAngleGene, organ.getRelativeAngle(), 0);
		assertEquals(rgbGene, organ.getRGB(), 0);
	}
	
	@Test
	public void clipsTheMaximumRelativeAngleOfTheOrgan() {
		int relativeAngleGene = 200;
		
		Organ organ = buildSegment(relativeAngleGene, Side.RIGHT);
		
		assertEquals(relativeAngleGene % OrganBuilder.PART_MAX_RELATIVE_ANGLE, organ.getRelativeAngle(), 0);
	}
	
	@Test
	public void generateAMirroredOrgan() {
		int relativeAngleGene = 20;
		
		Organ organ = buildSegment(relativeAngleGene, Side.LEFT);
		
		assertEquals(-20, organ.getRelativeAngle(), 0);
	}

	private Organ buildSegment(int relativeAngleGene, Side side) {
		OrganBuilder builder = new OrganBuilder(new int[] {0, 50, 60, relativeAngleGene, 10});
		Organ head = builder.buildHead();
		return builder.buildSegment(head, side);
	}
}
