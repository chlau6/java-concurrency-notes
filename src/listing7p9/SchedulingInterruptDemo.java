package listing7p9;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static listing5p13.UncheckedExceptionWrapper.launderThrowable;

public class SchedulingInterruptDemo {
    private static final int NTHREADS = 100;
    private static final ScheduledExecutorService cancelExec = Executors.newScheduledThreadPool(NTHREADS);

    public static void timedRun(final Runnable r, long timeout, TimeUnit unit) throws InterruptedException {
        class RethrowableTask implements Runnable {
            private volatile Throwable t;
            public void run() {
                try {
                    r.run();
                }
                catch (Throwable t) {
                    this.t = t;
                }
            }

            void rethrow() {
                if (t != null)
                    throw launderThrowable(t);
            }
        }

        RethrowableTask task = new RethrowableTask();
        final Thread taskThread = new Thread(task);

        taskThread.start();

        cancelExec.schedule(new Runnable() {
            public void run() {
                taskThread.interrupt();
            }
        }, timeout, unit);

        taskThread.join(unit.toMillis(timeout));

        task.rethrow();
    }
}

/*
Listing 7.9 addresses the exception-handling problem of aSecondOfPrimes and the problems with the previous attempt.
The thread created to run the task can have its own execution policy,
and even if the task doesn’t respond to the interrupt, the timed run method can still return to its caller.
After starting the task thread, timedRun executes a timed join with the newly created thread.
After join returns, it checks if an exception was thrown from the task and if so,
rethrows it in the thread calling timedRun.

The saved Throwable is shared between the two threads,
and so is declared volatile to safely publish it from the task thread to the timedRun thread.

This version addresses the problems in the previous examples, but because it relies on a timed join,
it shares a deficiency with join:
we don’t know if control was returned because the thread exited normally or because the join timed out.
 */
