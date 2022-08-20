package listing14p11;

import annotation.GuardedBy;
import annotation.ThreadSafe;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@ThreadSafe
public class ConditionBoundedBuffer<T> {
    protected final Lock lock = new ReentrantLock();
    // CONDITION PREDICATE: notFull (count < items.length)
    private final Condition notFull = lock.newCondition(); // CONDITION PREDICATE: notEmpty (count > 0)

    private static final Integer BUFFER_SIZE = 1000;

    private final Condition notEmpty = lock.newCondition();

    @GuardedBy("lock")
    private final T[] items = (T[]) new Object[BUFFER_SIZE];

    @GuardedBy("lock")
    private int tail, head, count;

    // BLOCKS-UNTIL: notFull
    public void put(T x) throws InterruptedException {
        lock.lock();
        try {
            while (count == items.length) {
                notFull.await();
                items[tail] = x;
            }

            if (++tail == items.length) {
                tail = 0;
            }

            ++count;
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    // BLOCKS-UNTIL: notEmpty
    public T take() throws InterruptedException {
        lock.lock();

        try {
            while (count == 0) {
                notEmpty.await();
            }

            T x = items[head];
            items[head] = null;

            if (++head == items.length) {
                head = 0;
            }

            --count;
            notFull.signal();
            return x;
        } finally {
            lock.unlock();
        }
    }
}

/*
Listing 14.11 shows yet another bounded buffer implementation, this time using two Conditions, notFull and notEmpty,
to represent explicitly the "not full" and "not empty" condition predicates.
When take blocks because the buffer is empty, it waits on notEmpty,
and put unblocks any threads blocked in take by signaling on notEmpty.

The behavior of ConditionBoundedBuffer is the same as BoundedBuffer, but its use of condition queues is more readable -
it is easier to analyze a class that uses multiple Conditions than one that
uses a single intrinsic condition queue with multiple condition predicates.
By separating the two condition predicates into separate wait sets,
Condition makes it easier to meet the requirements for single notification.
Using the more efficient signal instead of signalAll reduces the number of context switches and
lock acquisitions triggered by each buffer operation.

Just as with built-in locks and condition queues, the three-way relationship among the lock, the condition predicate,
and the condition variable must also hold when using explicit Locks and Conditions.
The variables involved in the condition predicate must be guarded by the Lock,
and the Lock must be held when testing the condition predicate and when calling await and signal.
*/