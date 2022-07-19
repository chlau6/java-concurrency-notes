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
Without reentrant locks, the very natural-looking code at the above,
in which a subclass overrides a synchronized method and then calls the superclass method, would deadlock.

Because the doSomething methods in Widget and LoggingWidget are both synchronized,
each tries to acquire the lock on the Widget before proceeding.

But if intrinsic locks were not reentrant, the call to super.doSomething would never be able to acquire the lock
because it would be considered already held,
and the thread would permanently stall waiting for a lock it can never acquire.

Reentrancy saves us from deadlock in situations like this.
 */
