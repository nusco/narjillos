package org.nusco.narjillos;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.nusco.narjillos.core.physics.Vector;
import org.nusco.narjillos.core.things.LifeFormEnergy;
import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.creature.body.Organ;
import org.nusco.narjillos.ecosystem.chemistry.Atmosphere;
import org.nusco.narjillos.genomics.DNA;
import org.nusco.narjillos.genomics.GenePool;
import org.nusco.narjillos.genomics.GenePoolWithHistory;
import org.nusco.narjillos.serializer.JSON;

/**
 * The code that moves Narjillos, and in particular the code in the Organ
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

	final static String SAMPLE_DNA_DOCUMENT = "{145_227_116_072_163_201_077_221_217_001_002_003}{060_227_157_252_209_149_056_114_167_001_002_003}{250_253_092_189_010_247_016_214_009_001_002_003}{027_039_203_179_042_042_175_110_008_001_002_003}";
	final static String EXPECTED_STATE = "{\"body\":{\"head\":{\"type\":\"Head\",\"data\":{\"metabolicRate\":1.9176470588235295,\"byproduct\":\"NITROGEN\",\"energyToChildren\":30100.0,\"eggVelocity\":77,\"eggInterval\":22100,\"startPoint\":\"(100.0, 199.74500000000012)\",\"angleToParent\":90.0,\"children\":[],\"nerve\":{\"type\":\"Wave\",\"data\":{\"frequency\":0.019176470588235295,\"angle\":81.8258823529409}},\"adultLength\":116.0,\"adultThickness\":15,\"adultMass\":1740.0,\"fiber\":\"(217, 1, 2)\",\"length\":5.509999999999989,\"thickness\":1.5100000000000005,\"cachedAbsoluteAngle\":90.0,\"cachedStartPoint\":\"(100.0, 199.74500000000012)\",\"cachedVector\":\"(-0.0, 5.509999999999989)\",\"cachedEndPoint\":\"(100.0, 205.2550000000001)\",\"cachedMass\":8.320099999999986,\"cachedCenterOfMass\":\"(100.0, 202.5000000000001)\",\"cachedPositionInSpace\":{\"startPoint\":\"(100.0, 199.74500000000012)\",\"vector\":\"(-0.0, 5.509999999999989)\"}}},\"metabolicConsumption\":2.6555410426479704,\"adultMass\":1740.0,\"mass\":8.320099999999986,\"redMass\":8.206644090909077,\"greenMass\":0.0378186363636363,\"blueMass\":0.0756372727272726},\"dna\":{\"genes\":\"{145_227_116_072_163_201_077_221_217_001_002_003}{060_227_157_252_209_149_056_114_167_001_002_003}{250_253_092_189_010_247_016_214_009_001_002_003}{027_039_203_179_042_042_175_110_008_001_002_003}\",\"id\":1},\"energy\":{\"type\":\"life_form_energy\",\"data\":{\"initialValue\":10000.0,\"value\":10000.0,\"maxForAge\":49915.000000000124,\"decay\":1.6666666666666667}},\"mouth\":{\"directionAngle\":-51.0},\"target\":\"(0.0, 0.0)\",\"age\":51,\"nextEggAge\":0}";
	
	List<NarjilloReader> readingThreads = new LinkedList<>();

	@Test
	public void narjillosTickingIsDeterministic() {
		GenePool genePool = new GenePoolWithHistory();
		DNA sampleDNA = genePool.createDNA(SAMPLE_DNA_DOCUMENT);
		Atmosphere atmosphere = new Atmosphere();
		
		// Create the sample narjillo.
		Narjillo narjillo = new Narjillo(sampleDNA, Vector.cartesian(100, 200), 90, new LifeFormEnergy(10000, 30000));

		// Start a few reading threads to make things more interesting.
		// Then tick the narjillo for a while, and stop the readers.
		startReadThreads(narjillo);
		for (int i = 0; i < 50; i++)
			narjillo.tick(atmosphere);
		stopReadThreads();

		// Tick one last time to ensure that any cached value is correctly
		// updated at the end of the tick, even when no readers are around.
		narjillo.tick(atmosphere);

		String currentState = JSON.toJson(narjillo, Narjillo.class);

		// Enable this line to get the new expected value whenever I need to
		// update this test (that is, whenever I make changes that cause this
		// test to break, as long as I was expecting it to happen).
		//System.out.println(currentState);

		// Check that the narjillo's state is exactly what we expect. We use
		// JSON to make the comparison easier, and also to make sure that we're
		// not missing anything in the narjillo's state.
		assertEquals(EXPECTED_STATE, currentState);
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
					organ.getPositionInSpace();
					organ.getStartPoint();
					organ.getThickness();
				}
			}
		}
	}
}