package listing12p9;

import junit.framework.TestCase;
import listing12p8.TestingThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import static junit.framework.Assert.assertEquals;

class TestThreadPoolExpansionDemo extends TestCase {
    private final TestingThreadFactory threadFactory = new TestingThreadFactory();

    public void testPoolExpansion() throws InterruptedException {
        int MAX_SIZE = 1;
        ExecutorService exec = Executors.newFixedThreadPool(MAX_SIZE);

        for (int i = 0; i < 10 * MAX_SIZE; i++) {
            exec.execute(new Runnable() {
                public void run() {
                    try {
                        Thread.sleep(Long.MAX_VALUE);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
        }

        for (int i = 0; i < 20 && threadFactory.numCreated.get() < MAX_SIZE; i++) {
            Thread.sleep(100);
        }

        assertEquals(threadFactory.numCreated.get(), MAX_SIZE);
        exec.shutdownNow();
    }

    public static void main(String[] args) throws InterruptedException {
        new TestThreadPoolExpansionDemo().testPoolExpansion();
    }
}

/*
If the core pool size is smaller than the maximum size, the thread pool should grow as demand for execution increases.
Submitting long-running tasks to the pool makes the number of executing tasks stay constant for long enough to
make a few assertions, such as testing that the pool is expanded as expected, as shown in Listing 12.9.
*/