package listing12p3;

import junit.framework.TestCase;
import listing12p1.BoundedBuffer;

class BoundedBufferTest extends TestCase {
    private static final long LOCKUP_DETECT_TIMEOUT = 10000;

    void testTakeBlocksWhenEmpty() {
        final BoundedBuffer<Integer> bb = new BoundedBuffer<Integer>(10);

        Thread taker = new Thread() {
            public void run() {
                try {
                    int unused = bb.take();
                    fail(); // if we get here, it's an error
                } catch (InterruptedException success) { }
            }
        };

        try {
            taker.start();
            Thread.sleep(LOCKUP_DETECT_TIMEOUT);
            taker.interrupt();
            taker.join(LOCKUP_DETECT_TIMEOUT);
            assertFalse(taker.isAlive());
        } catch (Exception unexpected) {
            fail();
        }
    }
}

/*
Listing 12.3 shows an approach to testing blocking operations.
It creates a "taker" thread that attempts to take an element from an empty buffer.
If take succeeds, it registers failure.
The test runner thread starts the taker thread, waits a long time, and then interrupts it.
If the taker thread has correctly blocked in the take operation, it will throw InterruptedException,
and the catch block for this exception treats this as success and lets the thread exit.
The main test runner thread then attempts to join with the taker thread and
verifies that the join returned successfully by calling Thread.isAlive;
if the taker thread responded to the interrupt, the join should complete quickly.

The timed join ensures that the test completes even if take gets stuck in some unexpected way.
This test method tests several properties of take -
not only that it blocks but that, when interrupted, it throws InterruptedException.
This is one of the few cases in which it is appropriate to subclass Thread explicitly instead of
using a Runnable in a pool: in order to test proper termination with join.
The same approach can be used to test that the taker thread unblocks after
an element is placed in the queue by the main thread.
*/