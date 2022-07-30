package listing4p1;

import annotation.GuardedBy;
import annotation.ThreadSafe;

@ThreadSafe
public final class Counter {
    @GuardedBy("this")
    private long value = 0;

    public synchronized long getValue() {
        return value;
    }

    public synchronized long increment() {
        if (value == Long.MAX_VALUE) {
            throw new IllegalStateException("counter overflow");
        }

        return ++value;
    }
}

/*
An object’s state starts with its fields.
If they are all of primitive type, the fields comprise the entire state.
Counter in Listing 4.1 has only one field, so the value field comprises its entire state.

The state of an object with n primitive fields is just the n-tuple of its field values;
the state of a 2D Point is its (x, y) value.

If the object has fields that are references to other objects,
its state will encompass fields from the referenced objects as well.
For example, the state of a LinkedList includes the state of all the link node objects belonging to the list.
 */