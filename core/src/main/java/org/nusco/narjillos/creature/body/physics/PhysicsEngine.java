package org.nusco.narjillos.creature.body.physics;

import org.nusco.narjillos.shared.physics.Segment;

/**
 * Registers rigid bodies' movements, and calculates stuff based on them.
 */
public interface PhysicsEngine {

	public static final double ENERGY_SCALE = 1.0 / 75_000_000_000L;

	public void registerMovement(Segment initialPositionInSpace, Segment finalPositionInSpace, double mass);

	public double getEnergy();
}
