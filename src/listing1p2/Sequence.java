package listing1p2;

import annotation.GuardedBy;
import annotation.ThreadSafe;

@ThreadSafe
public class Sequence {
    @GuardedBy("this")
    private int value;

    /** Returns a unique value. */
    public synchronized int getNext() {
        return value++;
    }
}

/*
UnsafeSequence can be fixed by making getNext a synchronized method, as shown in Sequence in Listing 1.2,
thus preventing the unfortunate interaction
 */
