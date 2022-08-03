package listing7p10;

import java.util.concurrent.*;

import static listing5p13.UncheckedExceptionWrapper.launderThrowable;

public class SchedulingInterruptDemo {
    private static final int NTHREADS = 100;
    private static final ExecutorService taskExec = Executors.newFixedThreadPool(NTHREADS);

    public static void timedRun(Runnable r, long timeout, TimeUnit unit) throws InterruptedException {
        Future<?> task = taskExec.submit(r);
        try {
            task.get(timeout, unit);
        } catch (TimeoutException e) {
            // task will be cancelled below
        } catch (ExecutionException e) {
            // exception thrown in task; rethrow
            throw launderThrowable(e.getCause());
        } finally {
            // Harmless if task already completed
            task.cancel(true); // interrupt if running
        }
    }
}

/*
Listing 7.10 shows a version of timedRun that submits the task to an ExecutorService and
retrieves the result with a timed Future.get.
If get terminates with a TimeoutException, the task is cancelled via its Future.
(To simplify coding, this version calls Future.cancel unconditionally in a finally block,
taking advantage of the fact that cancelling a completed task has no effect.)
If the underlying computation throws an exception prior to cancellation, it is rethrown from timedRun,
which is the most convenient way for the caller to deal with the exception.
Listing 7.10 also illustrates another good practice: cancelling tasks whose result is no longer needed.
 */
