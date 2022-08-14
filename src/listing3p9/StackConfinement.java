package listing3p9;

import dummy.Animal;
import dummy.AnimalPair;
import dummy.Ark;
import dummy.SpeciesGenderComparator;

import java.util.Collection;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

public class StackConfinement {
    Ark ark;

    public int loadTheArk(Collection<Animal> candidates) {
        SortedSet<Animal> animals;
        int numPairs = 0;
        Animal candidate = null;

        // animals confined to method, don't let them escape!
        animals = new TreeSet<Animal>(new SpeciesGenderComparator());
        animals.addAll(candidates);

        for (Animal a : animals) {
            if (candidate == null || !candidate.isPotentialMate(a)) {
                candidate = a;
            } else {
                ark.load(new AnimalPair(candidate, a));
                ++numPairs;
                candidate = null;
            }
        }
        return numPairs;
    }
}

/*
Stack confinement is a special case of thread confinement in which
an object can only be reached through local variables.

Just as encapsulation can make it easier to preserve invariants,
local variables can make it easier to confine objects to a thread.

Local variables are intrinsically confined to the executing thread; they exist on the executing thread's stack,
which is not accessible to other threads.

Stack confinement (also called within-thread or thread-local usage,
but not to be confused with the ThreadLocal library class) is simpler to maintain and
less fragile than ad-hoc thread confinement.

For primitively typed local variables, such as numPairs in loadTheArk in Listing 3.9,
you cannot violate stack confinement even if you tried.

There is no way to obtain a reference to a primitive variable,
so the language semantics ensure that primitive local variables are always stack confined.

Maintaining stack confinement for object references requires a little more assistance from the programmer
to ensure that the referent does not escape.

In loadTheArk, we instantiate a TreeSet and store a reference to it in animals.
At this point, there is exactly one reference to the Set,
held in a local variable and therefore confined to the executing thread.

However, if we were to publish a reference to the Set (or any of its internals),
the confinement would be violated and the animals would escape.
 */
