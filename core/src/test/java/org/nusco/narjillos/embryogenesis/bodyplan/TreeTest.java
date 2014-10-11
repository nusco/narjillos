package org.nusco.narjillos.embryogenesis.bodyplan;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TreeTest {

	@Test
	public void executesContinueInstructions() {
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
	public void executesStopInstructions() {
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
	public void executesBranchInstructions() {
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

}
