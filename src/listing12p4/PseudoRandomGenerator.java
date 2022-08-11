package listing12p4;

import junit.framework.TestCase;
import listing12p1.BoundedBuffer;

public class PseudoRandomGenerator {
    public static int xorShift(int y) {
        y ^= (y << 6);
        y ^= (y >>> 21);
        y ^= (y << 7);
        return y;
    }
}

/*
PutTakeTest in Listings 12.5 and 12.6 starts N producer threads that generate elements and enqueue them,
and N consumer threads that dequeue them.
Each thread updates the checksum of the elements as they go in or out,
using a perthread checksum that is combined at the end of the test run so as to
add no more synchronization or contention than required to test the buffer.

Depending on your platform, creating and starting a thread can be a moderately heavyweight operation.
If your thread is short-running and you start a number of threads in a loop,
the threads run sequentially rather than concurrently in the worst case.  Even in the not-quite-worst case,
the fact that the first thread has a head start on the others means that you may get fewer interleavings than expected:
the first thread runs by itself for some amount of time,
and then the first two threads run concurrently for some amount of time,
and only eventually are all the threads running concurrently.
(The same thing happens at the end of the run: the threads that got a head start also finish early.)

We presented a technique for mitigating this problem in Section 5.5.1,
using a CountDownLatch as a starting gate and another as a finish gate.
Another way to get the same effect is to use a CyclicBarrier, initialized with the number of worker threads plus one,
and have the worker threads and the test driver wait at the barrier at the beginning and end of their run.
This ensures that all threads are up and running before any start working.
PutTakeTest uses this technique to coordinate starting and stopping the worker threads,
creating more potential concurrent interleavings.
We still can't guarantee that the scheduler won't run each thread to completion sequentially,
but making the runs long enough reduces the extent to which scheduling distorts our results.

The final trick employed by PutTakeTest is to use a deterministic termination criterion so that
no additional inter-thread coordination is needed to figure out when the test is finished.
The test method starts exactly as many producers as consumers and
each of them puts or  takes the same number of elements, so the total number of items added and removed is the same.

Tests like PutTakeTest tend to be good at finding safety violations.
For example, a common error in implementing semaphore-controlled buffers is to forget that
the code actually doing the insertion and extraction requires mutual exclusion (using synchronized or ReentrantLock).
A sample run of PutTakeTest with a version of BoundedBuffer that
omits making doInsert and doExtract synchronized fails fairly quickly.
Running PutTakeTest with a few dozen threads iterating a few million times on buffers of various capacity
on various systems increases our confidence about the lack of data corruption in put and take.
*/