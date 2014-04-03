package org.nusco.swimmer.genetics;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.swimmer.body.Organ;

public class OrganBuilderTest {

	@Test
	public void createsAHead() {
		int controlGene = 0b00000000;
		int lengthGene = 10;
		int thicknessGene = 20;
		int ignoredGene = 30;
		int rgbGene = 40;
		
		OrganBuilder builder = new OrganBuilder(new int[] {controlGene, lengthGene, thicknessGene, ignoredGene, rgbGene});
		Organ head = builder.buildHead();

		assertEquals(10 * OrganBuilder.PART_LENGTH_MULTIPLIER, head.getLength(), 0);
		assertEquals(20 * OrganBuilder.PART_THICKNESS_MULTIPLIER, head.getThickness(), 0);
		assertEquals(40, head.getRGB(), 0);
	}

	@Test
	public void createsABodyPart() {
		int controlGene = 0b00000000;
		int lengthGene = 10;
		int thicknessGene = 20;
		int relativeAngleGene = 30;
		int rgbGene = 40;
		
		OrganBuilder builder = new OrganBuilder(new int[] {controlGene, lengthGene, thicknessGene, relativeAngleGene, rgbGene});
		Organ head = builder.buildHead();
		Organ bodyPart = builder.buildBodyPart(head, +1);
		
		assertEquals(lengthGene * OrganBuilder.PART_LENGTH_MULTIPLIER, bodyPart.getLength(), 0);
		assertEquals(thicknessGene * OrganBuilder.PART_THICKNESS_MULTIPLIER, bodyPart.getThickness(), 0);
		assertEquals(relativeAngleGene, bodyPart.getRelativeAngle(), 0);
		assertEquals(rgbGene, bodyPart.getRGB(), 0);
	}
	
	@Test
	public void clipsTheMaximumRelativeAngle() {
		int relativeAngleGene = 200;
		int angleSign = +1;
		
		Organ bodyPart = buildBodyPart(relativeAngleGene, angleSign);
		
		assertEquals(relativeAngleGene % OrganBuilder.PART_MAX_RELATIVE_ANGLE, bodyPart.getRelativeAngle(), 0);
	}
	
	@Test
	public void generateAMirroredBodyPart() {
		int relativeAngleGene = 20;
		int angleSign = -1;
		
		Organ bodyPart = buildBodyPart(relativeAngleGene, angleSign);
		
		assertEquals(-20, bodyPart.getRelativeAngle(), 0);
	}

	private Organ buildBodyPart(int relativeAngleGene, int angleSign) {
		OrganBuilder builder = new OrganBuilder(new int[] {0, 10, 10, relativeAngleGene, 10});
		Organ head = builder.buildHead();
		Organ bodyPart = builder.buildBodyPart(head, angleSign);
		return bodyPart;
	}
}
