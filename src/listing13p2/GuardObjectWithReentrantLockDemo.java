package listing13p2;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GuardObjectWithReentrantLockDemo {
    void demo() {
        Lock lock = new ReentrantLock();

        lock.lock();
        try {
            // update object state
            // catch exceptions and restore invariants if necessary
        } finally {
            lock.unlock();
        }
    }
}

/*
The Lock interface, shown in Listing 13.1, defines a number of abstract locking operations.
Unlike intrinsic locking, Lock offers a choice of unconditional, polled, timed, and interruptible lock acquisition,
and all lock and unlock operations are explicit.
Lock implementations must provide the same memory-visibility semantics as intrinsic locks,
but can differ in their locking semantics, scheduling algorithms, ordering guarantees, and performance characteristics.
*/