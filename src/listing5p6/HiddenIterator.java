package listing5p6;

import annotation.GuardedBy;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class HiddenIterator {
    @GuardedBy("this")
    private final Set<Integer> set = new HashSet<Integer>();

    public synchronized void add(Integer i) {
        set.add(i);
    }

    public synchronized void remove(Integer i) {
        set.remove(i);
    }

    public void addTenThings() {
        Random r = new Random();
        for (int i = 0; i < 10; i++) {
            add(r.nextInt());
        }

        // bold
        System.out.println("DEBUG: added ten elements to " + set);
    }
}

/*
There is no explicit iteration in HiddenIterator, but the code in bold entails iteration just the same.
The string concatenation gets turned by the compiler into a call to StringBuilder.append(Object),
which in turn invokes the collection's toString method-
and the implementation of toString in the standard collections iterates the collection and
calls toString on each element to produce a nicely formatted representation of the collection's contents.

The addTenThings method could throw ConcurrentModificationException,
because the collection is being iterated by toString in the process of preparing the debugging message.
Of course, the real problem is that HiddenIterator is not thread-safe;
the HiddenIterator lock should be acquired before using set in the println call,
but debugging and logging code commonly neglect to do this.

The real lesson here is that the greater the distance between the state and the synchronization that guards it,
the more likely that someone will forget to use proper synchronization when accessing that state.
If HiddenIterator wrapped the HashSet with a synchronizedSet, encapsulating the synchronization,
this sort of error would not occur.
 */
