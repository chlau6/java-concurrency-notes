package listing13p7;

import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteMap<K,V> {
    private final Map<K,V> map;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock r = lock.readLock();
    private final Lock w = lock.writeLock();

    public ReadWriteMap(Map<K,V> map) {
        this.map = map;
    }

    public V put(K key, V value) {
        w.lock();
        try {
            return map.put(key, value);
        } finally {
            w.unlock();
        }
    }
    // Do the same for remove(), putAll(), clear()

    public V get(Object key) {
        r.lock();
        try {
            return map.get(key);
        } finally {
            r.unlock();
        }
    }
    // Do the same for other read-only Map methods
}

/*
ReentrantReadWriteLock provides reentrant locking semantics for both locks.
Like ReentrantLock, a ReentrantReadWriteLock can be constructed as nonfair (the default) or fair.
With a fair lock, preference is given to the thread that has been waiting the longest;
if the lock is held by readers and a thread requests the write lock,
no more readers are allowed to acquire the read lock until the writer has been serviced and releases the write lock.
With a nonfair lock, the order in which threads are granted access is unspecified.
Downgrading from writer to reader is permitted; upgrading from reader to writer is not
(attempting to do so results in deadlock).

Like ReentrantLock, the write lock in ReentrantReadWriteLock has a unique owner and
can be released only by the thread that acquired it.
In Java 5.0, the read lock behaves more like a Semaphore than a lock, maintaining only the count of active readers,
not their identities.
This behavior was changed in Java 6 to keep track also of which threads have been granted the read lock.

Read-write locks can improve concurrency when locks are typically held for a moderately long time and
most operations do not modify the guarded resources.
ReadWriteMap in Listing 13.7 uses a ReentrantReadWriteLock to wrap a Map so that
it can be shared safely by multiple readers and still prevent reader-writer or writer-writer conflicts.
In reality, ConcurrentHashMap’s performance is so good that you would probably use it rather than this approach if
all you needed was a concurrent hash-based map, but this technique would be useful if
you want to provide more concurrent access to an alternate Map implementation such as LinkedHashMap.
*/