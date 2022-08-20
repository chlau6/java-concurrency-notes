package listing15p1;

import annotation.GuardedBy;
import annotation.ThreadSafe;

@ThreadSafe
public class SimulatedCAS {
    @GuardedBy("this")
    private int value;

    public synchronized int get() {
        return value;
    }

    public synchronized int compareAndSwap(int expectedValue, int newValue) {
        int oldValue = value;
        if (oldValue == expectedValue) {
            value = newValue;
        }

        return oldValue;
    }

    public synchronized boolean compareAndSet(int expectedValue, int newValue) {
        return (expectedValue == compareAndSwap(expectedValue, newValue));
    }
}
/*
The approach taken by most processor architectures, including IA32 and Sparc,
is to implement a compare-and-swap (CAS) instruction. (Other processors, such as PowerPC,
implement the same functionality with a pair of instructions: loadlinked and store-conditional.)
CAS has three operands - a memory location V on which to operate, the expected old value A, and the new value B.
CAS atomically updates V to the new value B, but only if the value in V matches the expected old value A;
otherwise it does nothing. In either case, it returns the value currently in V.
(The variant called compare-and-set instead returns whether the operation succeeded.)
CAS means "I think V should have the value A; if it does, put B there,
otherwise don't change it but tell me I was wrong.
" CAS is an optimistic technique - it proceeds with the update in the hope of success,
and can detect failure if another thread has updated the variable since it was last examined.
SimulatedCAS in Listing 15.1 illustrates the semantics (but not the implementation or performance) of CAS.

When multiple threads attempt to update the same variable simultaneously using CAS,
one wins and updates the variable's value, and the rest lose. But the losers are not punished by suspension,
as they could be if they failed to acquire a lock;
instead, they are told that they didn't win the race this time but can try again.
Because a thread that loses a CAS is not blocked, it can decide whether it wants to try again,
take some other recovery action, or do nothing.
This flexibility eliminates many of the liveness hazards associated with locking
(though in unusual cases can introduce the risk of livelock - see Section 10.3.3).

The typical pattern for using CAS is first to read the value A from V, derive the new value B from A,
and then use CAS to atomically change V from A to B
so long as no other thread has changed V to another value in the meantime.
CAS addresses the problem of implementing atomic read-modify-write sequences without locking,
because it can detect interference from other threads.
*/