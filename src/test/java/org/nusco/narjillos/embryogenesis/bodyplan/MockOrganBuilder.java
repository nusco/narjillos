package org.nusco.narjillos.embryogenesis.bodyplan;

import org.nusco.narjillos.creature.body.ConnectedOrgan;
import org.nusco.narjillos.creature.body.MovingOrgan;

class MockOrganBuilder implements OrganBuilder {

	private final int id;
	private final BodyPlanInstruction instruction;

	public MockOrganBuilder(int id, BodyPlanInstruction instruction) {
		this.id = id;
		this.instruction = instruction;
	}

	@Override
	public BodyPlanInstruction getBodyPlanInstruction() {
		return instruction;
	}
	
	@Override
	public MovingOrgan buildOrgan(ConnectedOrgan parent, int sign) {
		return new MockOrgan(id, parent, sign);
	}
}
