package org.nusco.narjillos.embryogenesis.bodyplan;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TreeTest {

	@Test
	public void executesContinues() {
		TreeBuilder bodyPlan = new TreeBuilder(new OrganBuilder[] {
				new MockOrganBuilder(1, Instruction.CONTINUE),
				new MockOrganBuilder(2, Instruction.CONTINUE),
				new MockOrganBuilder(3, Instruction.CONTINUE),
				new MockOrganBuilder(4, Instruction.CONTINUE),
		});

		String expectedBodyPlan = "1-2-3-4";
		assertEquals(expectedBodyPlan, bodyPlan.buildTree().toString());
	}

	@Test
	public void ignoresInstructionsAfterTheStop() {
		TreeBuilder bodyPlan = new TreeBuilder(new OrganBuilder[] {
				new MockOrganBuilder(1, Instruction.CONTINUE),
				new MockOrganBuilder(2, Instruction.CONTINUE),
				new MockOrganBuilder(3, Instruction.STOP),
				new MockOrganBuilder(4, Instruction.CONTINUE),
		});

		String expectedBodyPlan = "1-2-3";
		assertEquals(expectedBodyPlan, bodyPlan.buildTree().toString());
	}

	@Test
	public void executesBranches() {
		TreeBuilder bodyPlan = new TreeBuilder(new OrganBuilder[] {
				new MockOrganBuilder(1, Instruction.CONTINUE),
				new MockOrganBuilder(2, Instruction.BRANCH),
				new MockOrganBuilder(3, Instruction.CONTINUE),
				new MockOrganBuilder(4, Instruction.STOP),
				new MockOrganBuilder(5, Instruction.CONTINUE),
				new MockOrganBuilder(6, Instruction.STOP),
		});

		String expectedBodyPlan = "1-2-(3-4, 5-6)";
		assertEquals(expectedBodyPlan, bodyPlan.buildTree().toString());
	}

	@Test
	public void executesRecursiveBranches() {
		TreeBuilder bodyPlan = new TreeBuilder(new OrganBuilder[] {
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
		assertEquals(expectedBodyPlan, bodyPlan.buildTree().toString());
	}

	@Test
	public void ignoresInstructionsAfterTheSecondBranchStop() {
		TreeBuilder bodyPlan = new TreeBuilder(new OrganBuilder[] {
				new MockOrganBuilder(1, Instruction.BRANCH),
				new MockOrganBuilder(2, Instruction.STOP),
				new MockOrganBuilder(3, Instruction.STOP),
				new MockOrganBuilder(4, Instruction.CONTINUE),
		});

		String expectedBodyPlan = "1-(2, 3)";
		assertEquals(expectedBodyPlan, bodyPlan.buildTree().toString());
	}

	@Test
	public void executesMirroring() {
		TreeBuilder bodyPlan = new TreeBuilder(new OrganBuilder[] {
				new MockOrganBuilder(1, Instruction.MIRROR),
				new MockOrganBuilder(2, Instruction.BRANCH),
				new MockOrganBuilder(3, Instruction.STOP),
				new MockOrganBuilder(4, Instruction.STOP),
				new MockOrganBuilder(5, Instruction.CONTINUE),
				new MockOrganBuilder(6, Instruction.STOP),
		});

		String expectedBodyPlan = "1-(2-(3, 4), ^2-(^3, ^4), 5-6)";
		assertEquals(expectedBodyPlan, bodyPlan.buildTree().toString());
	}

	@Test
	public void executesRecursiveMirroring() {
		TreeBuilder bodyPlan = new TreeBuilder(new OrganBuilder[] {
				new MockOrganBuilder(1, Instruction.MIRROR),
				new MockOrganBuilder(2, Instruction.MIRROR),
				new MockOrganBuilder(3, Instruction.STOP),
				new MockOrganBuilder(4, Instruction.STOP),
				new MockOrganBuilder(5, Instruction.STOP),
				new MockOrganBuilder(6, Instruction.STOP),
		});

		String expectedBodyPlan = "1-(2-(3, ^3, 4), ^2-(^3, 3, ^4), 5)";
		assertEquals(expectedBodyPlan, bodyPlan.buildTree().toString());
	}
}
