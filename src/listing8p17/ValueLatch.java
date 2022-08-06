package listing8p17;

import annotation.GuardedBy;
import annotation.ThreadSafe;
import listing8p13.Puzzle;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

@ThreadSafe
public class ValueLatch<T> {
    @GuardedBy("this")
    private T value = null;

    private final CountDownLatch done = new CountDownLatch(1);

    public boolean isSet() {
        return (done.getCount() == 0);
    }

    public synchronized void setValue(T newValue) {
        if (!isSet()) {
            value = newValue;
            done.countDown();
        }
    }

    public T getValue() throws InterruptedException {
        done.await();
        synchronized (this) {
            return value;
        }
    }
}

/*
In order to stop searching when we find a solution,
we need a way to determine whether any thread has found a solution yet.
If we want to accept the first solution found,
we also need to update the solution only if no other task has already found one.
These requirements describe a sort of latch and in particular, a result-bearing latch.
We could easily build a blocking resultbearing latch using the techniques in Chapter 14,
but it is often easier and less error-prone to use existing library classes rather than low-level language mechanisms.
ValueLatch in Listing 8.17 uses a CountDownLatch to provide the needed latching behavior,
and uses locking to ensure that the solution is set only once.

Each task first consults the solution latch and stops if a solution has already been found.
The main thread needs to wait until a solution is found;
getValue in ValueLatch blocks until some thread has set the value.
ValueLatch provides a way to hold a value such that only the first call actually sets the value,
callers can test whether it has been set, and callers can block waiting for it to be set.
On the first call to setValue, the solution is updated and the CountDownLatch is decremented,
releasing the main solver thread from getValue.
 */