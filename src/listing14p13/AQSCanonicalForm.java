package listing14p13;

import annotation.GuardedBy;
import annotation.ThreadSafe;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AQSCanonicalForm {
    /*
    boolean acquire() throws InterruptedException {
        while (state does not permit acquire) {
            if (blocking acquisition requested) {
                enqueue current thread if not already queued
                block current thread
            } else {
                return failure
            }
        }

        possibly update synchronization state
        dequeue thread if it was queued
        return success
    }
    void release() {
        update synchronization state
        if (new state may permit a blocked thread to acquire) {
            unblock one or more queued threads
        }
    }
     */
}

/*
Acquisition and release in AQS take the forms shown in Listing 14.13. Depending on the synchronizer,
acquisition might be exclusive, as with Reentrant- Lock, or nonexclusive, as with Semaphore and CountDownLatch.

An acquire operation has two parts. First, the synchronizer decides whether the current state permits acquisition;
if so, the thread is allowed to proceed, and if not, the acquire blocks or fails.

This decision is determined by the synchronizer semantics; for example,
acquiring a lock can succeed if the lock is unheld,
and acquiring a latch can succeed if the latch is in its terminal state.

The second part involves possibly updating the synchronizer state; 
one thread acquiring the synchronizer can affect whether other threads can acquire it. 
For example, acquiring a lock changes the lock state from "unheld" to "held", 
and acquiring a permit from a Semaphore reduces the number of permits left. 
On the other hand, the acquisition of a latch by one thread does not affect whether other threads can acquire it, 
so acquiring a latch does not change its state.
*/