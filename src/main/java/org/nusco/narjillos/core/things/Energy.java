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
		public void tick(double additionalEnergy) {};
		
		@Override
		public void steal(Energy other) {
			other.dropToZero();
		};
		
		@Override
		public void increaseBy(double amount) {
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
	
	public void tick(double additionalEnergy);
	
	public double getMaximumValue();

	void steal(Energy other);
	
	public void increaseBy(double amount);

	public boolean isZero();

	public void dropToZero();
}