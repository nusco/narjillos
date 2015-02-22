package org.nusco.narjillos;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.creature.body.Organ;
import org.nusco.narjillos.embryogenesis.Embryo;
import org.nusco.narjillos.genomics.DNA;
import org.nusco.narjillos.genomics.GenePool;
import org.nusco.narjillos.serializer.JSON;
import org.nusco.narjillos.shared.physics.Vector;

/**
 * The code that moves Narjllos, and in particular the code in the Organ
 * hierarchy, is quite complicated. Blame it on difficult, conflicting
 * requirements:
 * 
 * 1) This code must be fast. The system spends most of its time ticking
 * narjillos - so the fastest, the better.
 * 
 * 2) This code must be deterministic. The same ticks should result in exactly
 * the same state of the narjillo, down to the exact values of each geometric
 * property of each organ. (Also see DeterministicExperimentTest).
 * 
 * 3) This code should be thread-safe. In general, only one thread will call
 * stateful methods such as Narjillo#tick() - but multiple threads can read data
 * from the Narjillo, such as the position of its organs in space. In
 * particular, this data is read from the graphics system.
 * 
 * 4) This code should provide a convincing simulation of physics.
 * 
 * Because of (1), we need to optimize a lot. For example, we need to calculate
 * things like the angles of Organs as few times as possible. On the other hand,
 * because of (2) and (3), we need to be careful when using obvious
 * optimizations, such as lazy calculations and caches. An external thread might
 * read data from a Narjillo at any time, triggering lazy calculations in
 * non-predictable order - thus breaking deterministic behavior. Finally, (4) is
 * making the code complicated and hard to follow and test, which makes the
 * previous problems even worse.
 * 
 * That's why I wrote this test. The idea: check that a sample narjillo, when
 * ticked a specific number of times, results in a known state - even as
 * multiple threads are reading its data. If any optimization or refactoring
 * breaks any of the previous constraints, this test will probably go red.
 * 
 * (This test will also break if I make "legitimate" changes, like changing
 * physics or embryology - but in that case, I just need to update the expected
 * result in the test).
 */
public class NarjilloTickingTest {

	final static String SAMPLE_DNA_DOCUMENT = "{145_227_116_072_163_201_077_221_217_001_002}{060_227_157_252_209_149_056_114_167_001_002}{250_253_092_189_010_247_016_214_009_001_002}{027_039_203_179_042_042_175_110_008_001_002}";
	final static String EXPECTED_STATE = "{\"body\":{\"head\":{\"type\":\"Head\",\"data\":{\"metabolicRate\":1.9176470588235295,\"percentEnergyToChildren\":0.7890625,\"startPoint\":\"(103.13107323858429, 194.40306999327356)\",\"angleToParent\":80.9686003029885,\"children\":[{\"type\":\"BodyPart\",\"data\":{\"angleToParentAtRest\":-39.0,\"orientation\":-1,\"amplitude\":47,\"skewing\":-9,\"currentSkewing\":2.3344139120076646,\"cachedMetabolicRate\":1.9176470588235295,\"angleToParent\":7.070049796810831,\"children\":[{\"type\":\"BodyPart\",\"data\":{\"angleToParentAtRest\":-61.0,\"orientation\":-1,\"amplitude\":78,\"skewing\":61,\"currentSkewing\":-9.779999999999992,\"cachedMetabolicRate\":1.9176470588235295,\"angleToParent\":-7.105538444061471,\"children\":[{\"type\":\"BodyPart\",\"data\":{\"angleToParentAtRest\":26.0,\"orientation\":1,\"amplitude\":14,\"skewing\":-12,\"currentSkewing\":3.112551882676886,\"cachedMetabolicRate\":1.9176470588235295,\"angleToParent\":32.572905760217395,\"children\":[],\"nerve\":{\"type\":\"Delay\",\"data\":{\"delay\":5,\"buffer\":[0.008726535498373938,-0.22987988192969674,-0.45554490723353785,-0.6548727250613987,-0.8163392507171605]}},\"adultLength\":203.0,\"adultThickness\":35,\"adultMass\":7105.0,\"fiber\":\"(18, 0, 0)\",\"length\":7.040000000000002,\"thickness\":3.040000000000002,\"cachedAbsoluteAngle\":113.50601741595527,\"cachedStartPoint\":\"(105.23004176487179, 212.30942365830418)\",\"cachedVector\":\"(-2.8071934452338323, 6.455612877657953)\",\"cachedEndPoint\":\"(102.42284831963795, 218.76503653596214)\",\"cachedMass\":21.40160000000002,\"cachedCenterOfMass\":\"(103.82644504225487, 215.53723009713315)\",\"cachedPositionInSpace\":{\"startPoint\":\"(105.23004176487179, 212.30942365830418)\",\"vector\":\"(-2.8071934452338323, 6.455612877657953)\"}}}],\"nerve\":{\"type\":\"Delay\",\"data\":{\"delay\":1,\"buffer\":[-0.9305454443575142]}},\"adultLength\":92.0,\"adultThickness\":37,\"adultMass\":3404.0,\"fiber\":\"(137, 0, 0)\",\"length\":6.530000000000013,\"thickness\":2.5299999999999976,\"cachedAbsoluteAngle\":80.93311165573787,\"cachedStartPoint\":\"(104.20177125908302, 205.861071625849)\",\"cachedVector\":\"(1.028270505788765, 6.448352032455187)\",\"cachedEndPoint\":\"(105.23004176487179, 212.30942365830418)\",\"cachedMass\":16.520900000000015,\"cachedCenterOfMass\":\"(104.7159065119774, 209.0852476420766)\",\"cachedPositionInSpace\":{\"startPoint\":\"(104.20177125908302, 205.861071625849)\",\"vector\":\"(1.028270505788765, 6.448352032455187)\"}}},{\"type\":\"BodyPart\",\"data\":{\"angleToParentAtRest\":61.0,\"orientation\":1,\"amplitude\":78,\"skewing\":61,\"currentSkewing\":-9.779999999999992,\"cachedMetabolicRate\":1.9176470588235295,\"angleToParent\":-12.454461555938513,\"children\":[{\"type\":\"BodyPart\",\"data\":{\"angleToParentAtRest\":-26.0,\"orientation\":-1,\"amplitude\":14,\"skewing\":-12,\"currentSkewing\":3.112551882676886,\"cachedMetabolicRate\":1.9176470588235295,\"angleToParent\":-26.347801994863627,\"children\":[],\"nerve\":{\"type\":\"Delay\",\"data\":{\"delay\":5,\"buffer\":[0.008726535498373938,-0.22987988192969674,-0.45554490723353785,-0.6548727250613987,-0.8163392507171605]}},\"adultLength\":203.0,\"adultThickness\":35,\"adultMass\":7105.0,\"fiber\":\"(18, 0, 0)\",\"length\":7.040000000000002,\"thickness\":3.040000000000002,\"cachedAbsoluteAngle\":49.2363865489972,\"cachedStartPoint\":\"(105.82682009176004, 212.1853524189692)\",\"cachedVector\":\"(4.596359416371086, 5.331653057963344)\",\"cachedEndPoint\":\"(110.42317950813113, 217.51700547693252)\",\"cachedMass\":21.40160000000002,\"cachedCenterOfMass\":\"(108.12499979994558, 214.85117894795087)\",\"cachedPositionInSpace\":{\"startPoint\":\"(105.82682009176004, 212.1853524189692)\",\"vector\":\"(4.596359416371086, 5.331653057963344)\"}}}],\"nerve\":{\"type\":\"Delay\",\"data\":{\"delay\":1,\"buffer\":[-0.9305454443575142]}},\"adultLength\":92.0,\"adultThickness\":37,\"adultMass\":3404.0,\"fiber\":\"(137, 0, 0)\",\"length\":6.530000000000013,\"thickness\":2.5299999999999976,\"cachedAbsoluteAngle\":75.58418854386083,\"cachedStartPoint\":\"(104.20177125908302, 205.861071625849)\",\"cachedVector\":\"(1.625048832677017, 6.3242807931201845)\",\"cachedEndPoint\":\"(105.82682009176004, 212.1853524189692)\",\"cachedMass\":16.520900000000015,\"cachedCenterOfMass\":\"(105.01429567542152, 209.0232120224091)\",\"cachedPositionInSpace\":{\"startPoint\":\"(104.20177125908302, 205.861071625849)\",\"vector\":\"(1.625048832677017, 6.3242807931201845)\"}}}],\"nerve\":{\"type\":\"Delay\",\"data\":{\"delay\":25,\"buffer\":[-0.9910532188724427,-0.9942448326331166,-0.974173386969859,-0.9399907318200738,-0.8921131712669956,-0.831372634341083,-0.7585894019756783,-0.6746889494463316,-0.5811291459785862,-0.47915150311251337,-0.37007106319220795,-0.25578322739461573,-0.1377902906846359,-0.017626912513571766,0.10244531374706607,0.22137829857663763,0.33693093568149135,0.4476030216652872,0.5519369853120859,0.6481199010631379,0.73491459514995,0.8111659010097108,0.8755488623535189,0.9272492216291719,0.9655635052852711]}},\"adultLength\":157.0,\"adultThickness\":50,\"adultMass\":7850.0,\"fiber\":\"(255, 0, 0)\",\"length\":6.019999999999978,\"thickness\":2.020000000000001,\"cachedAbsoluteAngle\":88.03865009979934,\"cachedStartPoint\":\"(103.99587653242698, 199.84462966643582)\",\"cachedVector\":\"(0.20589472665604683, 6.01644195941319)\",\"cachedEndPoint\":\"(104.20177125908302, 205.861071625849)\",\"cachedMass\":12.160399999999962,\"cachedCenterOfMass\":\"(104.098823895755, 202.85285064614243)\",\"cachedPositionInSpace\":{\"startPoint\":\"(103.99587653242698, 199.84462966643582)\",\"vector\":\"(0.20589472665604683, 6.01644195941319)\"}}}],\"nerve\":{\"type\":\"Wave\",\"data\":{\"frequency\":0.019176470588235295,\"angle\":81.8258823529409}},\"adultLength\":116.0,\"adultThickness\":15,\"adultMass\":1740.0,\"fiber\":\"(217, 1, 2)\",\"length\":5.509999999999989,\"thickness\":1.5100000000000005,\"cachedAbsoluteAngle\":80.9686003029885,\"cachedStartPoint\":\"(103.13107323858429, 194.40306999327356)\",\"cachedVector\":\"(0.8648032938426832, 5.441559673162268)\",\"cachedEndPoint\":\"(103.99587653242698, 199.84462966643582)\",\"cachedMass\":8.320099999999986,\"cachedCenterOfMass\":\"(103.56347488550563, 197.1238498298547)\",\"cachedPositionInSpace\":{\"startPoint\":\"(103.13107323858429, 194.40306999327356)\",\"vector\":\"(0.8648032938426832, 5.441559673162268)\"}}},\"metabolicConsumption\":2.6555410426479704,\"adultMass\":30608.0,\"mass\":94.75000000000001},\"dna\":{\"genes\":\"{145_227_116_072_163_201_077_221_217_001_002}{060_227_157_252_209_149_056_114_167_001_002}{250_253_092_189_010_247_016_214_009_001_002}{027_039_203_179_042_042_175_110_008_001_002}\",\"id\":1},\"target\":\"(0.0, 0.0)\",\"energy\":{\"initialValue\":10000.0,\"value\":9999.99993167793,\"maxForAge\":49915.000000000124,\"decay\":1.6666666666666667},\"mouth\":{\"directionAngle\":-51.0},\"age\":51}";

	List<NarjilloReader> readingThreads = new LinkedList<>();

	@Test
	public void narjillosTickingIsDeterministic() {
		GenePool genePool = new GenePool();
		DNA sampleDNA = genePool.createDNA(SAMPLE_DNA_DOCUMENT);

		// Create the sample narjillo.
		Narjillo narjillo = new Narjillo(sampleDNA, new Embryo(sampleDNA).develop(), Vector.cartesian(100, 200), 10_000);

		// Start a few reading threads to make things more interesting.
		// Then tick the narjillo for a while, and stop the readers.
		startReadThreads(narjillo);
		for (int i = 0; i < 50; i++)
			narjillo.tick();
		stopReadThreads();

		// Tick one last time to check that any cached value is correctly
		// updated at the end of the tick, even when no readers are around.
		narjillo.tick();

		// Check that the narjillo's state is exactly what we expect. We use
		// JSON to make the comparison easier, and also to make sure that we're
		// not missing anything in the narjillo's state.
		assertEquals(EXPECTED_STATE, JSON.toJson(narjillo, Narjillo.class));
	}

	private void startReadThreads(Narjillo narjillo) {
		for (int i = 0; i < 20; i++) {
			NarjilloReader readThread = new NarjilloReader(narjillo);
			readingThreads.add(readThread);
			readThread.start();
		}
	}

	private void stopReadThreads() {
		for (NarjilloReader narjilloReader : readingThreads) {
			narjilloReader.stopped = true;
			while (narjilloReader.isAlive())
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
				}
		}
	}

	// Reads stuff from a Narjillo to check that reading doesn't break
	// deterministic
	// behavior. I tried to call all interesting read methods here.
	class NarjilloReader extends Thread {
		private final Narjillo narjillo;
		volatile boolean stopped = false;

		NarjilloReader(Narjillo narjillo) {
			this.narjillo = narjillo;
		}

		@Override
		public void run() {
			while (true && !stopped) {
				narjillo.calculateCenterOfMass();
				narjillo.getAge();
				narjillo.getDNA();
				narjillo.getEnergy().getValue();
				narjillo.getEnergyPercent();
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
					organ.getPositionInSpace();
					organ.getStartPoint();
					organ.getThickness();
				}
			}
		}
	}
}