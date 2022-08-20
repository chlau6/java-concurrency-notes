package listing14p16;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class SemaphoreAQSDemo {
    private final Sync sync = new Sync();

    private class Sync extends AbstractQueuedSynchronizer {
        protected int tryAcquireShared(int acquires) {
            while (true) {
                int available = getState();
                int remaining = available - acquires;
                if (remaining < 0 || compareAndSetState(available, remaining)) {
                    return remaining;
                }
            }
        }

        protected boolean tryReleaseShared(int releases) {
            while (true) {
                int p = getState();
                if (compareAndSetState(p, p + releases)) {
                    return true;
                }
            }
        }
    }
}

/*
Semaphore uses the AQS synchronization state to hold the count of permits currently available.
The tryAcquireShared method (see Listing 14.16) first computes the number of permits remaining,
and if there are not enough, returns a value indicating that the acquire failed.
If sufficient permits appear to be left, it attempts to atomically reduce the permit count using compareAndSetState.
If that succeeds (meaning that the permit count had not changed since it last looked),
it returns a value indicating that the acquire succeeded.
The return value also encodes whether other shared acquisition attempts might succeed,
in which case other waiting threads will also be unblocked.

The while loop terminates either when there are not enough permits or
when tryAcquireShared can atomically update the permit count to reflect acquisition.
While any given call to compareAndSetState may fail due to contention with another thread (see Section 15.3),
causing it to retry, one of these two termination criteria will become true within a reasonable number of retries.
Similarly, tryReleaseShared increases the permit count, potentially unblocking waiting threads,
and retries until the update succeeds.
The return value of tryReleaseShared indicates whether other threads might have been unblocked by the release.
*/