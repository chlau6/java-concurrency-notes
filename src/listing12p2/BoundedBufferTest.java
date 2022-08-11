package listing12p2;

import annotation.GuardedBy;
import annotation.ThreadSafe;
import junit.framework.TestCase;
import listing12p1.BoundedBuffer;

import java.util.concurrent.Semaphore;

class BoundedBufferTest extends TestCase {
    void testIsEmptyWhenConstructed() {
        BoundedBuffer<Integer> bb = new BoundedBuffer<Integer>(10);
        assertTrue(bb.isEmpty());
        assertFalse(bb.isFull());
    }

    void testIsFullAfterPuts() throws InterruptedException {
        BoundedBuffer<Integer> bb = new BoundedBuffer<Integer>(10);

        for (int i = 0; i < 10; i++) {
            bb.put(i);
        }

        assertTrue(bb.isFull());
        assertFalse(bb.isEmpty());
    }
}

/*
The most basic unit tests for BoundedBuffer are similar to what we'd use in a sequential context -
create a bounded buffer, call its methods, and assert postconditions and invariants.
Some invariants that quickly come to mind are that a freshly created buffer should identify itself as empty,
and also as not full.
A similar but slightly more complicated safety test is to insert N elements into a buffer with capacity N
(which should succeed without blocking), and test that the buffer recognizes that it is full (and not empty).
JUnit test methods for these properties are shown in Listing 12.2.
*/