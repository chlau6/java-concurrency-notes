package listing3p3;

import annotation.GuardedBy;
import annotation.ThreadSafe;

@ThreadSafe
public class SynchronizedInteger {
    @GuardedBy("this")
    private int value;

    public synchronized int get() {
        return value;
    }

    public synchronized void set(int value) {
        this.value = value;
    }
}

/*
We can make MutableInteger thread safe by synchronizing the getter and setter
as shown in SynchronizedInteger in Listing 3.3

Synchronizing only the setter would not be sufficient:
threads calling get would still be able to see stale values.
 */
