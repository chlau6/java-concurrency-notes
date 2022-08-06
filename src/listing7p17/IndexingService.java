package listing7p17;

import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.BlockingQueue;

import static java.util.concurrent.Executors.newSingleThreadExecutor;

public class IndexingService {
    private static final File POISON = new File("");
    private final IndexerThread consumer = new IndexerThread();
    private final CrawlerThread producer = new CrawlerThread();
    private final BlockingQueue<File> queue;
    private final FileFilter fileFilter;
    private final File root;

    public IndexingService(BlockingQueue<File> queue, FileFilter fileFilter, File root) {
        this.queue = queue;
        this.fileFilter = fileFilter;
        this.root = root;
    }

    class CrawlerThread extends Thread {
        /* Listing 7.18 */
        public void run() {
            try {
                crawl(root);
            } catch (InterruptedException e) {
                /* fall through */
            } finally {
                while (true) {
                    try {
                        queue.put(POISON);
                        break;
                    } catch (InterruptedException e1) {
                        /* retry */
                    }
                }
            }
        }

        /* Dummy Implementation */
        private void crawl(File root) throws InterruptedException {

        }
    }

    class IndexerThread extends Thread {
        /* Listing 7.19 */
        public void run() {
            try {
                while (true) {
                    File file = queue.take();
                    if (file == POISON) {
                        break;
                    } else {
                        indexFile(file);
                    }
                }
            } catch (InterruptedException consumed) {

            }
        }

        /* Dummy Implementation */
        private void indexFile(File file) {

        }
    }

    public void start() {
        producer.start();
        consumer.start();
    }

    public void stop() {
        producer.interrupt();
    }

    public void awaitTermination() throws InterruptedException {
        consumer.join();
    }
}

/*
Another way to convince a producer-consumer service to shut down is with a poison pill:
a recognizable object placed on the queue that means "when you get this, stop."
With a FIFO queue, poison pills ensure that consumers finish the work on their queue before shutting down,
since any work submitted prior to submitting the poison pill will be retrieved before the pill;
producers should not submit any work after putting a poison pill on the queue.
IndexingService in Listings 7.17, 7.18, and 7.19 shows a single-producer,
single-consumer version of the desktop search example from Listing 5.8 that uses a poison pill to shut down the service.

Poison pills work only when the number of producers and consumers is known.
The approach in IndexingService can be extended to multiple producers by having
each producer place a pill on the queue and having the consumer stop only when it receives Nproducers pills.
It can be extended to multiple consumers by having each producer place Nconsumers pills on the queue,
though this can get unwieldy with large numbers of producers and consumers.
Poison pills work reliably only with unbounded queues.
 */
