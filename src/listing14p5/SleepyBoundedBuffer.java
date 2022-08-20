package listing14p5;

import annotation.ThreadSafe;
import listing14p2.BaseBoundedBuffer;

@ThreadSafe
public class SleepyBoundedBuffer<V> extends BaseBoundedBuffer<V> {
    private static final long SLEEP_GRANULARITY = 1000;

    public SleepyBoundedBuffer(int size) {
        super(size);
    }

    public void put(V v) throws InterruptedException {
        while (true) {
            synchronized (this) {
                if (!isFull()) {
                    doPut(v);
                    return;
                }
            }
            Thread.sleep(SLEEP_GRANULARITY);
        }
    }
    public V take() throws InterruptedException {
        while (true) {
            synchronized (this) {
                if (!isEmpty())
                    return doTake();
            }
            Thread.sleep(SLEEP_GRANULARITY);
        }
    }
}

/*
SleepyBoundedBuffer in Listing 14.5 attempts to spare callers the inconvenience of implementing the retry logic
on each call by encapsulating the same crude "poll and sleep" retry mechanism within the put and take operations.
If the buffer is empty, take sleeps until another thread puts some data into the buffer;
if the buffer is full, put sleeps until another thread makes room by removing some data.
This approach encapsulates precondition management and simplifies using the buffer - 
definitely a step in the right direction.

The implementation of SleepyBoundedBuffer is more complicated than the previous attempt.
The buffer code must test the appropriate state condition with the buffer lock held,
because the variables that represent the state condition are guarded by the buffer lock.
If the test fails, the executing thread sleeps for a while,
first releasing the lock so other threads can access the buffer.
Once the thread wakes up, it reacquires the lock and tries again,
alternating between sleeping and testing the state condition until the operation can proceed.

From the perspective of the caller, this works nicely - if the operation can proceed immediately, it does,
and otherwise it blocks - and the caller need not deal with the mechanics of failure and retry.
Choosing the sleep granularity is a tradeoff between responsiveness and CPU usage;
the smaller the sleep granularity, the more responsive, but also the more CPU resources consumed.

SleepyBoundedBuffer also creates another requirement for the caller - dealing with InterruptedException.
When a method blocks waiting for a condition to become true,
the polite thing to do is to provide a cancellation mechanism.
Like most well-behaved blocking library methods, SleepyBoundedBuffer supports cancellation through interruption,
returning early and throwing InterruptedException if interrupted.

These attempts to synthesize a blocking operation from polling and sleeping were fairly painful.
It would be nice to have a way of suspending a thread but ensuring that it is awakened promptly when a certain condition
(such as the buffer being no longer full) becomes true. This is exactly what condition queues do.
*/