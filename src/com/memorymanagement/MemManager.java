/**
 * MemManager.java
 *
 * @author Marco Piazzi: mp526
 *
 * Abstract class MemManager representing the current 
 * state of memory and methods to grant access to it
 * 
 */
package com.memorymanagement;

public abstract class MemManager {

	volatile boolean changed; // change in memory
	volatile int largestSpace; // largest free block in memory
	char[] memory; // memory
	private int size; // memory size
	
	// Constructor
	public MemManager(int size) {
		memory = new char[size];
		for (int i = 0; i < size; i++) {
			memory[i] = '.'; // populate memory
		}
		this.largestSpace = size;
		this.size = size;
		this.changed = true;
	}
	
	public boolean isChanged() {
		return changed;
	}
	
	public int getSize() {
		return size;
	}
	
	protected abstract int findSpace(int size);
	
	synchronized int countFreeSpacesAt(int pos) {
		int count = 0;
		for (int i = pos; i < memory.length; i++) {
			if (memory[i] == '.') {	// if memory is free
				count++; // increase count
			} else {
				break;
			}
		}
		return count;
	}
	
	/*
	 * Method for finding the largest space in memory
	 */
	private int findLargestSpace() {
		int max = 0;
		int count = 0;
		for (int i=0; i < memory.length; i++) {
			if (memory[i] == '.') {
				count++;
			} 
			else {
				count = 0;
			}
			// setting the max
			if (count > max) {
				max = count;
			}
		}
		return max;
	}
	
	public synchronized void allocate(Process p) 
			throws InterruptedException {
		while (largestSpace < p.getSize()) { // no space in memory for process
			this.wait();
		}
		int addr = findSpace(p.getSize()); // set index
		for (int i = addr; i < (addr + p.getSize()); i++) {
			memory[i] = p.getId(); // replace free memory with process
		}
		changed = true;
		p.setAddress(addr);
		largestSpace = findLargestSpace();
	}
	
	public synchronized void free(Process p)
			throws InterruptedException {
		int addr = p.getAddress();
		for (int i = addr; i < (addr + p.getSize()); i++) {
			memory[i] = '.'; // free memory
		}
		changed = true;
		p.setAddress(-1);
		largestSpace = findLargestSpace();
		this.notifyAll(); // signal to other threads that memory is freed up
	}
	
	@Override
	public String toString() {
		String returned = "";
		int row = 0;
		String padding = "  "; // initialized at 2 spaces
		for (int i = 0; i < memory.length; i+=20) {
			returned += padding + row + "|";
			int count = 0;
			while (count < 20) {
				returned += memory[i+count];
				count++;
			}
			returned += "|\n";
			row += 20;
			if (row > 9 && row < 100) {
				padding = " "; // padding set to 1 space
			}
			else if (row > 99 && row < 1000) {
				padding = ""; // padding set to no space
			}
		}
		returned += " ls: " + largestSpace;
		return returned;
	}
	
}
