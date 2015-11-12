package org.nusco.narjillos.creature.embryogenesis.bodyplan;

import java.util.LinkedList;

import org.nusco.narjillos.creature.body.MovingOrgan;

/**
 * The interpreter for the body plan "program". It takes a sequence of
 * OrganBuilders, each one of which contains an encoded instruction. The
 * interpreter walks through the instructions to decide how to assemble the body
 * segments generated by the builders. The result is a tree of body segments.
 */
public class BodyPlan {

	private final OrganBuilder[] builders;

	public BodyPlan(OrganBuilder[] builders) {
		this.builders = builders;
	}

	public MovingOrgan buildBodyTree() {
		return buildBodyTree(null, getBuildersQueue(), 1);
	}

	private MovingOrgan buildBodyTree(MovingOrgan parent, LinkedList<OrganBuilder> buildersQueue, int sign) {
		if (buildersQueue.isEmpty())
			return null;

		MovingOrgan result = null;
		OrganBuilder nextBuilder = buildersQueue.pop();
		switch (nextBuilder.getBodyPlanInstruction()) {
		case STOP:
			result = nextBuilder.buildOrgan(parent, sign);
			break;
		case CONTINUE:
			result = nextBuilder.buildOrgan(parent, sign);
			buildChild(result, buildersQueue, sign);
			break;
		case BRANCH:
			result = nextBuilder.buildOrgan(parent, sign);
			buildChild(result, buildersQueue, sign);
			buildChild(result, buildersQueue, sign);
			break;
		case MIRROR:
			result = nextBuilder.buildOrgan(parent, sign);
			buildChild(result, copyOf(buildersQueue), sign);
			buildChild(result, buildersQueue, -sign);
			buildChild(result, buildersQueue, sign);
			break;
		case SKIP:
			if (parent == null) {
				// never skip the first organ (it's the head)
				result = nextBuilder.buildOrgan(parent, sign);
				buildChild(result, buildersQueue, sign);
			} else
				result = buildBodyTree(parent, buildersQueue, sign);
			break;
		}
		return result;
	}

	private void buildChild(MovingOrgan parent, LinkedList<OrganBuilder> buildersQueue, int sign) {
		MovingOrgan child = buildBodyTree(parent, buildersQueue, sign);
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