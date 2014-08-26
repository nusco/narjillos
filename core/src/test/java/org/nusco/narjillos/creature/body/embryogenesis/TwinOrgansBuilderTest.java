package org.nusco.narjillos.creature.body.embryogenesis;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.nusco.narjillos.creature.body.BodyPart;
import org.nusco.narjillos.creature.body.Head;
import org.nusco.narjillos.creature.body.Organ;
import org.nusco.narjillos.creature.body.embryogenesis.TwinOrgansBuilder;
import org.nusco.narjillos.creature.genetics.Chromosome;
import org.nusco.narjillos.shared.utilities.ColorByte;

public class TwinOrgansBuilderTest {
	
	private static final int MIRRORING = 1;
	private static final int NOT_MIRRORING = 0;

	Chromosome notMirroring1 = new Chromosome(NOT_MIRRORING, 60, 70, 80, 90, 100);
	Chromosome notMirroring2 = new Chromosome(NOT_MIRRORING, 61, 71, 81, 91, 101);
	Chromosome mirroring1 = new Chromosome(MIRRORING, 62, 72, 82, 92, 102);
	Chromosome mirroring2 = new Chromosome(MIRRORING, 63, 73, 83, 93, 103);

	Organ parent = new Head(10, 10, new ColorByte(0), 10);

	@Test
	public void buildsRegularSegmentIfNeitherGenesIsMirroring() {
		List<Organ> bodyParts = new TwinOrgansBuilder(notMirroring1, notMirroring2).buildChildren(parent);
		
		assertEquals(60, bodyParts.get(0).getLength());
		assertEquals(61, bodyParts.get(1).getLength());
	}

	@Test
	public void buildsMirrorSegmentOfSecondSegmentIfBothGenesAreMirroring() {
		List<Organ> bodyParts = new TwinOrgansBuilder(mirroring1, mirroring2).buildChildren(parent);
		
		assertEquals(63, bodyParts.get(0).getLength());
		assertEqualOrgans(bodyParts.get(0), bodyParts.get(1));
	}

	@Test
	public void buildsMirrorSegmentIfFirstSegmentGenesAreMirroring() {
		TwinOrgansBuilder builder = new TwinOrgansBuilder(mirroring1, notMirroring2);
		List<Organ> bodyParts = builder.buildChildren(parent);
		
		assertEqualOrgans(bodyParts.get(0), bodyParts.get(1));
	}

	@Test
	public void buildsMirrorSegmentIfSecondSegmentGenesAreMirroring() {
		TwinOrgansBuilder builder = new TwinOrgansBuilder(notMirroring1, mirroring2);
		List<Organ> bodyParts = builder.buildChildren(parent);
		
		assertEqualOrgans(bodyParts.get(0), bodyParts.get(1));
	}

	private void assertEqualOrgans(BodyPart organ1, BodyPart organ2) {
		assertEquals(organ1.getLength(), organ2.getLength());
		assertEquals(organ1.getThickness(), organ2.getThickness());
		assertEquals(organ1.getColor(), organ2.getColor());
		assertEquals(-organ1.getAbsoluteAngle(), organ2.getAbsoluteAngle(), 0);
	}
}
