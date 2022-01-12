package org.nusco.narjillos.core.geometry;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import org.junit.jupiter.api.Test;

public class FastMathTest {

	private final double EXPECTED_TRIG_PRECISION = 0.001; // 1/1000th of a point

	@Test
	public void preciselyCalculatesSinForMainAngles() {
		assertThat(FastMath.sin(0)).isEqualTo(0.0, within(EXPECTED_TRIG_PRECISION));
		assertThat(FastMath.sin(90)).isEqualTo(1.0, within(EXPECTED_TRIG_PRECISION));
		assertThat(FastMath.sin(180)).isEqualTo(0.0, within(EXPECTED_TRIG_PRECISION));
		assertThat(FastMath.sin(270)).isEqualTo(-1.0, within(EXPECTED_TRIG_PRECISION));
	}

	@Test
	public void calculatesSinForNegativeAngles() {
		assertThat(FastMath.sin(-90)).isEqualTo(-1.0, within(EXPECTED_TRIG_PRECISION));
		assertThat(FastMath.sin(-180)).isEqualTo(0.0, within(EXPECTED_TRIG_PRECISION));
	}

	@Test
	public void calculatesApproximatedSin() {
		double step = 0.0003;
		for (double degrees = -360; degrees < 360 * 2; degrees += step) {
			double javaSin = Math.sin(Math.toRadians(degrees));
			double fastSin = FastMath.sin(degrees);

			assertThat(fastSin)
				.withFailMessage("Mismatched sin(%f)", degrees)
				.isEqualTo(javaSin, within(EXPECTED_TRIG_PRECISION));
		}
	}

	@Test
	public void preciselyCalculatesCosForMainAngles() {
		assertThat(FastMath.cos(0)).isEqualTo(1.0, within(EXPECTED_TRIG_PRECISION));
		assertThat(FastMath.cos(90)).isEqualTo(0.0, within(EXPECTED_TRIG_PRECISION));
		assertThat(FastMath.cos(180)).isEqualTo(-1.0, within(EXPECTED_TRIG_PRECISION));
		assertThat(FastMath.cos(270)).isEqualTo(0.0, within(EXPECTED_TRIG_PRECISION));
	}

	@Test
	public void calculatesCosForNegativeAngles() {
		assertThat(FastMath.cos(-90)).isEqualTo(0.0, within(EXPECTED_TRIG_PRECISION));
		assertThat(FastMath.cos(-180)).isEqualTo(-1.0, within(EXPECTED_TRIG_PRECISION));
	}

	@Test
	public void calculatesApproximatedCos() {
		final double step = 0.0003;
		for (double degrees = -360; degrees < 360 * 2; degrees += step) {
			double javaCos = Math.cos(Math.toRadians(degrees));
			double fastCos = FastMath.cos(degrees);

			assertThat(fastCos)
				.withFailMessage("Mismatched cos(%f)", degrees)
				.isEqualTo(javaCos, within(EXPECTED_TRIG_PRECISION));
		}
	}

	@Test
	public void doesNotThrowsExceptionsIfCalculatingAnArcTangentWithZeros() {
		FastMath.atan(0, 10);
		FastMath.atan(10, 0);
	}

	@Test
	public void preciselyCalculatesAtanForMainAngles() {
		assertThat(FastMath.atan(0, 100)).isEqualTo(0.0);
		assertThat(FastMath.atan(100, 0)).isEqualTo(90.0);
		assertThat(FastMath.atan(0, -100)).isEqualTo(180.0);
		assertThat(FastMath.atan(-100, 0)).isEqualTo(-90.0);
	}

	@Test
	public void calculatesApproximatedArcTangentForSegmentsCloseToTheAxes() {
		final double VERY_LARGE = Double.MAX_VALUE;
		final double VERY_SMALL = 1 / Double.MAX_VALUE;

		assertEqualsAtan(VERY_SMALL, VERY_LARGE);
		assertEqualsAtan(VERY_SMALL, -VERY_LARGE);
		assertEqualsAtan(-VERY_SMALL, VERY_LARGE);
		assertEqualsAtan(-VERY_SMALL, -VERY_LARGE);

		assertEqualsAtan(VERY_LARGE, VERY_SMALL);
		assertEqualsAtan(VERY_LARGE, -VERY_SMALL);
		assertEqualsAtan(-VERY_LARGE, VERY_SMALL);
		assertEqualsAtan(-VERY_LARGE, -VERY_SMALL);
	}

	// Very slow test, so keep it disabled by default
	//@Test
	public void calculatesApproximatedArcTangent() {
		for (double y = -6000; y <= 6000; y += 0.7)
			for (double x = -100; x <= 100; x += 0.13)
				assertEqualsAtan(y, x);
	}

	private void assertEqualsAtan(double y, double x) {
		double javaAtan = Math.toDegrees(Math.atan2(y, x));
		double fastAtan = FastMath.atan(y, x);

		double EXPECTED_ATAN_PRECISION = 0.01;  // 1/100th of a degree

		assertThat(fastAtan)
			.withFailMessage("Mismatched atan(%f, %f)", y, x)
			.isEqualTo(javaAtan, within(EXPECTED_ATAN_PRECISION));
	}

	@Test
	public void calculatesApproximatedLogInARange() {
		final double step = 0.0003;
		for (double log = FastMath.LOG_MIN; log <= FastMath.LOG_MAX; log += step) {
			double javaLog = Math.log(log);
			double fastLog = FastMath.log(log);

			double EXPECTED_LOG_PRECISION = 0.01;

			assertThat(fastLog)
				.withFailMessage("Mismatched log(%f)", log)
				.isEqualTo(javaLog, within(EXPECTED_LOG_PRECISION));
		}
	}
}
