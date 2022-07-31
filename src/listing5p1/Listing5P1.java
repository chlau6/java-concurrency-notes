package listing5p1;

import java.util.Vector;

public class Listing5P1 {
    public static Object getLast(Vector list) {
        int lastIndex = list.size() - 1;
        return list.get(lastIndex);
    }

    public static void deleteLast(Vector list) {
        int lastIndex = list.size() - 1;
        list.remove(lastIndex);
    }
}

/*
Listing 5.1 shows two methods that operate on a Vector, getLast and deleteLast,
both of which are check-then-act sequences.
Each calls size to determine the size of the array and uses the resulting value to retrieve or
remove the last element.

These methods seem harmless, and in a sense they are-they can't corrupt the Vector,
no matter how many threads call them simultaneously.

But the caller of these methods might have a different opinion. If thread A calls getLast on
a Vector with ten elements, thread B calls deleteLast on the same Vector, and
the operations are interleaved, getLast throws ArrayIndexOutOfBoundsException.

Between the call to size and the subsequent call to get in getLast,
the Vector shrank and the index computed in the first step is no longer valid.
This is perfectly consistent with the specification of Vector-it throws an exception if asked for a nonexistent element.
But this is not what a caller expects getLast to do, even in the face of concurrent modification,
unless perhaps the Vector was empty to begin with.
 */
