package org.nusco.narjillos.creature.body.pns;

import org.nusco.narjillos.shared.physics.Vector;

/**
 * Processes nervous impulses, turning input signals into
 * (generally different) output signals.
 */
public interface Nerve {

	public Vector tick(Vector inputSignal);
}
