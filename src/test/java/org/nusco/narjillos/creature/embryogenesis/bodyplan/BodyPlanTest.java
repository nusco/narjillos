package org.nusco.narjillos.creature.embryogenesis.bodyplan;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class BodyPlanTest {

	@Test
	public void executesContinues() {
		var bodyPlan = new BodyPlan(new OrganBuilder[]{
			new MockOrganBuilder(1, BodyPlanInstruction.CONTINUE),
			new MockOrganBuilder(2, BodyPlanInstruction.CONTINUE),
			new MockOrganBuilder(3, BodyPlanInstruction.CONTINUE),
			new MockOrganBuilder(4, BodyPlanInstruction.CONTINUE),
		});

		var expectedBodyPlan = "1-2-3-4";

		assertThat(bodyPlan.buildBodyTree().toString()).isEqualTo(expectedBodyPlan);
	}

	@Test
	public void ignoresInstructionsAfterTheStop() {
		var bodyPlan = new BodyPlan(new OrganBuilder[]{
			new MockOrganBuilder(1, BodyPlanInstruction.CONTINUE),
			new MockOrganBuilder(2, BodyPlanInstruction.CONTINUE),
			new MockOrganBuilder(3, BodyPlanInstruction.STOP),
			new MockOrganBuilder(4, BodyPlanInstruction.CONTINUE),
		});

		var expectedBodyPlan = "1-2-3";

		assertThat(bodyPlan.buildBodyTree().toString()).isEqualTo(expectedBodyPlan);
	}

	@Test
	public void executesBranches() {
		var bodyPlan = new BodyPlan(new OrganBuilder[]{
			new MockOrganBuilder(1, BodyPlanInstruction.CONTINUE),
			new MockOrganBuilder(2, BodyPlanInstruction.BRANCH),
			new MockOrganBuilder(3, BodyPlanInstruction.CONTINUE),
			new MockOrganBuilder(4, BodyPlanInstruction.STOP),
			new MockOrganBuilder(5, BodyPlanInstruction.CONTINUE),
			new MockOrganBuilder(6, BodyPlanInstruction.STOP),
		});

		var expectedBodyPlan = "1-2-(3-4, 5-6)";

		assertThat(bodyPlan.buildBodyTree().toString()).isEqualTo(expectedBodyPlan);
	}

	@Test
	public void executesRecursiveBranches() {
		var bodyPlan = new BodyPlan(new OrganBuilder[]{
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

		var expectedBodyPlan = "1-2-(3-4-(5, 6-7), 8-9)";

		assertThat(bodyPlan.buildBodyTree().toString()).isEqualTo(expectedBodyPlan);
	}

	@Test
	public void ignoresInstructionsAfterTheSecondBranchStop() {
		var bodyPlan = new BodyPlan(new OrganBuilder[]{
			new MockOrganBuilder(1, BodyPlanInstruction.BRANCH),
			new MockOrganBuilder(2, BodyPlanInstruction.STOP),
			new MockOrganBuilder(3, BodyPlanInstruction.STOP),
			new MockOrganBuilder(4, BodyPlanInstruction.CONTINUE),
		});

		var expectedBodyPlan = "1-(2, 3)";

		assertThat(bodyPlan.buildBodyTree().toString()).isEqualTo(expectedBodyPlan);
	}

	@Test
	public void executesMirroring() {
		var bodyPlan = new BodyPlan(new OrganBuilder[]{
			new MockOrganBuilder(1, BodyPlanInstruction.MIRROR),
			new MockOrganBuilder(2, BodyPlanInstruction.BRANCH),
			new MockOrganBuilder(3, BodyPlanInstruction.STOP),
			new MockOrganBuilder(4, BodyPlanInstruction.STOP),
			new MockOrganBuilder(5, BodyPlanInstruction.CONTINUE),
			new MockOrganBuilder(6, BodyPlanInstruction.STOP),
		});

		var expectedBodyPlan = "1-(2-(3, 4), ^2-(^3, ^4), 5-6)";

		assertThat(bodyPlan.buildBodyTree().toString()).isEqualTo(expectedBodyPlan);
	}

	@Test
	public void executesRecursiveMirroring() {
		var bodyPlan = new BodyPlan(new OrganBuilder[]{
			new MockOrganBuilder(1, BodyPlanInstruction.MIRROR),
			new MockOrganBuilder(2, BodyPlanInstruction.MIRROR),
			new MockOrganBuilder(3, BodyPlanInstruction.STOP),
			new MockOrganBuilder(4, BodyPlanInstruction.STOP),
			new MockOrganBuilder(5, BodyPlanInstruction.STOP),
			new MockOrganBuilder(6, BodyPlanInstruction.STOP),
		});

		var expectedBodyPlan = "1-(2-(3, ^3, 4), ^2-(^3, 3, ^4), 5)";

		assertThat(bodyPlan.buildBodyTree().toString()).isEqualTo(expectedBodyPlan);
	}

	@Test
	public void executesSkips() {
		var bodyPlan = new BodyPlan(new OrganBuilder[]{
			new MockOrganBuilder(1, BodyPlanInstruction.CONTINUE),
			new MockOrganBuilder(2, BodyPlanInstruction.SKIP),
			new MockOrganBuilder(3, BodyPlanInstruction.CONTINUE),
			new MockOrganBuilder(4, BodyPlanInstruction.CONTINUE),
			new MockOrganBuilder(5, BodyPlanInstruction.SKIP),
			new MockOrganBuilder(6, BodyPlanInstruction.SKIP),
			new MockOrganBuilder(7, BodyPlanInstruction.CONTINUE),
			new MockOrganBuilder(8, BodyPlanInstruction.SKIP),
		});

		var expectedBodyPlan = "1-3-4-7";

		assertThat(bodyPlan.buildBodyTree().toString()).isEqualTo(expectedBodyPlan);
	}

	@Test
	public void neverSkipsTheHead() {
		var bodyPlan = new BodyPlan(new OrganBuilder[]{
			new MockOrganBuilder(1, BodyPlanInstruction.SKIP),
			new MockOrganBuilder(2, BodyPlanInstruction.SKIP),
			new MockOrganBuilder(3, BodyPlanInstruction.CONTINUE),
		});

		var expectedBodyPlan = "1-3";

		assertThat(bodyPlan.buildBodyTree().toString()).isEqualTo(expectedBodyPlan);
	}
}
