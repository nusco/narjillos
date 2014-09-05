package org.nusco.narjillos.creature.genetics;

public interface DNAObserver {

	public void created(DNA newDNA, DNA parent);

	DNAObserver NULL = new DNAObserver() {
		
		@Override
		public void created(DNA newDNA, DNA parent) {
		}
	};
}
