package org.nusco.narjillos.embryogenesis.bodyplan;

import org.nusco.narjillos.creature.body.Organ;

class MockOrganBuilder implements OrganBuilder {

	private final int id;
	private final BodyPlanInstruction instruction;

	public MockOrganBuilder(int id, BodyPlanInstruction instruction) {
		this.id = id;
		this.instruction = instruction;
	}

	@Override
	public BodyPlanInstruction getInstruction() {
		return instruction;
	}
	
	@Override
	public Organ buildOrgan(Organ parent, int sign) {
		return new MockOrgan(id, parent, sign);
	}
}
