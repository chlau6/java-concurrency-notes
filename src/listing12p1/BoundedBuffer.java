package listing12p1;

import annotation.GuardedBy;
import annotation.ThreadSafe;

import java.util.concurrent.Semaphore;

@ThreadSafe
public class BoundedBuffer<E> {
    private final Semaphore availableItems, availableSpaces;

    @GuardedBy("this")
    private final E[] items;

    @GuardedBy("this")
    private int putPosition = 0, takePosition = 0;

    public BoundedBuffer(int capacity) {
        availableItems = new Semaphore(0);
        availableSpaces = new Semaphore(capacity);
        items = (E[]) new Object[capacity];
    }

    public boolean isEmpty() {
        return availableItems.availablePermits() == 0;
    }

    public boolean isFull() {
        return availableSpaces.availablePermits() == 0;
    }

    public void put(E x) throws InterruptedException {
        availableSpaces.acquire();
        doInsert(x);
        availableItems.release();
    }

    public E take() throws InterruptedException {
        availableItems.acquire();
        E item = doExtract();
        availableSpaces.release();
        return item;
    }

    private synchronized void doInsert(E x) {
        int i = putPosition;
        items[i] = x;
        putPosition = (++i == items.length)? 0 : i;
    }

    private synchronized E doExtract() {
        int i = takePosition;
        E x = items[i];
        items[i] = null;
        takePosition = (++i == items.length)? 0 : i;
        return x;
    }
}

/*
As a concrete illustration, we're going to build a set of test cases for a bounded buffer.
Listing 12.1 shows our BoundedBuffer implementation, using Semaphore to implement the required bounding and blocking.

BoundedBuffer implements a fixed-length array-based queue with blocking put and
take methods controlled by a pair of counting semaphores.
The availableItems semaphore represents the number of elements that can be removed from the buffer,
and is initially zero (since the buffer is initially empty).
Similarly, availableSpaces represents how many items can be inserted into the buffer,
and is initialized to the size of the buffer.

A take operation first requires that a permit be obtained from availableItems.
This succeeds immediately if the buffer is nonempty, and otherwise blocks until the buffer becomes nonempty.
Once a permit is obtained, the next element from the buffer is removed and
a permit is released to the availableSpaces semaphore. The put operation is defined conversely,
so that on exit from either the put or take methods, the sum of the counts of both semaphores always equals the bound.
(In practice, if you need a bounded buffer
you should use ArrayBlockingQueue or LinkedBlockingQueue rather than rolling your own,
but the technique used here illustrates how insertions and removals can be controlled in other data structures as well.)
*/