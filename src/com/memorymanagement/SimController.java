/**
 * SimController.java
 *
 * @author Marco Piazzi: mp526
 *
 * Controller Class for the simulation.
 * 
 * This should be invoked with 3 command line arguments for:
 * 
 * 		The mode of operation: f for first fit; b for best fit; or w for worst fit.
 * 		The total memory size for the MemManager
 * 		Filename of simulated Process data. 
 * 
 */
package com.memorymanagement;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SimController implements Runnable {
	
	static MemManager manager;
	static ThreadPoolExecutor executor;
	
	public SimController() {}
	
	public static void main(String[] args) {
		if (args.length != 3) {
			System.err.println("Usage: SimController <policy> <size> <file>");
			System.exit(1);
		}
		
		char policy = args[0].charAt(0);
		int memorySize = Integer.parseInt(args[1]);	
		String fname = args[2];
		
		if (policy == 'f') {
			manager = new FirstFitMemManager(memorySize);
			System.out.println("Policy: FIRST fit");
		} 
		else if (policy == 'b') {
			manager = new BestFitMemManager(memorySize);
			System.out.println("Policy: BEST fit");
		}
		else if (policy == 'w') {
			manager = new WorstFitMemManager(memorySize);
			System.out.println("Policy: WORST fit");
		}
		else {
			System.err.println("Policy options: f, b, w");
			System.exit(1);
		}
		
		// create ThreadPoolExecutor
		executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
		Thread watcher = new Thread(new SimController());
		Thread qhThread = new Thread(new QueueHandler(executor, manager, fname));
		// start threads
		try {
			watcher.start();
			qhThread.start();
			// wait for threads to finish
			watcher.join();
			qhThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// wait for ThreadPoolExecutor to finish
		// terminate executor
		try {
			executor.shutdown();
			executor.awaitTermination(20L, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			System.out.println(e);
		}
		System.out.println("All threads have terminated");
		System.exit(1);
	}

	@Override
	public void run() {
		// give threads some time to start
		try {
			System.out.println(manager.toString());
			Thread.sleep(50);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		while (true) {
			try {	
				Thread.sleep(1000);	// 1 second intervals
				if (manager.isChanged()) { // if change in memory
					System.out.println(manager.toString());	// print state of memory
					manager.changed = false; // set changed to false
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// check if threads are finished
			if (executor.getActiveCount() < 1) {
				break;
			}
		}
		// end while-loop
	}

}
