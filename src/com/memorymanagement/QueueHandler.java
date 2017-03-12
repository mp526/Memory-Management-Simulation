/**
 * QueueHandler.java
 *
 * @author Marco Piazzi: mp526
 *
 * Class to read in a file of Process data, create 
 * a suitable Process instance, and add them to the 
 * ThreadPool of the SimController
 * 
 */
package com.memorymanagement;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.concurrent.ThreadPoolExecutor;

public class QueueHandler implements Runnable {
	
	ThreadPoolExecutor executor;
	MemManager manager;
	String fname;
	
	public QueueHandler(ThreadPoolExecutor ex, MemManager man, String fname) {
		this.executor = ex;
		this.manager = man;
		this.fname = fname;
	}

	@Override
	public void run() {
		Path fpath = Paths.get(fname);
		try (Scanner file = new Scanner(fpath)) {
			int delay, size, runtime;
			char pid;
			
			while (file.hasNextLine()) {
				Scanner line = new Scanner(file.nextLine());
				line.useDelimiter(":");
				delay = line.nextInt();
				pid = line.next().charAt(0);
				size = line.nextInt();
				runtime = line.nextInt();
				line.close();
				
				try {
					Thread.sleep(delay * 100); // delay before each process
				} catch (InterruptedException e) {
					System.out.println(e);
				}
				Process p = new Process(manager, delay, pid, size, runtime);
				executor.submit(p);
			}
			file.close();	
		} catch (NoSuchFileException e) {
			System.err.println("File not found: " + fname);
			System.exit(1);
		} catch (IOException e) {
			System.err.println(e);
			System.exit(1);
		}		
		
	}

}
