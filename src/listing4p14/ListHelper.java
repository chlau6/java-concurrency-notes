package listing4p14;

import annotation.NotThreadSafe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@NotThreadSafe
public class ListHelper<E> {
    public List<E> list = Collections.synchronizedList(new ArrayList<E>());

    public synchronized boolean putIfAbsent(E x) {
        boolean absent = !list.contains(x);
        if (absent) {
            list.add(x);
        }

        return absent;
    }
}

/*
Listing 4.14 shows a failed attempt to create a helper class with
an atomic put-if-absent operation for operating on a thread-safe List.

Why wouldn’t this work? After all, putIfAbsent is synchronized, right?
The problem is that it synchronizes on the wrong lock.
Whatever lock the List uses to guard its state, it sure isn’t the lock on the ListHelper.

ListHelper provides only the illusion of synchronization; the various list operations, while all synchronized,
use different locks, which means that putIfAbsent is not atomic relative to other operations on the List.
So there is no guarantee that another thread won’t modify the list while putIfAbsent is executing.

To make this approach work,
we have to use the same lock that the List uses by using client-side locking or external locking.
Client-side locking entails guarding client code that uses some object X with the lock X uses to guard its own state.

In order to use client-side locking, you must know what lock X uses.
The documentation for Vector and the synchronized wrapper classes states, albeit obliquely,
that they support client-side locking,
by using the intrinsic lock for the Vector or the wrapper collection (not the wrapped collection).
 */
