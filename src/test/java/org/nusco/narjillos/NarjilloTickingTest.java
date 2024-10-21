package org.nusco.narjillos;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.nusco.narjillos.core.geometry.Vector;
import org.nusco.narjillos.core.things.LifeFormEnergy;
import org.nusco.narjillos.core.utilities.NumGen;
import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.creature.body.Organ;
import org.nusco.narjillos.genomics.DNA;
import org.nusco.narjillos.persistence.serialization.JSON;

/**
 * A test that raises a red flag whenever the Narjillo-related code changes.
 * <p>
 * The code that moves Narjillos, and in particular the code in the Organ
 * hierarchy, is quite complicated. Blame it on difficult, conflicting
 * requirements:
 * <p>
 * 1) This code must be fast. The system spends most of its time ticking
 * narjillos - so the faster, the better.
 * <p>
 * 2) This code must be deterministic. The same ticks should result in exactly
 * the same state of the narjillo, down to the exact values of each geometric
 * property of each organ. (Also see DeterministicExperimentTest).
 * <p>
 * 3) This code should be thread-safe. In general, only one thread will call
 * stateful methods such as Narjillo#tick() - but multiple threads can read data
 * from the Narjillo, such as the position of its organs in space. In
 * particular, the graphics system needs to read that data.
 * <p>
 * 4) This code should provide a convincing simulation of physics.
 * <p>
 * Because of (1), we need to optimize a lot. For example, we need to calculate
 * things like the angles of Organs as few times as possible. On the other hand,
 * because of (2) and (3), we need to be careful when using obvious
 * optimizations, such as lazy calculations and caches. An external thread might
 * read data from a Narjillo at any time, triggering lazy calculations in
 * non-predictable order - thus breaking deterministic behavior. Finally, (4) is
 * making the code complicated and hard to follow and test, which makes the
 * previous problems even worse.
 * <p>
 * That's why I wrote this test. The idea: check that a sample narjillo, when
 * ticked a specific number of times, results in a known state - even as
 * multiple threads are reading its data. If any optimization or refactoring
 * breaks any of the previous constraints, this test will probably go red.
 * <p>
 * (This test will also break if I make "legitimate" changes, like changing
 * physics or embryology - but in that case, I just need to update the expected
 * result in the test. This happens often, but it's worth it. To make it less
 * of a chore, see the comment to PRINT_NEW_EXPECTED_STATE_DECLARATION).
 */
public class NarjilloTickingTest {

	// Set this to true when you make "legitimate" code changes
	// that cause this test to break. You will get a new definition
	// of EXPECTED_STATE to copy-paste over the existing one in this
	// same class.
	private static final boolean PRINT_NEW_EXPECTED_STATE_DECLARATION = false;

	private final static String SAMPLE_DNA_DOCUMENT = "{010_099_000_233_073_175_102_230_014_157_181_045_068_134}{129_062_090_255_028_214_236_133_035_015_072_011_115_177}{231_015_237_066_073_033_096_100_247_114_166_127_181_077}{120_048_048_147_189_016_187_190_159_191_032_070_234_040}{081_069_145_160_233_163_084_012_149_217_148_091_091_238}{210_255_086_237_055_070_035_128_043_203_205_077_026_190}{093_217_239_195_235_231_112_141_023_148_220_166_176_204}{188_012_229_234_233_091_248_009_017_132_061_211_226_202}{232_231_037_111_246_177_231_236_150_114_134_093_053_165}{140_085_140_253_025_153_224_249_188_161_055_000_073_186}{037_236_004_229_079_157_169_229_111_107_080_137_214_083}{022_166_164_111_162_226_184_134_134_220_082_109_111_046}{189_055_035_184_132_204_197_065_102_046_183_248_104_034}{132_212_044_130_084_203_035_073_008_237_084_164_049_006}{232_207_177_124_113_163_190_146_253_126_182_143_033_081}{110_129_200_111_134_003_184_008_165_248_118_096_090_178}";

	private final static String EXPECTED_STATE = "{\"body\":{\"head\":{\"type\":\"Head\",\"data\":{\"metabolicRate\":2.7058823529411766,\"waveBeatRatio\":0.4635294117647059,\"byproduct\":\"HYDROGEN\",\"energyToChildren\":28100.0,\"eggVelocity\":45,\"eggInterval\":6800,\"startPoint\":\"(91.78453517250433, 210.26620138695046)\",\"angleToParent\":95.99680548063894,\"children\":[{\"type\":\"BodyPart\",\"data\":{\"angleToParentAtRest\":3.0,\"orientation\":1,\"amplitude\":5,\"skewing\":-39,\"currentSkewing\":7.09572328823005,\"cachedMetabolicRate\":2.7058823529411766,\"angleToParent\":5.172291869566124,\"children\":[{\"type\":\"BodyPart\",\"data\":{\"angleToParentAtRest\":-14.0,\"orientation\":-1,\"amplitude\":36,\"skewing\":27,\"currentSkewing\":-4.912423814928496,\"cachedMetabolicRate\":2.7058823529411766,\"angleToParent\":-46.88176844979851,\"children\":[{\"type\":\"BodyPart\",\"data\":{\"angleToParentAtRest\":-63.0,\"orientation\":-1,\"amplitude\":68,\"skewing\":14,\"currentSkewing\":-2.547182718851813,\"cachedMetabolicRate\":2.7058823529411766,\"angleToParent\":-65.54718271885181,\"children\":[{\"type\":\"BodyPart\",\"data\":{\"angleToParentAtRest\":0.0,\"orientation\":0,\"amplitude\":64,\"skewing\":54,\"currentSkewing\":-9.879998339012138,\"cachedMetabolicRate\":2.7058823529411766,\"angleToParent\":-9.879998339012138,\"children\":[{\"type\":\"BodyPart\",\"data\":{\"angleToParentAtRest\":7.0,\"orientation\":1,\"amplitude\":47,\"skewing\":65,\"currentSkewing\":-11.901485483268539,\"cachedMetabolicRate\":2.7058823529411766,\"angleToParent\":-4.901485483268539,\"children\":[{\"type\":\"BodyPart\",\"data\":{\"angleToParentAtRest\":-65.0,\"orientation\":-1,\"amplitude\":42,\"skewing\":-46,\"currentSkewing\":8.370987382634478,\"cachedMetabolicRate\":2.7058823529411766,\"angleToParent\":-56.62901261736552,\"children\":[],\"nerve\":{\"type\":\"Delay\",\"data\":{\"delay\":2,\"buffer\":[0.0,0.0]}},\"adultLength\":229.0,\"adultThickness\":46,\"adultMass\":10534.0,\"fiber\":\"(255, 156, 241)\",\"length\":8.570000000000014,\"thickness\":4.5699999999999985,\"cachedAbsoluteAngle\":-82.67035025809145,\"cachedStartPoint\":\"(115.61839937820332, 213.84031027219348)\",\"cachedVector\":\"(1.091910875919878, -8.499964038201961)\",\"cachedEndPoint\":\"(116.7103102541232, 205.3403462339915)\",\"cachedMass\":39.16490000000005,\"cachedCenterOfMass\":\"(116.16435481616325, 209.5903282530925)\",\"cachedSegment\":{\"startPoint\":\"(115.61839937820332, 213.84031027219348)\",\"vector\":\"(1.091910875919878, -8.499964038201961)\"}}},{\"type\":\"BodyPart\",\"data\":{\"angleToParentAtRest\":67.0,\"orientation\":1,\"amplitude\":51,\"skewing\":-51,\"currentSkewing\":9.306658208463647,\"cachedMetabolicRate\":2.7058823529411766,\"angleToParent\":76.30665820846365,\"children\":[{\"type\":\"BodyPart\",\"data\":{\"angleToParentAtRest\":56.0,\"orientation\":1,\"amplitude\":34,\"skewing\":-33,\"currentSkewing\":6.004073551579273,\"cachedMetabolicRate\":2.7058823529411766,\"angleToParent\":62.004073551579275,\"children\":[{\"type\":\"BodyPart\",\"data\":{\"angleToParentAtRest\":-34.0,\"orientation\":-1,\"amplitude\":15,\"skewing\":39,\"currentSkewing\":-7.09572328823005,\"cachedMetabolicRate\":2.7058823529411766,\"angleToParent\":-41.09572328823005,\"children\":[{\"type\":\"BodyPart\",\"data\":{\"angleToParentAtRest\":-29.0,\"orientation\":-1,\"amplitude\":75,\"skewing\":-30,\"currentSkewing\":5.458248683253885,\"cachedMetabolicRate\":2.7058823529411766,\"angleToParent\":-23.541751316746115,\"children\":[],\"nerve\":{\"type\":\"Delay\",\"data\":{\"delay\":0,\"buffer\":[]}},\"adultLength\":44.0,\"adultThickness\":26,\"adultMass\":1144.0,\"fiber\":\"(0, 255, 70)\",\"length\":10.099999999999982,\"thickness\":6.099999999999995,\"cachedAbsoluteAngle\":47.63191951434082,\"cachedStartPoint\":\"(124.18978808197586, 229.506988075234)\",\"cachedVector\":\"(6.805245510614845, 7.461963863212853)\",\"cachedEndPoint\":\"(130.99503359259072, 236.96895193844685)\",\"cachedMass\":61.60999999999984,\"cachedCenterOfMass\":\"(127.59241083728328, 233.23797000684044)\",\"cachedSegment\":{\"startPoint\":\"(124.18978808197586, 229.506988075234)\",\"vector\":\"(6.805245510614845, 7.461963863212853)\"}}}],\"nerve\":{\"type\":\"Delay\",\"data\":{\"delay\":12,\"buffer\":[0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0]}},\"adultLength\":35.0,\"adultThickness\":36,\"adultMass\":1260.0,\"fiber\":\"(9, 255, 255)\",\"length\":9.589999999999993,\"thickness\":5.589999999999995,\"cachedAbsoluteAngle\":71.17367083108694,\"cachedStartPoint\":\"(121.0960913053761, 220.43024111177917)\",\"cachedVector\":\"(3.0936967765997574, 9.076746963454825)\",\"cachedEndPoint\":\"(124.18978808197586, 229.506988075234)\",\"cachedMass\":53.608099999999915,\"cachedCenterOfMass\":\"(122.64293969367598, 224.96861459350657)\",\"cachedSegment\":{\"startPoint\":\"(121.0960913053761, 220.43024111177917)\",\"vector\":\"(3.0936967765997574, 9.076746963454825)\"}}},{\"type\":\"BodyPart\",\"data\":{\"angleToParentAtRest\":-65.0,\"orientation\":-1,\"amplitude\":78,\"skewing\":-6,\"currentSkewing\":1.091649736650777,\"cachedMetabolicRate\":2.7058823529411766,\"angleToParent\":-63.90835026334922,\"children\":[],\"nerve\":{\"type\":\"Delay\",\"data\":{\"delay\":19,\"buffer\":[0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0]}},\"adultLength\":200.0,\"adultThickness\":22,\"adultMass\":4400.0,\"fiber\":\"(13, 6, 255)\",\"length\":9.589999999999993,\"thickness\":5.589999999999995,\"cachedAbsoluteAngle\":48.361043855967765,\"cachedStartPoint\":\"(121.0960913053761, 220.43024111177917)\",\"cachedVector\":\"(6.370806434100015, 7.166936899512577)\",\"cachedEndPoint\":\"(127.46689773947612, 227.59717801129173)\",\"cachedMass\":53.608099999999915,\"cachedCenterOfMass\":\"(124.28149452242612, 224.01370956153545)\",\"cachedSegment\":{\"startPoint\":\"(121.0960913053761, 220.43024111177917)\",\"vector\":\"(6.370806434100015, 7.166936899512577)\"}}}],\"nerve\":{\"type\":\"Delay\",\"data\":{\"delay\":13,\"buffer\":[0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0]}},\"adultLength\":0.0,\"adultThickness\":45,\"adultMass\":1.0,\"fiber\":\"(0, 255, 255)\",\"length\":0.0,\"thickness\":5.080000000000004,\"cachedAbsoluteAngle\":112.26939411931698,\"cachedStartPoint\":\"(121.0960913053761, 220.43024111177917)\",\"cachedVector\":\"(-0.0, 0.0)\",\"cachedEndPoint\":\"(121.0960913053761, 220.43024111177917)\",\"cachedMass\":1.0,\"cachedCenterOfMass\":\"(121.0960913053761, 220.43024111177917)\",\"cachedSegment\":{\"startPoint\":\"(121.0960913053761, 220.43024111177917)\",\"vector\":\"(-0.0, 0.0)\"}}}],\"nerve\":{\"type\":\"Delay\",\"data\":{\"delay\":22,\"buffer\":[0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0]}},\"adultLength\":140.0,\"adultThickness\":50,\"adultMass\":7000.0,\"fiber\":\"(50, 255, 193)\",\"length\":8.570000000000014,\"thickness\":4.5699999999999985,\"cachedAbsoluteAngle\":50.26532056773771,\"cachedStartPoint\":\"(115.61839937820332, 213.84031027219348)\",\"cachedVector\":\"(5.477691927172786, 6.589930839585681)\",\"cachedEndPoint\":\"(121.0960913053761, 220.43024111177917)\",\"cachedMass\":39.16490000000005,\"cachedCenterOfMass\":\"(118.35724534178972, 217.13527569198632)\",\"cachedSegment\":{\"startPoint\":\"(115.61839937820332, 213.84031027219348)\",\"vector\":\"(5.477691927172786, 6.589930839585681)\"}}}],\"nerve\":{\"type\":\"Delay\",\"data\":{\"delay\":2,\"buffer\":[0.0,0.0]}},\"adultLength\":239.0,\"adultThickness\":39,\"adultMass\":9321.0,\"fiber\":\"(255, 229, 0)\",\"length\":8.059999999999981,\"thickness\":4.060000000000002,\"cachedAbsoluteAngle\":-26.041337640725935,\"cachedStartPoint\":\"(108.3772054840606, 217.37863831539264)\",\"cachedVector\":\"(7.241193894142725, -3.5383280431991526)\",\"cachedEndPoint\":\"(115.61839937820332, 213.84031027219348)\",\"cachedMass\":32.72359999999994,\"cachedCenterOfMass\":\"(111.99780243113196, 215.60947429379306)\",\"cachedSegment\":{\"startPoint\":\"(108.3772054840606, 217.37863831539264)\",\"vector\":\"(7.241193894142725, -3.5383280431991526)\"}}}],\"nerve\":{\"type\":\"Delay\",\"data\":{\"delay\":5,\"buffer\":[0.0,0.0,0.0,0.0,0.0]}},\"adultLength\":86.0,\"adultThickness\":47,\"adultMass\":4042.0,\"fiber\":\"(66, 22, 0)\",\"length\":7.549999999999991,\"thickness\":3.5499999999999954,\"cachedAbsoluteAngle\":-21.139852157457398,\"cachedStartPoint\":\"(101.33530551764389, 220.10030196858668)\",\"cachedVector\":\"(7.0418999664167075, -2.7216636531940326)\",\"cachedEndPoint\":\"(108.3772054840606, 217.37863831539264)\",\"cachedMass\":26.802499999999934,\"cachedCenterOfMass\":\"(104.85625550085224, 218.73947014198967)\",\"cachedSegment\":{\"startPoint\":\"(101.33530551764389, 220.10030196858668)\",\"vector\":\"(7.0418999664167075, -2.7216636531940326)\"}}},{\"type\":\"BodyPart\",\"data\":{\"angleToParentAtRest\":0.0,\"orientation\":0,\"amplitude\":64,\"skewing\":54,\"currentSkewing\":-9.879998339012138,\"cachedMetabolicRate\":2.7058823529411766,\"angleToParent\":-9.879998339012138,\"children\":[{\"type\":\"BodyPart\",\"data\":{\"angleToParentAtRest\":-7.0,\"orientation\":-1,\"amplitude\":47,\"skewing\":65,\"currentSkewing\":-11.901485483268539,\"cachedMetabolicRate\":2.7058823529411766,\"angleToParent\":-18.901485483268537,\"children\":[{\"type\":\"BodyPart\",\"data\":{\"angleToParentAtRest\":65.0,\"orientation\":1,\"amplitude\":42,\"skewing\":-46,\"currentSkewing\":8.370987382634478,\"cachedMetabolicRate\":2.7058823529411766,\"angleToParent\":73.37098738263448,\"children\":[],\"nerve\":{\"type\":\"Delay\",\"data\":{\"delay\":2,\"buffer\":[0.0,0.0]}},\"adultLength\":229.0,\"adultThickness\":46,\"adultMass\":10534.0,\"fiber\":\"(255, 156, 241)\",\"length\":8.570000000000014,\"thickness\":4.5699999999999985,\"cachedAbsoluteAngle\":33.32964974190855,\"cachedStartPoint\":\"(114.54700018481475, 212.19346095694104)\",\"cachedVector\":\"(7.160404507190697, 4.707625576529948)\",\"cachedEndPoint\":\"(121.70740469200544, 216.901086533471)\",\"cachedMass\":39.16490000000005,\"cachedCenterOfMass\":\"(118.1272024384101, 214.547273745206)\",\"cachedSegment\":{\"startPoint\":\"(114.54700018481475, 212.19346095694104)\",\"vector\":\"(7.160404507190697, 4.707625576529948)\"}}},{\"type\":\"BodyPart\",\"data\":{\"angleToParentAtRest\":-67.0,\"orientation\":-1,\"amplitude\":51,\"skewing\":-51,\"currentSkewing\":9.306658208463647,\"cachedMetabolicRate\":2.7058823529411766,\"angleToParent\":-57.693341791536355,\"children\":[{\"type\":\"BodyPart\",\"data\":{\"angleToParentAtRest\":-56.0,\"orientation\":-1,\"amplitude\":34,\"skewing\":-33,\"currentSkewing\":6.004073551579273,\"cachedMetabolicRate\":2.7058823529411766,\"angleToParent\":-49.995926448420725,\"children\":[{\"type\":\"BodyPart\",\"data\":{\"angleToParentAtRest\":34.0,\"orientation\":1,\"amplitude\":15,\"skewing\":39,\"currentSkewing\":-7.09572328823005,\"cachedMetabolicRate\":2.7058823529411766,\"angleToParent\":26.90427671176995,\"children\":[{\"type\":\"BodyPart\",\"data\":{\"angleToParentAtRest\":29.0,\"orientation\":1,\"amplitude\":75,\"skewing\":-30,\"currentSkewing\":5.458248683253885,\"cachedMetabolicRate\":2.7058823529411766,\"angleToParent\":34.458248683253885,\"children\":[],\"nerve\":{\"type\":\"Delay\",\"data\":{\"delay\":0,\"buffer\":[]}},\"adultLength\":44.0,\"adultThickness\":26,\"adultMass\":1144.0,\"fiber\":\"(0, 255, 70)\",\"length\":10.099999999999982,\"thickness\":6.099999999999995,\"cachedAbsoluteAngle\":-86.36808048565918,\"cachedStartPoint\":\"(108.48092577364518, 195.46668548737566)\",\"cachedVector\":\"(0.6394620723597885, -10.0796247565826)\",\"cachedEndPoint\":\"(109.12038784600497, 185.38706073079305)\",\"cachedMass\":61.60999999999984,\"cachedCenterOfMass\":\"(108.80065680982507, 190.42687310908437)\",\"cachedSegment\":{\"startPoint\":\"(108.48092577364518, 195.46668548737566)\",\"vector\":\"(0.6394620723597885, -10.0796247565826)\"}}}],\"nerve\":{\"type\":\"Delay\",\"data\":{\"delay\":12,\"buffer\":[0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0]}},\"adultLength\":35.0,\"adultThickness\":36,\"adultMass\":1260.0,\"fiber\":\"(9, 255, 255)\",\"length\":9.589999999999993,\"thickness\":5.589999999999995,\"cachedAbsoluteAngle\":-120.82632916891306,\"cachedStartPoint\":\"(113.39429195243802, 203.7015386427755)\",\"cachedVector\":\"(-4.913366178792839, -8.234853155399835)\",\"cachedEndPoint\":\"(108.48092577364518, 195.46668548737566)\",\"cachedMass\":53.608099999999915,\"cachedCenterOfMass\":\"(110.9376088630416, 199.58411206507557)\",\"cachedSegment\":{\"startPoint\":\"(113.39429195243802, 203.7015386427755)\",\"vector\":\"(-4.913366178792839, -8.234853155399835)\"}}},{\"type\":\"BodyPart\",\"data\":{\"angleToParentAtRest\":65.0,\"orientation\":1,\"amplitude\":78,\"skewing\":-6,\"currentSkewing\":1.091649736650777,\"cachedMetabolicRate\":2.7058823529411766,\"angleToParent\":66.09164973665078,\"children\":[],\"nerve\":{\"type\":\"Delay\",\"data\":{\"delay\":19,\"buffer\":[0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0]}},\"adultLength\":200.0,\"adultThickness\":22,\"adultMass\":4400.0,\"fiber\":\"(13, 6, 255)\",\"length\":9.589999999999993,\"thickness\":5.589999999999995,\"cachedAbsoluteAngle\":-81.63895614403224,\"cachedStartPoint\":\"(113.39429195243802, 203.7015386427755)\",\"cachedVector\":\"(1.39431264266254, -9.48785390110955)\",\"cachedEndPoint\":\"(114.78860459510057, 194.21368474166593)\",\"cachedMass\":53.608099999999915,\"cachedCenterOfMass\":\"(114.09144827376929, 198.95761169222072)\",\"cachedSegment\":{\"startPoint\":\"(113.39429195243802, 203.7015386427755)\",\"vector\":\"(1.39431264266254, -9.48785390110955)\"}}}],\"nerve\":{\"type\":\"Delay\",\"data\":{\"delay\":13,\"buffer\":[0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0]}},\"adultLength\":0.0,\"adultThickness\":45,\"adultMass\":1.0,\"fiber\":\"(0, 255, 255)\",\"length\":0.0,\"thickness\":5.080000000000004,\"cachedAbsoluteAngle\":-147.73060588068302,\"cachedStartPoint\":\"(113.39429195243802, 203.7015386427755)\",\"cachedVector\":\"(-0.0, -0.0)\",\"cachedEndPoint\":\"(113.39429195243802, 203.7015386427755)\",\"cachedMass\":1.0,\"cachedCenterOfMass\":\"(113.39429195243802, 203.7015386427755)\",\"cachedSegment\":{\"startPoint\":\"(113.39429195243802, 203.7015386427755)\",\"vector\":\"(-0.0, -0.0)\"}}}],\"nerve\":{\"type\":\"Delay\",\"data\":{\"delay\":22,\"buffer\":[0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0]}},\"adultLength\":140.0,\"adultThickness\":50,\"adultMass\":7000.0,\"fiber\":\"(50, 255, 193)\",\"length\":8.570000000000014,\"thickness\":4.5699999999999985,\"cachedAbsoluteAngle\":-97.73467943226228,\"cachedStartPoint\":\"(114.54700018481475, 212.19346095694104)\",\"cachedVector\":\"(-1.1527082323767244, -8.491922314165562)\",\"cachedEndPoint\":\"(113.39429195243802, 203.7015386427755)\",\"cachedMass\":39.16490000000005,\"cachedCenterOfMass\":\"(113.9706460686264, 207.94749979985826)\",\"cachedSegment\":{\"startPoint\":\"(114.54700018481475, 212.19346095694104)\",\"vector\":\"(-1.1527082323767244, -8.491922314165562)\"}}}],\"nerve\":{\"type\":\"Delay\",\"data\":{\"delay\":2,\"buffer\":[0.0,0.0]}},\"adultLength\":239.0,\"adultThickness\":39,\"adultMass\":9321.0,\"fiber\":\"(255, 229, 0)\",\"length\":8.059999999999981,\"thickness\":4.060000000000002,\"cachedAbsoluteAngle\":-40.041337640725935,\"cachedStartPoint\":\"(108.3772054840606, 217.37863831539264)\",\"cachedVector\":\"(6.169794700754152, -5.185177358451606)\",\"cachedEndPoint\":\"(114.54700018481475, 212.19346095694104)\",\"cachedMass\":32.72359999999994,\"cachedCenterOfMass\":\"(111.46210283443767, 214.78604963616684)\",\"cachedSegment\":{\"startPoint\":\"(108.3772054840606, 217.37863831539264)\",\"vector\":\"(6.169794700754152, -5.185177358451606)\"}}}],\"nerve\":{\"type\":\"Delay\",\"data\":{\"delay\":5,\"buffer\":[0.0,0.0,0.0,0.0,0.0]}},\"adultLength\":86.0,\"adultThickness\":47,\"adultMass\":4042.0,\"fiber\":\"(66, 22, 0)\",\"length\":7.549999999999991,\"thickness\":3.5499999999999954,\"cachedAbsoluteAngle\":-21.139852157457398,\"cachedStartPoint\":\"(101.33530551764389, 220.10030196858668)\",\"cachedVector\":\"(7.0418999664167075, -2.7216636531940326)\",\"cachedEndPoint\":\"(108.3772054840606, 217.37863831539264)\",\"cachedMass\":26.802499999999934,\"cachedCenterOfMass\":\"(104.85625550085224, 218.73947014198967)\",\"cachedSegment\":{\"startPoint\":\"(101.33530551764389, 220.10030196858668)\",\"vector\":\"(7.0418999664167075, -2.7216636531940326)\"}}}],\"nerve\":{\"type\":\"Delay\",\"data\":{\"delay\":18,\"buffer\":[0.0,0.16917748928861923,0.3334777949500711,0.488164336697696,0.6287776875005239,0.7512641335034982,0.8520925377243529,0.928356138487615,0.9778563478690898,0.9991732451905739,0.991626143823507,0.9784008827158318,0.959067219889833,0.9338303869951169,0.9027355060802535,0.8661126570562447,0.8240273195329518,0.7769262398575004]}},\"adultLength\":145.0,\"adultThickness\":32,\"adultMass\":4640.0,\"fiber\":\"(211, 137, 105)\",\"length\":7.040000000000002,\"thickness\":3.040000000000002,\"cachedAbsoluteAngle\":-11.259853818445258,\"cachedStartPoint\":\"(94.43081695854832, 221.4737378355802)\",\"cachedVector\":\"(6.904488559095576, -1.3734358669935198)\",\"cachedEndPoint\":\"(101.33530551764389, 220.10030196858668)\",\"cachedMass\":21.40160000000002,\"cachedCenterOfMass\":\"(97.8830612380961, 220.78701990208344)\",\"cachedSegment\":{\"startPoint\":\"(94.43081695854832, 221.4737378355802)\",\"vector\":\"(6.904488559095576, -1.3734358669935198)\"}}}],\"nerve\":{\"type\":\"Delay\",\"data\":{\"delay\":29,\"buffer\":[0.724893632131307,0.668481835453954,0.6077917109691603,0.5433209829879491,0.4756242090703004,0.4048224272693573,0.33166721614552624,0.25628937313299205,0.17948905282245853,0.10140356989986359,0.022861821228399262,-0.05582150499316338,-0.1341591425733553,-0.21183654122955345,-0.28802913601476615,-0.36260069882237217,-0.434759633926995,-0.5043769251803668,-0.5708568765647583,-0.6336509552251867,-0.6926468888539968,-0.7472184209116156,-0.7972679805447461,-0.842264279203567,-0.8821268660176369,-0.9164325896926148,-0.9451201135094063,-0.9678849122526261,-0.9846862837327852]}},\"adultLength\":237.0,\"adultThickness\":13,\"adultMass\":3081.0,\"fiber\":\"(0, 66, 192)\",\"length\":6.530000000000013,\"thickness\":2.5299999999999976,\"cachedAbsoluteAngle\":54.28732890040656,\"cachedStartPoint\":\"(90.61936737483818, 216.1721628518603)\",\"cachedVector\":\"(3.8114495837101496, 5.30157498371992)\",\"cachedEndPoint\":\"(94.43081695854832, 221.4737378355802)\",\"cachedMass\":16.520900000000015,\"cachedCenterOfMass\":\"(92.52509216669326, 218.82295034372027)\",\"cachedSegment\":{\"startPoint\":\"(90.61936737483818, 216.1721628518603)\",\"vector\":\"(3.8114495837101496, 5.30157498371992)\"}}}],\"nerve\":{\"type\":\"Delay\",\"data\":{\"delay\":4,\"buffer\":[-0.99534587720406,-0.9998446339083109,-0.9981241282392824,-0.9733392406205625]}},\"adultLength\":90.0,\"adultThickness\":50,\"adultMass\":4500.0,\"fiber\":\"(0, 255, 255)\",\"length\":6.019999999999978,\"thickness\":2.020000000000001,\"cachedAbsoluteAngle\":101.16909735020506,\"cachedStartPoint\":\"(91.78453517250433, 210.26620138695046)\",\"cachedVector\":\"(-1.1651677976661547, 5.905961464909842)\",\"cachedEndPoint\":\"(90.61936737483818, 216.1721628518603)\",\"cachedMass\":12.160399999999962,\"cachedCenterOfMass\":\"(91.20195127367126, 213.2191821194054)\",\"cachedSegment\":{\"startPoint\":\"(91.78453517250433, 210.26620138695046)\",\"vector\":\"(-1.1651677976661547, 5.905961464909842)\"}}}],\"nerve\":{\"type\":\"Wave\",\"data\":{\"frequency\":0.027058823529411767,\"angle\":292.99166782006955}},\"adultLength\":0.0,\"adultThickness\":46,\"adultMass\":1.0,\"fiber\":\"(73, 175, 102)\",\"length\":0.0,\"thickness\":1.5100000000000005,\"cachedAbsoluteAngle\":95.99680548063894,\"cachedStartPoint\":\"(91.78453517250433, 210.26620138695046)\",\"cachedVector\":\"(-0.0, 0.0)\",\"cachedEndPoint\":\"(91.78453517250433, 210.26620138695046)\",\"cachedMass\":1.0,\"cachedCenterOfMass\":\"(91.78453517250433, 210.26620138695046)\",\"cachedSegment\":{\"startPoint\":\"(91.78453517250433, 210.26620138695046)\",\"vector\":\"(-0.0, 0.0)\"}}},\"metabolicConsumption\":4.451059148021038,\"adultMass\":87626.0,\"mass\":666.4470999999993,\"redMass\":130.3080158197794,\"greenMass\":273.202601851249,\"blueMass\":262.9364823289709},\"dna\":{\"genes\":\"{010_099_000_233_073_175_102_230_014_157_181_045_068_134}{129_062_090_255_028_214_236_133_035_015_072_011_115_177}{231_015_237_066_073_033_096_100_247_114_166_127_181_077}{120_048_048_147_189_016_187_190_159_191_032_070_234_040}{081_069_145_160_233_163_084_012_149_217_148_091_091_238}{210_255_086_237_055_070_035_128_043_203_205_077_026_190}{093_217_239_195_235_231_112_141_023_148_220_166_176_204}{188_012_229_234_233_091_248_009_017_132_061_211_226_202}{232_231_037_111_246_177_231_236_150_114_134_093_053_165}{140_085_140_253_025_153_224_249_188_161_055_000_073_186}{037_236_004_229_079_157_169_229_111_107_080_137_214_083}{022_166_164_111_162_226_184_134_134_220_082_109_111_046}{189_055_035_184_132_204_197_065_102_046_183_248_104_034}{132_212_044_130_084_203_035_073_008_237_084_164_049_006}{232_207_177_124_113_163_190_146_253_126_182_143_033_081}{110_129_200_111_134_003_184_008_165_248_118_096_090_178}\",\"id\":1,\"parentId\":0},\"energy\":{\"type\":\"life_form_energy\",\"data\":{\"initialValue\":10000.0,\"value\":9999.999418304764,\"maxForAge\":49915.000000000124,\"decay\":1.6666666666666667}},\"mouth\":{\"directionAngle\":-51.0},\"target\":\"(0.0, 0.0)\",\"age\":51,\"nextEggAge\":0}";
	
	private final List<NarjilloReader> readThreads = new LinkedList<>();

	@Test
	public void narjillosTickingIsDeterministic() {
		// Create the sample narjillo.
		DNA sampleDna = new DNA(new NumGen(1234).nextSerial(), SAMPLE_DNA_DOCUMENT, DNA.NO_PARENT);
		Narjillo narjillo = new Narjillo(sampleDna, Vector.cartesian(100, 200), 90, new LifeFormEnergy(10000, 30000));

		// Start a few reading threads to make things more interesting.
		// Then tick the narjillo for a while, and stop the readers.
		startReadThreads(narjillo);
		for (int i = 0; i < 50; i++)
			narjillo.tick();
		stopReadThreads();

		// Tick one last time to ensure that any cached value is correctly
		// updated at the end of the tick, even when no readers are around.
		narjillo.tick();

		String currentState = JSON.toJson(narjillo, Narjillo.class);

		if (PRINT_NEW_EXPECTED_STATE_DECLARATION)
			System.out.println("private final static String EXPECTED_STATE = \"" + currentState.replace("\"", "\\\"") + "\";");

		// Check that the narjillo's state is exactly what we expect. We use
		// JSON to make the comparison easier, and also to make sure that we're
		// not missing anything in the narjillo's state.
		assertEquals(EXPECTED_STATE, currentState);
	}

	private void startReadThreads(Narjillo narjillo) {
		for (int i = 0; i < 20; i++) {
			NarjilloReader readThread = new NarjilloReader(narjillo);
			readThreads.add(readThread);
			readThread.start();
		}
	}

	private void stopReadThreads() {
		for (NarjilloReader narjilloReader : readThreads) {
			narjilloReader.stopped = true;
			while (narjilloReader.isAlive())
				try {
					Thread.sleep(10);
				} catch (InterruptedException ignored) {
				}
		}
	}

	// Reads stuff from a Narjillo to check that read accesses don't
	// break deterministic behavior. I tried to call all interesting
	// read methods here.
	private static class NarjilloReader extends Thread {

		private final Narjillo narjillo;

		volatile boolean stopped = false;

		NarjilloReader(Narjillo narjillo) {
			this.narjillo = narjillo;
		}

		@Override
		public void run() {
			while (!stopped) {
				narjillo.getCenterOfMass();
				narjillo.getAge();
				narjillo.getDNA();
				narjillo.getEnergy().getValue();
				narjillo.getMouth().getDirection();
				narjillo.getNeckLocation();
				narjillo.getPosition();
				narjillo.getTarget();

				narjillo.getBody().getAdultMass();
				narjillo.getBody().getAngle();
				narjillo.getBody().getMass();
				narjillo.getBody().getRadius();

				for (Organ organ : narjillo.getOrgans()) {
					organ.getAbsoluteAngle();
					organ.getAdultLength();
					organ.getAdultMass();
					organ.getAdultThickness();
					organ.getCenterOfMass();
					organ.getFiber();
					organ.getEndPoint();
					organ.getLength();
					organ.getMass();
					organ.toSegment();
					organ.getStartPoint();
					organ.getThickness();
				}
			}
		}
	}
}