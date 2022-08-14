package listing13p6;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public interface ReadWriteLock {
    Lock readLock();
    Lock writeLock();
}

/*
ReadWriteLock, shown in Listing 13.6, exposes two Lock objects—one for reading and one for writing.
To read data guarded by a ReadWriteLock you must first acquire the read lock,
and to modify data guarded by a ReadWriteLock you must first acquire the write lock.
While there may appear to be two separate locks,
the read lock and write lock are simply different views of an integrated read-write lock object.

The locking strategy implemented by read-write locks allows multiple simultaneous readers but only a single writer.
Like Lock, ReadWriteLock admits multiple implementations that can vary in performance, scheduling guarantees,
acquisition preference, fairness, or locking semantics.

Read-write locks are a performance optimization designed to allow greater concurrency in certain situations.
In practice, read-write locks can improve performance for frequently accessed read-mostly data structures on
multiprocessor systems;
under other conditions they perform slightly worse than exclusive locks due to their greater complexity.
Whether they are an improvement in any given situation is best determined via profiling;
because ReadWriteLock uses Lock for the read and write portions of the lock,
it is relatively easy to swap out a readwrite lock for an exclusive one if
profiling determines that a read-write lock is not a win.
*/