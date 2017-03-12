/**
 * FirstFitMemManager.java
 *
 * @author Marco Piazzi: mp526
 *
 * Concrete class to represent a MemManager
 * using a first fit policy.
 * 
 */
package com.memorymanagement;

public class FirstFitMemManager extends MemManager {

	public FirstFitMemManager(int size) {
		super(size);
	}

	@Override
	protected int findSpace(int size) {
		int index = 0; // returned index
		int block = 0; // memory block
		// loop through memory
		for (int i = 0; i < memory.length; i++) {
			if (memory[i] == '.') {	// if memory is free
				block = countFreeSpacesAt(i);
				if (block >= size) { // if block can fit process
					index = i; // set index to block index
					break;
				}
				i += block; // skip over rest of block
			}
		}
		return index;
	}
}
