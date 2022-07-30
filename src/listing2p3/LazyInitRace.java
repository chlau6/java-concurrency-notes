package listing2p3;

import annotation.NotThreadSafe;
import dummy.ExpensiveObject;

@NotThreadSafe
public class LazyInitRace {
    private ExpensiveObject instance = null;

    public ExpensiveObject getInstance() {
        if (instance == null) {
            instance = new ExpensiveObject();
        }

        return instance;
    }
}

/*
A common idiom that uses check-then-act is lazy initialization.
The goal of lazy initialization is to defer initializing an object until it is actually needed while
at the same time ensuring that it is initialized only once.

LazyInitRace in Listing 2.3 has race conditions that can undermine its correctness.

Say that threads A and B execute getInstance at the same time. A sees that instance is null,
and instantiates a new ExpensiveObject. B also checks if instance is null.

Whether instance is null at this point depends unpredictably on timing,
including the vagaries of scheduling and how long A takes to instantiate the ExpensiveObject and set the instance field.

If instance is null when B examines it, the two callers to getInstance may receive two different results,
even though getInstance is always supposed to return the same instance.
 */
