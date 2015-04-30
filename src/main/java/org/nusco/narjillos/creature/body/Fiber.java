package org.nusco.narjillos.creature.body;

/**
 * The coloured fibers in the bodies or narjillos.
 */
public class Fiber {

	// 0 to 255
	private final int red;
	private final int green;
	private final int blue;

	// 0.0 to 1.0
	private final double percentOfRed;
	private final double percentOfGreen;
	private final double percentOfBlue;

	public Fiber(int red, int green, int blue) {
		this.red = clipToByte(red);
		this.green = clipToByte(green);
		this.blue = clipToByte(blue);

		double totalComponents = this.red + this.green + this.blue;
		this.percentOfRed = percent(this.red, totalComponents);
		this.percentOfGreen = percent(this.green, totalComponents);
		this.percentOfBlue = percent(this.blue, totalComponents);
	}

	public double getPercentOfRed() {
		return percentOfRed;
	}

	public double getPercentOfGreen() {
		return percentOfGreen;
	}

	public double getPercentOfBlue() {
		return percentOfBlue;
	}

	public Fiber shift(int redShift, int greenShift, int blueShift) {
		return new Fiber(red + redShift, green + greenShift, blue + blueShift);
	}

	@Override
	public boolean equals(Object obj) {
		Fiber other = (Fiber) obj;
		return red == other.red && blue == other.blue && green == other.green
				&& Double.doubleToLongBits(getPercentOfRed()) == Double.doubleToLongBits(other.getPercentOfRed())
				&& Double.doubleToLongBits(getPercentOfGreen()) == Double.doubleToLongBits(other.getPercentOfGreen())
				&& Double.doubleToLongBits(getPercentOfBlue()) == Double.doubleToLongBits(other.getPercentOfBlue());
	}

	public String getVisualHash() {
		return "" + approximate(red) + "_" + approximate(green) + "_" + approximate(blue);
	}
	
	private int approximate(int color) {
		return color / 25; // 0 to 10
	}

	@Override
	public int hashCode() {
		return red ^ green ^ blue;
	}

	@Override
	public String toString() {
		return "(" + red + ", " + green + ", " + blue + ")";
	}

	private int clipToByte(int n) {
		if (n < 0)
			return 0;
		if (n > 255)
			return 255;
		return n;
	}

	private double percent(double n, double total) {
		return total == 0 ? 1.0 : n / total;
	}
}
