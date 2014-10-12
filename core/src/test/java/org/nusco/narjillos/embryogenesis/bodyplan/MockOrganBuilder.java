package org.nusco.narjillos.embryogenesis.bodyplan;

import org.nusco.narjillos.creature.body.Organ;

class MockOrganBuilder implements OrganBuilder {

	private final int id;
	private final Instruction instruction;

	public MockOrganBuilder(int id, Instruction instruction) {
		this.id = id;
		this.instruction = instruction;
	}

	@Override
	public Instruction getInstruction() {
		return instruction;
	}
	
	@Override
	public Organ buildOrgan(Organ parent, int sign) {
		return new MockOrgan(id, parent, sign);
	}
}
