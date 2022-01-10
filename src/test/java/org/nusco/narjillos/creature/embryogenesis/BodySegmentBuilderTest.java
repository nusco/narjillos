package org.nusco.narjillos.creature.embryogenesis;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import org.junit.jupiter.api.Test;
import org.nusco.narjillos.creature.body.BodyPart;
import org.nusco.narjillos.creature.body.Fiber;
import org.nusco.narjillos.creature.body.Head;
import org.nusco.narjillos.creature.body.HeadParameters;
import org.nusco.narjillos.genomics.Chromosome;

public class BodySegmentBuilderTest extends ConcreteOrganBuilderTest {

	@Override
	protected BodySegmentBuilder getConcreteOrganBuilder(Chromosome chromosome) {
		return new BodySegmentBuilder(chromosome);
	}

	@Test
	public void decodesAFiberShift() {
		var chromosome1 = new Chromosome(0, 0, 0, 0, 0, 255, 1);
		assertThat(getConcreteOrganBuilder(chromosome1).getRedShift()).isEqualTo(-255);
		assertThat(getConcreteOrganBuilder(chromosome1).getGreenShift()).isEqualTo(255);
		assertThat(getConcreteOrganBuilder(chromosome1).getBlueShift()).isEqualTo(-253);

		var chromosome2 = new Chromosome(0, 0, 0, 0, 126, 127, 128);
		assertThat(getConcreteOrganBuilder(chromosome2).getRedShift()).isEqualTo(-3);
		assertThat(getConcreteOrganBuilder(chromosome2).getGreenShift()).isEqualTo(-1);
		assertThat(getConcreteOrganBuilder(chromosome2).getBlueShift()).isEqualTo(1);
	}

	@Test
	public void decodesAnAngleToTheParentBetweenMinus70And70() {
		assertThat(getConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 0)).getAngleToParent(1)).isEqualTo(-70);
		assertThat(getConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 1)).getAngleToParent(1)).isEqualTo(-69);
		assertThat(getConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 2)).getAngleToParent(1)).isEqualTo(-68);
		assertThat(getConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 128)).getAngleToParent(1)).isEqualTo(0);
		assertThat(getConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 129)).getAngleToParent(1)).isEqualTo(1);
		assertThat(getConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 255)).getAngleToParent(1)).isEqualTo(70);
	}

	@Test
	public void mirrorsTheAngleToTheParentIfTheMirrorSignIsNegative() {
		assertThat(getConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 0)).getAngleToParent(-1)).isEqualTo(70);
		assertThat(getConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 1)).getAngleToParent(-1)).isEqualTo(69);
		assertThat(getConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 2)).getAngleToParent(-1)).isEqualTo(68);
		assertThat(getConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 128)).getAngleToParent(-1)).isEqualTo(0);
		assertThat(getConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 129)).getAngleToParent(-1)).isEqualTo(-1);
		assertThat(getConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 255)).getAngleToParent(-1)).isEqualTo(-70);
	}

	@Test
	public void decodesADelayBetween0And30() {
		assertThat(getConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 0, 0)).getDelay()).isEqualTo(0);
		assertThat(getConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 0, 8)).getDelay()).isEqualTo(0);
		assertThat(getConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 0, 9)).getDelay()).isEqualTo(1);
		assertThat(getConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 0, 255)).getDelay()).isEqualTo(30);
	}

	@Test
	public void decodesAnAmplitudeBetween1And100() {
		assertThat(getConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 0, 0, 1)).getAmplitude()).isEqualTo(1);
		assertThat(getConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 0, 0, 5)).getAmplitude()).isEqualTo(2);
		assertThat(getConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 0, 0, 255)).getAmplitude()).isEqualTo(80);
	}

	@Test
	public void decodesASkewingBetweenMinus90And90() {
		assertThat(getConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)).getSkewing()).isEqualTo(-90);
		assertThat(getConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 63)).getSkewing()).isEqualTo(-45);
		assertThat(getConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 127)).getSkewing()).isEqualTo(0);
		assertThat(getConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 255)).getSkewing()).isEqualTo(90);
	}

	@Test
	public void buildsABodySegment() {
		int controlFlowGene = 0b00000000;
		int controlLoopGene = 0b00000000;
		int lengthGene = 30;
		int thicknessGene = 126;
		int redShiftGene = 126;
		int greenShiftGene = 127;
		int blueShiftGene = 128;
		int angleToParentGene = 81;
		int delayGene = 90;
		int amplitudeGene = 107;
		int skewingGene = 150;

		var chromosome = new Chromosome(controlFlowGene, controlLoopGene, lengthGene, thicknessGene, redShiftGene,
			greenShiftGene, blueShiftGene, angleToParentGene, delayGene, amplitudeGene, skewingGene);
		BodySegmentBuilder builder = getConcreteOrganBuilder(chromosome);

		var headParameters = new HeadParameters();
		headParameters.setRed(50);
		headParameters.setGreen(60);
		headParameters.setBlue(70);
		var head = new Head(headParameters);

		var bodyPart = (BodyPart) builder.buildOrgan(head, 1);

		head.update();
		bodyPart.update();

		assertThat(bodyPart.getFiber()).isEqualTo(new Fiber(47, 59, 71));

		assertThat(bodyPart.getAbsoluteAngle()).isEqualTo(-25.0);
		assertThat(bodyPart.getDelay()).isEqualTo(10);
		assertThat(bodyPart.getAmplitude()).isEqualTo(34);
		assertThat(bodyPart.getSkewing()).isEqualTo(16);

		head.growToAdultFormWithChildren();
		bodyPart.growToAdultFormWithChildren();

		assertThat(bodyPart.getLength()).isEqualTo(30.0);
		assertThat(bodyPart.getThickness()).isEqualTo(25.0, within(0.01));
	}
}
