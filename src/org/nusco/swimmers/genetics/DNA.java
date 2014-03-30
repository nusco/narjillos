package org.nusco.swimmers.genetics;

import org.nusco.swimmers.body.HeadPart;
import org.nusco.swimmers.body.Part;
import org.nusco.swimmers.body.Swimmer;

public class DNA {

	private static final byte NUMBER_MASK_1 = (byte)0b00;
	private static final byte NUMBER_MASK_2 = (byte)0b10;
	private static final byte PART_TERMINATOR = (byte)0b01;
	private static final byte NULL = (byte)0b11;
	
	private byte[] genes;

	public DNA(byte... genes) {
		this.genes = genes;
	}

	public static DNA createRandomDNA() {
		int[] genes =  new int[]{
									50, 10, PART_TERMINATOR,
									120, 12, 45, 0, PART_TERMINATOR,
									120, 12, -45, 0, PART_TERMINATOR,
									70, 15, 30, PART_TERMINATOR,
									70, 15, -30, PART_TERMINATOR,
									30, 15, 30, PART_TERMINATOR,
									30, 15, -30, PART_TERMINATOR
								};
		 return new DNA(toByteArray(genes));
	}

	private static byte[] toByteArray(int[] ints) {
		byte[] result = new byte[ints.length];
		for(int i = 0; i < ints.length; i++)
			result[i] = (byte)ints[i];
		return result;
	}

	public Swimmer toPhenotype() {
		Part head = new HeadPart(genes[0], genes[1]);
		int index = 2;
		while(index < genes.length - 1) {
			
		}
		Part child_1 = head.sproutChild(120, 12, 45);
		Part child_2 = head.sproutChild(120, 12, -45);
		child_1.sproutChild(70, 15, 30);
		child_1.sproutChild(70, 15, -30);
		child_2.sproutChild(30, 15, 30);
		child_2.sproutChild(30, 15, -30);
		return new Swimmer(head);
	}

}
