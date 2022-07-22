package listing3p2;

import annotation.NotThreadSafe;

@NotThreadSafe
public class MutableInteger {
    private int value;

    public int get() {
        return value;
    }

    public void set(int value) {
        this.value = value;
    }
}

/*
MutableInteger is not thread-safe because the value field is accessed from both get and set without synchronization.

Among other hazards, it is susceptible to stale values:
if one thread calls set, other threads calling get may or may not see that update.
 */
