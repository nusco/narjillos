package org.nusco.narjillos.core.things;

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
		public void tick(double energySpent) {};
		
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
	
	public void tick(double energySpent);
	
	public double getMaximumValue();

	void steal(Energy other);
	
	public void decreaseBy(double energy);

	public boolean isZero();

	public void dropToZero();
}