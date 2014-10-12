package org.nusco.narjillos.embryogenesis.bodyplan;

import java.util.LinkedList;

import org.nusco.narjillos.creature.body.Organ;

public class BodyPlan {

	private final OrganBuilder[] builders;

	public BodyPlan(OrganBuilder[] builders) {
		this.builders = builders;
	}

	public Organ buildOrganTree() {
		return buildTree(null, getBuildersQueue(), 1);
	}

	private Organ buildTree(Organ parent, LinkedList<OrganBuilder> buildersQueue, int sign) {
		if (buildersQueue.isEmpty())
			return null;
		
		OrganBuilder nextBuilder = buildersQueue.pop();
		Organ result = nextBuilder.buildOrgan(parent, sign);
		
		switch (nextBuilder.getInstruction()) {
		case STOP:
			break;
		case CONTINUE:
			buildChild(result, buildersQueue, sign);
			break;
		case BRANCH:
			buildChild(result, buildersQueue, sign);
			buildChild(result, buildersQueue, sign);
			break;
		case MIRROR:
			buildChild(result, copyOf(buildersQueue), sign);
			buildChild(result, buildersQueue, -sign);
			buildChild(result, buildersQueue, sign);
			break;
		}
		return result;
	}

	private void buildChild(Organ parent, LinkedList<OrganBuilder> buildersQueue, int sign) {
		Organ child = buildTree(parent, buildersQueue, sign);
		if (child != null)
			parent.addChild(child);
	}

	private LinkedList<OrganBuilder> copyOf(LinkedList<OrganBuilder> queue) {
		LinkedList<OrganBuilder> result = new LinkedList<OrganBuilder>();
		result.addAll(queue);
		return result;
	}

	private LinkedList<OrganBuilder> getBuildersQueue() {
		LinkedList<OrganBuilder> result = new LinkedList<>();
		for (int i = 0; i < builders.length; i++)
			result.add(builders[i]);
		return result;
	}	
}
