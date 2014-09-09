package org.nusco.narjillos.creature.body.physics;

import org.nusco.narjillos.shared.physics.Segment;


/**
 * This interface's implementors contain most of the physics engine.
 */
public strictfp interface ForceField {

	public static final double ENERGY_SCALE = 1.0 / 75_000_000_000L;

	public void registerMovement(Segment initialPositionInSpace, Segment finalPositionInSpace, double mass);
	public double getEnergy();
}
