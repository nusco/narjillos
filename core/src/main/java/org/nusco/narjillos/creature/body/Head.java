package org.nusco.narjillos.creature.body;

import org.nusco.narjillos.creature.body.pns.WaveNerve;
import org.nusco.narjillos.shared.physics.Segment;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.utilities.ColorByte;

public class Head extends BodySegment {

	private static final double ROTATION_SPEED = 0.5;
	private static final double ROTATION_HISTERESIS = ROTATION_SPEED;
	private static final double WAVE_SIGNAL_FREQUENCY = 0.005;

	private final double metabolicRate;
	
	public Head(int length, int thickness, ColorByte hue, double metabolicRate) {
		super(length, thickness, hue, new WaveNerve(metabolicRate * WAVE_SIGNAL_FREQUENCY), 0, null);
		this.metabolicRate = metabolicRate;
	}

	public double getMetabolicRate() {
		return metabolicRate;
	}

	@Override
	protected Vector calculateStartPoint() {
		return Vector.ZERO;
	}
	
	@Override
	protected double calculateAbsoluteAngle() {
		return getAngleToParent();
	}
	
	@Override
	protected Vector calculateMainAxis() {
		return getVector().normalize(1);
	}

	@Override
	public void tick(Vector inputSignal, ForceField forceField) {
		Segment beforeMovement = getSegment();
		
		double updatedAngle = calculateUpdatedAngle(inputSignal);
		setAngleToParent(getAngleToParent() + updatedAngle);
		Vector outputSignal = getNerve().tick(inputSignal);

		forceField.record(beforeMovement, this);
		tickChildren(outputSignal, forceField);
	}

	@Override
	protected double calculateUpdatedAngle(Vector signal) {
		// HACK. will stay in place until I have real physical rotation
		double difference = signal.invert().getAngleWith(getVector());
		
		// special case: in case the main axis is exactly opposite to the target
		if (Math.abs(difference - 180) < 2)
			difference = -178;

		if (Math.abs(difference) < ROTATION_HISTERESIS)
			return 0;
		double sign = Math.signum(180 - Math.abs(difference));
		double unsignedResult = ROTATION_SPEED * Math.signum(difference);
		return sign * unsignedResult;
	}
}