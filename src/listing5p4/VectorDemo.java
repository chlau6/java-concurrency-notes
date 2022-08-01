package listing5p4;

import java.util.Vector;

public class VectorDemo {
    public void iterate(Vector vector) {
        synchronized (vector) {
            for (int i = 0; i < vector.size(); i++) {
                doSomething(vector.get(i));
            }
        }
    }

    /* Dummy Implementation */
    private void doSomething(Object o) {

    }
}

/*
The problem of unreliable iteration can again be addressed by client-side locking,
at some additional cost to scalability.

By holding the Vector lock for the duration of iteration, as shown in Listing 5.4,
we prevent other threads from modifying the Vector while we are iterating it.

Unfortunately, we also prevent other threads from accessing it at all during this time, impairing concurrency.
 */
