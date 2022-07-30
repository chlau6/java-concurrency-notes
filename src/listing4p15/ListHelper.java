package listing4p15;

import annotation.ThreadSafe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ThreadSafe
public class ListHelper<E> {
    public List<E> list = Collections.synchronizedList(new ArrayList<E>());
    public boolean putIfAbsent(E x) {
        synchronized (list) {
            boolean absent = !list.contains(x);
            if (absent) {
                list.add(x);
            }

            return absent;
        }
    }
}

/*
Listing 4.15 shows a putIfAbsent operation on a thread-safe List that correctly uses client-side locking.

If extending a class to add another atomic operation is fragile because
it distributes the locking code for a class over multiple classes in an object hierarchy,
client-side locking is even more fragile because it entails putting locking code for class C into classes that
are totally unrelated to C.

Exercise care when using client-side locking on classes that do not commit to their locking strategy.
Client-side locking has a lot in common with class extension—
they both couple the behavior of the derived class to the implementation of the base class.

Just as extension violates encapsulation of implementation,
client-side locking violates encapsulation of synchronization policy.
 */