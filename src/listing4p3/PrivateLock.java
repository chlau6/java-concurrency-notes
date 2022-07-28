package listing4p3;

import annotation.GuardedBy;

public class PrivateLock {
    private final Object myLock = new Object();

    @GuardedBy("myLock")
    Widget widget;

    void someMethod() {
        synchronized (myLock) {
            // Access or modify the state of widget
        }
    }
}

class Widget {

}

/*
There are advantages to using a private lock object instead of an object’s intrinsic lock (or any other publicly accessible lock).

Making the lock object private encapsulates the lock so that client code cannot acquire it,
whereas a publicly accessible lock allows client code to participate in its synchronization policy— correctly or incorrectly.

Clients that improperly acquire another object’s lock could cause liveness problems,
and verifying that a publicly accessible lock is properly used requires examining the entire program rather than a single class.
 */