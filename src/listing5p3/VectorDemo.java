package listing5p3;

import java.util.Vector;

public class VectorDemo {
    public void iterate(Vector vector) {
        for (int i = 0; i < vector.size(); i++) {
            doSomething(vector.get(i));
        }
    }

    /* Dummy Implementation */
    private void doSomething(Object o) {

    }
}

/*
The risk that the size of the list might change between a call to size and
the corresponding call to get is also present when we iterate through the elements of a Vector as shown in Listing 5.3.

This iteration idiom relies on a leap of faith that
other threads will not modify the Vector between the calls to size and get.
In a single-threaded environment, this assumption is perfectly valid,
but when other threads may concurrently modify the Vector it can lead to trouble.

Just as with getLast, if another thread deletes an element while you are iterating through the Vector and
the operations are interleaved unluckily, this iteration idiom throws ArrayIndexOutOfBoundsException.
 */
