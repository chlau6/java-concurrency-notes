package listing5p11;

import java.util.concurrent.CountDownLatch;

public class TestHarness {
    public long timeTasks(int nThreads, final Runnable task) throws InterruptedException {
        final CountDownLatch startGate = new CountDownLatch(1);
        final CountDownLatch endGate = new CountDownLatch(nThreads);

        for (int i = 0; i < nThreads; i++) {
            Thread t = new Thread() {
                public void run() {
                    try {
                        startGate.await();
                        try {
                            task.run();
                        } finally {
                            endGate.countDown();
                        }
                    } catch (InterruptedException ignored) {

                    }
                }
            };

            t.start();
        }

        long start = System.nanoTime();

        startGate.countDown();
        endGate.await();

        long end = System.nanoTime();

        return end - start;
    }
}

/*
FutureTask is used by the Executor framework to represent asynchronous tasks,
and can also be used to represent any potentially lengthy computation that can be started before the results are needed.
Preloader in Listing 5.12 uses FutureTask to perform an expensive computation whose results are needed later;
by starting the computation early, you reduce the time you would have to wait later when you actually need the results.
 */