package listing4p2;

import annotation.GuardedBy;
import annotation.ThreadSafe;

import java.util.HashSet;
import java.util.Set;

@ThreadSafe
public class PersonSet {
    @GuardedBy("this")
    private final Set<Person> mySet = new HashSet<Person>();
    public synchronized void addPerson(Person p) {
        mySet.add(p);
    }
    public synchronized boolean containsPerson(Person p) {
        return mySet.contains(p);
    }
}

class Person {

}

/*
PersonSet illustrates how confinement and locking can work together to make a class thread-safe
even when its component state variables are not.

The state of PersonSet is managed by a HashSet, which is not thread-safe.
But because mySet is private and not allowed to escape, the HashSet is confined to the PersonSet.
The only code paths that can access mySet are addPerson and containsPerson,
and each of these acquires the lock on the PersonSet.
All its state is guarded by its intrinsic lock, making PersonSet thread-safe.

This example makes no assumptions about the thread-safety of Person, but if it is mutable,
additional synchronization will be needed when accessing a Person retrieved from a PersonSet.
The most reliable way to do this would be to make Person thread-safe;
less reliable would be to guard the Person objects with a lock and
ensure that all clients follow the protocol of acquiring the appropriate lock before accessing the Person.
 */

