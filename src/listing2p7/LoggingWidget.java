package listing2p7;

public class LoggingWidget extends Widget {
    public synchronized void doSomething() {
        System.out.println(toString() + ": calling doSomething");
        super.doSomething();
    }
}

class Widget {
    public synchronized void doSomething() {

    }
}

/*
When a thread requests a lock that is already held by another thread, the requesting thread blocks.
But because intrinsic locks are reentrant, if a thread tries to acquire a lock that it already holds,
the request succeeds.

Reentrancy means that locks are acquired on a per-thread rather than per-invocation basis.
Reentrancy is implemented by associating with each lock an acquisition count and an owning thread.
When the count is zero, the lock is considered unheld.
When a thread acquires a previously unheld lock, the JVM records the owner and sets the acquisition count to one.
If that same thread acquires the lock again, the count is incremented,
and when the owning thread exits the synchronized block, the count is decremented.
When the count reaches zero, the lock is released.

Reentrancy facilitates encapsulation of locking behavior,
and thus simplifies the development of object-oriented concurrent code.

Without reentrant locks, the very natural-looking code in Listing 2.7,
in which a subclass overrides a synchronized method and then calls the superclass method, would deadlock.

Because the doSomething methods in Widget and LoggingWidget are both synchronized,
each tries to acquire the lock on the Widget before proceeding.

But if intrinsic locks were not reentrant,
the call to super.doSomething would never be able to acquire the lock because it would be considered already held,
and the thread would permanently stall waiting for a lock it can never acquire.

Reentrancy saves us from deadlock in situations like this.
 */
