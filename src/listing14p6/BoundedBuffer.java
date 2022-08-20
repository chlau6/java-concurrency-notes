package listing14p6;

import annotation.ThreadSafe;
import listing14p2.BaseBoundedBuffer;

@ThreadSafe
public class BoundedBuffer<V> extends BaseBoundedBuffer<V> {
    // CONDITION PREDICATE: not-full (!isFull())
    // CONDITION PREDICATE: not-empty (!isEmpty())
    public BoundedBuffer(int size) {
        super(size);
    }

    // BLOCKS-UNTIL: not-full
    public synchronized void put(V v) throws InterruptedException {
        while (isFull()) {
            wait();
        }

        doPut(v);
        notifyAll();
    }

    // BLOCKS-UNTIL: not-empty
    public synchronized V take() throws InterruptedException {
        while (isEmpty()) {
            wait();
        }

        V v = doTake();
        notifyAll();

        return v;
    }
}

/*
BoundedBuffer in Listing 14.6 implements a bounded buffer using wait and notifyAll.
This is simpler than the sleeping version,
and is both more efficient (waking up less frequently if the buffer state does not change) and more responsive
(waking up promptly when an interesting state change happens).
This is a big improvement, but note that the introduction of condition queues didn't change the semantics compared to
the sleeping version.
It is simply an optimization in several dimensions: CPU efficiency, context-switch overhead, and responsiveness.
Condition queues don't let you do anything you can't do with sleeping and polling,
but they make it a lot easier and more efficient to express and manage state dependence.
*/