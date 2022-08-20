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
The interfaces of ReentrantLock and Semaphore have a lot in common. Both classes act as a "gate",
allowing only a limited number of threads to pass at a time; threads arrive at the gate and are allowed through
(lock or acquire returns successfully), are made to wait (lock or acquire blocks), or are turned away
(tryLock or tryAcquire returns false, indicating that the lock or permit did not become available in the time allowed).
Further, both allow interruptible, uninterruptible,
and timed acquisition attempts, and both allow a choice of fair or nonfair queueing of waiting threads.

Given this commonality, you might think that Semaphore was implemented on top of ReentrantLock,
or perhaps ReentrantLock was implemented as a Semaphore with one permit. This would be entirely practical;
it is a common exercise to prove that a counting semaphore can be implemented using a lock
(as in SemaphoreOnLock in Listing 14.12) and that a lock can be implemented using a counting semaphore.
*/