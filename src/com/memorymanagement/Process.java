/**
 * Process.java
 *
 * @author Marco Piazzi: mp526
 *
 * Class to represent a Process
 * 
 */
package com.memorymanagement;

public class Process implements Runnable {
	
	char pid;
	int delay;
	int size;
	int runtime;
	int memAddr = -1;	// no address yet
	MemManager manager;
	
	//Constructor
	public Process(MemManager man, int delay, char id, int size, int runtime) {
		
		this.manager = man;
		this.delay = delay;
		this.pid = id;
		this.size = size;
		this.runtime = runtime;
	}

	public int getAddress() {
		return memAddr;
	}
	
	public char getId() {
		return pid;
	}
	
	public int getDelay() {
		return delay;
	}
	
	public int getSize() {
		return size;
	}
	
	public void setAddress(int addr) {
		memAddr = addr;
	}
	
	@Override
	public String toString() {
		String returned = "";
		if (memAddr > -1) {	// if an address is allocated
			returned = pid + ":" + memAddr + "+" + size;
		} else {
			returned = pid + ":" + "U+" + size;
		}
		return returned;
	}
	
	@Override
	public void run() {
		try {
			// process wait
			System.out.println(this.toString() + " waiting to run.");
			manager.allocate(this);
			// proccess run
			System.out.println(this.toString() + " running.");
			Thread.sleep(100*this.runtime);
			// process finish
			System.out.println(this.toString() + " has finished.");
			manager.free(this);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
