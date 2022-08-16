package listing14p8;

import annotation.ThreadSafe;
import listing14p2.BaseBoundedBuffer;

@ThreadSafe
public class ConditionalNotificationDemo<V> extends BaseBoundedBuffer<V> {
    public ConditionalNotificationDemo(int capacity) {
        super(capacity);
    }

    public synchronized void put(V v) throws InterruptedException {
        while (isFull())
            wait();
        boolean wasEmpty = isEmpty();
        doPut(v);
        if (wasEmpty)
            notifyAll();
    }
}

/*
The notification done by put and take in BoundedBuffer is conservative:
a notification is performed every time an object is put into or removed from the buffer.
This could be optimized by observing that a thread can be released from a wait
only if the buffer goes from empty to not empty or from full to not full,
and notifying only if a put or take effected one of these state transitions.
This is called conditional notification. While conditional notification can improve performance,
it is tricky to get right (and also complicates the implementation of subclasses) and so should be used carefully.
Listing 14.8 illustrates using conditional notification in BoundedBuffer.put.
*/