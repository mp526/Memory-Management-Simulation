/**
 * WorstFitMemManager.java
 *
 * @author Marco Piazzi: mp526
 *
 * Concrete class to represent a MemManager
 * using a worst fit policy.
 * 
 */
package com.memorymanagement;

public class WorstFitMemManager extends MemManager {

	public WorstFitMemManager(int size) {
		super(size);
	}

	@Override
	protected int findSpace(int size) {
		int index 		= 0; // returned index
		int block 		= 0; // biggest block 
		int newBlock 	= 0; // new block
		// loop through memory
		for (int i = 0; i < memory.length; i++) {
			if (memory[i] == '.') {	// if memory is free
				newBlock = countFreeSpacesAt(i); // set newBlock
				// compare newBlock to biggest known block
				if (newBlock > block) {
					block = newBlock; // set block
					index = i; // set index to block index
				}
				i += newBlock; // skip over rest of newBlock
			}
		}
		return index;
	}
}
