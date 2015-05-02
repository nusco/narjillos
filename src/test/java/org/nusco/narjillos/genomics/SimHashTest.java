package org.nusco.narjillos.genomics;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SimHashTest {

	@Test
	public void calculatesDistanceBetweenDNAs() {
		DNA dna1 = new DNA(100000, "{002_028_171_203_248_055_000_023_061_107_096}{246_091_059_197_047_114_131_198_045_220_123}{163_233_254_202_238_046_224_175_199_141_221}{090_058_060_010_103_152_070_152_071_210_157}{219_110_073_091_178_091_229_131_120_043_057}{031_048_118_079_087_033_109_108_118_253_130}");
		DNA dna2 = new DNA(100000, "{017_028_000_223_248_055_000_023_061_107_096}{250_091_059_198_047_114_131_198_045_220_123}{163_233_254_202_238_046_224_175_199_141_221}{090_058_060_010_103_152_070_152_071_210_157}{219_110_073_091_178_091_229_131_120_043_057}{031_048_118_079_087_033_109_108_118_253_130}");
		DNA dna3 = new DNA(100000, "{246_091_059_197_047_114_131_198_045_220_123}{016_028_171_203_248_055_000_023_061_107_096}{090_058_060_010_103_152_070_152_071_210_157}{219_110_073_091_178_091_229_131_120_043_057}{031_048_118_079_087_033_109_108_118_253_130}");

		int distance1to1 = SimHash.getDistance(dna1, dna1);
		int distance1to2 = SimHash.getDistance(dna1, dna2);
		int distance1to3 = SimHash.getDistance(dna1, dna3);
		
		assertEquals(0, distance1to1);
		assertTrue(distance1to2 > 0);
		assertTrue(distance1to3 > distance1to2);
	}
}
