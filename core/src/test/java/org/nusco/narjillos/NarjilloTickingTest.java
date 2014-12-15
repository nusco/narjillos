package org.nusco.narjillos;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.creature.body.Organ;
import org.nusco.narjillos.embryogenesis.Embryo;
import org.nusco.narjillos.genomics.DNA;
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

	final static String SAMPLE_DNA_DOCUMENT = "{145_227_116_072_163_201_077_221_217}{060_227_157_252_209_149_056_114_167}{250_253_092_189_010_247_016_214_009}{027_039_203_179_042_042_175_110_008}";
	final static String EXPECTED_STATE = "{\"body\":{\"head\":{\"type\":\"Head\",\"data\":{\"metabolicRate\":1.9176470588235295,\"percentEnergyToChildren\":0.7890625,\"startPoint\":\"(236.3230023995909, 287.01529125721004)\",\"angleToParent\":46.964271313126254,\"children\":[{\"type\":\"BodyPart\",\"data\":{\"angleToParentAtRest\":-39.0,\"orientation\":-1,\"amplitude\":47,\"skewing\":-9,\"currentSkewing\":0.26383532788282055,\"cachedMetabolicRate\":1.9176470588235295,\"angleToParent\":-23.82284582107778,\"children\":[{\"type\":\"BodyPart\",\"data\":{\"angleToParentAtRest\":-61.0,\"orientation\":-1,\"amplitude\":78,\"skewing\":61,\"currentSkewing\":0.23971256956113818,\"cachedMetabolicRate\":1.9176470588235295,\"angleToParent\":-27.29104991804707,\"children\":[{\"type\":\"BodyPart\",\"data\":{\"angleToParentAtRest\":26.0,\"orientation\":1,\"amplitude\":14,\"skewing\":-12,\"currentSkewing\":0.35178043717709406,\"cachedMetabolicRate\":1.9176470588235295,\"angleToParent\":14.237210443356638,\"children\":[],\"nerve\":{\"type\":\"Delay\",\"data\":{\"delay\":5,\"buffer\":[-0.7988455344601881,-0.7207931106757792,-0.6321645463662985,-0.5344998672707689,-0.4290927886204076]}},\"adultLength\":203.0,\"adultThickness\":35,\"adultMass\":7105.0,\"color\":{\"colorByte\":9},\"length\":44.99999999999968,\"thickness\":35.0,\"cachedAbsoluteAngle\":10.087586017358042,\"cachedStartPoint\":\"(304.45391999129413, 305.276149659845)\",\"cachedVector\":\"(44.30401975112098, 7.876037653887234)\",\"cachedEndPoint\":\"(348.7579397424151, 313.1521873137322)\",\"cachedMass\":1574.9999999999889,\"cachedCenterOfMass\":\"(326.60592986685464, 309.2141684867886)\",\"cachedPositionInSpace\":{\"startPoint\":\"(304.45391999129413, 305.276149659845)\",\"vector\":\"(44.30401975112098, 7.876037653887234)\"}}}],\"nerve\":{\"type\":\"Delay\",\"data\":{\"delay\":1,\"buffer\":[-0.31730465640509364]}},\"adultLength\":92.0,\"adultThickness\":37,\"adultMass\":3404.0,\"color\":{\"colorByte\":1},\"length\":35.00000000000167,\"thickness\":31.000000000002046,\"cachedAbsoluteAngle\":-4.149624425998596,\"cachedStartPoint\":\"(269.5456896465046, 307.8029316757452)\",\"cachedVector\":\"(34.90823034478949, -2.5267820159002152)\",\"cachedEndPoint\":\"(304.45391999129413, 305.276149659845)\",\"cachedMass\":1085.0000000001235,\"cachedCenterOfMass\":\"(286.99980481889935, 306.53954066779505)\",\"cachedPositionInSpace\":{\"startPoint\":\"(269.5456896465046, 307.8029316757452)\",\"vector\":\"(34.90823034478949, -2.5267820159002152)\"}}},{\"type\":\"BodyPart\",\"data\":{\"angleToParentAtRest\":61.0,\"orientation\":1,\"amplitude\":78,\"skewing\":61,\"currentSkewing\":0.23971256956113818,\"cachedMetabolicRate\":1.9176470588235295,\"angleToParent\":27.770475057169346,\"children\":[{\"type\":\"BodyPart\",\"data\":{\"angleToParentAtRest\":-26.0,\"orientation\":-1,\"amplitude\":14,\"skewing\":-12,\"currentSkewing\":0.35178043717709406,\"cachedMetabolicRate\":1.9176470588235295,\"angleToParent\":-13.53364956900245,\"children\":[],\"nerve\":{\"type\":\"Delay\",\"data\":{\"delay\":5,\"buffer\":[-0.7988455344601881,-0.7207931106757792,-0.6321645463662985,-0.5344998672707689,-0.4290927886204076]}},\"adultLength\":203.0,\"adultThickness\":35,\"adultMass\":7105.0,\"color\":{\"colorByte\":9},\"length\":44.99999999999968,\"thickness\":35.0,\"cachedAbsoluteAngle\":37.37825098021537,\"cachedStartPoint\":\"(291.60986036652366, 334.96840808863357)\",\"cachedVector\":\"(35.75819638334635, 27.313191088285897)\",\"cachedEndPoint\":\"(327.36805674987, 362.2815991769195)\",\"cachedMass\":1574.9999999999889,\"cachedCenterOfMass\":\"(309.4889585581968, 348.6250036327765)\",\"cachedPositionInSpace\":{\"startPoint\":\"(291.60986036652366, 334.96840808863357)\",\"vector\":\"(35.75819638334635, 27.313191088285897)\"}}}],\"nerve\":{\"type\":\"Delay\",\"data\":{\"delay\":1,\"buffer\":[-0.31730465640509364]}},\"adultLength\":92.0,\"adultThickness\":37,\"adultMass\":3404.0,\"color\":{\"colorByte\":1},\"length\":35.00000000000167,\"thickness\":31.000000000002046,\"cachedAbsoluteAngle\":50.91190054921782,\"cachedStartPoint\":\"(269.5456896465046, 307.8029316757452)\",\"cachedVector\":\"(22.064170720019035, 27.16547641288837)\",\"cachedEndPoint\":\"(291.60986036652366, 334.96840808863357)\",\"cachedMass\":1085.0000000001235,\"cachedCenterOfMass\":\"(280.5777750065141, 321.38566988218935)\",\"cachedPositionInSpace\":{\"startPoint\":\"(269.5456896465046, 307.8029316757452)\",\"vector\":\"(22.064170720019035, 27.16547641288837)\"}}}],\"nerve\":{\"type\":\"Delay\",\"data\":{\"delay\":25,\"buffer\":[-0.20107792114596118,-0.08193850863003994,0.03838780908751989,0.15815806725448098,0.2756373558169942,0.3892847213961903,0.4971254071004318,0.5977650741210204,0.6898722853831578,0.7718465695580186,0.8427343812360372,0.9013039611597154,0.9468178682667919,0.978652704312062,0.9962553015071346,0.9994209020316777,0.962407846499129,0.869753437661449,0.7269343295366317,0.5420015903703262,0.3257331736204649,0.09063258019777899,-0.149362770623536,-0.3810703763502854,-0.5907464997675891]}},\"adultLength\":157.0,\"adultThickness\":50,\"adultMass\":7850.0,\"color\":{\"colorByte\":129},\"length\":25.000000000001172,\"thickness\":21.000000000000483,\"cachedAbsoluteAngle\":23.141425492048473,\"cachedStartPoint\":\"(246.55872043650334, 297.97845223855484)\",\"cachedVector\":\"(22.98696921000127, 9.824479437190327)\",\"cachedEndPoint\":\"(269.5456896465046, 307.8029316757452)\",\"cachedMass\":525.0000000000367,\"cachedCenterOfMass\":\"(258.05220504150395, 302.89069195715)\",\"cachedPositionInSpace\":{\"startPoint\":\"(246.55872043650334, 297.97845223855484)\",\"vector\":\"(22.98696921000127, 9.824479437190327)\"}}}],\"nerve\":{\"type\":\"Wave\",\"data\":{\"frequency\":0.019176470588235295,\"angle\":230.0188235294051}},\"adultLength\":116.0,\"adultThickness\":15,\"adultMass\":1740.0,\"color\":{\"colorByte\":217},\"length\":14.999999999999787,\"thickness\":10.99999999999981,\"cachedAbsoluteAngle\":46.964271313126254,\"cachedStartPoint\":\"(236.3230023995909, 287.01529125721004)\",\"cachedVector\":\"(10.235718036912441, 10.96316098134481)\",\"cachedEndPoint\":\"(246.55872043650334, 297.97845223855484)\",\"cachedMass\":164.9999999999948,\"cachedCenterOfMass\":\"(241.44086141804712, 292.49687174788244)\",\"cachedPositionInSpace\":{\"startPoint\":\"(236.3230023995909, 287.01529125721004)\",\"vector\":\"(10.235718036912441, 10.96316098134481)\"}}},\"metabolicConsumption\":2.6555410426479704,\"adultMass\":30608.0,\"mass\":6002.062300000255},\"dna\":{\"genes\":\"{145_227_116_072_163_201_077_221_217}{060_227_157_252_209_149_056_114_167}{250_253_092_189_010_247_016_214_009}{027_039_203_179_042_042_175_110_008}\",\"id\":1},\"target\":\"(0.0, 0.0)\",\"energy\":{\"initialValue\":10000.0,\"value\":9998.552557464014,\"maxForAge\":48333.33333333576,\"decay\":1.6666666666666667},\"mouth\":{\"LATERAL_VIEWFIELD\":135.0,\"directionAngle\":-130.0},\"age\":1000}";

	List<NarjilloReader> readingThreads = new LinkedList<>();

	@Test
	public void narjillosTickingIsDeterministic() {
		// Create DNA with an id of 1.
		DNA.setSerial(0);
		DNA sampleDNA = new DNA(SAMPLE_DNA_DOCUMENT);

		// Create the sample narjillo.
		Narjillo narjillo = new Narjillo(sampleDNA, new Embryo(sampleDNA).develop(), Vector.cartesian(100, 200), 10_000);

		// Start a few reading threads to make things more interesting.
		// Then tick the narjillo for a while, and stop the readers.
		startReadThreads(narjillo);
		for (int i = 0; i < 999; i++)
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
					organ.getColor();
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