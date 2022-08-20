package listing16p2;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class FutureClassInnerClass<V> {
    // Inner class of FutureTask
    private final class Sync extends AbstractQueuedSynchronizer {
        private static final int RUNNING = 1, RAN = 2, CANCELLED = 4;
        private V result;
        private Exception exception;

        void innerSet(V v) {
            while (true) {
                int s = getState();
                if (ranOrCancelled(s)) {
                    return;
                }
                if (compareAndSetState(s, RAN)) {
                    break;
                }
            }

            result = v;
            releaseShared(0);
            done();
        }

        V innerGet() throws InterruptedException, ExecutionException {
            acquireSharedInterruptibly(0);
            if (getState() == CANCELLED) {
                throw new CancellationException();
            } else if (exception != null) {
                throw new ExecutionException(exception);
            }

            return result;
        }

        /* Dummy Implementation */
        private boolean ranOrCancelled(int state) {
            return true;
        }

        /* Dummy Implementation */
        private void done() {
        }
    }
}

/*
The implementation of the protected AbstractQueuedSynchronizer methods in FutureTask illustrates piggybacking.
AQS maintains an integer of synchronizer state that FutureTask uses to store the task state:
running, completed, or cancelled.
But FutureTask also maintains additional variables, such as the result of the computation.
When one thread calls set to save the result and another thread calls get to retrieve it,
the two had better be ordered by happens-before.
This could be done by making the reference to the result volatile,
but it is possible to exploit existing synchronization to achieve the same result at lower cost.

FutureTask is carefully crafted to ensure that
a successful call to tryReleaseShared always happens-before a subsequent call to tryAcquireShared;
tryReleaseShared always writes to a volatile variable that is read by tryAcquireShared.
Listing 16.2 shows the innerSet and innerGet methods that are called when the result is saved or retrieved;
since innerSet writes result before calling releaseShared (which calls tryReleaseShared) and
innerGet reads result after calling acquireShared (which calls tryAcquireShared),
the program order rule combines with the volatile variable rule to ensure that
the write of result in innerGet happens-before the read of result in innerGet.
*/