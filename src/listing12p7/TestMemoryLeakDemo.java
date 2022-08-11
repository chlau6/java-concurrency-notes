package listing12p7;

import listing12p1.BoundedBuffer;

import static junit.framework.Assert.assertTrue;

public class TestMemoryLeakDemo {
    private static final int CAPACITY = 100;
    private static final int THRESHOLD = 100;

    class Big {
        double[] data = new double[100000];
    }

    void testLeak() throws InterruptedException {
        BoundedBuffer<Big> bb = new BoundedBuffer<Big>(CAPACITY);

        int heapSize1 = (int) (Math.random() * 10000) /* snapshot heap */;

        for (int i = 0; i < CAPACITY; i++) {
            bb.put(new Big());
        }

        for (int i = 0; i < CAPACITY; i++) {
            bb.take();
        }

        int heapSize2 = (int) (Math.random() * 10000) /* snapshot heap */;

        assertTrue(Math.abs(heapSize1 - heapSize2) < THRESHOLD);
    }
}

/*
Undesirable memory retention can be easily tested with heap-inspection tools that measure application memory usage;
a variety of commercial and open-source heap-profiling tools can do this.
The testLeak method in Listing 12.7 contains placeholders for a heap-inspection tool to snapshot the heap,
which forces a garbage collection and then records information about the heap size and memory usage.

The testLeak method inserts several large objects into a bounded buffer and then removes them;
memory usage at heap snapshot #2 should be approximately the same as at heap snapshot #1.
On the other hand, if doExtract forgot to null out the reference to the returned element (items[i]=null),
the reported memory usage at the two snapshots would definitely not be the same.
(This is one of the few times where explicit nulling is necessary;
most of the time, it is either not helpful or actually harmful.)
*/