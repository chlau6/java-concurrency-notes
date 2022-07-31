package listing5p14;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Semaphore;

public class BoundedHashSet<T> {
    private final Set<T> set;
    private final Semaphore sem;

    public BoundedHashSet(int bound) {
        this.set = Collections.synchronizedSet(new HashSet<T>());
        sem = new Semaphore(bound);
    }

    public boolean add(T o) throws InterruptedException {
        sem.acquire();
        boolean wasAdded = false;
        try {
            wasAdded = set.add(o);
            return wasAdded;
        } finally {
            if (!wasAdded) {
                sem.release();
            }
        }
    }

    public boolean remove(Object o) {
        boolean wasRemoved = set.remove(o);
        if (wasRemoved) {
            sem.release();
        }
        return wasRemoved;
    }
}

/*
Similarly, you can use a Semaphore to turn any collection into a blocking bounded collection,
as illustrated by BoundedHashSet in Listing 5.14.

The semaphore is initialized to the desired maximum size of the collection.
The add operation acquires a permit before adding the item into the underlying collection.

If the underlying add operation does not actually add anything, it releases the permit immediately.
Similarly, a successful remove operation releases a permit, enabling more elements to be added.
The underlying Set implementation knows nothing about the bound; this is handled by BoundedHashSet.
 */
