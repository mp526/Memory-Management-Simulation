/**
 * BestFitMemManager.java
 *
 * @author Marco Piazzi: mp526
 *
 * Concrete class to represent a MemManager
 * using a best fit policy.
 * 
 */
package com.memorymanagement;

public class BestFitMemManager extends MemManager {

	public BestFitMemManager(int size) {
		super(size);
	}

	@Override
	protected int findSpace(int size) {
		int index 	= 0; // returned index
		int block 	= 0; // memory block
		int diff 	= 0; // difference between a memory block and process size
		int minDiff = memory.length; // smallest possible difference between memory block
									 // and size (initially set memory length)
		// loop through memory
		for (int i = 0; i < memory.length; i++) {
			if (memory[i] == '.') {	// if memory is free
				block = countFreeSpacesAt(i); // set block
				
				if (block >= size) { // if block can hold process
					diff = block - size; // calculate difference
					if (diff < minDiff) { // compare difference
						minDiff = diff;	// set the minimum difference
						index = i; // set index to block index
					}
				}
				i += block;	// skip over rest of block
			}
		}
		return index;
	}
}
