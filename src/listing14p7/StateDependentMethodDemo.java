package listing14p7;

import annotation.ThreadSafe;
import listing14p2.BaseBoundedBuffer;

@ThreadSafe
public class StateDependentMethodDemo {
    Object lock;

    void stateDependentMethod() throws InterruptedException {
    // condition predicate must be guarded by lock
        synchronized(lock) {
            while (!conditionPredicate()) {
                lock.wait();
            }
            // object is now in desired state
        }
    }

    /* Dummy Implementation */
    private boolean conditionPredicate() {
        return true;
    }
}

/*
A single intrinsic condition queue may be used with more than one condition predicate.
When your thread is awakened because someone called notifyAll,
that doesn’t mean that the condition predicate you were waiting for is now true.
(This is like having your toaster and coffee maker share a single bell; when it rings,
you still have to look to see which device raised the signal.)
Additionally, wait is even allowed to return “spuriously”—not in response to any thread calling notify.

When control re-enters the code calling wait, it has reacquired the lock associated with the condition queue.
Is the condition predicate now true? Maybe. It might have been true at the time the notifying thread called notifyAll,
but could have become false again by the time you reacquire the lock.
Other threads may have acquired the lock and changed the object’s state between
when your thread was awakened and when wait reacquired the lock.
Or maybe it hasn’t been true at all since you called wait.
You don’t know why another thread called notify or notifyAll;
maybe it was because another condition predicate associated with the same condition queue became true.
Multiple condition predicates per condition queue are quite common—BoundedBuffer uses the same condition queue for
both the “not full” and “not empty” predicates.

For all these reasons, when you wake up from wait you must test the condition predicate again,
and go back to waiting (or fail) if it is not yet true.
Since you can wake up repeatedly without your condition predicate being true,
you must therefore always call wait from within a loop, testing the condition predicate in each iteration.
The canonical form for a condition wait is shown in Listing 14.7.
*/