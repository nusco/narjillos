package org.nusco.narjillos.creature.embryogenesis.bodyplan;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BodyPlanTest {

	@Test
	public void executesContinues() {
		BodyPlan bodyPlan = new BodyPlan(new OrganBuilder[] {
				new MockOrganBuilder(1, BodyPlanInstruction.CONTINUE),
				new MockOrganBuilder(2, BodyPlanInstruction.CONTINUE),
				new MockOrganBuilder(3, BodyPlanInstruction.CONTINUE),
				new MockOrganBuilder(4, BodyPlanInstruction.CONTINUE),
		});

		String expectedBodyPlan = "1-2-3-4";
		assertEquals(expectedBodyPlan, bodyPlan.buildBodyTree().toString());
	}

	@Test
	public void ignoresInstructionsAfterTheStop() {
		BodyPlan bodyPlan = new BodyPlan(new OrganBuilder[] {
				new MockOrganBuilder(1, BodyPlanInstruction.CONTINUE),
				new MockOrganBuilder(2, BodyPlanInstruction.CONTINUE),
				new MockOrganBuilder(3, BodyPlanInstruction.STOP),
				new MockOrganBuilder(4, BodyPlanInstruction.CONTINUE),
		});

		String expectedBodyPlan = "1-2-3";
		assertEquals(expectedBodyPlan, bodyPlan.buildBodyTree().toString());
	}

	@Test
	public void executesBranches() {
		BodyPlan bodyPlan = new BodyPlan(new OrganBuilder[] {
				new MockOrganBuilder(1, BodyPlanInstruction.CONTINUE),
				new MockOrganBuilder(2, BodyPlanInstruction.BRANCH),
				new MockOrganBuilder(3, BodyPlanInstruction.CONTINUE),
				new MockOrganBuilder(4, BodyPlanInstruction.STOP),
				new MockOrganBuilder(5, BodyPlanInstruction.CONTINUE),
				new MockOrganBuilder(6, BodyPlanInstruction.STOP),
		});

		String expectedBodyPlan = "1-2-(3-4, 5-6)";
		assertEquals(expectedBodyPlan, bodyPlan.buildBodyTree().toString());
	}

	@Test
	public void executesRecursiveBranches() {
		BodyPlan bodyPlan = new BodyPlan(new OrganBuilder[] {
				new MockOrganBuilder(1, BodyPlanInstruction.CONTINUE),
				new MockOrganBuilder(2, BodyPlanInstruction.BRANCH),
				new MockOrganBuilder(3, BodyPlanInstruction.CONTINUE),
				new MockOrganBuilder(4, BodyPlanInstruction.BRANCH),
				new MockOrganBuilder(5, BodyPlanInstruction.STOP),
				new MockOrganBuilder(6, BodyPlanInstruction.CONTINUE),
				new MockOrganBuilder(7, BodyPlanInstruction.STOP),
				new MockOrganBuilder(8, BodyPlanInstruction.CONTINUE),
				new MockOrganBuilder(9, BodyPlanInstruction.STOP),
		});

		String expectedBodyPlan = "1-2-(3-4-(5, 6-7), 8-9)";
		assertEquals(expectedBodyPlan, bodyPlan.buildBodyTree().toString());
	}

	@Test
	public void ignoresInstructionsAfterTheSecondBranchStop() {
		BodyPlan bodyPlan = new BodyPlan(new OrganBuilder[] {
				new MockOrganBuilder(1, BodyPlanInstruction.BRANCH),
				new MockOrganBuilder(2, BodyPlanInstruction.STOP),
				new MockOrganBuilder(3, BodyPlanInstruction.STOP),
				new MockOrganBuilder(4, BodyPlanInstruction.CONTINUE),
		});

		String expectedBodyPlan = "1-(2, 3)";
		assertEquals(expectedBodyPlan, bodyPlan.buildBodyTree().toString());
	}

	@Test
	public void executesMirroring() {
		BodyPlan bodyPlan = new BodyPlan(new OrganBuilder[] {
				new MockOrganBuilder(1, BodyPlanInstruction.MIRROR),
				new MockOrganBuilder(2, BodyPlanInstruction.BRANCH),
				new MockOrganBuilder(3, BodyPlanInstruction.STOP),
				new MockOrganBuilder(4, BodyPlanInstruction.STOP),
				new MockOrganBuilder(5, BodyPlanInstruction.CONTINUE),
				new MockOrganBuilder(6, BodyPlanInstruction.STOP),
		});

		String expectedBodyPlan = "1-(2-(3, 4), ^2-(^3, ^4), 5-6)";
		assertEquals(expectedBodyPlan, bodyPlan.buildBodyTree().toString());
	}

	@Test
	public void executesRecursiveMirroring() {
		BodyPlan bodyPlan = new BodyPlan(new OrganBuilder[] {
				new MockOrganBuilder(1, BodyPlanInstruction.MIRROR),
				new MockOrganBuilder(2, BodyPlanInstruction.MIRROR),
				new MockOrganBuilder(3, BodyPlanInstruction.STOP),
				new MockOrganBuilder(4, BodyPlanInstruction.STOP),
				new MockOrganBuilder(5, BodyPlanInstruction.STOP),
				new MockOrganBuilder(6, BodyPlanInstruction.STOP),
		});

		String expectedBodyPlan = "1-(2-(3, ^3, 4), ^2-(^3, 3, ^4), 5)";
		assertEquals(expectedBodyPlan, bodyPlan.buildBodyTree().toString());
	}
}
