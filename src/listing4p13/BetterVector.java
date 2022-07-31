package listing4p13;

import annotation.ThreadSafe;

import java.util.Vector;

@ThreadSafe
public class BetterVector<E> extends Vector<E> {
    public synchronized boolean putIfAbsent(E x) {
        boolean absent = !contains(x);
        if (absent) {
            add(x);
        }
        return absent;
    }
}

/*
BetterVector in Listing 4.13 extends Vector to add a putIfAbsent method. Extending Vector is straightforward enough,
but not all classes expose enough of their state to subclasses to admit this approach.

Extension is more fragile than adding code directly to a class,
because the implementation of the synchronization policy is now distributed over multiple,
separately maintained source files.

If the underlying class were to change its synchronization policy by
choosing a different lock to guard its state variables, the subclass would subtly and silently break,
because it no longer used the right lock to control concurrent access to the base class's state.
(The synchronization policy of Vector is fixed by its specification,
so BetterVector would not suffer from this problem.)
 */
