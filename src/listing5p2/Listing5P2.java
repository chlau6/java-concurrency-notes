package listing5p2;

import java.util.Vector;

public class Listing5P2 {
    public static Object getLast(Vector list) {
        synchronized (list) {
            int lastIndex = list.size() - 1;
            return list.get(lastIndex);
        }
    }

    public static void deleteLast(Vector list) {
        synchronized (list) {
            int lastIndex = list.size() - 1;
            list.remove(lastIndex);
        }
    }
}

/*
Because the synchronized collections commit to a synchronization policy that supports client-side locking,
it is possible to create new operations that are atomic with respect to other collection operations as long as
we know which lock to use.

The synchronized collection classes guard each method with the lock on the synchronized collection object itself.
By acquiring the collection lock we can make getLast and deleteLast atomic,
ensuring that the size of the Vector does not change between calling size and get, as shown in Listing 5.2.
 */
