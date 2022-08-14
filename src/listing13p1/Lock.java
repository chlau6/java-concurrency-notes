package listing13p1;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

public interface Lock {
    void lock();

    void lockInterruptibly() throws InterruptedException;

    boolean tryLock();

    boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException;

    void unlock();

    Condition newCondition();
}

/*
The Lock interface, shown in Listing 13.1, defines a number of abstract locking operations.
Unlike intrinsic locking, Lock offers a choice of unconditional, polled, timed, and interruptible lock acquisition,
and all lock and unlock operations are explicit.
Lock implementations must provide the same memory-visibility semantics as intrinsic locks,
but can differ in their locking semantics, scheduling algorithms, ordering guarantees, and performance characteristics.
*/