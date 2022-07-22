package listing3p9;

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

        // animals confined to method, don’t let them escape!
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
Dummy Implementation
 */
class Ark {
    public void load(AnimalPair pair) {

    }
}

/*
Dummy Implementation
 */
class SpeciesGenderComparator implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        return 0;
    }
}

/*
Dummy Implementation
 */
class Animal {
    public boolean isPotentialMate(Animal animal) {
        return true;
    }
}

class AnimalPair {
    public AnimalPair(Animal a, Animal b) {

    }
}

/*
For primitively typed local variables, such as numPairs in loadTheArk,
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
