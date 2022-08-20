package listing14p9;

import annotation.GuardedBy;
import annotation.ThreadSafe;
import listing14p2.BaseBoundedBuffer;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

@ThreadSafe
public class ThreadGate {
    // CONDITION-PREDICATE: opened-since(n) (isOpen || generation>n)
    @GuardedBy("this")
    private boolean isOpen;

    @GuardedBy("this")
    private int generation;

    public synchronized void close() {
        isOpen = false;
    }

    public synchronized void open() {
        ++generation;
        isOpen = true;
        notifyAll();
    }

    // BLOCKS-UNTIL: opened-since(generation on entry)
    public synchronized void await() throws InterruptedException {
        int arrivalGeneration = generation;
        while (!isOpen && arrivalGeneration == generation) {
            wait();
        }
    }
}

/*
The starting gate latch in TestHarness on page 96 was constructed with an initial count of one,
creating a binary latch: one with two states, the initial state and the terminal state.
The latch prevents threads from passing the starting gate until it is opened,
at which point all the threads can pass through.
While this latching mechanism is often exactly what is needed,
sometimes it is a drawback that a gate constructed in this manner cannot be reclosed once opened.

It is easy to develop a recloseable ThreadGate class using condition waits, as shown in Listing 14.9.
ThreadGate lets the gate be opened and closed, providing an await method that blocks until the gate is opened.
The open method uses notifyAll because the semantics of this class fail
the "one-in, one-out" test for single notification.

The condition predicate used by await is more complicated than simply testing isOpen.
This is needed because if N threads are waiting at the gate at the time it is opened,
they should all be allowed to proceed.
But, if the gate is opened and closed in rapid succession,
all threads might not be released if await examines only isOpen: by the time all the threads receive the notification,
reacquire the lock, and emerge from wait, the gate may have closed again.
So ThreadGate uses a somewhat more complicated condition predicate: every time the gate is closed,
a "generation" counter is incremented,
and a thread may pass await if the gate is open now or if the gate has opened since this thread arrived at the gate.

Since ThreadGate only supports waiting for the gate to open, it performs notification only in open;
to support both "wait for open" and "wait for close" operations, it would have to notify in both open and close.
This illustrates why state-dependent classes can be fragile to maintain -
the addition of a new state-dependent operation may require modifying many code paths that
modify the object state so that the appropriate notifications can be performed.
*/