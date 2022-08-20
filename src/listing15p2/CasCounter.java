package listing15p2;

import annotation.GuardedBy;
import annotation.ThreadSafe;
import listing15p1.SimulatedCAS;

@ThreadSafe
public class CasCounter {
    private SimulatedCAS value;
    public int getValue() {
        return value.get();
    }
    public int increment() {
        int v;
        do {
            v = value.get();
        }
        while (v != value.compareAndSwap(v, v + 1));
        return v + 1;
    }
}
/*
CasCounter in Listing 15.2 implements a thread-safe counter using CAS.
The increment operation follows the canonical form - fetch the old value, transform it to the new value (adding one),
and use CAS to set the new value. If the CAS fails, the operation is immediately retried.
Retrying repeatedly is usually a reasonable strategy,
although in cases of extreme contention it might be desirable to wait or back off before retrying to avoid livelock.

CasCounter does not block, though it may have to retry several times if
other threads are updating the counter at the same time.
(In practice, if all you need is a counter or sequence generator, just use AtomicInteger or AtomicLong,
which provide atomic increment and other arithmetic methods.)

At first glance, the CAS-based counter looks as if it should perform worse than a lock-based counter;
it has more operations and a more complicated control flow, and depends on the seemingly complicated CAS operation.
But in reality, CAS-based counters significantly outperform lock-based counters if
there is even a small amount of contention, and often even if there is no contention.
The fast path for uncontended lock acquisition typically requires at least one CAS plus other lock-related housekeeping,
so more work is going on in the best case for a lock-based counter than in the normal case for the CAS-based counter.
Since the CAS succeeds most of the time (assuming low to moderate contention),
the hardware will correctly predict the branch implicit in the while loop,
minimizing the overhead of the more complicated control logic.

The language syntax for locking may be compact, but the work done by the JVM and OS to manage locks is not.
Locking entails traversing a relatively complicated code path in the JVMand may entail OS-level locking,
thread suspension, and context switches. In the best case, locking requires at least one CAS,
so using locks moves the CAS out of sight but doesn't save any actual execution cost. On the other hand,
executing a CAS from within the program involves no JVM code, system calls, or scheduling activity.
What looks like a longer code path at the application level is in fact
a much shorter code path when JVM and OS activity are taken into account.
The primary disadvantage of CAS is that it forces the caller to deal with contention  (by retrying, backing off,
or giving up),  whereas locks deal with contention automatically by blocking until the lock is available.
*/