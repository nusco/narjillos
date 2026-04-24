package org.nusco.narjillos.core.things;

public interface Energy {

	Energy INFINITE = new Energy() {

		@Override
		public double getValue() {
			return Double.MAX_VALUE;
		}

		@Override
		public double getMaximumValue() {
			return Double.MAX_VALUE;
		}

		@Override
		public void tick(double additionalEnergy) {
		}

		@Override
		public void increaseBy(double amount) {
		}

		@Override
		public boolean isZero() {
			return false;
		}

		@Override
		public void absorb(Energy other) {
			other.dropToZero();
		}

		@Override
		public void dropToZero() {
		}

		@Override
		public void damage() {
		}
	};

	double getValue();

	void tick(double additionalEnergy);

	double getMaximumValue();

	void increaseBy(double amount);

	boolean isZero();

	void absorb(Energy other);

	void dropToZero();

	void damage();
}