package org.nusco.narjillos.genomics;

/**
 * Observes DNA getting created and destroyed.
 */
public interface DNAObserver {

	public void created(DNA newDna, DNA parent);
	public void removed(DNA dna);

	DNAObserver NULL = new DNAObserver() {
		
		@Override
		public void created(DNA newDNA, DNA parent) {
		}

		@Override
		public void removed(DNA dna) {
		}
	};
}
