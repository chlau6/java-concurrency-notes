package listing1p1;

import annotation.NotThreadSafe;

@NotThreadSafe
public class UnsafeSequence {
    private int value;

    /** Returns a unique value. */
    public int getNext() {
        return value++;
    }
}

/*
The problem with UnsafeSequence is that with some unlucky timing,
two threads could call getNext and receive the same value.

The increment notation, nextValue++, may appear to be a single operation, but is in fact three separate operations:
read the value, add one to it, and write out the new value.

Since operations in multiple threads may be arbitrarily interleaved by the runtime,
it is possible for two threads to read the value at the same time, both see the same value, and then both add one to it.

The result is that the same sequence number is returned from multiple calls in different threads.
 */
