package listing5p8;

import java.io.File;
import java.util.concurrent.BlockingQueue;

public class Indexer implements Runnable {
    private final BlockingQueue<File> queue;

    public Indexer(BlockingQueue<File> queue) {
        this.queue = queue;
    }

    public void run() {
        try {
            while (true) {
                indexFile(queue.take());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /* Dummy Implementation */
    private void indexFile(File file) {

    }
}

/*
Indexer in Listing 5.8 shows the consumer task that takes file names from the queue and indexes them.
 */
