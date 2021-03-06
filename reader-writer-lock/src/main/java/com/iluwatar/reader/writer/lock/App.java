package com.iluwatar.reader.writer.lock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * 
 * In a multiple thread applications, the threads may try to synchronize the shared resources
 * regardless of read or write operation. It leads to a low performance especially in a "read more
 * write less" system as indeed the read operations are thread-safe to another read operation.
 * <p>
 * Reader writer lock is a synchronization primitive that try to resolve this problem. This pattern
 * allows concurrent access for read-only operations, while write operations require exclusive
 * access. This means that multiple threads can read the data in parallel but an exclusive lock is
 * needed for writing or modifying data. When a writer is writing the data, all other writers or
 * readers will be blocked until the writer is finished writing.
 * 
 * <p>
 * This example use two mutex to demonstrate the concurrent access of multiple readers and
 * writers.
 * 
 * 
 * @author hongshuwei@gmail.com
 */
public class App {

  /**
   * Program entry point
   * 
   * @param args command line args
   */
  public static void main(String[] args) {

    ExecutorService executeService = Executors.newFixedThreadPool(1000);
    ReaderWriterLock lock = new ReaderWriterLock();

    // Start 10 readers
    IntStream.range(0, 10)
        .forEach(i -> executeService.submit(new Reader("Reader " + i, lock.readLock())));

    // Start 10 writers
    IntStream.range(0, 10)
        .forEach(i -> executeService.submit(new Writer("Writer " + i, lock.writeLock())));
    // In the system console, it can see that the read operations are executed concurrently while
    // write operations are exclusive.
    executeService.shutdown();
    try {
      executeService.awaitTermination(5, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      System.out.println("Error waiting for ExecutorService shutdown");
    }

  }

}
