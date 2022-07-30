package listing5p9;

import listing5p8.FileCrawler;
import listing5p8.Indexer;

import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Listing5P9 {
    public final static int BOUND = 10;
    private static final int N_CONSUMERS = 2;

    public static void startIndexing(File[] roots) {
        BlockingQueue<File> queue = new LinkedBlockingQueue<File>(BOUND);
        FileFilter filter = new FileFilter() {
            public boolean accept(File file) {
                return true;
            }
        };

        for (File root : roots) {
            new Thread(new FileCrawler(queue, filter, root)).start();
        }

        for (int i = 0; i < N_CONSUMERS; i++) {
            new Thread(new Indexer(queue)).start();
        }
    }
}

/*
Listing 5.9 starts several crawlers and indexers, each in their own thread.
As written, the consumer threads never exit, which prevents the program from terminating;
we examine several techniques for addressing this problem in Chapter 7.
While this example uses explicitly managed threads,
many producer-consumer designs can be expressed using the Executor task execution framework,
which itself uses the producer-consumer pattern.
 */
