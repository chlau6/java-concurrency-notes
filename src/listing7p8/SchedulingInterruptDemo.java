package listing7p8;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SchedulingInterruptDemo {
    private static final int NTHREADS = 100;
    private static final ScheduledExecutorService cancelExec = Executors.newScheduledThreadPool(NTHREADS);

    public static void timedRun(Runnable r, long timeout, TimeUnit unit) {
        final Thread taskThread = Thread.currentThread();

        cancelExec.schedule(new Runnable() {
            public void run() {
                taskThread.interrupt();
            }
        }, timeout, unit);

        r.run();
    }
}

/*
Listing 7.8 shows an attempt at running an arbitrary Runnable for a given amount of time. 
It runs the task in the calling thread and schedules a cancellation task to interrupt it after a given time interval. 
This addresses the problem of unchecked exceptions thrown from the task, 
since they can then be caught by the caller of timedRun.
This is an appealingly simple approach, but it violates the rules:
you should know a thread's interruption policy before interrupting it. 
Since timedRun can be called from an arbitrary thread, it cannot know the calling thread's interruption policy. 
If the task completes before the timeout, 
the cancellation task that interrupts the thread in which timedRun was called could 
go off after timedRun has returned to its caller. 
We don't know what code will be running when that happens, but the result won't be good.
(It is possible but surprisingly tricky to eliminate this risk by using
the ScheduledFuture returned by schedule to cancel the cancellation task.)

Further, if the task is not responsive to interruption, timedRun will not return until the task finishes,
which may be long after the desired timeout (or even not at all).
A timed run service that doesn't return after the specified time is likely to be irritating to its callers.
 */
