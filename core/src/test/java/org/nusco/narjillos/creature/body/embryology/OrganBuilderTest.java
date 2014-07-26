package org.nusco.narjillos.creature.body.embryology;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.nusco.narjillos.creature.body.BodyPart;
import org.nusco.narjillos.creature.body.Organ;
import org.nusco.narjillos.shared.utilities.ColorByte;

public class OrganBuilderTest {

	@Test
	public void createsAHeadWithAnAttachedNeck() {
		int controlGene = 0b00000000;
		int lengthGene = 50;
		int thicknessGene = 60;
		int ignoredGene1 = 0;
		int ignoredGene2 = 0;
		int colorGene = 40;
		
		OrganBuilder builder = new OrganBuilder(new int[] {controlGene, lengthGene, thicknessGene, ignoredGene1, ignoredGene2, colorGene});
		BodyPart head = builder.buildHeadSystem();

		assertEquals(lengthGene * OrganBuilder.PART_LENGTH_MULTIPLIER, head.getLength(), 0);
		assertEquals(thicknessGene * OrganBuilder.PART_THICKNESS_MULTIPLIER, head.getThickness(), 0);
		assertEquals(new ColorByte(colorGene), head.getColor());
		assertNotNull(head.getChildren().get(0));
	}

	@Test
	public void createsAnOrgan() {
		int controlGene = 0b00000000;
		int lengthGene = 50;
		int thicknessGene = 60;
		int delayGene = 90;
		int angleToParentGene = 80;
		int colorGene = 40;
		
		OrganBuilder builder = new OrganBuilder(new int[] {controlGene, lengthGene, thicknessGene, delayGene, angleToParentGene, colorGene});
		BodyPart headSystem = builder.buildHeadSystem();
		Organ organ = builder.buildBodyPart(headSystem, 1);
		
		assertEquals(lengthGene * OrganBuilder.PART_LENGTH_MULTIPLIER, organ.getLength(), 0);
		assertEquals(thicknessGene * OrganBuilder.PART_THICKNESS_MULTIPLIER, organ.getThickness(), 0);
		assertEquals(angleToParentGene % OrganBuilder.PART_MAX_ANGLE_TO_PARENT, organ.getAbsoluteAngle(), 0);
	}
	
	@Test
	public void generateAMirroredOrgan() {
		int angleToParentGene = 80;
		
		Organ organ = buildSegment(angleToParentGene, -1);
		
		assertEquals(-10, organ.getAbsoluteAngle(), 0);
	}

	private Organ buildSegment(int angleToParentGene, int angleSign) {
		OrganBuilder builder = new OrganBuilder(new int[] {0, 50, 60, 70, angleToParentGene, 10});
		BodyPart head = builder.buildHeadSystem();
		BodyPart neck = head.getChildren().get(0);
		return builder.buildBodyPart(neck, angleSign);
	}
}
