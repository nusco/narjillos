package org.nusco.narjillos.shared.things;

public interface Energy {

	public static final Energy INFINITE = new Energy() {
		@Override
		public double getValue() {
			return Double.MAX_VALUE;
		};
		
		@Override
		public double getMaximumValue() {
			return Double.MAX_VALUE;
		};

		@Override
		public double getLevel() {
			return 1;
		};
		
		@Override
		public void tick(double energySpent, double energyGained) {};
		
		@Override
		public void steal(Energy other) {
			other.dropToZero();
		};
		
		@Override
		public void decreaseBy(double energy) {
		};

		@Override
		public boolean isZero() {
			return false;
		}

		@Override
		public void dropToZero() {
		}
	};

	public double getValue();
	
	public void tick(double energySpent, double energyGained);
	
	public double getMaximumValue();

	/**
	 * The current value in percent of the initial value (0 to 1).
	 * If the current value is 0 or lower, it returns 0.
	 * If the current value is over the initial value, it returns 1.
	 */
	public double getLevel();
	
	void steal(Energy other);
	
	public void decreaseBy(double energy);

	public boolean isZero();

	public void dropToZero();
}