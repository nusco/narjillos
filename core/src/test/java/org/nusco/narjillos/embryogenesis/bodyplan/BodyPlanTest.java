package org.nusco.narjillos.embryogenesis.bodyplan;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BodyPlanTest {

	@Test
	public void executesContinues() {
		BodyPlan bodyPlan = new BodyPlan(new OrganBuilder[] {
				new MockOrganBuilder(1, Instruction.CONTINUE),
				new MockOrganBuilder(2, Instruction.CONTINUE),
				new MockOrganBuilder(3, Instruction.CONTINUE),
				new MockOrganBuilder(4, Instruction.CONTINUE),
		});

		String expectedBodyPlan = "1-2-3-4";
		assertEquals(expectedBodyPlan, bodyPlan.buildBodyTree().toString());
	}

	@Test
	public void ignoresInstructionsAfterTheStop() {
		BodyPlan bodyPlan = new BodyPlan(new OrganBuilder[] {
				new MockOrganBuilder(1, Instruction.CONTINUE),
				new MockOrganBuilder(2, Instruction.CONTINUE),
				new MockOrganBuilder(3, Instruction.STOP),
				new MockOrganBuilder(4, Instruction.CONTINUE),
		});

		String expectedBodyPlan = "1-2-3";
		assertEquals(expectedBodyPlan, bodyPlan.buildBodyTree().toString());
	}

	@Test
	public void executesBranches() {
		BodyPlan bodyPlan = new BodyPlan(new OrganBuilder[] {
				new MockOrganBuilder(1, Instruction.CONTINUE),
				new MockOrganBuilder(2, Instruction.BRANCH),
				new MockOrganBuilder(3, Instruction.CONTINUE),
				new MockOrganBuilder(4, Instruction.STOP),
				new MockOrganBuilder(5, Instruction.CONTINUE),
				new MockOrganBuilder(6, Instruction.STOP),
		});

		String expectedBodyPlan = "1-2-(3-4, 5-6)";
		assertEquals(expectedBodyPlan, bodyPlan.buildBodyTree().toString());
	}

	@Test
	public void executesRecursiveBranches() {
		BodyPlan bodyPlan = new BodyPlan(new OrganBuilder[] {
				new MockOrganBuilder(1, Instruction.CONTINUE),
				new MockOrganBuilder(2, Instruction.BRANCH),
				new MockOrganBuilder(3, Instruction.CONTINUE),
				new MockOrganBuilder(4, Instruction.BRANCH),
				new MockOrganBuilder(5, Instruction.STOP),
				new MockOrganBuilder(6, Instruction.CONTINUE),
				new MockOrganBuilder(7, Instruction.STOP),
				new MockOrganBuilder(8, Instruction.CONTINUE),
				new MockOrganBuilder(9, Instruction.STOP),
		});

		String expectedBodyPlan = "1-2-(3-4-(5, 6-7), 8-9)";
		assertEquals(expectedBodyPlan, bodyPlan.buildBodyTree().toString());
	}

	@Test
	public void ignoresInstructionsAfterTheSecondBranchStop() {
		BodyPlan bodyPlan = new BodyPlan(new OrganBuilder[] {
				new MockOrganBuilder(1, Instruction.BRANCH),
				new MockOrganBuilder(2, Instruction.STOP),
				new MockOrganBuilder(3, Instruction.STOP),
				new MockOrganBuilder(4, Instruction.CONTINUE),
		});

		String expectedBodyPlan = "1-(2, 3)";
		assertEquals(expectedBodyPlan, bodyPlan.buildBodyTree().toString());
	}

	@Test
	public void executesMirroring() {
		BodyPlan bodyPlan = new BodyPlan(new OrganBuilder[] {
				new MockOrganBuilder(1, Instruction.MIRROR),
				new MockOrganBuilder(2, Instruction.BRANCH),
				new MockOrganBuilder(3, Instruction.STOP),
				new MockOrganBuilder(4, Instruction.STOP),
				new MockOrganBuilder(5, Instruction.CONTINUE),
				new MockOrganBuilder(6, Instruction.STOP),
		});

		String expectedBodyPlan = "1-(2-(3, 4), ^2-(^3, ^4), 5-6)";
		assertEquals(expectedBodyPlan, bodyPlan.buildBodyTree().toString());
	}

	@Test
	public void executesRecursiveMirroring() {
		BodyPlan bodyPlan = new BodyPlan(new OrganBuilder[] {
				new MockOrganBuilder(1, Instruction.MIRROR),
				new MockOrganBuilder(2, Instruction.MIRROR),
				new MockOrganBuilder(3, Instruction.STOP),
				new MockOrganBuilder(4, Instruction.STOP),
				new MockOrganBuilder(5, Instruction.STOP),
				new MockOrganBuilder(6, Instruction.STOP),
		});

		String expectedBodyPlan = "1-(2-(3, ^3, 4), ^2-(^3, 3, ^4), 5)";
		assertEquals(expectedBodyPlan, bodyPlan.buildBodyTree().toString());
	}
}
